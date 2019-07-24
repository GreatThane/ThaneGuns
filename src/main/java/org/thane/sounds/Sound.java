package org.thane.sounds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;

public class Sound implements Cloneable, Serializable {

    private String sound;
    private SoundCategory category = SoundCategory.MASTER;
    private int range = 16;

    public Sound(String sound) {
        this.sound = sound;
    }

    public Sound(String sound, SoundCategory category) {
        this.sound = sound;
        this.category = category;
    }

    public Sound(String sound, int range) {
        this.sound = sound;
        this.range = range;
    }

    public Sound(String sound, SoundCategory category, int range) {
        this.sound = sound;
        this.category = category;
        this.range = range;
    }

    public void play(Location location, float pitchVariation, Player... players) {
        if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);

        for (Player player : players) {
            if (range >= 16) {
                double distance = location.distance(player.getLocation());
                if (distance > 16) {
                    player.playSound(percentMidPoint((float) (distance / range), player.getLocation(), location), sound, category,
                            (float) ThreadLocalRandom.current().nextDouble(1 - pitchVariation, 1 + pitchVariation), 1);
                }
            }
            player.playSound(location, sound, category, (float) ThreadLocalRandom.current().nextDouble(1 - pitchVariation, 1 + pitchVariation), 1);
        }
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public SoundCategory getCategory() {
        return category;
    }

    public void setCategory(SoundCategory category) {
        this.category = category;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public static Location percentMidPoint(float percent, Location start, Location end) {
        double x = start.getX() + (end.getX() - start.getX()) * percent;
        double y = start.getY() + (end.getY() - start.getY()) * percent;
        double z = start.getZ() + (end.getZ() - start.getZ()) * percent;
        return new Location(start.getWorld(), x, y, z);
    }

    @Override
    public Sound clone() {
        try {
            Sound sound = (Sound) super.clone();
            sound.sound = this.sound;
            sound.category = category;
            return sound;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
