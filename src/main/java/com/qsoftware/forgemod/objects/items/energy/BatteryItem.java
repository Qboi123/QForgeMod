package com.qsoftware.forgemod.objects.items.energy;

import com.qsoftware.forgemod.groups.Groups;
import net.minecraft.item.Rarity;

public class BatteryItem extends EnergyStoringItem {
    private static final int MAX_ENERGY = 500_000;
    private static final int MAX_TRANSFER = 500;

    public BatteryItem() {
        super(new Properties().group(Groups.MACHINES).maxStackSize(1).rarity(Rarity.UNCOMMON), MAX_ENERGY, MAX_TRANSFER);
    }
}