package com.juanmuscaria.hotswap.blocks;

import com.juanmuscaria.hotswap.items.HItems;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.juanmuscaria.hotswap.Hotswap.MOD_ID;

public class HBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);

    public static final Map<Rock, Map<HOre, RegistryObject<Block>>> ORES = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(HOre.class, ore -> !ore.isGraded(), ore ->
            register(("ore/" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
        )
    );
    public static final Map<Rock, Map<HOre, Map<Ore.Grade, RegistryObject<Block>>>> GRADED_ORES = Helpers.mapOfKeys(Rock.class, rock ->
        Helpers.mapOfKeys(HOre.class, HOre::isGraded, ore ->
            Helpers.mapOfKeys(Ore.Grade.class, grade ->
                register(("ore/" + grade.name() + "_" + ore.name() + "/" + rock.name()), () -> ore.create(rock))
            )
        )
    );

    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier) {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties) {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory) {
        return RegistrationHelpers.registerBlock(HBlocks.BLOCKS, HItems.ITEMS, name, blockSupplier, blockItemFactory);
    }
}
