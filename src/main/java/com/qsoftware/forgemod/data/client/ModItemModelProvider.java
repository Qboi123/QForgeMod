package com.qsoftware.forgemod.data.client;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import com.qsoftware.silent.lib.block.IBlockProvider;
import com.qsoftware.silent.lib.util.NameUtils;
import com.qsoftware.forgemod.QForgeMod;
import com.qsoftware.forgemod.init.Metals;
import com.qsoftware.forgemod.init.ModBlocks;
import com.qsoftware.forgemod.init.ModItems;
import com.qsoftware.forgemod.init.Registration;
import com.qsoftware.forgemod.objects.item.CraftingItems;
import com.qsoftware.forgemod.objects.item.MachineUpgrades;

import java.util.Arrays;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, QForgeMod.MOD_ID, existingFileHelper);
    }

    @Override
    public String getName() {
        return "QForgeMod - Item Models";
    }

    @Override
    protected void registerModels() {
        Registration.BLOCKS.getEntries().forEach(block -> blockBuilder(block.get()));

        ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));

        //noinspection OverlyLongLambda
        Arrays.stream(Metals.values()).forEach(metal -> {
            metal.getOre().ifPresent(this::blockBuilder);
            metal.getStorageBlock().ifPresent(this::blockBuilder);
            metal.getChunks().ifPresent(item -> builder(item, itemGenerated));
            metal.getDust().ifPresent(item -> builder(item, itemGenerated));
            metal.getIngot().ifPresent(item -> builder(item, itemGenerated));
            metal.getNugget().ifPresent(item -> builder(item, itemGenerated));
        });
        Arrays.stream(CraftingItems.values()).forEach(item -> builder(item, itemGenerated));
        Arrays.stream(MachineUpgrades.values()).forEach(item -> builder(item, itemGenerated));

        builder(ModItems.WRENCH, itemGenerated);
        builder(ModItems.DEBUG_ITEM, itemGenerated);
        builder(ModItems.BATTERY, itemGenerated);
        builder(ModItems.HAND_PUMP, itemGenerated);
    }

    private void blockBuilder(IBlockProvider block) {
        blockBuilder(block.asBlock());
    }

    private void blockBuilder(Block block) {
        String name = NameUtils.from(block).getPath();
        if (block == ModBlocks.BATTERY_BOX.get()) {
            withExistingParent(name, modLoc("block/" + name + "_6"));
        } else {
            withExistingParent(name, modLoc("block/" + name));
        }
    }

    private void builder(IItemProvider item, ModelFile parent) {
        String name = NameUtils.fromItem(item).getPath();
        builder(item, parent, "item/" + name);
    }

    private void builder(IItemProvider item, ModelFile parent, String texture) {
        getBuilder(NameUtils.fromItem(item).getPath())
                .parent(parent)
                .texture("layer0", modLoc(texture));
    }
}
