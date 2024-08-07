package com.juanmuscaria.hotswap.blocks;

import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

public enum HOre {
    iridosmine(true),
    URANINITE(true),
    GALENA(true);

    private final boolean graded;

    HOre(boolean graded) {
        this.graded = graded;
    }

    public boolean isGraded() {
        return graded;
    }

    public Block create(RegistryRock rock) {
        return new Block(Block.Properties.of()
            .mapColor(MapColor.STONE)
            .sound(SoundType.STONE)
            .strength(rock.category().hardness(6.5f), 10)
            .requiresCorrectToolForDrops());
    }
}
