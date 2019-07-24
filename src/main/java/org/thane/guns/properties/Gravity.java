package org.thane.guns.properties;

import org.thane.guns.Bullet;

public class Gravity implements BulletProperty {

    private float forceOfGravity = 0.8F;

    public Gravity() {
    }

    public Gravity(float forceOfGravity) {
        this.forceOfGravity = forceOfGravity;
    }

    @Override
    public boolean onIteration(Bullet bullet, int iterationCount) {
        bullet.getNextLocation().setPitch(bullet.getNextLocation().getPitch() - forceOfGravity);
        return BulletProperty.super.onIteration(bullet, iterationCount);
    }

    @Override
    public Gravity clone() {
        try {
            return (Gravity) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
