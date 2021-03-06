package com.qsoftware.forgemod.modules.tiles.blocks.base;

import com.qsoftware.forgemod.common.OreProperties;
import com.qsoftware.forgemod.common.interfaces.IHasOreProperties;
import net.minecraft.block.OreBlock;

public class BaseOreBlock extends OreBlock implements IHasOreProperties {
    private final OreProperties oreProperties;

    public BaseOreBlock(Properties properties, OreProperties oreProperties) {
        super(properties);
        this.oreProperties = oreProperties;
    }

    @Override
    public OreProperties getOreProperties() {
        return oreProperties;
    }
}
