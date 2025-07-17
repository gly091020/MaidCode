package com.gly091020.MaidCode.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.ai.IExtraMaidBrain;
import com.gly091020.MaidCode.MaidCode;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import java.util.Collections;
import java.util.List;

public class ProgrammingTaskMemory implements IExtraMaidBrain {
    @Override
    public List<MemoryModuleType<?>> getExtraMemoryTypes() {
        return Collections.singletonList(MaidCode.COMPUTER_POS_MEMORY);
    }
}
