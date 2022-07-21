package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.LuaVM;

public class InstCall {

    public static void closure(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int bx = Instruction.getBx(i);

        a += 1;
        vm.loadProto(bx);
        vm.replace(a);
    }

    public static void call(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;

        int nArgs = pushFuncAndArgs(a, b, vm);
        vm.call(nArgs, c - 1);
        popResults(a, c, vm);
    }

    private static int pushFuncAndArgs(int a, int b, LuaVM vm) {
        if (b >= 1) {
            vm.checkStack(b);
            for (int i = a; i < a + b; i++) {
                vm.pushValue(i);
            }
            return b - 1;
        } else {
            fixStack(a, vm);
            return vm.getTop() - vm.registerCount() - 1;
        }
    }

    private static void fixStack(int a, LuaVM vm) {
        int x = (int) vm.toInteger(-1);
        vm.pop(1);

        vm.checkStack(x - a);
        for (int i = a; i < x; i++) {
            vm.pushValue(i);
        }
        vm.rotate(vm.registerCount() + 1, x - a);
    }

    private static void popResults(int a, int c, LuaVM vm) {
        if (c > 1) {
            for (int i = a + c - 2; i >= a; i--) {
                vm.replace(i);
            }
        } else if (c != 1) {
            vm.checkStack(1);
            vm.pushInteger(a);
        }
    }

    /**
     * return指令，将存放在连续多个寄存器中的值返回给主调函数，由于return为关键字前加下划线
     *
     * @param i  指令
     * @param vm LuaVM
     */
    public static void _return(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;

        if (b > 1) {
            vm.checkStack(b - 1);
            for (int j = a; j <= a + b - 2; j++) {
                vm.pushValue(j);
            }
        } else if (b != 1) {
            fixStack(a, vm);
        }
    }

    public static void vararg(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;

        if (b != 1) {
            vm.loadVararg(b - 1);
            popResults(a, b, vm);
        }
    }

    public static void tailCall(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;

        int c = 0;

        int nArgs = pushFuncAndArgs(a, b, vm);
        vm.call(nArgs, c - 1);
        popResults(a, c, vm);
    }

    public static void self(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        b += 1;

        vm.copy(b, a + 1);
        vm.getRK(c);
        vm.getTable(b);
        vm.replace(a);
    }
}
