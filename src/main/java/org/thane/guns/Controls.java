package org.thane.guns;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.thane.ThaneGuns;

public class Controls implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) return;
        if (ThaneGuns.isGun(event.getItem())) {
            Gun gun = ThaneGuns.toGun(event.getItem());
            assert gun != null;
            gun.setOwner(event.getPlayer());
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getPlayer().hasMetadata("right click time")) {
                    MetadataValue value = event.getPlayer().getMetadata("right click time").get(0);
                    event.getPlayer().removeMetadata("right click time", ThaneGuns.INSTANCE);
                    event.getPlayer().setMetadata("right click time", new FixedMetadataValue(ThaneGuns.INSTANCE, value.asLong() + 250));
                } else event.getPlayer().setMetadata("right click time", new FixedMetadataValue(ThaneGuns.INSTANCE, 0));

                if (gun.getBulletDelay() > 250) {
                    if (gun.canShoot()) gun.shoot();
                } else {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (gun.canShoot()) {
                                gun.shoot();
                            } else cancel();
                        }
                    }.runTaskTimer(ThaneGuns.INSTANCE, 0, gun.getBulletDelay()/ 250);
                }
            } else {
                if (event.getPlayer().hasMetadata("left click time")) {
                    MetadataValue value = event.getPlayer().getMetadata("left click time").get(0);
                    event.getPlayer().removeMetadata("left click time", ThaneGuns.INSTANCE);
                    event.getPlayer().setMetadata("left click time", new FixedMetadataValue(ThaneGuns.INSTANCE, value.asLong() + 250));
                } else event.getPlayer().setMetadata("left click time", new FixedMetadataValue(ThaneGuns.INSTANCE, 0));

                if (gun.getMaxAmmo() != gun.getAmmo()) {
                    gun.reload();
                }
            }
        }
    }
}