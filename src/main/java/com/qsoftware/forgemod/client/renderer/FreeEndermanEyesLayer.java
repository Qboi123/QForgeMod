package com.qsoftware.forgemod.client.renderer;

import com.qsoftware.forgemod.client.model.FreeEndermanModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.AbstractEyesLayer;
import net.minecraft.client.renderer.entity.model.EndermanModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FreeEndermanEyesLayer<T extends EndermanEntity> extends AbstractEyesLayer<T, FreeEndermanModel<T>> {
   private final RenderType renderType;

   public FreeEndermanEyesLayer(IEntityRenderer<T, FreeEndermanModel<T>> rendererIn, RenderType renderType) {
      super(rendererIn);
      this.renderType = renderType;
   }

   public @NotNull RenderType getRenderType() {
      return renderType;
   }
}