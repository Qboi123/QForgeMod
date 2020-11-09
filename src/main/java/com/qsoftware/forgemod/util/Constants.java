package com.qsoftware.forgemod.util;

import net.minecraft.util.ResourceLocation;
import com.qsoftware.forgemod.QForgeUtils;

public final class Constants {
    public static final ResourceLocation ALLOY_SMELTING = QForgeUtils.getId("alloy_smelting");
    public static final ResourceLocation COMPRESSING = QForgeUtils.getId("compressing");
    public static final ResourceLocation CRUSHING = QForgeUtils.getId("crushing");
    public static final ResourceLocation DRYING = QForgeUtils.getId("drying");
    public static final ResourceLocation INFUSING = QForgeUtils.getId("infusing");
    public static final ResourceLocation MIXING = QForgeUtils.getId("mixing");
    public static final ResourceLocation REFINING = QForgeUtils.getId("refining");
    public static final ResourceLocation SOLIDIFYING = QForgeUtils.getId("solidifying");

    // Machine upgrades
    public static final int UPGRADES_PER_SLOT = 1;
    public static final float UPGRADE_PROCESSING_SPEED_AMOUNT = 0.5f;
    public static final float UPGRADE_SECONDARY_OUTPUT_AMOUNT = 0.1f;
    public static final float UPGRADE_ENERGY_EFFICIENCY_AMOUNT = -0.15f;
    public static final int UPGRADE_RANGE_AMOUNT = 2;

    private Constants() {throw new IllegalAccessError("Utility class");}
}