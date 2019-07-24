package org.thane.guns.properties;

import org.bukkit.Location;
import org.bukkit.Material;
import org.thane.guns.Bullet;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("Duplicates")
public class BlockPiercing implements BulletProperty {

    private Map<Material, Float> damageLoss;

    public BlockPiercing() {
        damageLoss = new HashMap<Material, Float>() {{
            put(Material.OAK_PLANKS, 0.5F);
        }};
    }

    public BlockPiercing(Map<Material, Float> damageLoss) {
        this.damageLoss = damageLoss;
    }

    @Override
    public boolean onHitBlock(Bullet bullet, Location intersectionPoint) {
        if (damageLoss.containsKey(intersectionPoint.getBlock().getType())) {
            bullet.getDamageRange().setMax(bullet.getDamageRange().getMax() * damageLoss.get(intersectionPoint.getBlock().getType()));
            bullet.getDamageRange().setMin(bullet.getDamageRange().getMin() * damageLoss.get(intersectionPoint.getBlock().getType()));
            return true;
        } else return false;
    }

    @Override
    public BlockPiercing clone() {
        BlockPiercing piercing = null;
        try {
            piercing = (BlockPiercing) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        assert piercing != null;
        piercing.damageLoss = new HashMap<>(damageLoss);
        return piercing;
    }

    @Override
    public int getImportance() {
        return 0;
    }
}
