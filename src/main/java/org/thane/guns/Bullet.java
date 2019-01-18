package org.thane.guns;

import io.netty.util.internal.ConcurrentSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BlockIterator;
import org.thane.utils.DamageReport;
import org.thane.utils.Range;
import org.thane.utils.TaskUtil;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

public class Bullet {

//    private static final Map<Material, Byte> defaultPiercables = new HashMap<Material, Byte>() {{
//        put(Material.AIR, (byte) 0);
//        put(Material.IRON_BARS, (byte) 0);
//        put(Material.WATER, (byte) 0);
//        put(Material.LILY_PAD, (byte) 0);
//        put(Material.TORCH, (byte) 0);
//        put(Material.ACACIA_FENCE, (byte) 0);
//        put(Material.ACACIA_FENCE_GATE, (byte) 0);
//        put(Material.BIRCH_FENCE, (byte) 0);
//        put(Material.BIRCH_FENCE_GATE, (byte) 0);
//        put(Material.DARK_OAK_FENCE_GATE, (byte) 0);
//        put(Material.DARK_OAK_FENCE, (byte) 0);
//        put(Material.)
//    }};

    float accuracy, voltatility;
    int range;
    private Range<Float> damageRange;
    private float damageAtrophy = 0;
    private ParticleStream particleStream;
    private Set<BulletProperty> properties = new HashSet<>();
    private double multiplier = 1;

    private Location prev;
    private Location current;
    private Location next;

    public Bullet(float accuracy, float voltatility) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
    }

    public Bullet(float accuracy, float voltatility, int range) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
        this.range = range;
    }

    public Bullet(float accuracy, float voltatility, int range, Range<Float> damageRange) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
        this.range = range;
        this.damageRange = damageRange;
    }

    public Bullet(float accuracy, float voltatility, int range, Range<Float> damageRange, float damageAtrophy, ParticleStream particleStream) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
        this.range = range;
        this.damageRange = damageRange;
        this.damageAtrophy = damageAtrophy;
        this.particleStream = particleStream;
    }

    public Bullet(float accuracy, float voltatility, int range, Range<Float> damageRange, float damageAtrophy, ParticleStream particleStream, Set<BulletProperty> properties) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
        this.range = range;
        this.damageRange = damageRange;
        this.damageAtrophy = damageAtrophy;
        this.particleStream = particleStream;
        this.properties = properties;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public float getVoltatility() {
        return voltatility;
    }

    public void setVoltatility(float voltatility) {
        this.voltatility = voltatility;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public Range<Float> getDamageRange() {
        return damageRange;
    }

    public void setDamageRange(Range<Float> damageRange) {
        this.damageRange = damageRange;
    }

    public float getDamageAtrophy() {
        return damageAtrophy;
    }

    public void setDamageAtrophy(float damageAtrophy) {
        this.damageAtrophy = damageAtrophy;
    }

    public ParticleStream getParticleStream() {
        return particleStream;
    }

    public void setParticleStream(ParticleStream particleStream) {
        this.particleStream = particleStream;
    }

    public Set<BulletProperty> getProperties() {
        return properties;
    }

    public void setProperties(Set<BulletProperty> properties) {
        this.properties = properties;
    }

    public void addProperties(BulletProperty... properties) {
        Collections.addAll(this.properties, properties);
    }

    public void removeProperties(BulletProperty... properties) {
        this.properties.removeAll(Arrays.asList(properties));
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Location getLocation() {
        return current;
    }

    public void setLocation(Location current) {
        this.current = current;
    }

    public void removeProperties(Class<? extends BulletProperty>... properties) {
        Set<Class<? extends BulletProperty>> setProperties = new HashSet<>(Arrays.asList(properties));
        this.properties.forEach(p -> {
            if (setProperties.contains(p.getClass())) {
                this.properties.remove(p);
            }
        });
    }

    public Set<BulletProperty> getProperties(Class<? extends BulletProperty> property) {
        return properties.stream().filter(property::isInstance).collect(Collectors.toSet());
    }

    public Set<DamageReport> spawn(LivingEntity shooter, Location startPoint) {
        BlockIterator iterator = new BlockIterator(shooter.getEyeLocation(), 0, range);

        for (BulletProperty property : getProperties(BulletProperty.BlockPiercing.class)) {

        }

        current = startPoint;
        prev = startPoint.getDirection().add(startPoint.getDirection().normalize().multiply(-0.7)).toLocation(startPoint.getWorld());
        next = startPoint.getDirection().add(startPoint.getDirection().normalize().multiply(0.7)).toLocation(startPoint.getWorld());
    }

    public class ParticleStream {

        private Particle particle;
        private Range<Integer> range;
        private int frequency;

        public ParticleStream(Particle particle) {
            this.particle = particle;
        }

        public ParticleStream(Range<Integer> range, int frequency) {
            this.range = range;
            this.frequency = frequency;
        }

        public Particle getParticle() {
            return particle;
        }

        public void setParticle(Particle particle) {
            this.particle = particle;
        }

        public Range<Integer> getRange() {
            return range;
        }

        public void setRange(Range<Integer> range) {
            this.range = range;
        }

        public int getFrequency() {
            return frequency;
        }

        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }
    }

    private static final ConcurrentSkipListSet<Bullet> bullets = new ConcurrentSkipListSet<>();

    protected static void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    protected static void removeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    static {
        new TaskUtil(() -> {
            for (Bullet bullet : bullets) {
                boolean toContinue = true;
                for (BulletProperty property : bullet.properties) {
                    if (!property.onIteration(bullet)) toContinue = false;
                }
                if (!toContinue) continue;

            }
        }, 10, 1).runAsync();
    }
}
