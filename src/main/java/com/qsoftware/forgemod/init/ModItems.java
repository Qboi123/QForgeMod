package com.qsoftware.forgemod.init;

import com.qsoftware.forgemod.QForgeUtils;
import com.qsoftware.forgemod.groups.Groups;
import com.qsoftware.forgemod.objects.item.*;
import com.qsoftware.forgemod.registration.impl.ItemRegistryObject;
import com.qsoftware.forgemod.util.color.ColorGetter;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;

import java.util.function.Supplier;

public final class ModItems {
    static {
        Metals.registerItems();
        CraftingItems.register();
        MachineUpgrades.register();
    }

    public static final ItemRegistryObject<WrenchItem> WRENCH = register("wrench", WrenchItem::new);
    public static final ItemRegistryObject<DebugItem> DEBUG_ITEM = register("debug_item", DebugItem::new);
    public static final ItemRegistryObject<BatteryItem> BATTERY = register("battery", BatteryItem::new);
    public static final ItemRegistryObject<HandPumpItem> HAND_PUMP = register("hand_pump", HandPumpItem::new);
    public static final ItemRegistryObject<CanisterItem> CANISTER = register("canister", () ->
            new CanisterItem(new Item.Properties().group(Groups.MISC)));
    public static final ItemRegistryObject<EmptyCanisterItem> EMPTY_CANISTER = register("empty_canister", () ->
            new EmptyCanisterItem(new Item.Properties().group(Groups.MISC)));

    public static final ItemRegistryObject<BucketItem> OIL_BUCKET = register("oil_bucket", () ->
            createBucketItem(() -> ModFluids.OIL));
    public static final ItemRegistryObject<BucketItem> DIESEL_BUCKET = register("diesel_bucket", () ->
            createBucketItem(() -> ModFluids.DIESEL));
    public static final ItemRegistryObject<NoPlaceBucketItem> ETHANE_BUCKET = register("ethane_bucket", () ->
            createNoPlaceBucketItem(() -> ModFluids.ETHANE));
    public static final ItemRegistryObject<NoPlaceBucketItem> POLYETHYLENE_BUCKET = register("polyethylene_bucket", () ->
            createNoPlaceBucketItem(() -> ModFluids.POLYETHYLENE));

    private ModItems() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerItemColors(ColorHandlerEvent.Item event) {
        event.getItemColors().register((stack, tintIndex) -> {
            if (tintIndex == 1) {
                return ColorGetter.getColor(CANISTER.get().getFluid(stack).getFluid());
            }
            return 0xFFFFFF;
        }, CANISTER);
    }

    private static BucketItem createBucketItem(Supplier<FlowingFluid> fluid) {
        return new BucketItem(fluid, new Item.Properties().group(Groups.FLUIDS).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static NoPlaceBucketItem createNoPlaceBucketItem(Supplier<Fluid> fluid) {
        return new NoPlaceBucketItem(fluid, new Item.Properties().group(Groups.FLUIDS).maxStackSize(1).containerItem(Items.BUCKET));
    }

    private static <T extends Item> ItemRegistryObject<T> register(String name, Supplier<T> item) {
        return new ItemRegistryObject<>(Registration.ITEMS.register(name, item));
    }
}