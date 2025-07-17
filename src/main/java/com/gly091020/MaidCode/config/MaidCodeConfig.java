package com.gly091020.MaidCode.config;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.gly091020.MaidCode.MaidCode;
import com.gly091020.MaidCode.MaidFunctions;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;

import static com.gly091020.MaidCode.MaidCode.CONFIG;
import static com.gly091020.MaidCode.MaidFunctions.isGLY;

@Config(name = MaidCode.ModID)
public class MaidCodeConfig implements ConfigData {
    public boolean enable = true;
    public int findComputerSize = 10;
    public boolean isGLYMode = true;
    public boolean enableTask = true;
    public boolean enableTaskRequire = true;
    public String funListJson = "{}";

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
        var taskConfig = entryBuilder.startSubCategory(Component.translatable("config.maidcode.task_config")).setExpanded(true);
        taskConfig.add(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.task_config.enable"), CONFIG.enableTask)
                .setDefaultValue(true)
                .setSaveConsumer(x -> CONFIG.enableTask = x)
                .setTooltip(Component.translatable("config.maidcode.task_config.enable.tip"))
                .requireRestart()
                .build());
        taskConfig.add(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.task_config.enable_require"), CONFIG.enableTaskRequire)
                .setDefaultValue(true)
                .setSaveConsumer(x -> CONFIG.enableTaskRequire = x)
                .setTooltip(Component.translatable("config.maidcode.task_config.enable_require.tip"))
                .build());
        category.addEntry(taskConfig.build());
        var funConfig = entryBuilder.startSubCategory(Component.translatable("config.maidcode.fun_config"))
                .setTooltip(Component.translatable("config.maidcode.fun_config.tip")).setExpanded(true);
        for(IFunctionCall<?> functionCall: MaidFunctions.getFunCalls()){
            funConfig.add(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.fun_config." + functionCall.getId()),
                            MaidFunctions.isEnable(functionCall))
                    .setDefaultValue(true)
                    .setSaveConsumer(x -> MaidFunctions.setEnable(functionCall, x))
                    .setYesNoTextSupplier(x -> Component.translatable(x ? "config.maidcode.fun_config.enable" : "config.maidcode.fun_config.disable")
                            .withStyle(x ? ChatFormatting.GREEN : ChatFormatting.RED))
                    .requireRestart().build());
        }
        category.addEntry(funConfig.build());
        if(isGLY()){
            category.addEntry(entryBuilder.startBooleanToggle(Component.translatable("config.maidcode.is_gly_mode"), CONFIG.isGLYMode)
                    .setSaveConsumer(x -> {
                        if(!x){
                            Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(SoundEvents.VILLAGER_NO, SoundSource.PLAYERS, 1, 1,
                                    RandomSource.create(), new BlockPos(0, 0, 0)));
                        }
                        CONFIG.isGLYMode = x;
                    })
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
