package com.gly091020.MaidCode.config;

import com.gly091020.MaidCode.MaidCode;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import static com.gly091020.MaidCode.MaidCode.CONFIG;
import static com.gly091020.MaidCode.MaidCode._5112151111121;

@Config(name = MaidCode.ModID)
public class MaidCodeConfig implements ConfigData {
    public boolean enable = true;
    public int findComputerSize = 10;
    public boolean isGLYMode = true;

    public static ConfigBuilder getScreenBuilder(ConfigBuilder builder){
        var o = builder.getSavingRunnable();
        var category = builder.getOrCreateCategory(Component.translatable("config.maidcode.title_in_mod"));
        var entryBuilder = builder.entryBuilder();
        category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.enable"), CONFIG.enable)
                .setSaveConsumer(x -> CONFIG.enable = x)
                .setDefaultValue(true)
                .requireRestart()
                .build());
        category.addEntry(entryBuilder.startIntSlider(Component.translatable("config.maidcode.find_size"), CONFIG.findComputerSize, 1, 30)
                .setDefaultValue(10)
                .setSaveConsumer(x -> CONFIG.findComputerSize = x)
                .setTextGetter(x -> Component.translatable("config.maidcode.find_size.block", x))
                .build());

        if(Minecraft.getInstance().getUser().getProfileId().equals(_5112151111121)){
            category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.is_gly_mode"), CONFIG.isGLYMode)
                    .setSaveConsumer(x -> CONFIG.isGLYMode = x)
                    .setDefaultValue(true)
                    .setTooltip(Component.translatable("config.maidcode.is_gly_mode.tip"))
                    .build());
        }
        builder.setSavingRunnable(()->{
            if(o != null) o.run();
            AutoConfig.getConfigHolder(MaidCodeConfig.class).setConfig(CONFIG);
            AutoConfig.getConfigHolder(MaidCodeConfig.class).save();
            CONFIG = AutoConfig.getConfigHolder(MaidCodeConfig.class).getConfig();
        });
        return builder;
    }
}
