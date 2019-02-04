package org.thane.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.thane.events.DamageReportEvent;

public class DamageReport {

    private LivingEntity damager;
    private LivingEntity victim;
    private float damage;

    public DamageReport(LivingEntity damager, LivingEntity victim, float damage) {
        this.damager = damager;
        this.victim = victim;
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public LivingEntity getDamager() {
        return damager;
    }

    public void setDamager(LivingEntity damager) {
        this.damager = damager;
    }

    public LivingEntity getVictim() {
        return victim;
    }

    public void setVictim(LivingEntity victim) {
        this.victim = victim;
    }

    public boolean finalizeKill(boolean callEvent) {
        if (callEvent) {
//            DamageReportEvent event = new DamageReportEvent(victim, damager, EntityDamageEvent.DamageCause.CUSTOM, damage);
//            Bukkit.getPluginManager().callEvent(event);
//            if (!event.isCancelled()) {
//                victim.damage(damage);
//                return victim.getHealth() - damage <= 0;
//            }
            return false;
        } else {
            victim.damage(damage);
            return victim.getHealth() - damage <= 0;
        }
    }
}
