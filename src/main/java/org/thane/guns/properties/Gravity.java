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
        return false;
    }

    @Override
    public Gravity clone() throws CloneNotSupportedException {
        return (Gravity) super.clone();
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
