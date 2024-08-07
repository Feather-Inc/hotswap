package com.juanmuscaria.hotswap.items;

import com.juanmuscaria.hotswap.blocks.HOre;
import com.juanmuscaria.hotswap.fluids.AllFluids;
import net.dries007.tfc.common.blocks.rock.Ore;
import net.dries007.tfc.common.fluids.FluidId;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static com.juanmuscaria.hotswap.Hotswap.MOD_ID;

public class HItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);

    public static final Map<HOre, RegistryObject<Item>> ORES = Helpers.mapOfKeys(HOre.class, ore -> !ore.isGraded(), type ->
        register("ore/" + type.name())
    );

    public static final Map<HOre, Map<Ore.Grade, RegistryObject<Item>>> GRADED_ORES = Helpers.mapOfKeys(HOre.class, HOre::isGraded, ore ->
        Helpers.mapOfKeys(Ore.Grade.class, grade ->
            register("ore/" + grade.name() + '_' + ore.name())
        )
    );

    public static final Map<AllFluids, RegistryObject<BucketItem>> FLUID_BUCKETS = AllFluids.mapOf(fluid ->
        register("bucket/" + fluid.name(), () -> new BucketItem(fluid.fluid(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)))
    );

    private static RegistryObject<Item> register(String name) {
        return register(name, () -> new Item(new Item.Properties()));
    }

    private static <T extends Item> RegistryObject<T> register(String name, Supplier<T> item) {
        return ITEMS.register(name.toLowerCase(Locale.ROOT), item);
    }
}
