package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaType;
import cn.settile.lzjyzq2.zlua.exception.LuaStackUnderflowException;

public class DefaultLuaState implements LuaState {

    private final LuaStack stack;

    public DefaultLuaState() {
        this(20);
    }

    public DefaultLuaState(int size) {
        stack = new LuaStack(size);
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
        return value instanceof Long ? (Long) value : null;
    }

    @Override
    public double toNumber(int idx) {
        return toNumberX(idx);
    }

    @Override
    public Double toNumberX(int idx) {
        Object value = stack.get(idx);
        if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else {
            return null;
        }
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
}
