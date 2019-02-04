package org.thane.guns.properties;

import org.thane.guns.Bullet;

public class Speed implements BulletProperty {

    private float speed = 5000;

    public Speed() {
    }

    public Speed(float speed) {
        this.speed = speed;
    }

    @Override
    public boolean onSpawn(Bullet bullet) {
        bullet.setMultiplier(speed / 20);
        return false;
    }

    @Override
    public Speed clone() throws CloneNotSupportedException {
        return (Speed) super.clone();
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
