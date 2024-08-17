package com.juanmuscaria.hotswap.data;

import com.google.gson.JsonObject;
import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.blocks.HBlocks;
import com.juanmuscaria.hotswap.data.template.MetalData;
import com.juanmuscaria.hotswap.fluids.HFluids;
import com.juanmuscaria.hotswap.items.HItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class HMetalProvider implements DataProvider {
    private final PackOutput output;

    public HMetalProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cachedOut) {
        var futures = new ArrayList<CompletableFuture<?>>(MetalData.values().length);
        for (MetalData value : MetalData.values()) {
            var data = new JsonObject();
            var path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve(Hotswap.MOD_ID)
                .resolve("tfc")
                .resolve("metals")
                .resolve(value.getSerializedName() + ".json");

            data.addProperty("tier", value.tier());
            data.addProperty("fluid", HFluids.METALS.get(value.metal()).type().getId().toString());
            data.addProperty("melt_temperature", value.meltTemperature());
            data.addProperty("specific_heat_capacity", value.specificHeatCapacity());
            var ingots = new JsonObject();
            ingots.addProperty("tag", "forge:ingots/" + value.getSerializedName());
            data.add("ingots", ingots);
            var doubleIngots = new JsonObject();
            doubleIngots.addProperty("tag", "forge:double_ingots/" + value.getSerializedName());
            data.add("double_ingots", doubleIngots);
            var sheets = new JsonObject();
            sheets.addProperty("tag", "forge:sheets/" + value.getSerializedName());
            data.add("sheets", sheets);

            futures.add(DataProvider.saveStable(cachedOut, data, path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }


    @NotNull
    @Override
    public String getName() {
        return "Metals: hotswap";
    }
}
