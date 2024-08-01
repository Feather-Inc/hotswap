package com.juanmuscaria.hotswap.data;

import com.juanmuscaria.hotswap.Hotswap;
import com.juanmuscaria.hotswap.items.HItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class HItemModelProvider extends ItemModelProvider {
    public HItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Hotswap.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        HItems.ORES.forEach((ore, item) ->
            singleTexture(item.getId().getPath(), generated(), "layer0", texture(item.getId().getPath()))
        );
        HItems.GRADED_ORES.forEach((ore, gradeItems) ->
            gradeItems.forEach((grade, item) ->
                singleTexture(item.getId().getPath(), generated(), "layer0", texture(item.getId().getPath()))
            )
        );
    }

    protected ResourceLocation generated() {
        return mcLoc("item/generated");
    }

    protected ResourceLocation texture(String id) {
        return new ResourceLocation(modid, "item/" + id);
    }
}
