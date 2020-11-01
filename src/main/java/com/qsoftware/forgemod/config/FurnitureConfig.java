package com.qsoftware.forgemod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Author: MrCrayfish
 */
public class FurnitureConfig
{
    public static class Client
    {
        public final ForgeConfigSpec.BooleanValue drawCollisionShapes;

        Client(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Client configuration settings").push("client");
            this.drawCollisionShapes = builder
                    .comment("Draws the collision shape rather than the selection shape when hovering blocks. Used for debugging collisions.")
                    .translation("qforgemod.configgui.drawCollisionShapes")
                    .define("drawCollisionShapes", false);
            builder.pop();
        }
    }

    public static class Common
    {
        public final ForgeConfigSpec.IntValue maxMailQueue;
        public final ForgeConfigSpec.IntValue pullMailInterval;

        Common(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Mail configuration settings").push("mail");
            this.maxMailQueue = builder
                    .comment("The maximum amount of mail that can be in a player's mail queue.")
                    .translation("qforgemod.configgui.maxMailQueue")
                    .defineInRange("maxMailQueue", 20, 1, Integer.MAX_VALUE);
            this.pullMailInterval = builder
                    .comment("The interval in ticks for mail boxes to pull mail from the player's queue")
                    .translation("qforgemod.configgui.pullMailInterval")
                    .defineInRange("pullMailInterval", 20, 1, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static final ForgeConfigSpec clientSpec;
    public static final FurnitureConfig.Client CLIENT;

    public static final ForgeConfigSpec commonSpec;
    public static final FurnitureConfig.Common COMMON;

    static
    {
        final Pair<FurnitureConfig.Client, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(FurnitureConfig.Client::new);
        clientSpec = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();

        final Pair<FurnitureConfig.Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(FurnitureConfig.Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }
}
