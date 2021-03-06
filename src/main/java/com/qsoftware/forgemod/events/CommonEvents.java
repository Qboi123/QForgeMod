package com.qsoftware.forgemod.events;

import com.qsoftware.forgemod.QForgeMod;
import com.qsoftware.forgemod.capability.EnergyStorageImplBase;
import com.qsoftware.forgemod.modules.items.objects.energy.BatteryItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = QForgeMod.modId)
public class CommonEvents {
    @SubscribeEvent
    public static void onAttachItemCaps(AttachCapabilitiesEvent<Item> event) {
        if (event.getObject() instanceof BatteryItem) {
            event.addCapability(QForgeMod.rl("energy"), new EnergyStorageImplBase(500_000, 10_000, 10_000));
        }
    }
}
