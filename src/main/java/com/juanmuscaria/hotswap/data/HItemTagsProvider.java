package com.juanmuscaria.hotswap.data;

import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.items.HItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class HItemTagsProvider extends ItemTagsProvider {
    public HItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTagProvider, Hotswap.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        var oreSpecificTags = OreSpecificTags.itemTags();
        HItems.ORES.forEach((ore, item) -> {
            var oreTags = oreSpecificTags.get(ore);
            if (oreTags != null) {
                oreTags.stream().map(this::tag).forEach(tag -> tag.add(item.get()));
            }
        });

        HItems.GRADED_ORES.forEach((ore, gradeItems) ->
            gradeItems.forEach((grade, item) ->{
                var oreTags = oreSpecificTags.get(ore);
                if (oreTags != null) {
                    oreTags.stream().map(this::tag).forEach(tag -> tag.add(item.get()));
                }
            })
        );
    }
}
