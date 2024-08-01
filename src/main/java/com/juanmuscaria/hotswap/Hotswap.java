package com.juanmuscaria.hotswap;

import com.juanmuscaria.hotswap.blocks.HBlocks;
import com.juanmuscaria.hotswap.items.HItems;
import com.mojang.logging.LogUtils;
import net.dries007.tfc.common.TFCCreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.MavenVersionStringHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.IModInfo;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

@Mod(Hotswap.MOD_ID)
public class Hotswap {
    public static final String MOD_ID = "hotswap";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Hotswap() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::addCreativeTabItems);
        HBlocks.BLOCKS.register(modEventBus);
        HItems.ITEMS.register(modEventBus);
    }

    private void addCreativeTabItems(BuildCreativeModeTabContentsEvent event) {
        var tfcOres = TFCCreativeTabs.ORES.tab();
        if (tfcOres.get().equals(event.getTab())) {
            HBlocks.ORES.forEach((rock, rockOres) ->
                rockOres.forEach((ore, block) ->
                    event.accept(block)
                )
            );

            HBlocks.GRADED_ORES.forEach((rock, oreGrades) ->
                oreGrades.forEach((ore, gradeBlocks) ->
                    gradeBlocks.forEach((grade, block) ->
                        event.accept(block)
                    )
                )
            );

            HItems.ORES.forEach((ore, item) ->
                event.accept(item)
            );

            HItems.GRADED_ORES.forEach((ore, gradeItems) ->
                gradeItems.forEach((grade, item) ->
                    event.accept(item)
                )
            );
        }

    }

    private void gatherData(GatherDataEvent event) {
        HotswapDataGenerators.addProviders(event);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
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
