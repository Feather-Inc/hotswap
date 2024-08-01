package com.juanmuscaria.hotswap;

import com.juanmuscaria.hotswap.data.*;
import com.juanmuscaria.hotswap.data.info.OreNameDump;
import com.juanmuscaria.hotswap.data.loot.HLootTableProvider;
import net.minecraftforge.data.event.GatherDataEvent;

public class HotswapDataGenerators {
    static void addProviders(GatherDataEvent event) {
        var generator = event.getGenerator();
        var pack = generator.getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        var lookup = event.getLookupProvider();

        if (event.includeReports()) {
            generator.addProvider(true, new OreNameDump(pack));
        }

        if (event.includeClient()) {
            generator.addProvider(true, new HBlockStateProvider(pack, fileHelper));
            generator.addProvider(true, new HItemModelProvider(pack, fileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(true, new HRecipeProvider(pack));
            generator.addProvider(true, HLootTableProvider.create(pack));

            var blockTags = new HBlockTagsProvider(pack, lookup, fileHelper);
            generator.addProvider(true, blockTags);
            generator.addProvider(true, new HItemTagsProvider(pack, lookup, blockTags.contentsGetter(), fileHelper));
        }
    }
}
