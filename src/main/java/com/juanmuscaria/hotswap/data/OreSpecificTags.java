package com.juanmuscaria.hotswap.data;

import com.juanmuscaria.hotswap.blocks.HOre;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OreSpecificTags {
    static Map<HOre, List<String>> ORE_TO_TAGS = Map.of(
        HOre.GALENA, List.of("forge:ores/lead"),
        HOre.IRIDOSMINE, List.of("forge:ores/osmium"),
        HOre.URANINITE, List.of("forge:ores/uranium")
    );

    public static Map<HOre, List<TagKey<Block>>> blockTags() {
        var map = new HashMap<HOre, List<TagKey<Block>>>(ORE_TO_TAGS.size());
        for (var entry : ORE_TO_TAGS.entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().map(ResourceLocation::new).map(BlockTags::create).toList());
        }

        return map;
    }

    public static Map<HOre, List<TagKey<Item>>> itemTags() {
        var map = new HashMap<HOre, List<TagKey<Item>>>(ORE_TO_TAGS.size());
        for (var entry : ORE_TO_TAGS.entrySet()) {
            map.put(entry.getKey(), entry.getValue().stream().map(ResourceLocation::new).map(ItemTags::create).toList());
        }

        return map;
    }
}
