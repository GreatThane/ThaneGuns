package org.thane;

import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.thane.guns.Gun;
import org.thane.guns.Scope;
import org.thane.utils.NBTUtil;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class ThaneGuns extends JavaPlugin {

    public static JavaPlugin thaneGuns;

    public ThaneGuns() {
        thaneGuns = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!getDataFolder().exists()) {
            generateDefaultFiles();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("test")) {
                Player player = (Player) sender;
                ItemStack stack = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
                if (stack.hasTag()) {
                    Scope element = NBTUtil.nbtToObject(stack.getTag(), Scope.class);
                    getLogger().info(new GsonBuilder().setPrettyPrinting().create().toJson(element));
                } else {
                    stack.setTag((NBTTagCompound) NBTUtil.objectToNBT(new Scope()));
                    player.getInventory().setItemInMainHand(CraftItemStack.asBukkitCopy(stack));
                    getLogger().info("Set object");
                }
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void generateDefaultFiles() {
        try {
            ThaneGuns.getPlugin().saveDefaultConfig();
            File javascriptFolder = new File(ThaneGuns.getPlugin().getDataFolder().getAbsolutePath() + "/javascripts");
            if (!javascriptFolder.exists()) javascriptFolder.mkdirs();

            Files.copy(ThaneGuns.class.getResourceAsStream("/zoomFunction.js"),
                    new File(javascriptFolder.getAbsolutePath() + "/zoomFunction.js").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static JavaPlugin getPlugin() {
        return thaneGuns;
    }
}
