package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.*;
import cn.settile.lzjyzq2.zlua.chunk.Prototype;
import cn.settile.lzjyzq2.zlua.exception.*;

public class DefaultLuaState implements LuaState, LuaVM {

    private final LuaStack stack;

    private int pc;

    private final Prototype prototype;

    public DefaultLuaState(Prototype prototype) {
        this(20, prototype);
    }

    public DefaultLuaState(int size, Prototype prototype) {
        this.stack = new LuaStack(size);
        this.prototype = prototype;
        this.pc = 0;
    }

    @Override
    public int getTop() {
        return stack.top();
    }

    @Override
    public int absIndex(int idx) {
        return stack.absIndex(idx);
    }

    @Override
    public boolean checkStack(int n) {
        return stack.check(n);
    }

    @Override
    public void pop(int n) {
        for (int i = 0; i < n; i++) {
            stack.pop();
        }
    }

    @Override
    public void copy(int fromIdx, int toIdx) {
        Object tmpValue = stack.get(fromIdx);
        stack.set(toIdx, tmpValue);
    }

    @Override
    public void pushValue(int idx) {
        Object tmpValue = stack.get(idx);
        stack.push(tmpValue);
    }

    @Override
    public void replace(int idx) {
        stack.set(idx, stack.pop());
    }

    @Override
    public void insert(int idx) {
        rotate(idx, 1);
    }

    @Override
    public void remove(int idx) {
        rotate(idx, -1);
        pop(1);
    }

    @Override
    public void rotate(int idx, int n) {
        int tmpTop = stack.top() - 1;
        int absIndex = stack.absIndex(idx) - 1;
        int m = n >= 0 ? tmpTop - n : absIndex - n - 1;
        stack.reverse(absIndex, m);
        stack.reverse(m + 1, tmpTop);
        stack.reverse(absIndex, tmpTop);
    }

    @Override
    public void setTop(int idx) {
        int newTop = stack.absIndex(idx);
        if (newTop < 0) {
            throw new LuaStackUnderflowException();
        }
        int n = stack.top() - newTop;
        if (n > 0) {
            for (int i = 0; i < n; i++) {
                stack.pop();
            }
        } else if (n < 0) {
            for (int i = 0; i > n; i--) {
                stack.push(null);
            }
        }
    }

    @Override
    public String typeName(LuaType tp) {
        switch (tp) {
            case LUA_TNONE:
                return "no value";
            case LUA_TNIL:
                return "nil";
            case LUA_TBOOLEAN:
                return "boolean";
            case LUA_TNUMBER:
                return "number";
            case LUA_TSTRING:
                return "string";
            case LUA_TTABLE:
                return "table";
            case LUA_TFUNCTION:
                return "function";
            case LUA_TTHREAD:
                return "thread";
            default:
                return "userdata";
        }
    }

    @Override
    public LuaType type(int idx) {
        if (stack.isValid(idx)) {
            Object val = stack.get(idx);
            return LuaValue.typeOf(val);
        }
        return LuaType.LUA_TNONE;
    }

    @Override
    public boolean isNone(int idx) {
        return type(idx) == LuaType.LUA_TNONE;
    }

    @Override
    public boolean isNil(int idx) {
        return type(idx) == LuaType.LUA_TNIL;
    }

    @Override
    public boolean isNoneOrNil(int idx) {
        LuaType luaType = type(idx);
        return luaType == LuaType.LUA_TNONE || luaType == LuaType.LUA_TNIL;
    }

    @Override
    public boolean isBoolean(int idx) {
        return type(idx) == LuaType.LUA_TBOOLEAN;
    }

    @Override
    public boolean isInteger(int idx) {
        return stack.get(idx) instanceof Long;
    }

    @Override
    public boolean isNumber(int idx) {
        return toNumberX(idx) != null;
    }

    @Override
    public boolean isString(int idx) {
        LuaType luaType = type(idx);
        return luaType == LuaType.LUA_TSTRING || luaType == LuaType.LUA_TNUMBER;
    }

    @Override
    public boolean isTable(int idx) {
        return type(idx).equals(LuaType.LUA_TNONE);
    }

    @Override
    public boolean isThread(int idx) {
        return type(idx).equals(LuaType.LUA_TNONE);
    }

    @Override
    public boolean isFunction(int idx) {
        return type(idx).equals(LuaType.LUA_TNONE);
    }

    @Override
    public boolean toBoolean(int idx) {
        Object value = stack.get(idx);
        return LuaValue.convertToBoolean(value);
    }

    @Override
    public long toInteger(int idx) {
        return toIntegerX(idx);
    }

    @Override
    public Long toIntegerX(int idx) {
        Object value = stack.get(idx);
        return LuaValue.convertToInteger(value);
    }

    @Override
    public double toNumber(int idx) {
        return toNumberX(idx);
    }

    @Override
    public Double toNumberX(int idx) {
        Object value = stack.get(idx);
        return LuaValue.convertToFloat(value);
    }

    @Override
    public String toString(int idx) {
        Object value = stack.get(idx);
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof Long || value instanceof Double) {
            return value.toString();
        } else {
            return null;
        }
    }

    @Override
    public void pushNil() {
        stack.push(null);
    }

    @Override
    public void pushBoolean(boolean b) {
        stack.push(b);
    }

    @Override
    public void pushInteger(long n) {
        stack.push(n);
    }

    @Override
    public void pushNumber(double n) {
        stack.push(n);
    }

    @Override
    public void pushString(String s) {
        stack.push(s);
    }

    @Override
    public void arith(ArithOp op) {
        Object b = stack.pop();
        Object a = op != ArithOp.LUA_OPUNM && op != ArithOp.LUA_OPBNOT ? stack.pop() : b;
        Operator operator = ApiArith.getOperator(op);
        Object result = ApiArith.arith(a, b, operator);
        if (result != null) {
            stack.push(result);
        } else {
            throw new LuaArithmeticException();
        }
    }

    @Override
    public boolean compare(int idx1, int idx2, CompareOp op) {
        Object value1 = stack.get(idx1);
        Object value2 = stack.get(idx2);
        switch (op) {
            case LUA_OPEQ:
                return ApiCompare.eq(value1, value2);
            case LUA_OPLT:
                return ApiCompare.lt(value1, value2);
            case LUA_OPLE:
                return ApiCompare.le(value1, value2);
            default:
                throw new LuaInvalidCompareOpException();
        }
    }

    @Override
    public void len(int idx) {
        Object value = stack.get(idx);
        if (value instanceof String) {
            pushInteger(((String) value).length());
        } else if (value instanceof LuaTable) {
            pushInteger(((LuaTable) value).len());
        } else {
            throw new LuaLengthException();
        }
    }

    @Override
    public void concat(int n) {
        if (n == 0) {
            stack.push("");
        } else if (n >= 2) {
            for (int i = 1; i < n; i++) {
                if (isString(-1) && isString(-2)) {
                    String s2 = toString(-1);
                    String s1 = toString(-2);
                    pop(2);
                    stack.push(s1 + s2);
                    continue;
                }
                throw new LuaConcatException();
            }
        }
        // n == 1, do nothing
    }

    @Override
    public int getPC() {
        return pc;
    }

    @Override
    public void addPC(int n) {
        pc += n;
    }

    @Override
    public int fetch() {
        int i = prototype.getCode()[pc];
        pc++;
        return i;
    }

    @Override
    public void getConst(int idx) {
        Object c = prototype.getConstants()[idx];
        stack.push(c);
    }

    @Override
    public void getRK(int rk) {
        if (rk > 0xFF) {
            getConst(rk & 0xFF);
        } else {
            pushValue(rk + 1);
        }
    }
}
