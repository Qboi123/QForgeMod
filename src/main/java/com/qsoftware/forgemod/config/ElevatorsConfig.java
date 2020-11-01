package com.qsoftware.forgemod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import com.qsoftware.forgemod.QForgeUtils;

import static net.minecraftforge.fml.loading.LogMarkers.FORGEMOD;

@Mod.EventBusSubscriber(modid = QForgeUtils.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ElevatorsConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final CommonGeneral GENERAL = new CommonGeneral(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class CommonGeneral {
        public final ForgeConfigSpec.BooleanValue sameColor;
        public final ForgeConfigSpec.BooleanValue precisionTarget;
        public final ForgeConfigSpec.BooleanValue mobSpawn;
        public final ForgeConfigSpec.BooleanValue resetPitchNormal;
        public final ForgeConfigSpec.BooleanValue resetPitchDirectional;
        public final ForgeConfigSpec.IntValue range;
        public final ForgeConfigSpec.BooleanValue useXP;
        public final ForgeConfigSpec.IntValue XPPointsAmount;

        CommonGeneral(ForgeConfigSpec.Builder builder) {
            builder.push("General");

            sameColor = builder.worldRestart().comment("Should elevators have the same color in order to teleport ?").define("sameColor", false);
            range = builder.worldRestart().comment("Elevator range").defineInRange("range", 256, 3, 256);
            precisionTarget = builder.comment("Realign players to the center of elevator ?").define("precisionTarget", true);
            mobSpawn = builder.comment("Can mobs spawn on elevators ?").define("mobSpawn", false);
            resetPitchNormal = builder.comment("Reset pitch to 0 when teleporting to normal elevators ?").define("resetPitchNormal", false);
            resetPitchDirectional = builder.comment("Reset pitch to 0 when teleporting to directional elevators ?").define("resetPitchDirectional", true);
            useXP = builder.comment("Should teleporting require XP ?").define("useXP", false);
            XPPointsAmount = builder.comment("Amount of XP points to use when useXP is enabled", "Note this is NOT experience levels").defineInRange("XPPointsAmount", 1, 1, Integer.MAX_VALUE);

            builder.pop();
        }
    }

    // Same debug messages as forge config

    @SubscribeEvent
    public static void onLoad(final net.minecraftforge.fml.config.ModConfig.Loading configEvent) {
        LogManager.getLogger().debug(FORGEMOD, "Loaded elevator config file {}", configEvent.getConfig().getFileName());
    }

    @SubscribeEvent
    public static void onFileChange(final net.minecraftforge.fml.config.ModConfig.Reloading configEvent) {
        LogManager.getLogger().debug(FORGEMOD, "Elevator config just got changed on the file system!");
    }
}
