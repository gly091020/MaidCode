package com.gly091020.MaidCode;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.FunctionCallRegister;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ExtraMaidBrainManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.gly091020.MaidCode.task.ProgrammingTask;
import com.gly091020.MaidCode.task.ProgrammingTaskMemory;

import static com.gly091020.MaidCode.MaidCode.CONFIG;

@LittleMaidExtension
public class MaidPlugin implements ILittleMaid {
    @Override
    public void registerAIFunctionCall(FunctionCallRegister register) {
        MaidFunctions.registry(register);
    }

    @Override
    public void addExtraMaidBrain(ExtraMaidBrainManager manager) {
        manager.addExtraMaidBrain(new ProgrammingTaskMemory());
    }

    @Override
    public void addMaidTask(TaskManager manager) {
        if(!CONFIG.enable || !CONFIG.enableTask){return;}
        manager.add(new ProgrammingTask());
    }
}
