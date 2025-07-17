package com.gly091020.MaidCode.task;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidMoveToBlockTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.MaidCode.MaidCode;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class ProgrammingTaskAI extends MaidMoveToBlockTask {
    public ProgrammingTaskAI(float movementSpeed) {
        super(movementSpeed);
    }

    @Override
    protected boolean shouldMoveTo(@NotNull ServerLevel serverLevel, @NotNull EntityMaid entityMaid, @NotNull BlockPos blockPos) {
        return entityMaid.getTask() instanceof ProgrammingTask &&
                entityMaid.getBrain().getMemory(MaidCode.COMPUTER_POS_MEMORY).filter(pos ->
                pos.equals(blockPos) && serverLevel.getBlockEntity(blockPos) instanceof
                        ComputerBlockEntity).isPresent();
    }

    @Override
    protected void tick(@NotNull ServerLevel level, @NotNull EntityMaid owner, long gameTime) {
        super.tick(level, owner, gameTime);
    }
}
