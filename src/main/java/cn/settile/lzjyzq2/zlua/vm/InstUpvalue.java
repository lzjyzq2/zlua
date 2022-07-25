package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaVM;
import cn.settile.lzjyzq2.zlua.util.LuaStateUtil;

public class InstUpvalue {

    public static void getUpval(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        b += 1;
        vm.copy(LuaStateUtil.luaUpvalueIndex(b), a);
    }

    public static void setUpval(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        b += 1;
        vm.copy(a, LuaStateUtil.luaUpvalueIndex(b));
    }

    public static void getTabUp(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        b += 1;
        vm.getRK(c);
        vm.getTable(LuaStateUtil.luaUpvalueIndex(b));
        vm.replace(a);
    }

    public static void setTabUp(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        vm.getRK(b);
        vm.getRK(c);
        vm.setTable(LuaStateUtil.luaUpvalueIndex(a));
    }

}
