package com.gly091020.MaidCode;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.MaidCode.config.MaidCodeConfig;
import com.mojang.logging.LogUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.UUID;

@Mod(MaidCode.ModID)
public class MaidCode {
    public static final String ModID = "maidcode";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final UUID _5112151111121 = UUID.fromString("91bd580f-5f17-4e30-872f-2e480dd9a220");
    public static final String noGLY = String.format("""
            访问被拒绝,请向你的主人说:呦呦呦,这不%s吗,几天不见,这么拉了.(他听不见这句话会以为程序出bug了的)""", Minecraft.getInstance().getUser().getName());
    public static MaidCodeConfig CONFIG;
    public static boolean isGLYMaid(EntityMaid maid){
        return CONFIG.isGLYMode && Objects.equals(maid.getOwnerUUID(), _5112151111121);
    }

    public MaidCode(ModContainer container){
        AutoConfig.register(MaidCodeConfig.class, Toml4jConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(MaidCodeConfig.class).getConfig();
        container.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent) ->
                MaidCodeConfig.getScreenBuilder(ConfigBuilder.create().setTitle(Component.translatable("config.maidcode.title")))
                        .build());
    }
}
