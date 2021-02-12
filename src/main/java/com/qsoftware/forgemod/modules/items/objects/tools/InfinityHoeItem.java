package com.qsoftware.forgemod.modules.items.objects.tools;

import com.qsoftware.forgemod.common.damagesource.DamageSourceInfinitySword;
import com.qsoftware.forgemod.modules.items.ModItems;
import com.qsoftware.forgemod.modules.ui.ModItemGroups;
import com.qsoftware.forgemod.modules.ui.ModStats;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Infinity hoe item class.
 *
 * @author Qboi123
 */
public class InfinityHoeItem extends HoeItem {
    @MethodsReturnNonnullByDefault
    public InfinityHoeItem() {
        super(new IItemTier() {
            @Override
            public int getMaxUses() {
                return Integer.MAX_VALUE;
            }

            @Override
            public float getEfficiency() {
                return Float.POSITIVE_INFINITY;
            }

            @Override
            public float getAttackDamage() {
                return Float.POSITIVE_INFINITY;
            }

            @Override
            public int getHarvestLevel() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getEnchantability() {
                return Integer.MAX_VALUE;
            }

            @Override
            public @NotNull Ingredient getRepairMaterial() {
                return Ingredient.fromItems(ModItems.INFINITY_INGOT.get());
            }
        }, Integer.MAX_VALUE, -0.0f, new Properties().group(ModItemGroups.OVERPOWERED).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        player.world.destroyBlock(pos, true);
        return false;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hitEntity(ItemStack stack, LivingEntity victim, LivingEntity player) {

        if (player.world.isRemote) {
            return true;
        }
        if (victim instanceof PlayerEntity) {
            PlayerEntity pvp = (PlayerEntity) victim;
            if (isInfinite(pvp)) {
                victim.attackEntityFrom(new DamageSourceInfinitySword(player).setDamageBypassesArmor(), 4.0F);
                return true;
            }
            if (pvp.getHeldItem(Hand.MAIN_HAND).getItem() == ModItems.INFINITY_SWORD.get() && pvp.isHandActive()) {
                return true;
            }
        }

        victim.recentlyHit = 60;
        victim.getCombatTracker().trackDamage(new DamageSourceInfinitySword(player), victim.getHealth(), victim.getHealth());
        victim.setHealth(0);
        victim.onDeath(new EntityDamageSource("infinity", player));

//        return true;
//        if (target instanceof PlayerEntity) {
//            // Get player
//            PlayerEntity player1 = (PlayerEntity) target;
//
//            // Check Armor
//            if (!armor.isEmpty()) {
//                if (armor.get(0).getItem().equals(ModItems.INFINITY_BOOTS.get())) {
//                    if (armor.get(1).getItem().equals(ModItems.INFINITY_LEGGINGS.get())) {
//                        if (armor.get(2).getItem().equals(ModItems.INFINITY_CHESTPLATE.get())) {
//                            if (armor.get(3).getItem().equals(ModItems.INFINITY_HELMET.get())) {
//                                return false;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        EntityUtils.instantKill(target, "infinity_sword");
        return true;
    }

    private boolean isInfinite(PlayerEntity player) {
        // Get armor list.
        List<ItemStack> armor = (List<ItemStack>) player.getArmorInventoryList();

        // Check Armor
        if (!armor.isEmpty()) {
            if (armor.get(0).getItem().equals(ModItems.INFINITY_BOOTS.get())) {
                if (armor.get(1).getItem().equals(ModItems.INFINITY_LEGGINGS.get())) {
                    if (armor.get(2).getItem().equals(ModItems.INFINITY_CHESTPLATE.get())) {
                        return armor.get(3).getItem().equals(ModItems.INFINITY_HELMET.get());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, PlayerEntity player, Entity entity) {
        if (!entity.world.isRemote && entity instanceof PlayerEntity) {
            PlayerEntity victim = (PlayerEntity) entity;
            if (victim.isCreative() && !(victim.getHealth() <= 0) && victim.getHealth() > 0 && !isInfinite(victim)) {
                victim.getCombatTracker().trackDamage(new DamageSourceInfinitySword(player), victim.getHealth(), victim.getHealth());
                victim.setHealth(0);
                victim.onDeath(new EntityDamageSource("infinity", player));
                player.addStat(ModStats.INFINITY_KILL, 1);
                return true;
            }
        }
        return false;
//        if (entity instanceof LivingEntity) {
//            LivingEntity target = (LivingEntity) entity;
//
//            if (target instanceof PlayerEntity) {
//                // Get player
//                PlayerEntity player1 = (PlayerEntity) target;
//
//                // Get armor list.
//                List<ItemStack> armor = (List<ItemStack>) player1.getArmorInventoryList();
//
//                // Check Armor
//                if (!armor.isEmpty()) {
//                    if (armor.get(0).getItem().equals(ModItems.INFINITY_BOOTS.get())) {
//                        if (armor.get(1).getItem().equals(ModItems.INFINITY_LEGGINGS.get())) {
//                            if (armor.get(2).getItem().equals(ModItems.INFINITY_CHESTPLATE.get())) {
//                                if (armor.get(3).getItem().equals(ModItems.INFINITY_HELMET.get())) {
//                                    return true;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//
//            EntityUtils.instantKill(target, "infinity_sword");
//        }
//        return false;
//        return super.onLeftClickEntity(stack, player, entity);
    }
}
