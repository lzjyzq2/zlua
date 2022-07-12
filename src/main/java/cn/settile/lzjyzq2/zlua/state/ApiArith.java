package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.ArithOp;
import cn.settile.lzjyzq2.zlua.api.DoubleBinaryOperator;
import cn.settile.lzjyzq2.zlua.api.LongBinaryOperator;
import cn.settile.lzjyzq2.zlua.api.Operator;
import cn.settile.lzjyzq2.zlua.number.Math;
import cn.settile.lzjyzq2.zlua.number.Number;

public class ApiArith {

    private static final Operator[] operators = new Operator[]{
            new Operator(LongBinaryOperator.ADD, DoubleBinaryOperator.ADD),
            new Operator(LongBinaryOperator.SUB, DoubleBinaryOperator.SUB),
            new Operator(LongBinaryOperator.MUL, DoubleBinaryOperator.MUL),
            new Operator(LongBinaryOperator.MOD, DoubleBinaryOperator.MOD),
            new Operator(null, DoubleBinaryOperator.POW),
            new Operator(null, DoubleBinaryOperator.DIV),
            new Operator(LongBinaryOperator.IDIV, DoubleBinaryOperator.IDIV),
            new Operator(LongBinaryOperator.AND, null),
            new Operator(LongBinaryOperator.OR, null),
            new Operator(LongBinaryOperator.XOR, null),
            new Operator(LongBinaryOperator.SHL, null),
            new Operator(LongBinaryOperator.SHR, null),
            new Operator(LongBinaryOperator.UNM, DoubleBinaryOperator.UNM),
            new Operator(LongBinaryOperator.NOT, null),
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
