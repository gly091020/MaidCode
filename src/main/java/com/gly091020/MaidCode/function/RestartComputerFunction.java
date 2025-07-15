package com.gly091020.MaidCode.function;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.MaidCode.MaidCode;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.Arrays;
import java.util.Objects;

public class RestartComputerFunction implements IFunctionCall<RestartComputerFunction.Result> {
    // ID不能有冒号!!!!!!!!!!!!!!!!!!!!!!!!
    // 会500!!!!!!!!!!!!!!!!!!!!!!!
    // 【【重音テト/MV/中译版】气到原地爆炸！/イライラしている（BY：じん OFFICIAL YOUTUBE CHANNEL）-哔哩哔哩】 https://b23.tv/Bo8I5ri
    public static final String ID = "maid_restart_computer";
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription(EntityMaid maid) {
        return """
                当用户提到需要打开,关闭或重启游戏中的电脑时，提取对话中的电脑方块坐标，并调用此函数""";
    }

    @Override
    public Parameter addParameters(ObjectParameter root, EntityMaid maid) {
        StringParameter pos = StringParameter.create().setTitle("pos").setDescription("电脑方块的坐标，用英文逗号分割(示例:1,1,1)");
        StringParameter mode = StringParameter.create().setTitle("mode").setDescription("需要的操作,可以为reboot(重启),close(关闭)或open(打开)");
        root.addProperties("pos", pos);
        root.addProperties("mode", mode);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("pos").forGetter(Result::pos),
                Codec.STRING.fieldOf("mode").forGetter(Result::mode)
        ).apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        if(MaidCode.isGLYMaid(maid)){
            return new ToolResponse(MaidCode.noGLY);
        }
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
            if(computerBlock.getServerComputer() == null){
                return new ToolResponse("对应坐标电脑未初始化");
            }
            var c = computerBlock.getServerComputer();
            if(Objects.equals(result.mode, "open")){
                c.turnOn();
            } else if (Objects.equals(result.mode, "close")) {
                c.close();
            } else if (Objects.equals(result.mode, "reboot")) {
                c.reboot();
            }else{
                return new ToolResponse("无效操作");
            }
        }else{
            return new ToolResponse("对应坐标不是电脑");
        }
        return new ToolResponse("完成");
    }

    public record Result(String pos, String mode) {
    }
}
