package org.thane.guns.properties;

import org.bukkit.Particle;
import org.thane.guns.Bullet;
import org.thane.utils.ranges.IntegerRange;

public class ParticleStream implements BulletProperty {

    private Particle particle;
    private IntegerRange range;
    private int frequency;

    public ParticleStream(Particle particle, IntegerRange range, int frequency) {
        this.particle = particle;
        this.range = range;
        this.frequency = frequency;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public IntegerRange getRange() {
        return range;
    }

    public void setRange(IntegerRange range) {
        this.range = range;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public boolean onIteration(Bullet bullet, int iterationCount) {
//        if (bullet.getDistance() >= range.getMin() && bullet.getDistance() <= range.getMax()) {
//            if (bullet.isShot() && bullet.getDistance() - range.getMin() % frequency == 0) {
                bullet.getLocation().getWorld().spawnParticle(particle, bullet.getLocation(), 1, 0D, 0D, 0D, 0D, null, true);
//            }
//        }
        return BulletProperty.super.onIteration(bullet, iterationCount);
    }

    @Override
    public ParticleStream clone() {
        ParticleStream stream = null;
        try {
            stream = (ParticleStream) super.clone();
            stream.range = range.clone();
            stream.particle = particle;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stream;
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
