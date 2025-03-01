package net.notcoded.wayfix.config;

import com.google.common.collect.Lists;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.Requirement;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.notcoded.wayfix.WayFix;
import net.notcoded.wayfix.util.WindowHelper;

import java.util.HashMap;

//? if <1.19 {
/*import net.minecraft.text.TranslatableText;
*///?}

@Config(name = "wayfix")
public class ModClothConfig extends ModConfig implements ConfigData {

    public static HashMap<String, Long> monitors = new HashMap<>();

    public static Screen buildScreen(Screen parent) {
        ModConfig config = WayFix.config;

        ConfigBuilder builder = ConfigBuilder.create();
        builder.setParentScreen(parent);
        builder.setTitle(getText("title"));
        builder.setDoesConfirmSave(false);

        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModClothConfig.class).save());

        ConfigCategory category = builder.getOrCreateCategory(getText("title"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        category.addEntry(entryBuilder.startBooleanToggle(getText("autoScaleGUI"), config.autoScaleGUI)
                .setDefaultValue(true)
                .setTooltip(getText("autoScaleGUI.tooltip"), getText("autoScaleGUI.tooltip2"))
                .setSaveConsumer(value -> config.autoScaleGUI = value)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(getText("injectIcon"), config.injectIcon)
                .setDefaultValue(true)
                .setTooltip(getText("injectIcon.tooltip"))
                .requireRestart()
                .setSaveConsumer(value -> config.injectIcon = value)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(getText("keyModifiersFix"), config.keyModifiersFix)
                .setDefaultValue(true)
                .setTooltip(getText("keyModifiersFix.tooltip"))
                .setSaveConsumer(value -> config.keyModifiersFix = value)
                .build());

        DropdownMenuBuilder<String> dropdown = entryBuilder.startDropdownMenu(getText("monitorName"), DropdownMenuBuilder.TopCellElementBuilder.of(config.monitorName, (s) -> s))
                .setDefaultValue("")
                .setSuggestionMode(false)
                .setTooltip(getText("monitorName.tooltip"), getText("monitorName.tooltip2"))
                .setSaveConsumer(value -> config.monitorName = value)
                .setSelections(Lists.newArrayList(monitors.keySet()));

        try {
            dropdown.setDisplayRequirement(Requirement.isFalse(() -> WindowHelper.canUseWindowHelper));
            // Doesn't work on 1.17.1 and 1.19.3
        } catch (NoClassDefFoundError ignored) { }

        category.addEntry(dropdown.build());

        return builder.build();
    }

    private static Text getText(String key) {
        //? if >=1.19 {
        return Text.translatable("wayfix.option." + key);
         //?} elif <1.19 {
        /*return new TranslatableText("wayfix.option." + key);
        *///?}
    }
}
