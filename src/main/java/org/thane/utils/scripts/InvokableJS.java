package org.thane.utils.scripts;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.thane.ThaneGuns;

import java.io.File;

public interface InvokableJS {

    static InvokableJS getFunction(String string) {
        if (JSFunction.isFunction(string)) {
            return new JSFunction(string);
        } else if (string.split("\n")[0].contains(".js")) {
            File file = new File(ThaneGuns.getPlugin().getDataFolder().getAbsolutePath()
                    + File.separatorChar + "javascripts" + File.separatorChar + string);
            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + File.separatorChar + string);
                if (!file.exists()) {
                    file = new File(string);
                    if (!file.exists()) {
                        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                            file = new File(plugin.getDataFolder().getAbsolutePath()
                                    + File.separatorChar + "javascripts" + File.separatorChar + string);
                            if (file.exists()) break;
                        }
                    }
                }
            }
            return getFunction(file);
        }
        return new JSExpression(string);
    }

    static InvokableJS getFunction(File file) {
        return new JSFunction(file);
    }
}
