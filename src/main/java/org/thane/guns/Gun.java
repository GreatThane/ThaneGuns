package org.thane.guns;


import org.bukkit.entity.Player;
import org.thane.sounds.Sound;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Gun extends Reloader implements Cloneable {

    private Map<String, Sound> sounds = new HashMap<>();
    private Bullet bullet;

    public Gun(int maxAmmo, int reloadTime) {
        super(maxAmmo, reloadTime);
    }

    public Bullet shoot() {
        Bullet bullet = this.bullet.clone();
        bullet.spawn(getOwner());
        return bullet;
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

    @Override
    public Gun clone() {
        Gun gun = (Gun) super.clone();
        gun.sounds = this.sounds.entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().clone()))
                .collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue));
        gun.bullet = this.bullet.clone();
        return gun;
    }
}
