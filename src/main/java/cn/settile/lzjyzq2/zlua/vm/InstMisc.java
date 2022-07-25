package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaVM;

public class InstMisc {

    public static void move(int i, LuaVM vm) {
        int a = Instruction.getA(i) + 1;
        int b = Instruction.getB(i) + 1;
        vm.copy(b,a);
    }

    public static void jmp(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int sBx = Instruction.getSBx(i);
        vm.addPC(sBx);
        if (a != 0) {
            vm.closeUpvalues(a);
        }
    }
}
