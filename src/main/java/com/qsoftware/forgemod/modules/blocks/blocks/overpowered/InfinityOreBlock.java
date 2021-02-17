package com.qsoftware.forgemod.modules.blocks.blocks.overpowered;

import com.qsoftware.forgemod.modules.items.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

/**
 * Infinity ore block class.
 *
 * @author Qboi123
 */
@SuppressWarnings("deprecation")
public class InfinityOreBlock extends OreBlock {
    public InfinityOreBlock(Properties properties) {
        super(properties);
    }
    @Override
    public void onBlockClicked(@NotNull BlockState state, @NotNull World worldIn, @NotNull BlockPos pos, @NotNull PlayerEntity player) {
        super.onBlockClicked(state, worldIn, pos, player);

        ItemStack heldItemMainhand = player.getHeldItemMainhand();
        if (heldItemMainhand.isEmpty()) {
            player.getCombatTracker().trackDamage(new DamageSource("mine_infinity_ore"), player.getHealth(), player.getHealth());
            player.setHealth(0);

            Minecraft.getInstance().enqueue(() -> {
                player.onDeath(new DamageSource("mine.infinity_ore"));
            });
        } else if (heldItemMainhand.getItem() != ModItems.ULTRINIUM_PICKAXE.get() &&
                heldItemMainhand.getItem() != ModItems.INFINITY_SHOVEL.get() &&
                heldItemMainhand.getItem() != ModItems.INFINITY_PICKAXE.get() &&
                heldItemMainhand.getItem() != ModItems.INFINITY_HOE.get() &&
                heldItemMainhand.getItem() != ModItems.INFINITY_SWORD.get() &&
                heldItemMainhand.getItem() != ModItems.INFINITY_AXE.get()) {
            if (heldItemMainhand.isDamageable()) {
                player.getHeldItemMainhand().damageItem(player.getHeldItemMainhand().getMaxDamage(), player, (entity) -> {
                    entity.sendBreakAnimation(EquipmentSlotType.MAINHAND);
                });
            } else {
                player.getCombatTracker().trackDamage(new DamageSource("mine_infinity_ore"), player.getHealth(), player.getHealth());
                player.setHealth(0);

                Minecraft.getInstance().enqueue(() -> {
                    player.onDeath(new DamageSource("mine.infinity_ore"));
                });
            }
        }
    }
}