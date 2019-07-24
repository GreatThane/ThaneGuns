package org.thane.guns;

import com.google.common.collect.Sets;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.thane.NMSUtils;
import org.thane.events.DamageReportEvent;
import org.thane.guns.properties.BulletProperty;
import org.thane.utils.HealthTint;
import org.thane.utils.TaskUtil;
import org.thane.utils.ranges.DoubleRange;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class Bullet implements Cloneable, Serializable {

    private float accuracy, voltatility;
    private int range;
    private DoubleRange damageRange;
    private DoubleRange headshotMultiplier = new DoubleRange(1D, 2D);
    private Set<BulletProperty> properties = new TreeSet<>();
    private double multiplier = 1;
    private float distance = 0;

    private transient Location prev = null;
    private transient Location current = null;
    private transient Location next = null;

    private Set<LivingEntity> struckEntities = new HashSet<>();

    public Bullet(float accuracy, float voltatility) {
        this.accuracy = accuracy;
        this.voltatility = voltatility;
    }

    public Bullet(float accuracy, float voltatility, int range) {
        this(accuracy, voltatility);
        this.range = range;
        this.multiplier = range;
    }

    public Bullet(float accuracy, float voltatility, int range, DoubleRange damageRange) {
        this(accuracy, voltatility, range);
        this.damageRange = damageRange;
    }

    public Bullet(float accuracy, float voltatility, int range, DoubleRange damageRange, Set<BulletProperty> properties) {
        this(accuracy, voltatility, range, damageRange);
        this.properties = properties;
    }

    public Bullet(float accuracy, float voltatility, int range, DoubleRange damageRange, BulletProperty... properties) {
        this(accuracy, voltatility, range, damageRange, new HashSet<>(Arrays.asList(properties)));
    }

    public Set<LivingEntity> getStruckEntities() {
        return struckEntities;
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

    public DoubleRange getDamageRange() {
        return damageRange;
    }

    public void setDamageRange(DoubleRange damageRange) {
        this.damageRange = damageRange;
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

    public Location getNextLocation() {
        return next;
    }

    public void setLocation(Location current) {
        this.current = current;
    }

    @SafeVarargs
    public final void removeProperties(Class<? extends BulletProperty>... properties) {
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

    private LivingEntity shooter = null;

    public void spawn(LivingEntity shooter) {
        spawn(shooter, shooter.getEyeLocation());
    }

    public void spawn(LivingEntity shooter, Location startPoint) {
        if (isShot()) return;

        this.shooter = shooter;
        current = startPoint;
        prev = startPoint.clone().add(startPoint.getDirection().multiply(-0.5));
        next = startPoint.clone().add(startPoint.getDirection().multiply(0.5));

        Location location = NMSUtils.getIntersection(prev, current).ignoreBlocksWithoutBoundingBoxes(true).stopsOnLiquid(false).compute();
        if (location != null) {
            for (BulletProperty property : properties) {
                if (!property.onHitBlock(this, location)) {
                    terminate();
                    return;
                }
            }
        }
        for (BulletProperty property : getProperties()) {
            if (property.onSpawn(this)) {
                return;
            }
        }
        addBullet(this);
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    public boolean isShot() {
        return shooter != null && current != null;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Set<LivingEntity> terminate() {
        boolean terminate = true;
        for (BulletProperty property : getProperties()) {
            if (property.onTerminate(this)) terminate = false;
        }
        if (terminate) {
            removeBullet(this);
            return struckEntities;
        }
        return null;
    }

    public void iterate() {
        boolean toContinue = true;
        for (int i = 0; i < multiplier && distance < range; i++, distance += 0.5) {
            for (BulletProperty property : properties) {
                if (property.onIteration(this, i)) toContinue = false;
            }
            if (!toContinue) continue;
            prev = current;
            current = next;
            next = current.clone().add(current.getDirection().multiply(0.5));

            if (current.getBlock().getType() != Material.AIR) {
                Location intersection = NMSUtils.getIntersection(current, next).ignoreBlocksWithoutBoundingBoxes(true).stopsOnLiquid(false).compute();
                if (intersection != null) {
                    for (BulletProperty property : properties) {
                        if (!property.onHitBlock(this, intersection)) {
                            terminate();
                            return;
                        }
                    }
                }
            }
            for (Entity entity : current.getWorld().getNearbyEntities(current, 1, 1, 1)) {
                if (entity instanceof LivingEntity) {
                    if (!shooter.getUniqueId().equals(entity.getUniqueId()) && !struckEntities.contains(entity) && isInHitBox((LivingEntity) entity)) {
                        double damage = damageRange.random();
                        if (isHeadShot((LivingEntity) entity)) damage *= headshotMultiplier.random();
                        for (BulletProperty property : properties) {
                            if (!property.onHitEntity(this, (LivingEntity) entity, damage)) {
                                terminate();
                                break;
                            }
                        }
                        DamageReportEvent event = new DamageReportEvent((LivingEntity) entity, shooter, this, (float) damage,
                                entity instanceof Player ? new ComponentBuilder(ChatColor.stripColor(((Player) entity).getDisplayName()))
                                        .color(ChatColor.GREEN).append(" was shot by ").color(ChatColor.YELLOW)
                                        .append(ChatColor.stripColor(((Player) shooter).getDisplayName())).color(ChatColor.GREEN).append(" from ")
                                        .color(ChatColor.YELLOW).append(String.valueOf(distance)).color(ChatColor.YELLOW).append(" meters away.")
                                        .color(ChatColor.YELLOW).create() : null);
                        Bukkit.getPluginManager().callEvent(event);

                        if (event.isCancelled()) continue;
                        if (!event.willKill() && entity instanceof Player)
                            //noinspection deprecation
                            HealthTint.sendBorder((Player) entity, (int) (event.getDamage() / ((Player) entity).getMaxHealth() * 100));
                        ((LivingEntity) entity).damage(event.getDamage(), shooter);
                        struckEntities.add((LivingEntity) entity);
                        if (event.hasDeathMessage()) {
                            Bukkit.getOnlinePlayers().forEach(p -> p.spigot().sendMessage(event.getDeathMessage()));
                        }
                    }
                }
            }
        }
        if (distance >= range) {
            terminate();
        }
    }

    public boolean isInHitBox(LivingEntity entity) {
        return isInBoundingBox(entity.getBoundingBox());
    }

    public boolean isInBoundingBox(BoundingBox box) {
        return box.contains(current.getX(), current.getY(), current.getZ());
    }

    public boolean isHeadShot(LivingEntity entity) {
        BoundingBox box = null;
        switch (entity.getType()) {
            case PLAYER:
            case ZOMBIE:
            case SKELETON:
            case HUSK:
                box = new BoundingBox(entity.getBoundingBox().getMinX(), entity.getBoundingBox().getMinY() + 1.6, entity.getBoundingBox().getMinZ(),
                        entity.getBoundingBox().getMaxX(), entity.getBoundingBox().getMaxY(), entity.getBoundingBox().getMaxZ());
                break;
        }
        return box != null && isInBoundingBox(box);
    }

    public DoubleRange getHeadshotMultiplier() {
        return headshotMultiplier;
    }

    private static final Set<Bullet> BULLETS = Sets.newConcurrentHashSet();

    protected static void addBullet(Bullet bullet) {
        BULLETS.add(bullet);
    }

    protected static void removeBullet(Bullet bullet) {
        BULLETS.remove(bullet);
    }

    static {
        new TaskUtil(() -> {
            for (Bullet bullet : BULLETS) {
                if (bullet.isShot()) {
                    bullet.iterate();
                } else {
                    BULLETS.remove(bullet);
                }
            }
//            Bukkit.getLogger().info(String.valueOf(BULLETS.size()));
        }, 10, 1).runAsync();
    }

    @Override
    public Bullet clone() {
        try {
            Bullet bullet = (Bullet) super.clone();
            bullet.damageRange = this.damageRange.clone();
            if (isShot()) bullet.shooter = null;
            bullet.properties = this.properties.stream().map(BulletProperty::clone).collect(Collectors.toSet());
            bullet.struckEntities = new HashSet<>();
            return bullet;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
