package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaVM;

public class InstUpvalue {

    public static void getTabUp(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int c = Instruction.getC(i);

        a += 1;

        vm.pushGlobalTable();
        vm.getRK(c);
        vm.getTable(-2);
        vm.replace(a);
        vm.pop(1);
    }
}
