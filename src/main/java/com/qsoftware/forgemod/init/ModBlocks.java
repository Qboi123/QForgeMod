package com.qsoftware.forgemod.init;

import com.qsoftware.forgemod.groups.Groups;
import com.qsoftware.forgemod.objects.block.quarry.QuarryBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import com.qsoftware.silent.lib.registry.BlockRegistryObject;
import com.qsoftware.forgemod.QForgeMod;
import com.qsoftware.forgemod.objects.block.AbstractMachineBlock;
import com.qsoftware.forgemod.objects.block.MachineFrameBlock;
import com.qsoftware.forgemod.objects.block.alloysmelter.AlloySmelterBlock;
import com.qsoftware.forgemod.objects.block.batterybox.BatteryBoxBlock;
import com.qsoftware.forgemod.objects.block.compressor.CompressorBlock;
import com.qsoftware.forgemod.objects.block.crusher.CrusherBlock;
import com.qsoftware.forgemod.objects.block.dryingrack.DryingRackBlock;
import com.qsoftware.forgemod.objects.block.electricfurnace.ElectricFurnaceBlock;
import com.qsoftware.forgemod.objects.block.generator.coal.CoalGeneratorBlock;
import com.qsoftware.forgemod.objects.block.generator.diesel.DieselGeneratorBlock;
import com.qsoftware.forgemod.objects.block.generator.lava.LavaGeneratorBlock;
import com.qsoftware.forgemod.objects.block.infuser.InfuserBlock;
import com.qsoftware.forgemod.objects.block.mixer.MixerBlock;
import com.qsoftware.forgemod.objects.block.pipe.PipeBlock;
import com.qsoftware.forgemod.objects.block.pump.PumpBlock;
import com.qsoftware.forgemod.objects.block.refinery.RefineryBlock;
import com.qsoftware.forgemod.objects.block.solidifier.SolidifierBlock;
import com.qsoftware.forgemod.objects.block.wire.WireBlock;
import com.qsoftware.forgemod.util.MachineTier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ModBlocks {
    static {
        Metals.registerBlocks();
    }

    public static final BlockRegistryObject<DryingRackBlock> OAK_DRYING_RACK = register("oak_drying_rack", DryingRackBlock::new);
    public static final BlockRegistryObject<DryingRackBlock> BIRCH_DRYING_RACK = register("birch_drying_rack", DryingRackBlock::new);
    public static final BlockRegistryObject<DryingRackBlock> SPRUCE_DRYING_RACK = register("spruce_drying_rack", DryingRackBlock::new);
    public static final BlockRegistryObject<DryingRackBlock> JUNGLE_DRYING_RACK = register("jungle_drying_rack", DryingRackBlock::new);
    public static final BlockRegistryObject<DryingRackBlock> DARK_OAK_DRYING_RACK = register("dark_oak_drying_rack", DryingRackBlock::new);
    public static final BlockRegistryObject<DryingRackBlock> ACACIA_DRYING_RACK = register("acacia_drying_rack", DryingRackBlock::new);

    public static final BlockRegistryObject<MachineFrameBlock> STONE_MACHINE_FRAME = register("stone_machine_frame", () ->
            new MachineFrameBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 10).sound(SoundType.STONE).notSolid()));
    public static final BlockRegistryObject<MachineFrameBlock> ALLOY_MACHINE_FRAME = register("alloy_machine_frame", () ->
            new MachineFrameBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(3, 10).sound(SoundType.METAL).notSolid()));

    public static final BlockRegistryObject<AlloySmelterBlock> BASIC_ALLOY_SMELTER = register("basic_alloy_smelter", () ->
            new AlloySmelterBlock(MachineTier.BASIC));
    public static final BlockRegistryObject<AlloySmelterBlock> ALLOY_SMELTER = register("alloy_smelter", () ->
            new AlloySmelterBlock(MachineTier.STANDARD));
    public static final BlockRegistryObject<CrusherBlock> BASIC_CRUSHER = register("basic_crusher", () ->
            new CrusherBlock(MachineTier.BASIC));
    public static final BlockRegistryObject<CrusherBlock> CRUSHER = register("crusher", () ->
            new CrusherBlock(MachineTier.STANDARD));
    public static final BlockRegistryObject<CompressorBlock> COMPRESSOR = register("compressor", CompressorBlock::new);
    public static final BlockRegistryObject<ElectricFurnaceBlock> ELECTRIC_FURNACE = register("electric_furnace", ElectricFurnaceBlock::new);
    public static final BlockRegistryObject<RefineryBlock> REFINERY = register("refinery", RefineryBlock::new);
    public static final BlockRegistryObject<MixerBlock> MIXER = register("mixer", MixerBlock::new);
    public static final BlockRegistryObject<SolidifierBlock> SOLIDIFIER = register("solidifier", SolidifierBlock::new);
    public static final BlockRegistryObject<InfuserBlock> INFUSER = register("infuser", InfuserBlock::new);
    public static final BlockRegistryObject<PumpBlock> PUMP = register("pump", PumpBlock::new);
    public static final BlockRegistryObject<QuarryBlock> QUARRY = register("quarry", QuarryBlock::new);
    public static final BlockRegistryObject<CoalGeneratorBlock> COAL_GENERATOR = register("coal_generator", CoalGeneratorBlock::new);
    public static final BlockRegistryObject<LavaGeneratorBlock> LAVA_GENERATOR = register("lava_generator", LavaGeneratorBlock::new);
    public static final BlockRegistryObject<DieselGeneratorBlock> DIESEL_GENERATOR = register("diesel_generator", DieselGeneratorBlock::new);
    public static final BlockRegistryObject<BatteryBoxBlock> BATTERY_BOX = register("battery_box", BatteryBoxBlock::new);
    public static final BlockRegistryObject<WireBlock> WIRE = register("wire", () ->
            new WireBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1, 5)));
    public static final BlockRegistryObject<PipeBlock> PIPE = register("pipe", () ->
            new PipeBlock(Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(1, 5)));

    public static final BlockRegistryObject<FlowingFluidBlock> OIL = registerFluid("oil", () -> ModFluids.OIL);
    public static final BlockRegistryObject<FlowingFluidBlock> DIESEL = registerFluid("diesel", () -> ModFluids.DIESEL);

    private ModBlocks() {}

    static void register() {}

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderTypes(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(STONE_MACHINE_FRAME.get(), RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ALLOY_MACHINE_FRAME.get(), RenderType.getCutout());
        Registration.getBlocks(AbstractMachineBlock.class).forEach(block ->
                RenderTypeLookup.setRenderLayer(block, RenderType.getTranslucent()));
    }

    private static <T extends Block> BlockRegistryObject<T> registerNoItem(String name, Supplier<T> block) {
        return new BlockRegistryObject<>(Registration.BLOCKS.register(name, block));
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block) {
        return register(name, block, ModBlocks::defaultItem);
    }

    private static <T extends Block> BlockRegistryObject<T> register(String name, Supplier<T> block, Function<BlockRegistryObject<T>, Supplier<? extends BlockItem>> item) {
        BlockRegistryObject<T> ret = registerNoItem(name, block);
        Registration.ITEMS.register(name, item.apply(ret));
        return ret;
    }

    private static BlockRegistryObject<FlowingFluidBlock> registerFluid(String name, Supplier<FlowingFluid> fluid) {
        return registerNoItem(name, () ->
                new FlowingFluidBlock(fluid, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops()));
    }

    private static <T extends Block> Supplier<BlockItem> defaultItem(BlockRegistryObject<T> block) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(Groups.MACHINES));
    }

    @Nullable
    public static ITextComponent checkForMissingLootTables(PlayerEntity player) {
        // Checks for missing block loot tables, but only in dev
        if (!(player.world instanceof ServerWorld) || !QForgeMod.isDevBuild()) return null;

        LootTableManager lootTableManager = ((ServerWorld) player.world).getServer().getLootTableManager();
        Collection<String> missing = new ArrayList<>();

        for (Block block : ForgeRegistries.BLOCKS.getValues()) {
            ResourceLocation lootTable = block.getLootTable();
            // The AirBlock check filters out removed blocks
            if (lootTable.getNamespace().equals(QForgeMod.MOD_ID) && !(block instanceof AirBlock) && !lootTableManager.getLootTableKeys().contains(lootTable)) {
                QForgeMod.LOGGER.error("Missing block loot table '{}' for {}", lootTable, block.getRegistryName());
                missing.add(lootTable.toString());
            }
        }

        if (!missing.isEmpty()) {
            String list = String.join(", ", missing);
            return new StringTextComponent("The following block loot tables are missing: " + list).mergeStyle(TextFormatting.RED);
        }

        return null;
    }
}
