package com.juanmuscaria.hotswap.data.loot;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.data.loot.packs.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class HLootTableProvider {
    public static LootTableProvider create(PackOutput out) {
        return new LootTableProvider(out, Set.of(), List.of(
            new SubProviderEntry(HBlockLootProvider::new, LootContextParamSets.BLOCK)
        ));
    }
}
