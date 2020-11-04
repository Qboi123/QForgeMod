package com.qsoftware.forgemod.objects.items;

import com.qsoftware.forgemod.init.ItemInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class InfinityOre extends OreBlock {
    public InfinityOre(Properties properties) {
        super(properties);
    }

    @Override
    public void onBlockClicked(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player) {
        super.onBlockClicked(state, worldIn, pos, player);

        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        if (heldItemMainhand.isEmpty()) {
            player.getCombatTracker().trackDamage(new DamageSource("death.mine_infinity_ore"), player.getHealth(), player.getHealth());
            player.setHealth(0);
            player.onDeath(new DamageSource("death.mine.infinity_ore"));
        } else if (heldItemMainhand.getItem() != ItemInit.ULTRINIUM_SHOVEL &&
                heldItemMainhand.getItem() != ItemInit.ULTRINIUM_PICKAXE &&
                heldItemMainhand.getItem() != ItemInit.ULTRINIUM_HOE &&
                heldItemMainhand.getItem() != ItemInit.ULTRINIUM_SWORD &&
                heldItemMainhand.getItem() != ItemInit.ULTRINIUM_AXE &&
                heldItemMainhand.getItem() == ItemInit.INFINITY_SHOVEL &&
                heldItemMainhand.getItem() == ItemInit.INFINITY_PICKAXE &&
                heldItemMainhand.getItem() == ItemInit.INFINITY_HOE &&
                heldItemMainhand.getItem() == ItemInit.INFINITY_SWORD &&
                heldItemMainhand.getItem() == ItemInit.INFINITY_AXE) {
            if (heldItemMainhand.isDamageable()) {
                player.getHeldItemMainhand().damageItem(player.getHeldItemMainhand().getMaxDamage(), player, (entity) -> {
                    entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            } else {
                player.getCombatTracker().trackDamage(new DamageSource("death.mine_infinity_ore"), player.getHealth(), player.getHealth());
                player.setHealth(0);
                player.onDeath(new DamageSource("death.mine.infinity_ore"));
            }
        } else {
            player.getCombatTracker().trackDamage(new DamageSource("death.mine_infinity_ore"), player.getHealth(), player.getHealth());
            player.setHealth(0);
            player.onDeath(new DamageSource("death.mine.infinity_ore"));
        }
    }
}
