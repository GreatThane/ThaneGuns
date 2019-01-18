package org.thane.packetWrappers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;

public class WrapperPlayServerAbilities extends AbstractPacket {
    public static final PacketType TYPE = PacketType.Play.Server.ABILITIES;

    public WrapperPlayServerAbilities() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerAbilities(PacketContainer packet) {
        super(packet, TYPE);
    }

    public boolean isInvulnerable() {
        return handle.getBooleans().read(0);
    }

    public void setInvulnerable(boolean value) {
        handle.getBooleans().write(0, value);
    }

    /**Misspelled.
     * @see #isInvulnerable()
     */
    @Deprecated
    public boolean isInvulnurable() {
        return handle.getBooleans().read(0);
    }

    /**Misspelled.
     * @see #setInvulnerable(boolean)
     */
    @Deprecated
    public void setInvulnurable(boolean value) {
        handle.getBooleans().write(0, value);
    }

    public boolean isFlying() {
        return handle.getBooleans().read(1);
    }

    public void setFlying(boolean value) {
        handle.getBooleans().write(1, value);
    }

    public boolean canFly() {
        return handle.getBooleans().read(2);
    }

    public void setCanFly(boolean value) {
        handle.getBooleans().write(2, value);
    }

    public boolean canInstantlyBuild() {
        return handle.getBooleans().read(3);
    }

    public void setCanInstantlyBuild(boolean value) {
        handle.getBooleans().write(3, value);
    }

    public float getFlyingSpeed() {
        return handle.getFloat().read(0);
    }

    public void setFlyingSpeed(float value) {
        handle.getFloat().write(0, value);
    }

    public float getWalkingSpeed() {
        return handle.getFloat().read(1);
    }

    public void setWalkingSpeed(float value) {
        handle.getFloat().write(1, value);
    }
}