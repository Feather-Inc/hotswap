package com.juanmuscaria.hotswap.data.loot;

import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.blocks.HBlocks;
import com.juanmuscaria.hotswap.items.HItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Set;

public class HBlockLootProvider extends BlockLootSubProvider {
    protected HBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        HBlocks.ORES.forEach((rock, rockOres) ->
            rockOres.forEach((ore, block) ->
                dropOther(block.get(), HItems.ORES.get(ore).get())
            )
        );

        HBlocks.GRADED_ORES.forEach((rock, oreGrades) ->
            oreGrades.forEach((ore, gradeBlocks) ->
                gradeBlocks.forEach((grade, block) ->
                    dropOther(block.get(), HItems.GRADED_ORES.get(ore).get(grade).get())
                )
            )
        );
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ForgeRegistries.BLOCKS.getEntries().stream().filter(entry -> entry.getKey().location().getNamespace().equals(Hotswap.MOD_ID)).map(Map.Entry::getValue).toList();
    }
}
