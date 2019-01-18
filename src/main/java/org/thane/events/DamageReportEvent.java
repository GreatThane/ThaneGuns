package org.thane.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageReportEvent extends EntityDamageEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private LivingEntity damager;

    public DamageReportEvent(LivingEntity victim, LivingEntity damager, DamageCause cause, double damage) {
        super(victim, cause, damage);
        this.damager = damager;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public LivingEntity getVictim() {
        return (LivingEntity) super.entity;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
