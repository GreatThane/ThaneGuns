package org.thane.guns;


import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.thane.sounds.Sound;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Gun extends Reloader {

    private Map<String, Sound> sounds = new HashMap<>();
    private long lastShot = 0;
    private long bulletDelay = 2500;

    private Bullet bullet;

    public Gun(int maxAmmo, int reloadTime) {
        super(maxAmmo, reloadTime);
    }

    public Gun(int maxAmmo, int reloadTime, long bulletDelay) {
        super(maxAmmo, reloadTime);
        this.bulletDelay = bulletDelay;
    }

    public boolean canShoot() {
        return System.currentTimeMillis() - lastShot >= bulletDelay && !isReloading() && getAmmo() > 0;
    }

    public Bullet shoot() {
        Bullet bullet;
        if (canShoot()) {
            lastShot = System.currentTimeMillis();
            bullet = this.bullet.clone();
            bullet.spawn(getOwner());
            if (getAmmo() == 1) {
                reload();
            } else decrementAmmo();
        } else return null;
        return bullet;
    }

    public long getBulletDelay() {
        return bulletDelay;
    }

    public void setBulletDelay(long bulletDelay) {
        this.bulletDelay = bulletDelay;
    }

    public long getLastShot() {
        return lastShot;
    }

    public void setLastShot(long lastShot) {
        this.lastShot = lastShot;
    }

    public Map<String, Sound> getSounds() {
        return sounds;
    }

    public void setSounds(Map<String, Sound> sounds) {
        this.sounds = sounds;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public void setBullet(Bullet bullet) {
        this.bullet = bullet;
    }

    public ItemStack updateItem(ItemStack stack) {
        stack.setAmount(getAmmo());
        return stack;
    }

    @Override
    public Gun clone() {
        Gun gun = (Gun) super.clone();
        gun.sounds = this.sounds.entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().clone()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        gun.bullet = this.bullet.clone();
        return gun;
    }
}
