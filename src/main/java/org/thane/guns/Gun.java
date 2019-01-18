package org.thane.guns;

import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.thane.sounds.Sound;
import org.thane.utils.NBTUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Gun {

    Map<String, Sound> sounds = new HashMap<>();
    Scope scope;

    

    public static boolean equalsNBT(ItemStack stack, ItemStack stack1) {
        if (stack.isSimilar(stack1)) {
            return Objects.requireNonNull(NBTUtil.nbtToJsonElement(
                    CraftItemStack.asNMSCopy(stack).getOrCreateTag())).toString()
                    .equalsIgnoreCase(Objects.requireNonNull(NBTUtil.nbtToJsonElement(
                            CraftItemStack.asNMSCopy(stack1).getOrCreateTag())).toString());
        }
        return false;
    }
}
