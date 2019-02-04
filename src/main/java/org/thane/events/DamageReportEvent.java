package org.thane.events;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.thane.guns.Bullet;

public class DamageReportEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    private LivingEntity shooter;
    private LivingEntity victim;
    private float damage;
    private Bullet bullet;
    private BaseComponent[] deathMessage;

    public DamageReportEvent(LivingEntity victim, LivingEntity shooter, Bullet bullet, float damage, BaseComponent[] deathMessage) {
        this.shooter = shooter;
        this.victim = victim;
        this.damage = damage;
        this.bullet = bullet;
        this.deathMessage = deathMessage;
    }

    public LivingEntity getShooter() {
        return shooter;
    }

    public LivingEntity getVictim() {
        return victim;
    }

    public float getDamage() {
        return damage;
    }

    public Bullet getBullet() {
        return bullet;
    }

    public BaseComponent[] getDeathMessage() {
        return deathMessage;
    }

    public void setDeathMessage(BaseComponent[] deathMessage) {
        this.deathMessage = deathMessage;
    }

    public boolean hasDeathMessage() {
        return deathMessage != null;
    }

    public boolean willKill() {
        return victim.getHealth() - damage >= 0;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }


}
