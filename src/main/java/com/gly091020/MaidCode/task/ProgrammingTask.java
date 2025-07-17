package com.gly091020.MaidCode.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import dan200.computercraft.shared.ModRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

import static com.gly091020.MaidCode.MaidCode.CONFIG;
import static com.gly091020.MaidCode.MaidCode.ModID;

public class ProgrammingTask implements IMaidTask {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ModID, "programming_task");
    @Override
    public @NotNull ResourceLocation getUid() {
        return ID;
    }

    @Override
    public @NotNull ItemStack getIcon() {
        return ModRegistry.Items.COMPUTER_ADVANCED.get().getDefaultInstance();
    }

    @Override
    public @Nullable SoundEvent getAmbientSound(@NotNull EntityMaid entityMaid) {
        return null;
    }

    @Override
    public @NotNull List<Pair<Integer, BehaviorControl<? super EntityMaid>>> createBrainTasks(@NotNull EntityMaid entityMaid) {
//        return List.of(new Pair<>(1, new ProgrammingTaskAI(0.5f)));
        return List.of(); // todo:暂未实现
    }

    @Override
    public float searchRadius(@NotNull EntityMaid maid) {
        return CONFIG.findComputerSize * 2;
    }

    @Override
    public boolean isEnable(@NotNull EntityMaid maid) {
        return (!CONFIG.enableTaskRequire || maid.getFavorabilityManager().getLevel() >= 1) &&
                AIConfig.FUNCTION_CALL_ENABLED.getAsBoolean();
    }

    @Override
    public @NotNull List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc(@NotNull EntityMaid maid) {
        if(!CONFIG.enableTaskRequire){
            return List.of(Pair.of("text2", maid1 -> AIConfig.FUNCTION_CALL_ENABLED.getAsBoolean()));
        }
        return List.of(Pair.of("text1", maid1 ->
                        maid.getFavorabilityManager().getLevel() >= 1),
                Pair.of("text2", maid1 -> AIConfig.FUNCTION_CALL_ENABLED.getAsBoolean()));
    }
}
