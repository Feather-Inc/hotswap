package com.juanmuscaria.hotswap.fluids;

import com.juanmuscaria.hotswap.HMetals;
import net.minecraft.world.level.material.Fluid;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record AllFluids(String name, OptionalInt color, Supplier<? extends Fluid> fluid) {
    private static final Map<Enum<?>, AllFluids> IDENTITY = new HashMap<>();
    private static final List<AllFluids> VALUES = Stream.of(
           Arrays.stream(HMetals.values()).map(metal -> fromEnum(metal, metal.getColor(), "metal/" + metal.getSerializedName(), HFluids.METALS.get(metal).source()))
        )
        .flatMap(Function.identity())
        .toList();

    public static <R> Map<AllFluids, R> mapOf(Function<? super AllFluids, ? extends R> map) {
        return VALUES.stream().collect(Collectors.toMap(Function.identity(), map));
    }

    public static AllFluids asType(Enum<?> identity) {
        return IDENTITY.get(identity);
    }

    private static AllFluids fromEnum(Enum<?> identity, int color, String name, Supplier<? extends Fluid> fluid) {
        final AllFluids type = new AllFluids(name, OptionalInt.of(HFluids.ALPHA_MASK | color), fluid);
        IDENTITY.put(identity, type);
        return type;
    }
}
