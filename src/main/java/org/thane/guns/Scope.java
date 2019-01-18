package org.thane.guns;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.thane.ThaneGuns;
import org.thane.events.ZoomChangeEvent;
import org.thane.packetWrappers.WrapperPlayServerAbilities;
import org.thane.utils.*;
import org.thane.utils.scripts.InvokableJS;
import org.thane.utils.scripts.JSExpression;
import org.thane.utils.scripts.JSFunction;

import java.util.*;

public class Scope {

    static final Map<Player, Scope> zoomedPlayers = new HashMap<>();

    private static final String ZOOM_THEMING_KEY = "zoom theming";
    private static final String ZOOM_FUNCTION_KEY = "zoom function";
    private static final String ZOOM_SCRIPT_KEY = "zoom";

    private static InvokableJS ZOOM_FUNCTION = InvokableJS.getFunction(ThaneGuns.getPlugin().getConfig().getString(ZOOM_FUNCTION_KEY));

    public static Material DEFAULT_SCOPE = Material.matchMaterial(ThaneGuns.getPlugin().getConfig().getString("default scope"));
    public static short DEFAULT_SCOPE_DATA = (short) ThaneGuns.getPlugin().getConfig().getInt("default scope data");
    public static List<Float> DEFAULT_ZOOM_LEVELS = ThaneGuns.getPlugin().getConfig().getFloatList("default zoom levels");

    private List<Float> zoomLevels;

    private Material scope;
    private short scopeData;

    private int index = 0;

    public Scope() {
        this(DEFAULT_SCOPE, DEFAULT_SCOPE_DATA);
    }

    public Scope(Material scope) {
        this(scope, (short) 0);
    }

    public Scope(Material material, short scopeData) {
        this(material, scopeData, DEFAULT_ZOOM_LEVELS);
    }

    public Scope(Material material, List<Float> zoomLevels) {
        this(material, (short) 0, zoomLevels);
    }

    public Scope(Material material, Float... zoomLevels) {
        this(material, Arrays.asList(zoomLevels));
    }

    public Scope(Material material, short scopeData, Float... zoomLevels) {
        this(material, scopeData, Arrays.asList(zoomLevels));
    }

    public Scope(Material material, short scopeData, List<Float> zoomLevels) {
        scope = material;
        this.scopeData = scopeData;
        this.zoomLevels = zoomLevels;
        Collections.sort(this.zoomLevels);
    }

    public float getCurrentZoom() {
        return zoomLevels.get(index);
    }

    public void setCurrentZoom(float zoom) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoom, this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) index = zoomLevels.indexOf(zoom);
    }

    public void setCurrentZoom(int index) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(this.index), zoomLevels.get(index), this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) this.index = index;
    }

    public Material getScope() {
        return scope;
    }

    public void setScope(Material scope) {
        this.scope = scope;
    }

    public void setZoomLevels(Float... zoomLevels) {
        this.zoomLevels = Arrays.asList(zoomLevels);
    }

    public void increaseZoom() {
        increaseZoom(1);
    }

    public void increaseZoom(int amount) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoomLevels.get(index + amount), this);
        Bukkit.getPluginManager().callEvent(event);

        if (!event.isCancelled()) index += amount;
    }

    public void decreaseZoom() {
        decreaseZoom(1);
    }

    public void decreaseZoom(int amount) {
        ZoomChangeEvent event = new ZoomChangeEvent(zoomLevels.get(index), zoomLevels.get(index - amount), this);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) index -= amount;
    }

    public List<Float> getZoomLevels() {
        return zoomLevels;
    }

    public void setZoomLevels(List<Float> zoomLevels) {
        this.zoomLevels = zoomLevels;
    }

    public short getScopeData() {
        return scopeData;
    }

    public void setScopeData(short scopeData) {
        this.scopeData = scopeData;
    }

    public void sendPacket(Player player) {
        WrapperPlayServerAbilities packet = new WrapperPlayServerAbilities();

        if (getCurrentZoom() == 1) {
            packet.setWalkingSpeed(0.2F);
            player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
            zoomedPlayers.remove(player);
        } else {

            if (player.getInventory().getItemInOffHand() == null
                    || player.getInventory().getItemInOffHand().getType() == Material.AIR) {
                player.getInventory().setItemInOffHand(new ItemStack(scope, 1, scopeData));
                zoomedPlayers.put(player, this);
            }
            if (ZOOM_FUNCTION instanceof JSExpression) {

                JSExpression expression = ((JSExpression) ZOOM_FUNCTION);
                expression.setVariables(new HashMap<String, Object>() {{
                    this.put(ZOOM_SCRIPT_KEY, zoomLevels.get(index));
                }});
                packet.setWalkingSpeed((Float) expression.invoke(false));
            } else {
                JSFunction function = (JSFunction) ZOOM_FUNCTION;

                packet.setWalkingSpeed((Float) function
                        .invoke(zoomLevels.get(index)));
            }
        }
        switch (player.getGameMode()) {
            case CREATIVE:
                packet.setCanInstantlyBuild(true);
            case SPECTATOR:
                packet.setInvulnerable(true);
                packet.setCanFly(true);
                packet.setFlyingSpeed(0.085F);
        }
        packet.sendPacket(player);
    }

    private static final InvokableJS ZOOM_THEMING_FUNCTION = InvokableJS.getFunction(ThaneGuns.getPlugin().getConfig().getString(ZOOM_THEMING_KEY));

    private static final String ZOOM_INDEX_KEY = "index";
    private static final String ZOOM_LEVELS_KEY = "zoomLevels";

    private static final String ACTION_BAR_ID = "zoom message";

    static {
        ActionBarRunnable runnable;
        if (ZOOM_THEMING_FUNCTION instanceof JSExpression) {
            runnable = player -> {
                if (!zoomedPlayers.containsKey(player)) return null;

                ((JSExpression) ZOOM_THEMING_FUNCTION).setVariable(ZOOM_INDEX_KEY, zoomedPlayers.get(player).index);
                ((JSExpression) ZOOM_THEMING_FUNCTION).setVariable(ZOOM_LEVELS_KEY, zoomedPlayers.get(player).zoomLevels.toArray(new Float[0]));
                return (String) ((JSExpression) ZOOM_THEMING_FUNCTION).invoke(false);
            };
        } else {
            runnable = player -> {
                if (!zoomedPlayers.containsKey(player)) return null;

                return (String) ((JSFunction) ZOOM_THEMING_FUNCTION).invoke(zoomedPlayers.get(player).zoomLevels, zoomedPlayers.get(player).index);
            };
        }
        ActionBarManager.addMessage(ACTION_BAR_ID, runnable);
        ActionBarManager.addOrder(ACTION_BAR_ID);
    }
}
