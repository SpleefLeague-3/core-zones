package com.spleefleague.zone.gear.steampack;

import com.spleefleague.core.player.CorePlayer;
import com.spleefleague.core.world.projectile.global.GlobalWorld;
import com.spleefleague.zone.gear.Gear;
import org.bukkit.inventory.ItemStack;

/**
 * @author NickM13
 * @since 3/1/2021
 */
public class GearSteamPack extends Gear {

    public GearSteamPack() {
        super(GearType.STEAM_PACK);
    }

    public GearSteamPack(String identifier, String name) {
        super(GearType.STEAM_PACK, identifier, name);
    }

    @Override
    protected boolean onActivate(CorePlayer corePlayer) {
        GlobalWorld globalWorld = corePlayer.getGlobalWorld();

        return false;
    }

    @Override
    public ItemStack getGearItem(CorePlayer corePlayer) {
        return null;
    }

    @Override
    protected void createGearItems() {

    }

    @Override
    public void update() {

    }

}
