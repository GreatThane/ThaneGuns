package org.thane.guns.properties;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.thane.guns.Bullet;
import org.thane.utils.Importance;

public interface BulletProperty extends Cloneable, Importance {

    /**
     * @return Returning 'true' cancels the spawning of the bullet.
     */
    default boolean onSpawn(Bullet bullet) {
        return false;
    }

    /**
     * @param intersectionPoint The block which the bullet has intersected with.
     * @return Returning 'true' cancels the termination of the bullet because of this block.
     */
    default boolean onHitBlock(Bullet bullet, Location intersectionPoint) {
        return false;
    }

    /**
     * @param entity The entity which the bullet has made contact with.
     * @return Returning 'true' cancels the termination of the bullet because of this entity.
     */
    default boolean onHitEntity(Bullet bullet, LivingEntity entity) {
        return false;
    }


    /**
     * Called very time the bullet attempts to move forward. This can be multiple times in a tick.
     * @param iterationCount the current nth time the bullet has iterated in this tick.
     * @return Returning 'true' causes the the current iteration to be skipped, and as such the bullet will not move forward. Be careful, as this may cause false positives for other properties.
     */
    default boolean onIteration(Bullet bullet, int iterationCount) {
        return false;
    }

    /**
     * @return Returning 'true' cancels the termination of the bullet (you must take into account what causes this termination and revert it to prevent it from being called a second time).
     */
    default boolean onTerminate(Bullet bullet) {
        return false;
    }

    BulletProperty clone() throws CloneNotSupportedException;
}
