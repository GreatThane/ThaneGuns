package org.thane;

import com.google.gson.GsonBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.thane.guns.Gun;
import org.thane.guns.Scope;
import org.thane.utils.ActionBarPlayerOnlineListener;

public final class ThaneGuns extends JavaPlugin {

    public static JavaPlugin INSTANCE;

    public ThaneGuns() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            generateDefaultFiles();
        }
        this.getServer().getPluginManager().registerEvents(new ActionBarPlayerOnlineListener(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("test")) {
                // TODO: 2/4/19 fix this nms dependent mess
//                Player player = (Player) sender;
//                ItemStack stack = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
//                if (stack.hasTag()) {
//                    Scope element = NBTUtil.nbtToObject(stack.getTag(), Scope.class);
//                    getLogger().info(new GsonBuilder().setPrettyPrinting().create().toJson(element));
//                } else {
//                    stack.setTag((NBTTagCompound) NBTUtil.objectToNBT(new Scope()));
//                    player.getInventory().setItemInMainHand(CraftItemStack.asBukkitCopy(stack));
//                    getLogger().info("Set object");
//                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void generateDefaultFiles() {
        ThaneGuns.INSTANCE.saveDefaultConfig();
    }

//    @Override
//    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
//        return new ArrayList<>();
//    }

//    public static boolean registerGun(Gun gun) {
//
//    }

    public static void registerGun(Gun gun, boolean override) {

    }
}
