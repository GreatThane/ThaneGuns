package org.thane.guns;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.thane.ThaneGuns;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BulletProperty {

    public static final Set<EntityType> piercableEntities;
    public static final Map<Material, Float> piercableBlocks;
    public static final float percentDamageLoss;
    public static final float forceOfGravity;
    public static final float speed;

    public boolean onSpawn(Bullet bullet) {
        return true;
    }

    public boolean onHitBlock(Bullet bullet, Block block) {
        return true;
    }

    public boolean onHitEntity(Bullet bullet, LivingEntity entity) {
        return true;
    }

    public boolean onIteration(Bullet bullet) {
        return true;
    }

    public class BodyPiercing extends BulletProperty {

        float percentDamageLoss;

        public BodyPiercing() {
            this.percentDamageLoss = BulletProperty.percentDamageLoss;
        }

        public BodyPiercing(float percentDamageLoss) {
            this.percentDamageLoss = percentDamageLoss;
        }

        public float getPercentDamageLoss() {
            return percentDamageLoss;
        }

        public void setPercentDamageLoss(float percentDamageLoss) {
            this.percentDamageLoss = percentDamageLoss;
        }


    }

    public class BlockPiercing extends BulletProperty {

        Map<Material, Float> percentDamageLoss;

        public BlockPiercing() {
            this.percentDamageLoss = piercableBlocks;
        }

        public BlockPiercing(Map<Material, Float> percentDamageLoss) {
            this.percentDamageLoss = percentDamageLoss;
        }

        public Map<Material, Float> getPercentDamageLoss() {
            return percentDamageLoss;
        }

        public void setPercentDamageLoss(Map<Material, Float> percentDamageLoss) {
            this.percentDamageLoss = percentDamageLoss;
        }

        @Override
        public void apply(Bullet bullet) {

        }
    }

    public class Gravity extends BulletProperty {

        float forceOfGravity;

        public Gravity() {
            this.forceOfGravity = BulletProperty.forceOfGravity;
        }

        public Gravity(float forceOfGravity) {
            this.forceOfGravity = forceOfGravity;
        }

        public float getForceOfGravity() {
            return forceOfGravity;
        }

        public void setForceOfGravity(float forceOfGravity) {
            this.forceOfGravity = forceOfGravity;
        }

        @Override
        public void apply(Bullet bullet) {

        }
    }

    public class Speed extends BulletProperty {

        float speed;

        public Speed() {
            this.speed = BulletProperty.speed;
        }

        public Speed(float speed) {
            this.speed = speed;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        @Override
        public boolean onSpawn(Bullet bullet) {
            bullet.setMultiplier(speed / 20);
        }
    }

    static {
        piercableEntities = ThaneGuns.getPlugin().getConfig().getStringList("piercable entities").stream()
                .map(s -> EntityType.valueOf(s.toUpperCase().replaceAll("\\s+", "_").replaceAll("\\W", "")))
                .collect(Collectors.toSet());
        forceOfGravity = (float) ThaneGuns.getPlugin().getConfig().getDouble("force of gravity");
        speed = (float) ThaneGuns.getPlugin().getConfig().getDouble("bullet speed");
        percentDamageLoss = (float) ThaneGuns.getPlugin().getConfig().getDouble("percent damage loss");
        piercableBlocks = ThaneGuns.getPlugin().getConfig().getStringList("piercable blocks").stream()
                .map(Material::matchMaterial)
                .collect(Collectors.toMap(Function.identity(), o -> percentDamageLoss));
    }
}
