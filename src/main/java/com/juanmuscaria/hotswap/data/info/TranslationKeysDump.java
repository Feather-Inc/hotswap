package com.juanmuscaria.hotswap.data.info;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class TranslationKeysDump implements DataProvider {
    private final String modid;
    private final PackOutput output;

    public TranslationKeysDump(String modid, PackOutput output) {
        this.modid = modid;
        this.output = output;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cachedOut) {
        var json = new JsonObject();
        for (var block : ForgeRegistries.BLOCKS) {
            var id = ForgeRegistries.BLOCKS.getKey(block);
            if (id != null && id.getNamespace().equals(modid)) {
                json.addProperty(block.getDescriptionId(), "__");
            }
        }

        var path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("lang.json");
        return DataProvider.saveStable(cachedOut, json, path);
    }


    @NotNull
    @Override
    public String getName() {
        return "Translation Keys Dump: " + modid;
    }
}
