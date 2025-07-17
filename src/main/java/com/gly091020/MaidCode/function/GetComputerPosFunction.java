package com.gly091020.MaidCode.function;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.response.ToolResponse;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.ObjectParameter;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.schema.parameter.Parameter;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.gly091020.MaidCode.MaidCode;
import com.gly091020.MaidCode.task.ProgrammingTask;
import com.mojang.serialization.Codec;
import dan200.computercraft.shared.computer.blocks.CommandComputerBlock;
import dan200.computercraft.shared.computer.blocks.ComputerBlockEntity;
import dan200.computercraft.shared.pocket.items.PocketComputerItem;
import net.minecraft.core.BlockPos;

import static com.gly091020.MaidCode.MaidCode.CONFIG;
import static com.gly091020.MaidCode.MaidFunctions.isOP;

public class GetComputerPosFunction implements IFunctionCall<GetComputerPosFunction.Result> {
    public static final String ID = "maid_get_computer_pos";
    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getDescription(EntityMaid entityMaid) {
        return """
                当用户没有明确电脑的位置但要求电脑相关操作时,调用此方法,返回周围电脑的位置和标签""";
    }

    @Override
    public Parameter addParameters(ObjectParameter objectParameter, EntityMaid entityMaid) {
        return objectParameter;
    }

    @Override
    public Codec<Result> codec() {
        return Codec.unit(Result.INSTANCE);
    }

    @Override
    public ToolResponse onToolCall(Result result, EntityMaid entityMaid) {
        if(MaidCode.CONFIG.enableTask && !(entityMaid.getTask() instanceof ProgrammingTask)){
            return new ToolResponse("你没有在进行编程工作，请要求主人切换工作模式为编程");
        }
        var a = CONFIG.findComputerSize;
        var s = new StringBuilder();
        s.append(String.format("你的位置:(%d,%d,%d)", entityMaid.getBlockX(), entityMaid.getBlockY(), entityMaid.getBlockZ()));
        s.append("周围的电脑:\n");
        if(entityMaid.getMainHandItem().getItem() instanceof PocketComputerItem && entityMaid.getServer() != null){
            var computer = PocketComputerItem.getServerComputer(entityMaid.getServer(),
                    entityMaid.getMainHandItem());
            if (computer != null) {
                s.append(String.format("位置:手上,标签:%s\n", computer.getLabel()));
            }
        }
        for (int x = entityMaid.getBlockX() - a; x < entityMaid.getBlockX() + a; x++) {
            for (int y = entityMaid.getBlockY() - a; y < entityMaid.getBlockY() + a; y++) {
                for (int z = entityMaid.getBlockZ() - a; z < entityMaid.getBlockZ() + a; z++) {
                    var e = entityMaid.level().getBlockEntity(new BlockPos(x, y, z));
                    if (e instanceof ComputerBlockEntity computerBlockEntity){
                        var p = computerBlockEntity.getBlockPos();
                        s.append(String.format("位置:(%d,%d,%d),标签:%s", p.getX(),
                                p.getY(),
                                p.getZ(),
                                computerBlockEntity.getLabel()));
                        if(e.getBlockState().getBlock() instanceof CommandComputerBlock<?> && !isOP(entityMaid)){
                            s.append(",没有权限访问这台电脑");
                        }
                        s.append("\n");
                    }
                }
            }
        }
        return new ToolResponse(s.toString());
    }

    public enum Result {
        INSTANCE
    }
}
