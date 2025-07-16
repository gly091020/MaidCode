package com.gly091020.MaidCode;

import com.github.tartaricacid.touhoulittlemaid.ai.service.function.FunctionCallRegister;
import com.github.tartaricacid.touhoulittlemaid.ai.service.function.IFunctionCall;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.common.util.FakePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.gly091020.MaidCode.MaidCode.CONFIG;
import static com.gly091020.MaidCode.MaidCode._5112151111121;

public class MaidFunctions {
    private static Map<String, Boolean> funMap = null;
    private static final List<IFunctionCall<?>> functionCalls = new ArrayList<>();
    public static final String noGLY = String.format("""
            访问被拒绝,因为你的主人%s本身就是程序员,不能让他偷懒""", Minecraft.getInstance().getUser().getName());
    public static boolean isEnable(IFunctionCall<?> functionCall){
        init();
        return funMap.getOrDefault(functionCall.getId(), true);
    }
    public static void init(){
        if(CONFIG != null){
            funMap = new Gson().fromJson(CONFIG.funListJson, new TypeToken<Map<String, Boolean>>(){}.getType());
        }
    }
    public static void setEnable(IFunctionCall<?> functionCall, boolean enable){
        funMap.put(functionCall.getId(), enable);
        save();
    }
    public static void save(){
        if(funMap == null){init();}
        if(CONFIG != null){
            CONFIG.funListJson = new Gson().toJson(funMap);
        }
    }
    public static void addFunCalls(IFunctionCall<?> functionCall){
        functionCalls.add(functionCall);
    }
    public static List<IFunctionCall<?>> getFunCalls(){
        return functionCalls;
    }
    public static void registry(FunctionCallRegister register){
        if(!CONFIG.enable){return;}
        for(IFunctionCall<?> call: functionCalls){
            if(isEnable(call)){
                register.register(call);
            }
        }
    }

    public static boolean isOP(EntityMaid maid){
        var server = maid.getServer();
        if(server == null){return false;}
        var u = maid.getOwnerUUID();
        if (u == null) {
            return false;
        }
        var p = server.getPlayerList().getPlayer(u);
        if(p == null){
            return false;
        }
        if (p instanceof FakePlayer) return true;
        return server.getPlayerList().isOp(p.getGameProfile());
    }

    public static boolean isGLYMaid(EntityMaid maid){
        return CONFIG.isGLYMode && Objects.equals(maid.getOwnerUUID(), _5112151111121);
    }

    public static boolean isGLY(){
        return Minecraft.getInstance().getUser().getProfileId().equals(_5112151111121);
    }
}
