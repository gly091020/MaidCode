package com.gly091020.MaidCode.function;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.GameMasterBlock;

import java.util.Arrays;

import static com.gly091020.MaidCode.MaidFunctions.isOP;

public class SetComputerLabelFunction implements IFunctionCall<SetComputerLabelFunction.Result> {
    public static final String ID = "maid_set_computer_label";
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription(EntityMaid maid) {
        return """
                当需要给电脑设置标签时，调用此函数""";
    }

    @Override
    public Parameter addParameters(ObjectParameter root, EntityMaid maid) {
        StringParameter pos = StringParameter.create().setTitle("pos").setDescription("电脑方块的坐标，用英文逗号分割(示例:1,1,1)");
        StringParameter label = StringParameter.create().setTitle("label").setDescription("要设置的电脑标签，不能有中文");
        root.addProperties("pos", pos);
        root.addProperties("label", label);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("pos").forGetter(Result::pos),
                Codec.STRING.fieldOf("label").forGetter(Result::label)
        ).apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        var l = Arrays.stream(result.pos.split(",")).toList();
        if(l.size() != 3){
            return new ToolResponse("调用格式错误");
        }
        BlockPos p;
        try {
            p = new BlockPos(Integer.parseInt(l.get(0)), Integer.parseInt(l.get(1)), Integer.parseInt(l.get(2)));
        } catch (NumberFormatException e) {
            return new ToolResponse("坐标不是数字");
        }

        if(maid.level().getBlockEntity(p) instanceof ComputerBlockEntity computerBlock){
            if(maid.level().getBlockState(p).getBlock() instanceof GameMasterBlock && !isOP(maid)){
                return new ToolResponse("这是一台命令电脑，你的主人没有管理员权限");
            }
            computerBlock.setLabel(result.label);
        }else{
            return new ToolResponse("对应坐标不是电脑");
        }
        return new ToolResponse("完成");
    }

    public record Result(String pos, String label) {
    }
}
