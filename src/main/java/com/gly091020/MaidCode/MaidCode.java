package com.gly091020.MaidCode;

import com.gly091020.MaidCode.config.MaidCodeConfig;
import com.gly091020.MaidCode.function.GetComputerPosFunction;
import com.gly091020.MaidCode.function.ProgrammingFunction;
import com.gly091020.MaidCode.function.RestartComputerFunction;
import com.gly091020.MaidCode.function.SetComputerLabelFunction;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.UUID;

@Mod(MaidCode.ModID)
public class MaidCode {
    public static final String ModID = "maidcode";
    public static final UUID _5112151111121 = UUID.fromString("91bd580f-5f17-4e30-872f-2e480dd9a220");
    public static MaidCodeConfig CONFIG;

    public MaidCode(ModContainer container){
        AutoConfig.register(MaidCodeConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(MaidCodeConfig.class).getConfig();
        container.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent) ->
                MaidCodeConfig.getScreenBuilder(ConfigBuilder.create().setTitle(Component.translatable("config.maidcode.title")))
                        .build());

        MaidFunctions.addFunCalls(new RestartComputerFunction());
        MaidFunctions.addFunCalls(new ProgrammingFunction());
        MaidFunctions.addFunCalls(new GetComputerPosFunction());
        MaidFunctions.addFunCalls(new SetComputerLabelFunction());
    }
}
