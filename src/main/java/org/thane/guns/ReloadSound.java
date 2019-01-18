package org.thane.guns;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.thane.sounds.Sound;
import org.thane.utils.TaskUtil;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ReloadSound extends Sound {

    private boolean intervalLoading = false;
    private int bulletCount = 0;
    private String end;
    private int duration;

    public ReloadSound(String sound, int bulletCount, int duration) {
        super(sound);
        intervalLoading = true;
        this.bulletCount = bulletCount;
        this.duration = duration;
    }

    public ReloadSound(String start, String end, int duration) {
        super(start);
        this.end = end;
        this.duration = duration;
    }

    public boolean isIntervalLoading() {
        return intervalLoading;
    }

    public void setIntervalLoading(boolean intervalLoading) {
        this.intervalLoading = intervalLoading;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(int bulletCount) {
        this.bulletCount = bulletCount;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return super.getSound();
    }

    public void setStart(String start) {
        super.setSound(start);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void play(Player shooter) {
        if (!intervalLoading) {
            for (Player player : shooter.getNearbyEntities(20, 20, 20).stream()
                    .filter(e -> e instanceof Player && !e.getUniqueId().equals(shooter.getUniqueId()))
                    .map(e -> (Player) e)
                    .collect(Collectors.toSet())) {

                player.playSound(player.getLocation(), super.getSound(), super.getCategory(), 1, 1);
            }
            new TaskUtil(() -> {
                for (Player player : shooter.getNearbyEntities(20, 20, 20).stream()
                        .filter(e -> e instanceof Player && !e.getUniqueId().equals(shooter.getUniqueId()))
                        .map(e -> (Player) e)
                        .collect(Collectors.toSet())) {
                    if (player.getUniqueId().equals(shooter.getUniqueId())) continue;

                    player.playSound(player.getLocation(), end, super.getCategory(), 1, 1);
                }
            }, duration).runSync();
        } else {
            TaskUtil util = new TaskUtil(() -> {
                for (Player player : shooter.getNearbyEntities(20, 20, 20).stream()
                        .filter(e -> e instanceof Player && !e.getUniqueId().equals(shooter.getUniqueId()))
                        .map(e -> (Player) e)
                        .collect(Collectors.toSet())) {
                    if (player.getUniqueId().equals(shooter.getUniqueId())) continue;

                    player.playSound(player.getLocation(), super.getSound(), super.getCategory(), 1, 1);
                }
            });
            for (int i = 0; i <= duration; i += duration / bulletCount) {
                util.setDelay(i).runSync();
            }
        }
    }
}
