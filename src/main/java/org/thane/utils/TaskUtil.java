package org.thane.utils;

import org.bukkit.Bukkit;
import org.thane.ThaneGuns;

public class TaskUtil {

    private long delay = 0;
    private long period = 0;
    private Runnable runnable;

    public TaskUtil(Runnable runnable) {
        this.runnable = runnable;
    }

    public TaskUtil(Runnable runnable, long delay) {
        this.runnable = runnable;
        this.delay = delay;
    }

    public TaskUtil(Runnable runnable, long delay, long period) {
        this.runnable = runnable;
        this.delay = delay;
        this.period = period;
    }

    public long getDelay() {
        return delay;
    }

    public TaskUtil setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    public long getPeriod() {
        return period;
    }

    public TaskUtil setPeriod(long period) {
        this.period = period;
        return this;
    }

    public Runnable getRunnable() {
        return runnable;
    }

    public TaskUtil setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public int runSync() {
        if (period == 0) {
            if (delay == 0) {
                return Bukkit.getScheduler().runTask(ThaneGuns.getPlugin(), runnable).getTaskId();
            }
            return Bukkit.getScheduler().runTaskLater(ThaneGuns.getPlugin(), runnable, delay).getTaskId();
        }
        return Bukkit.getScheduler().runTaskTimer(ThaneGuns.getPlugin(), runnable, delay, period).getTaskId();
    }

    public int runAsync() {
        if (period == 0) {
            if (delay == 0) {
                return Bukkit.getScheduler().runTaskAsynchronously(ThaneGuns.getPlugin(), runnable).getTaskId();
            }
            return Bukkit.getScheduler().runTaskLaterAsynchronously(ThaneGuns.getPlugin(), runnable, delay).getTaskId();
        }
        return Bukkit.getScheduler().runTaskTimerAsynchronously(ThaneGuns.getPlugin(), runnable, delay,period).getTaskId();
    }
}
