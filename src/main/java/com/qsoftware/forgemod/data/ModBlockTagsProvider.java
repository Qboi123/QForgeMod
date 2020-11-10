package com.qsoftware.forgemod.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;
import com.qsoftware.forgemod.objects.block.dryingrack.DryingRackBlock;
import com.qsoftware.forgemod.init.Metals;
import com.qsoftware.forgemod.init.ModTags;
import com.qsoftware.forgemod.init.Registration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator gen) {
        super(gen);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(ModTags.Blocks.DRYING_RACKS).add(Registration.getBlocks(DryingRackBlock.class).toArray(new Block[0]));

        for (Metals metal : Metals.values()) {
            metal.getOreTag().ifPresent(tag ->
                    getOrCreateBuilder(tag).add(metal.getOre().get()));
            metal.getStorageBlockTag().ifPresent(tag ->
                    getOrCreateBuilder(tag).add(metal.getStorageBlock().get()));
        }

        groupBuilder(Tags.Blocks.ORES, Metals::getOreTag);
        groupBuilder(Tags.Blocks.STORAGE_BLOCKS, Metals::getStorageBlockTag);
    }

    private void groupBuilder(ITag.INamedTag<Block> tag, Function<Metals, Optional<ITag.INamedTag<Block>>> tagGetter) {
        Builder<Block> builder = getOrCreateBuilder(tag);
        for (Metals metal : Metals.values()) {
            tagGetter.apply(metal).ifPresent(builder::addTag);
        }
    }

    @Override
    public String getName() {
        return "QForgeMod - Block Tags";
    }

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

    @Override
    public void act(DirectoryCache cache) {
        // Temp fix that removes the broken safety check
        this.tagToBuilder.clear();
        this.registerTags();
        this.tagToBuilder.forEach((p_240524_4_, p_240524_5_) -> {
            JsonObject jsonobject = p_240524_5_.serialize();
            Path path = this.makePath(p_240524_4_);
            if (path == null)
                return; //Forge: Allow running this data provider without writing it. Recipe provider needs valid tags.

            try {
                String s = GSON.toJson((JsonElement) jsonobject);
                String s1 = HASH_FUNCTION.hashUnencodedChars(s).toString();
                if (!Objects.equals(cache.getPreviousHash(path), s1) || !Files.exists(path)) {
                    Files.createDirectories(path.getParent());

                    try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                        bufferedwriter.write(s);
                    }
                }

                cache.recordHash(path, s1);
            } catch (IOException ioexception) {
                LOGGER.error("Couldn't save tags to {}", path, ioexception);
            }

        });
    }
}
