package com.juanmuscaria.hotswap.mixin.datagen;


import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ModelProvider.class, remap = false)
public class ModelProviderMixin {
    @Shadow @Final protected String folder;

    /**
     * @author juanmuscaria
     * @reason meh
     */
    @Overwrite
    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().startsWith(folder + "/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
    }
}
