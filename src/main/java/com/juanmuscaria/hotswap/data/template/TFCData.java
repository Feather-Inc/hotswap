package com.juanmuscaria.hotswap.data.template;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public interface TFCData extends StringRepresentable {
    enum Size implements TFCData {
        TINY,
        VERY_SMALL,
        SMALL,
        NORMAL,
        LARGE,
        VERY_LARGE,
        HUGE,
    }

    enum Weight implements TFCData {
        VERY_LIGHT,
        LIGHT,
        MEDIUM,
        HEAVY,
        VERY_HEAVY,
    }

    enum Category implements TFCData {
        FRUIT,
        VEGETABLE,
        GRAIN,
        BREAD,
        DAIRY,
        MEAT,
        COOKED_MEAT,
        OTHER,
    }

    interface Metal<T> extends TFCData {
        int tier();
        Collection<String> types();
        float baseHeatCapacity();
        float meltTemperature();
        Optional<String> meltMetal();
        T metal();

        default float specificHeatCapacity() {
            return (float) Math.round(300 / baseHeatCapacity()) / 100_000F;
        }

        default float ingotHeatCapacity() {
            return 1 / baseHeatCapacity();
        }
    }


    @Override
    default @NotNull String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    String name();
}
