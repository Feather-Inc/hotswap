package com.juanmuscaria.hotswap.data;

import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.blocks.HBlocks;
import net.dries007.tfc.common.TFCTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HBlockTagsProvider extends BlockTagsProvider {
    public HBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Hotswap.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var allOreTags = List.of(
            tag(TFCTags.Blocks.PROSPECTABLE),
            tag(TFCTags.Blocks.CAN_COLLAPSE),
            tag(TFCTags.Blocks.CAN_START_COLLAPSE),
            tag(TFCTags.Blocks.CAN_TRIGGER_COLLAPSE),
            tag(TFCTags.Blocks.POWDERKEG_BREAKING_BLOCKS),
            tag(BlockTags.MINEABLE_WITH_PICKAXE),
            tag(BlockTags.NEEDS_STONE_TOOL),
            tag(Tags.Blocks.ORES)
        );
        var oreSpecificTags = OreSpecificTags.blockTags();

        HBlocks.ORES.forEach((rock, rockOres) ->
            rockOres.forEach((ore, block) -> {
                allOreTags.forEach(tag -> tag.add(block.get()));
                var oreTags = oreSpecificTags.get(ore);
                if (oreTags != null) {
                    oreTags.stream().map(this::tag).forEach(tag -> tag.add(block.get()));
                }
            })
        );

        HBlocks.GRADED_ORES.forEach((rock, oreGrades) ->
            oreGrades.forEach((ore, gradeBlocks) ->
                gradeBlocks.forEach((grade, block) -> {
                    allOreTags.forEach(tag -> tag.add(block.get()));
                    var oreTags = oreSpecificTags.get(ore);
                    if (oreTags != null) {
                        oreTags.stream().map(this::tag).forEach(tag -> tag.add(block.get()));
                    }
                })
            )
        );
    }

    private static TagKey<Block> forgeTag(String name) {
        return BlockTags.create(new ResourceLocation("forge", name));
    }
}
