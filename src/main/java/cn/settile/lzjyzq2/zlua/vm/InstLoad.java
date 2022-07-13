package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaVM;

public class InstLoad {

    public static void loadNil(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        vm.pushNil();
        for (int j = a; j <= a + b; j++) {
            vm.copy(-1, j);
        }
        vm.pop(1);
    }

    public static void loadBool(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        vm.pushBoolean(b != 0);
        vm.replace(a);
        if (c != 0) {
            vm.addPC(1);
        }
    }

    public static void loadK(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int bx = Instruction.getBx(i);
        a += 1;
        vm.getConst(bx);
        vm.replace(a);
    }

    public static void loadKx(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int ax = Instruction.Ax(vm.fetch());
        a += 1;
        vm.getConst(ax);
        vm.replace(a);
    }
}
