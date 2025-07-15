package com.gly091020.MaidCode.mixin;

import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.gly091020.MaidCode.config.MaidCodeConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MenuIntegration.class, remap = false)
public class ConfigMixin {
    @Inject(method = "getConfigBuilder", at = @At("RETURN"))
    private static void addConfig(CallbackInfoReturnable<ConfigBuilder> cir){
        MaidCodeConfig.getScreenBuilder(cir.getReturnValue());
    }
}
