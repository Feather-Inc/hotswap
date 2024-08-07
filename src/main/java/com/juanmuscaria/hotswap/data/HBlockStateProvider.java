package com.juanmuscaria.hotswap.data;

import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.blocks.HBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class HBlockStateProvider extends BlockStateProvider {
    public HBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Hotswap.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        HBlocks.ORES.forEach((rock, rockOres) ->
            rockOres.forEach((ore, block) ->
                simpleBlockWithItem(block.get(),
                    models().withExistingParent(name(block.get()), "tfc:block/ore")
                        .renderType("cutout")
                        .texture("all", new ResourceLocation("tfc", ("block/rock/raw/" + rock.name()).toLowerCase()))
                        .texture("overlay", new ResourceLocation(Hotswap.MOD_ID, "block/ore/" + ore.name().toLowerCase()))
                )
            )
        );

        HBlocks.GRADED_ORES.forEach((rock, oreGrades) ->
            oreGrades.forEach((ore, gradeBlocks) ->
                gradeBlocks.forEach((grade, block) ->
                    simpleBlockWithItem(block.get(),
                        models().withExistingParent(name(block.get()), "tfc:block/ore")
                            .renderType("cutout")
                            .texture("all", new ResourceLocation("tfc", ("block/rock/raw/" + rock.name()).toLowerCase()))
                            .texture("overlay", new ResourceLocation(Hotswap.MOD_ID, ("block/ore/" + grade.name() + "_" + ore.name()).toLowerCase()))
                    )
                )
            )
        );
    }

    public ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public String name(Block block) {
        return key(block).getPath();
    }
}
