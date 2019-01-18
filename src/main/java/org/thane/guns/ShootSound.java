package org.thane.guns;

import org.bukkit.Bukkit;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.thane.sounds.Sound;

import java.util.concurrent.ThreadLocalRandom;

public class ShootSound extends Sound {

    private String far;

    public ShootSound(String close, String far) {
        super(close, SoundCategory.VOICE, 100);
        this.far = far;
    }

    public void play(Player shooter, float pitchVariation) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(shooter.getUniqueId())) continue;

            if (super.getRange() >= 16) {
                double distance = shooter.getLocation().distance(player.getLocation());
                if (distance > 16) {
                    String sound = distance <= 20 ? super.getSound() : far;
                    player.playSound(percentMidPoint((float) (distance / super.getRange()), player.getLocation(),
                            shooter.getLocation()), sound, super.getCategory(),
                            (float) ThreadLocalRandom.current().nextDouble(1 - pitchVariation, 1 + pitchVariation), 1);
                    continue;
                }
            }
            player.playSound(player.getLocation(), super.getSound(), super.getCategory(),
                    (float) ThreadLocalRandom.current().nextDouble(1 - pitchVariation, 1 + pitchVariation), 1);
        }
    }

    public String getFar() {
        return far;
    }

    public void setFar(String far) {
        this.far = far;
    }

    public String getClose() {
        return super.getSound();
    }

    public void setClose(String close) {
        super.setSound(close);
    }
}
