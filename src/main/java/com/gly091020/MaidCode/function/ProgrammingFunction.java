package com.gly091020.MaidCode.function;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.BoolParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.StringParameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.MaidCode.MaidCode;
import com.gly091020.MaidCode.MaidFunctions;
import com.gly091020.MaidCode.task.ProgrammingTask;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.GameMasterBlock;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Set;

import static com.gly091020.MaidCode.MaidFunctions.isOP;

public class ProgrammingFunction implements IFunctionCall<ProgrammingFunction.Result> {
    public static final String ID = "maid_programming";
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription(EntityMaid entityMaid) {
        return """
                当用户提到给电脑编程时,提取对话中的坐标,
                根据要求写好兼容我的世界CC: Tweaked模组电脑的代码(代码不能有中文,聊天无所谓),并调用此函数""";
    }

    @Override
    public Parameter addParameters(ObjectParameter root, EntityMaid entityMaid) {
        StringParameter pos = StringParameter.create().setTitle("pos").setDescription("电脑方块的坐标，用英文逗号分割(示例:1,1,1)");
        StringParameter code = StringParameter.create().setTitle("code").setDescription("代码内容(我的世界CC: Tweaked模组电脑的代码,不能有中文)");
        StringParameter file_name = StringParameter.create().setTitle("file name").setDescription("文件名,如果用户没有指明默认为startup.lua(不能有中文,加上后缀名)");
        BoolParameter restart = (BoolParameter) BoolParameter.create().setTitle("restart").setDescription("是否重启电脑方块(默认不重启,如果文件名为startup.lua则默认重启)");
        root.addProperties("pos", pos);
        root.addProperties("code", code);
        root.addProperties("file_name", file_name);
        root.addProperties("restart", restart);
        return root;
    }

    @Override
    public Codec<Result> codec() {
        return RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("pos").forGetter(Result::pos),
                Codec.STRING.fieldOf("code").forGetter(Result::code),
                Codec.STRING.fieldOf("file_name").forGetter(Result::fileName),
                Codec.BOOL.fieldOf("restart").forGetter(Result::restart)
        ).apply(instance, Result::new));
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid maid) {
        if(MaidCode.CONFIG.enableTask && !(maid.getTask() instanceof ProgrammingTask)){
            return new ToolResponse("你没有在进行编程工作，请要求主人切换工作模式为编程");
        }
        if(MaidFunctions.isGLYMaid(maid)){
            return new ToolResponse(MaidFunctions.noGLY);
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

        maid.getBrain().setMemory(MaidCode.COMPUTER_POS_MEMORY, p);

        if (!(maid.level().getBlockEntity(p) instanceof ComputerBlockEntity computerBlock)) {
            return new ToolResponse("对应坐标不是电脑");
        }
        if(maid.level().getBlockState(p).getBlock() instanceof GameMasterBlock && !isOP(maid)){
            return new ToolResponse("这是一台命令电脑，你的主人没有管理员权限");
        }
        if(computerBlock.getServerComputer() == null){
            return new ToolResponse("对应坐标电脑未初始化");
        }
        var server = computerBlock.getServerComputer();
        var m = server.createRootMount();
        if(m == null){return new ToolResponse("对应坐标电脑不可写");}
        try(SeekableByteChannel f = m.openFile(result.fileName, Set.of(StandardOpenOption.CREATE,
                StandardOpenOption.WRITE))) {
            ByteBuffer buffer = ByteBuffer.wrap(result.code.getBytes());
            f.write(buffer);
        } catch (IOException e) {
            return new ToolResponse("出现错误:" + e.getMessage());
        }
        if(result.restart){
            server.reboot();
        }
        return new ToolResponse("完成");
    }

    public record Result(String pos, String code, String fileName, boolean restart) {
    }
}
