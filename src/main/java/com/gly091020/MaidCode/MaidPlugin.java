package com.gly091020.MaidCode;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.FunctionCallRegister;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.gly091020.MaidCode.function.GetComputerPosFunction;
import com.gly091020.MaidCode.function.ProgrammingFunction;
import com.gly091020.MaidCode.function.RestartComputerFunction;

import static com.gly091020.MaidCode.MaidCode.CONFIG;

@LittleMaidExtension
public class MaidPlugin implements ILittleMaid {
    @Override
    public void registerAIFunctionCall(FunctionCallRegister register) {
        if(!CONFIG.enable){return;}
        register.register(new RestartComputerFunction());
        register.register(new ProgrammingFunction());
        register.register(new GetComputerPosFunction());
    }
}
