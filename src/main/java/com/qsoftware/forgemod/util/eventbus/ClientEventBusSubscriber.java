package com.qsoftware.forgemod.util.eventbus;

import com.qsoftware.forgemod.QForgeMod;
import com.qsoftware.forgemod.client.renderer.*;
import com.qsoftware.forgemod.client.renderer.entity.RenderBabyCreeper;
import com.qsoftware.forgemod.client.renderer.entity.RenderBabyEnderman;
import com.qsoftware.forgemod.init.types.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.entity.StrayRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Client eventbus subscriber.
 * Currently used for registering renderers for entities.
 *
 * @author Qboi123
 */
@Mod.EventBusSubscriber(modid = QForgeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        // Screen factories.
//        ScreenManager.registerFactory(ContainerTypesInit.EXAMPLE_CHEST, ExampleChestScreen::new);

        // Entity renderers.

        // Baby variants
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABY_CREEPER.get(), RenderBabyCreeper::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABY_ENDERMAN.get(), RenderBabyEnderman::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABY_SKELETON.get(), SkeletonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABY_STRAY.get(), StrayRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BABY_WITHER_SKELETON.get(), WitherSkeletonRenderer::new);

        // Entities
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.HOG.get(), HogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.WARTHOG.get(), WarthogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.BISON.get(), BisonRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.OX.get(), OxRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DUCK.get(), DuckRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.MOOBLOOM.get(), MoobloomRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ICE_ENDERMAN.get(), IceEndermanRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.FIRE_CREEPER.get(), FireCreeperRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.GLOW_SQUID.get(), GlowSquidRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.LEGENDARY_ENDER_PEARL.get(), manager -> new SpriteRenderer<>(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.DYNAMITE.get(), manager -> new SpriteRenderer<>(Minecraft.getInstance().getRenderManager(), Minecraft.getInstance().getItemRenderer()));
    }
}
