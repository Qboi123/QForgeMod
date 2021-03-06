package com.qsoftware.forgemod.modules.tiles.tileentities;

import com.qsoftware.forgemod.modules.tiles.ModTileEntities;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.IChestLid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(
   value = Dist.CLIENT,
   _interface = IChestLid.class
)
public class ChristmasChestTileEntity extends ChestTileEntity {
   public ChristmasChestTileEntity() {
      super(ModTileEntities.CHRISTMAS_CHEST.get());
   }
}
