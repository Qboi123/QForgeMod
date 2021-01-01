package com.qsoftware.forgemod.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;

import java.util.Random;
import java.util.UUID;

public class CurseEffect extends Effect {
    public CurseEffect() {
        super(EffectType.HARMFUL, 0xff00ff);
        this.addAttributesModifier(Attributes.LUCK, "CURSED!!!"/*""CC5AF142-2BD2-4215-B636-2605AED11727"*/, -1.5D, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        int j = 25 >> amplifier;
        if (j > 0) {
            return duration % j == 0;
        } else {
            return true;
        }
    }

    @Override
    public void performEffect(LivingEntity entityLivingBaseIn, int amplifier) {
        Random rng = entityLivingBaseIn.getRNG();
        switch (rng.nextInt(4)) {
            case 0: {
                entityLivingBaseIn.attackEntityFrom(DamageSource.WITHER, 1.0F);
                break;
            }
            case 1: {
                entityLivingBaseIn.moveForced(((rng.nextDouble() - 0.5d) * 2d) * 10d, ((rng.nextDouble() - 0.5d) * 2d) * 10d, ((rng.nextDouble() - 0.5d) * 2d) * 10d);
                break;
            }
            case 2: {
                entityLivingBaseIn.setJumping(rng.nextBoolean());
                break;
            }
            case 3: {
                entityLivingBaseIn.setSneaking(rng.nextBoolean());
                break;
            }
            case 4: {
                entityLivingBaseIn.setFire(2);
                break;
            }
        }
    }
}