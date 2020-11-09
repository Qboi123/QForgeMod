package com.qsoftware.forgemod.objects.item;

import com.qsoftware.forgemod.groups.Groups;
import com.qsoftware.forgemod.init.Registration;
import net.minecraft.item.Food;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;
import net.silentchaos512.lib.registry.ItemRegistryObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;

import javax.annotation.Nullable;

public enum CraftingItems implements IItemProvider {
    // Other dusts
    COAL_DUST,
    // Crafting items
    CIRCUIT_BOARD,
    HEATING_ELEMENT,
    SOLDER,
    PLASTIC_PELLETS,
    PLASTIC_SHEET,
    UPGRADE_CASE,
    // Others
    ZOMBIE_LEATHER,
    BEEF_JERKY(Foods.COOKED_BEEF),
    PORK_JERKY(Foods.COOKED_PORKCHOP),
    CHICKEN_JERKY(Foods.COOKED_CHICKEN),
    MUTTON_JERKY(Foods.COOKED_MUTTON),
    RABBIT_JERKY(Foods.COOKED_RABBIT),
    COD_JERKY(Foods.COOKED_COD),
    SALMON_JERKY(Foods.SALMON),
    ;

    private final Food food;
    @SuppressWarnings("NonFinalFieldInEnum") private ItemRegistryObject<Item> item;

    CraftingItems() {
        this(null);
    }

    CraftingItems(@Nullable Food food) {
        this.food = food;
    }

    public static void register() {
        for (CraftingItems item : values()) {
            item.item = new ItemRegistryObject<>(Registration.ITEMS.register(item.getName(), () -> new Item(createProperties(item.food))));
        }
    }

    private static Item.Properties createProperties(@Nullable Food food) {
        if (food != null) {
            return new Item.Properties().group(Groups.FOOD).food(food);
        }
        return new Item.Properties().group(Groups.MISC);
    }

    public String getName() {
//        return TextUtils.lower(name());
        return StringUtils.lowerCase(name());
    }

    @Override
    public Item asItem() {
        return item.get();
    }
}