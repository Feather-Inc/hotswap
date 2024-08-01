package com.juanmuscaria.hotswap.data.info;

import com.google.gson.JsonObject;
import com.juanmuscaria.hotswap.blocks.HBlocks;
import com.juanmuscaria.hotswap.items.HItems;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class OreNameDump implements DataProvider {
    private final PackOutput output;

    public OreNameDump(PackOutput output) {
        this.output = output;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cachedOut) {
        var json = new JsonObject();
        HBlocks.ORES.forEach((rock, rockOres) ->
            rockOres.forEach((ore, block) ->
                json.addProperty(block.get().getDescriptionId(), "%s %s".formatted(capitalize(rock.name()), capitalize(ore.name())))
            )
        );

        HBlocks.GRADED_ORES.forEach((rock, oreGrades) ->
            oreGrades.forEach((ore, gradeBlocks) ->
                gradeBlocks.forEach((grade, block) ->
                    json.addProperty(block.get().getDescriptionId(), "%s %s %s".formatted(capitalize(grade.name()), capitalize(rock.name()), capitalize(ore.name())))
                )
            )
        );

        HItems.ORES.forEach((ore, item) ->
            json.addProperty(item.get().getDescriptionId(), capitalize(ore.name()))
        );
        HItems.GRADED_ORES.forEach((ore, gradeItems) ->
            gradeItems.forEach((grade, item) ->
                json.addProperty(item.get().getDescriptionId(), "%s %s".formatted(capitalize(grade.name()), capitalize(ore.name())))
            )
        );

        var path = this.output.getOutputFolder(PackOutput.Target.REPORTS).resolve("ore_lang.json");
        return DataProvider.saveStable(cachedOut, json, path);
    }


    protected String capitalize(String text) {
        return StringUtils.capitalize(text.toLowerCase(Locale.ROOT));
    }


    @NotNull
    @Override
    public String getName() {
        return "Ore Name Dump: hotswap";
    }
}
