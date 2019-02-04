package org.thane.guns.properties;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.thane.guns.Bullet;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class BodyPiercing implements BulletProperty {

    private Map<EntityType, Float> damageLoss = new HashMap<EntityType, Float>() {{
        put(EntityType.SKELETON, 0.5F);
    }};

    public BodyPiercing() {
    }

    public BodyPiercing(Map<EntityType, Float> damageLoss) {
        this.damageLoss = damageLoss;
    }

    @Override
    public boolean onHitEntity(Bullet bullet, LivingEntity entity) {
        if (damageLoss.containsKey(entity.getType())) {
            bullet.getDamageRange().setMax(bullet.getDamageRange().getMax() * damageLoss.get(entity.getType()));
            bullet.getDamageRange().setMin(bullet.getDamageRange().getMin() * damageLoss.get(entity.getType()));
            return false;
        } else return true;
    }

    @Override
    public BodyPiercing clone() throws CloneNotSupportedException {
        BodyPiercing piercing = (BodyPiercing) super.clone();
        piercing.damageLoss = new HashMap<>(damageLoss);
        return piercing;
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
