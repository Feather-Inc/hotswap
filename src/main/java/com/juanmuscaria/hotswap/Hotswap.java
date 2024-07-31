package com.juanmuscaria.hotswap;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.client.gui.widget.ModListWidget;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

@Mod(Hotswap.MODID)
public class Hotswap {

    public static final String MODID = "hotswap";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Hotswap() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

    }

    public void commonSetup(FMLCommonSetupEvent event) {
        if (!FMLEnvironment.production) {
            LOGGER.info("Generating mod mermaid diagram and mod list...");
            var idBlacklist = Arrays.asList("forge", "mcp", "fml", "minecraft");
            var template = """
            class `%s`["%s"] {
              id: %s
              version: %s
            }
            """;
            var graph = new StringBuilder("```mermaid\nclassDiagram\n");
            var list = new StringBuilder("# Mods ");
            var mods = ModList.get().getMods();
            mods.sort(Comparator.comparing(IModInfo::getModId));

            list.append('(').append(mods.size()).append(")\n");

            for (var mod : mods) {
                if (idBlacklist.contains(mod.getModId().toLowerCase(Locale.ROOT))) {
                    continue;
                }

                var id = mod.getModId();
                var name = mod.getDisplayName();
                var version = MavenVersionStringHelper.artifactVersionToString(mod.getVersion());
                graph.append(String.format(template, id, name, id, version));
                for (IModInfo.ModVersion dep : mod.getDependencies()) {
                    if (idBlacklist.contains(dep.getModId().toLowerCase(Locale.ROOT))) {
                        continue;
                    }
                    graph.append(String.format("  `%s` <-- `%s`\n", id, dep.getModId()));
                }
                graph.append("\n");

                list.append("* ").append(name).append("-\\[").append(id).append("]-").append(mod.getVersion()).append('\n');
            }
            graph.append("```");

            try {
                Files.writeString(Path.of("../mod-diagram.md"), graph.toString());
            } catch (IOException e) {
                LOGGER.error("Unable to write down mod diagram", e);
            }

            try {
                Files.writeString(Path.of("../mod-list.md"), list.toString());
            } catch (IOException e) {
                LOGGER.error("Unable to write down mod list", e);
            }
        }
    }
}
