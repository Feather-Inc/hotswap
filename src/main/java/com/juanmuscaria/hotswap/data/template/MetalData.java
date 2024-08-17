package com.juanmuscaria.hotswap.data.template;

import com.google.common.base.Suppliers;
import com.juanmuscaria.hotswap.HMetals;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public enum MetalData implements TFCData.Metal<HMetals> {
    LEAD(HMetals.LEAD, 0.2F, 327F, null),
    OSMIUM(HMetals.OSMIUM, 0.55F, 3033F, null),
    URANIUM(HMetals.URANIUM, 0.7F, 1132F, null);

    private final HMetals metal;
    private final float baseHeatCapacity;
    private final float meltTemperature;
    @Nullable
    private final String meltMetal;
    private final Supplier<List<String>> types;

    MetalData(HMetals metal, float baseHeatCapacity, float meltTemperature, @Nullable String meltMetal) {
        this.metal = metal;
        this.types = Suppliers.memoize(() -> Arrays.stream(HMetals.Type.values())
            .filter(type -> type != HMetals.Type.DEFAULT)
            .filter(type -> type.hasType(metal))
            .map(Enum::name)
            .map(String::toLowerCase)
            .toList());
        this.baseHeatCapacity = baseHeatCapacity;
        this.meltTemperature = meltTemperature;
        this.meltMetal = meltMetal;
    }
    
    @Override
    public int tier() {
        return this.metal.metalTier().ordinal();
    }

    @Override
    public Collection<String> types() {
        return this.types.get();
    }

    @Override
    public float baseHeatCapacity() {
        return this.baseHeatCapacity;
    }

    @Override
    public float meltTemperature() {
        return this.meltTemperature;
    }

    @Override
    public Optional<String> meltMetal() {
        return Optional.ofNullable(meltMetal);
    }

    @Override
    public HMetals metal() {
        return metal;
    }
}
