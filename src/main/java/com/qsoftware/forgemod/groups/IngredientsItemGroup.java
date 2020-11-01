package com.qsoftware.forgemod.groups;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.NotNull;

public class IngredientsItemGroup extends ItemGroup {
    public static final IngredientsItemGroup instance = new IngredientsItemGroup(ItemGroup.GROUPS.length, "ingredients_tab");

    public IngredientsItemGroup(int index, String label) {
        super(index, label);
    }

    @Override
    public @NotNull ItemStack createIcon() {
        return new ItemStack(Items.STRING);
    }
}