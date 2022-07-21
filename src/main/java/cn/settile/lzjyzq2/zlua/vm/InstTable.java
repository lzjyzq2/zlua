package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaVM;
import cn.settile.lzjyzq2.zlua.util.FloatingPointByte;

public class InstTable {

    public static final int LFIELDS_PER_FLUSH = 50;

    public static void newTable(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);

        a += 1;
        vm.createTable(FloatingPointByte.fb2int(b), FloatingPointByte.fb2int(c));
        vm.replace(a);
    }

    public static void getTable(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);

        a += 1;
        b += 1;
        vm.getRK(c);
        vm.getTable(b);
        vm.replace(a);
    }

    public static void setTable(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);

        a += 1;
        vm.getRK(b);
        vm.getRK(c);
        vm.setTable(a);
    }

    public static void setList(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;

        boolean bIsZero = b == 0;
        if (bIsZero) {
            b = (int) vm.toInteger(-1) - a - 1;
            vm.pop(1);
        }

        if (c > 0) {
            c = c - 1;
        } else {
            c = Instruction.Ax(vm.fetch());
        }
        long idx = (long) c * LFIELDS_PER_FLUSH;
        for (int j = 1; j <= b; j++) {
            idx++;
            vm.pushValue(a + j);
            vm.setI(a, idx);
        }

        if (bIsZero) {
            for (int j = vm.registerCount() + 1; j <= vm.getTop(); j++) {
                idx++;
                vm.pushValue(j);
                vm.setI(a, idx);
            }
            vm.setTop(vm.registerCount());
        }
    }
}
