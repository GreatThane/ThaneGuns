package org.thane.guns.properties;

import org.bukkit.Particle;
import org.thane.guns.Bullet;
import org.thane.utils.ranges.IntegerRange;

public class ParticleStream implements BulletProperty {

    private Particle particle;
    private IntegerRange range;
    private int frequency;

    public ParticleStream() {
    }

    public ParticleStream(Particle particle) {
        this.particle = particle;
    }

    public ParticleStream(Particle particle, IntegerRange range) {
        this(particle);
        this.range = range;
    }

    public ParticleStream(Particle particle, IntegerRange range, int frequency) {
        this(particle, range);
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
        if (bullet.getDistance() >= range.getMin() && bullet.getDistance() <= range.getMax()) {
            if (bullet.getDistance() - range.getMin() % frequency == 0) {
                bullet.getLocation().getWorld().spawnParticle(particle, bullet.getLocation(), 1);
            }
        }
        return false;
    }

    @Override
    public ParticleStream clone() throws CloneNotSupportedException {
        ParticleStream stream = (ParticleStream) super.clone();
        stream.range = range.clone();
        stream.particle = particle;
        return stream;
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
