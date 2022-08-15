package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.ArithOp;
import cn.settile.lzjyzq2.zlua.api.DoubleBinaryOperator;
import cn.settile.lzjyzq2.zlua.api.LongBinaryOperator;
import cn.settile.lzjyzq2.zlua.api.Operator;
import cn.settile.lzjyzq2.zlua.number.Math;
import cn.settile.lzjyzq2.zlua.number.Number;

public class ApiArith {

    private static final Operator[] operators = new Operator[]{
            new Operator("__add",LongBinaryOperator.ADD, DoubleBinaryOperator.ADD),
            new Operator("__sub",LongBinaryOperator.SUB, DoubleBinaryOperator.SUB),
            new Operator("__mul",LongBinaryOperator.MUL, DoubleBinaryOperator.MUL),
            new Operator("__mod",LongBinaryOperator.MOD, DoubleBinaryOperator.MOD),
            new Operator("__pow",null, DoubleBinaryOperator.POW),
            new Operator("__div",null, DoubleBinaryOperator.DIV),
            new Operator("__idiv",LongBinaryOperator.IDIV, DoubleBinaryOperator.IDIV),
            new Operator("__band",LongBinaryOperator.AND, null),
            new Operator("__bor",LongBinaryOperator.OR, null),
            new Operator("__bxor",LongBinaryOperator.XOR, null),
            new Operator("__shl",LongBinaryOperator.SHL, null),
            new Operator("__shr",LongBinaryOperator.SHR, null),
            new Operator("__unm",LongBinaryOperator.UNM, DoubleBinaryOperator.UNM),
            new Operator("__bnot",LongBinaryOperator.NOT, null),
    };

    public static Operator getOperator(ArithOp arithOp) {
        return operators[arithOp.ordinal()];
    }

    public static Object arith(Object a, Object b, Operator operator) {
        if (operator.getRight() == null) {
            Long x = LuaValue.convertToInteger(a);
            if (x != null) {
                Long y = LuaValue.convertToInteger(b);
                if (y != null) {
                    return operator.getLeft().applyAsLong(x, y);
                }
            }
        } else {
            if (operator.getLeft() != null) {
                if (a instanceof Long && b instanceof Long) {
                    return operator.getLeft().applyAsLong((Long) a, (Long) b);
                }
            }
            Double x = LuaValue.convertToFloat(a);
            if (x != null) {
                Double y = LuaValue.convertToFloat(b);
                if (y != null) {
                    return operator.getRight().applyAsDouble(x, y);
                }
            }
        }
        return null;
    }
}
