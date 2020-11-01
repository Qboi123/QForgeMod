package com.qsoftware.forgemod.objects.items;

import com.qsoftware.forgemod.groups.Groups;
import com.qsoftware.forgemod.init.ItemInit;
import com.qsoftware.forgemod.common.damagesource.DamageSourceInfinitySword;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InfinityAxeItem extends AxeItem {
    public InfinityAxeItem() {
        super(new IItemTier() {
            @Override
            public int getMaxUses() {
                return 99999;
            }

            @Override
            public float getEfficiency() {
                return 99999;
            }

            @Override
            public float getAttackDamage() {
                return 1;
            }

            @Override
            public int getHarvestLevel() {
                return 99999;
            }

            @Override
            public int getEnchantability() {
                return 99999;
            }

            @Override
            public @NotNull Ingredient getRepairMaterial() {
                return Ingredient.fromItems(ItemInit.INFINITY_INGOT);
            }
        }, 1, -0.0f, new Properties().group(Groups.OVERPOWERED));

        this.setRegistryName("infinity_axe");
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        player.world.destroyBlock(pos, true);
        return false;
    }

    @Override
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
            if (pvp.getHeldItem(Hand.MAIN_HAND) != null && pvp.getHeldItem(Hand.MAIN_HAND).getItem() == ItemInit.INFINITY_SWORD && pvp.isHandActive()) {
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
//                if (armor.get(0).getItem().equals(ItemInit.INFINITY_BOOTS)) {
//                    if (armor.get(1).getItem().equals(ItemInit.INFINITY_LEGGINGS)) {
//                        if (armor.get(2).getItem().equals(ItemInit.INFINITY_CHESTPLATE)) {
//                            if (armor.get(3).getItem().equals(ItemInit.INFINITY_HELMET)) {
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
            if (armor.get(0).getItem().equals(ItemInit.INFINITY_BOOTS)) {
                if (armor.get(1).getItem().equals(ItemInit.INFINITY_LEGGINGS)) {
                    if (armor.get(2).getItem().equals(ItemInit.INFINITY_CHESTPLATE)) {
                        if (armor.get(3).getItem().equals(ItemInit.INFINITY_HELMET)) {
                            return true;
                        }
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
                //TODO
                //player.addStat(Achievements.creative_kill, 1);
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
//                    if (armor.get(0).getItem().equals(ItemInit.INFINITY_BOOTS)) {
//                        if (armor.get(1).getItem().equals(ItemInit.INFINITY_LEGGINGS)) {
//                            if (armor.get(2).getItem().equals(ItemInit.INFINITY_CHESTPLATE)) {
//                                if (armor.get(3).getItem().equals(ItemInit.INFINITY_HELMET)) {
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