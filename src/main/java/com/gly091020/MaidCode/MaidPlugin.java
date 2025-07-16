package com.gly091020.MaidCode;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.FunctionCallRegister;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;

@LittleMaidExtension
public class MaidPlugin implements ILittleMaid {
    @Override
    public void registerAIFunctionCall(FunctionCallRegister register) {
        MaidFunctions.registry(register);
    }
}
