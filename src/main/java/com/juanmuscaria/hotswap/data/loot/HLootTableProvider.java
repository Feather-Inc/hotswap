package com.juanmuscaria.hotswap.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class HLootTableProvider {
    public static LootTableProvider create(PackOutput out) {
        return new LootTableProvider(out, Set.of(), List.of(
            new SubProviderEntry(HBlockLootProvider::new, LootContextParamSets.BLOCK)
        ));
    }
}
