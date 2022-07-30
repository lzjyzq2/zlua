package cn.settile.lzjyzq2.zlua.vm;

import cn.settile.lzjyzq2.zlua.api.ArithOp;
import cn.settile.lzjyzq2.zlua.api.CompareOp;
import cn.settile.lzjyzq2.zlua.api.LuaVM;

public class InstOperators {

    public static void binaryArith(int i, LuaVM vm, ArithOp op) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        vm.getRK(b);
        vm.getRK(c);
        vm.arith(op);
        vm.replace(a);
    }

    public static void unaryArith(int i, LuaVM vm, ArithOp op) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        b += 1;
        vm.pushValue(b);
        vm.arith(op);
        vm.replace(a);
    }

    public static void add(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPADD);
    }

    public static void sub(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPSUB);
    }

    public static void mul(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPMUL);
    }

    public static void mod(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPMOD);
    }

    public static void pow(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPPOW);
    }

    public static void div(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPDIV);
    }

    public static void idiv(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPIDIV);
    }

    public static void band(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPBAND);
    }

    public static void bor(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPBOR);
    }

    public static void bxor(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPBXOR);
    }

    public static void shl(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPSHL);
    }

    public static void shr(int i, LuaVM vm) {
        binaryArith(i, vm, ArithOp.LUA_OPSHR);
    }

    public static void unm(int i, LuaVM vm) {
        unaryArith(i, vm, ArithOp.LUA_OPUNM);
    }

    public static void bnot(int i, LuaVM vm) {
        unaryArith(i, vm, ArithOp.LUA_OPBNOT);
    }

    public static void len(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        b += 1;
        vm.len(b);
        vm.replace(a);
    }

    public static void concat(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        b += 1;
        c += 1;
        int n = c - b + 1;

        vm.checkStack(n);
        for (int j = b; j <= c; j++) {
            vm.pushValue(j);
        }
        vm.concat(n);
        vm.replace(a);
    }

    public static void compare(int i, LuaVM vm, CompareOp op) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        vm.getRK(b);
        vm.getRK(c);
        if (vm.compare(-2, -1, op) == (a == 0)) {
            vm.addPC(1);
        }
        vm.pop(2);
    }

    public static void eq(int i, LuaVM vm) {
        compare(i, vm, CompareOp.LUA_OPEQ);
    }

    public static void lt(int i, LuaVM vm) {
        compare(i, vm, CompareOp.LUA_OPLT);
    }

    public static void le(int i, LuaVM vm) {
        compare(i, vm, CompareOp.LUA_OPLE);
    }

    public static void not(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        a += 1;
        b += 1;
        vm.pushBoolean(!vm.toBoolean(b));
        vm.replace(a);
    }

    public static void testSet(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int b = Instruction.getB(i);
        int c = Instruction.getC(i);
        a += 1;
        b += 1;
        if (vm.toBoolean(b) == (c != 0)) {
            vm.copy(b, a);
        } else {
            vm.addPC(1);
        }
    }

    public static void test(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int c = Instruction.getC(i);
        a += 1;
        if (vm.toBoolean(a) == (c == 0)) {
            vm.addPC(1);
        }
    }

    public static void forPrep(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int sBx = Instruction.getSBx(i);
        a += 1;
        vm.pushValue(a);
        vm.pushValue(a + 2);
        vm.arith(ArithOp.LUA_OPSUB);
        vm.replace(a);
        vm.addPC(sBx);
    }

    public static void forLoop(int i, LuaVM vm) {
        int a = Instruction.getA(i);
        int sBx = Instruction.getSBx(i);
        a += 1;

        vm.pushValue(a + 2);
        vm.pushValue(a);

        vm.arith(ArithOp.LUA_OPADD);
        vm.replace(a);
        boolean isPositiveStep = vm.toNumber(a + 2) >= 0;
        if (isPositiveStep && vm.compare(a, a + 1, CompareOp.LUA_OPLE)
                || !isPositiveStep && vm.compare(a + 1, a, CompareOp.LUA_OPLE)) {
            vm.addPC(sBx);
            vm.copy(a, a + 3);
        }
    }
}
