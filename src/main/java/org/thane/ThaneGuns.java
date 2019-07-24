package org.thane;

import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.thane.guns.Controls;
import org.thane.guns.Gun;
import org.thane.utils.ActionBarPlayerOnlineListener;
import org.thane.utils.TaskUtil;

import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public final class ThaneGuns extends JavaPlugin {

    private static Gson GSON;

    private static final Map<UUID, Gun> ACTIVE_GUNS = new ConcurrentHashMap<>();

    private static final Map<NamespacedKey, Gun> GUNS = new ConcurrentHashMap<>();
    private static final Map<UUID, Location> LAST_LOCATION = new ConcurrentHashMap<>();
    private static final Map<UUID, Double> ENTITY_SPEED = new ConcurrentHashMap<>();

    public static JavaPlugin INSTANCE;

    public ThaneGuns() {
        INSTANCE = this;
        GSON = new Gson();
        UUID_KEY = new NamespacedKey(INSTANCE, "uuid");
        GUN_KEY = new NamespacedKey(INSTANCE, "gun");
    }

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            generateDefaultFiles();
        }
        this.getServer().getPluginManager().registerEvents(new Controls(), this);
        this.getServer().getPluginManager().registerEvents(new ActionBarPlayerOnlineListener(), this);

        new TaskUtil(() -> {
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof LivingEntity) {
                        Location location = LAST_LOCATION.get(entity.getUniqueId());
                        if (location != null) {
                            ENTITY_SPEED.put(entity.getUniqueId(), location.distance(entity.getLocation()));
                        }
                    }
                }
            }
        }, 1, 5).runAsync();
        new TaskUtil(() -> {
            for (World world : Bukkit.getWorlds()) {
                for (Entity entity : world.getEntities()) {
                    if (entity instanceof LivingEntity) {
                        LAST_LOCATION.put(entity.getUniqueId(), entity.getLocation());

                        if (entity instanceof Player) {
                            ItemStack[] contents = ((Player) entity).getInventory().getContents();
                            for (int i = 0; i <= 8; i++) {
                                if (isGun(contents[i])) {

                                    ItemStack stack = Objects.requireNonNull(toGun(contents[i])).updateItem(contents[i]);
                                    if (!stack.equals(contents[i])) ((Player) entity).getInventory().setItem(i, stack);
                                }
                            }
                        }
                    }
                }
            }
        }, 0, 1).runAsync();
    }

    @Override
    public void onDisable() {
        this.getServer().getScheduler().cancelTasks(this);
    }

    public static void generateDefaultFiles() {
        ThaneGuns.INSTANCE.saveDefaultConfig();
    }

    public static Gson getGson() {
        return GSON;
    }

    public static void setGson(Gson GSON) {
        ThaneGuns.GSON = GSON;
    }

    public static void registerGun(NamespacedKey key, Gun gun) {
        GUNS.put(key, gun);
    }

    public static void unregisterGun(NamespacedKey key) {
        GUNS.remove(key);
    }

    public static void unregiserGun(Gun gun) {
        GUNS.entrySet().stream().filter(e -> e.getValue().equals(gun)).forEach(e -> GUNS.remove(e.getKey()));
    }

    public static Gun getGun(NamespacedKey key) {
        return GUNS.get(key);
    }

    public static Set<Gun> getGun(String name) {
        return GUNS.keySet().stream()
                .filter(k -> k.getKey().equalsIgnoreCase(name))
                .map(GUNS::get)
                .collect(Collectors.toSet());
    }

    public static Set<Gun> getGuns(Plugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, "");
        return GUNS.keySet().stream()
                .filter(k -> k.getNamespace().equalsIgnoreCase(key.getNamespace()))
                .map(GUNS::get)
                .collect(Collectors.toSet());
    }

    private static NamespacedKey UUID_KEY;
    private static NamespacedKey GUN_KEY;

    public static void applyGun(ItemStack stack, Gun gun) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {

            UUID uuid = UUID.randomUUID();
            oos.writeObject(gun);
            ItemMeta meta = Objects.requireNonNull(stack.getItemMeta());
            meta.getCustomTagContainer().setCustomTag(UUID_KEY, ItemTagType.LONG_ARRAY, new long[]{uuid.getMostSignificantBits(), uuid.getLeastSignificantBits()});
            meta.getCustomTagContainer().setCustomTag(GUN_KEY, ItemTagType.BYTE_ARRAY, baos.toByteArray());
            stack.setItemMeta(meta);

            ACTIVE_GUNS.put(uuid, gun);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isGun(ItemStack stack) {
        return stack != null && stack.hasItemMeta() && stack.getItemMeta() != null && stack.getItemMeta().getCustomTagContainer().hasCustomTag(UUID_KEY, ItemTagType.LONG_ARRAY);
    }

    public static Gun toGun(ItemStack stack) {
        long[] array = Objects.requireNonNull(stack.getItemMeta()).getCustomTagContainer().getCustomTag(UUID_KEY, ItemTagType.LONG_ARRAY);
        assert array != null;
        UUID uuid = new UUID(array[0], array[1]);
        if (ACTIVE_GUNS.containsKey(uuid)) {
            return ACTIVE_GUNS.get(uuid);
        } else {
            CustomItemTagContainer container = stack.getItemMeta().getCustomTagContainer();

            try (ByteArrayInputStream bais = new ByteArrayInputStream(Objects.requireNonNull(container.getCustomTag(GUN_KEY, ItemTagType.BYTE_ARRAY)));
                 ObjectInputStream ois = new ObjectInputStream(bais)) {

                Gun gun = (Gun) ois.readObject();
                ACTIVE_GUNS.put(uuid, gun);
                return gun;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static double getEntitySpeed(LivingEntity entity) {
        return ENTITY_SPEED.get(entity.getUniqueId());
    }

    public static long getLeftClickHoldTime(Player player) {
        return player.getMetadata("left click time").get(0).asLong();
    }

    public static long getRightClickTime(Player player) {
        return player.getMetadata("right click time").get(0).asLong();
    }
}
