package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.exception.LuaStackIndexOutException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackOverflowException;
import cn.settile.lzjyzq2.zlua.exception.LuaStackUnderflowException;

import java.util.Map;

public class LuaStack {

    private Object[] slots;

    private int top = 0;

    private LuaStack prev;

    private Object[] varargs;

    private Closure closure;

    private int pc = 0;

    private final LuaState luaState;

    private Map<Integer, Upvalue> openuvs;


    public LuaStack(int size) {
        this(size, null);
    }

    public LuaStack(int size, LuaState luaState) {
        this.slots = new Object[size];
        this.luaState = luaState;
    }

    public boolean check(int n) {
        if (slots.length - top < n) {
            if ((top & n) + ((top ^ n) >> 1) > Integer.MAX_VALUE / 2) {
                return false;
            }
            Object[] tmpValues = new Object[top + n];
            System.arraycopy(slots, 0, tmpValues, 0, slots.length);
            slots = tmpValues;
        }
        return true;
    }

    public static LuaStack newLuaStack(int size) {
        return new LuaStack(size);
    }


    public void push(Object value) {
        if (top == slots.length) {
            throw new LuaStackOverflowException();
        }
        slots[top] = value;
        top++;
    }

    public Object pop() {
        if (top < 1) {
            throw new LuaStackUnderflowException();
        }
        top--;
        Object value = slots[top];
        slots[top] = null;
        return value;
    }

    public int absIndex(int idx) {
        if (idx <= LuaState.LUA_REGISTRYINDEX) {
            return idx;
        }
        if (idx >= 0) {
            return idx;
        }
        return idx + top + 1;
    }

    public boolean isValid(int idx) {
        if (idx < LuaState.LUA_REGISTRYINDEX) {
            int uvIdx = LuaState.LUA_REGISTRYINDEX - idx - 1;
            Closure c = this.closure;
            return c != null && uvIdx < c.getUpvalues().length;
        }
        if (idx == LuaState.LUA_REGISTRYINDEX) {
            return true;
        }
        int absIndex = absIndex(idx);
        return absIndex > 0 && absIndex <= top;
    }

    public void set(int idx, Object value) {
        if (idx < LuaState.LUA_REGISTRYINDEX) {
            int uvIdx = LuaState.LUA_REGISTRYINDEX - idx - 1;
            Closure c = this.closure;
            if (c != null && uvIdx < c.getUpvalues().length) {
                c.getUpvalues()[uvIdx].setVal(value);
            }
            return;
        }
        if (idx == LuaState.LUA_REGISTRYINDEX) {
            if (value instanceof LuaTable) {
                this.luaState.setRegistry((LuaTable) value);
            }
        }
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top) {
            slots[absIndex - 1] = value;
            return;
        }
        throw new LuaStackIndexOutException();
    }

    public Object get(int idx) {
        if (idx < LuaState.LUA_REGISTRYINDEX) {
            int uvIdx = LuaState.LUA_REGISTRYINDEX - idx - 1;
            Closure c = this.closure;
            if (c == null || uvIdx >= c.getUpvalues().length) {
                return null;
            }
            return c.getUpvalues()[uvIdx].getVal();
        }
        if (idx == LuaState.LUA_REGISTRYINDEX) {
            return this.luaState.getRegistry();
        }
        int absIndex = absIndex(idx);
        if (absIndex > 0 && absIndex <= top) {
            return slots[absIndex - 1];
        }
        return null;
    }

    public int size() {
        return slots.length;
    }

    public int top() {
        return top;
    }

    public void top(int top) {
        this.top = top;
    }

    public void reverse(int from, int to) {
        from = absIndex(from);
        to = absIndex(to);
        while (from < to) {
            Object tmpValue = slots[from];
            slots[from] = slots[to];
            slots[to] = tmpValue;
            from++;
            to--;
        }
    }


    public Object[] popN(int n) {
        Object[] values = new Object[n];
        for (int i = n - 1; i >= 0; i--) {
            values[i] = pop();
        }
        return values;
    }

    public void pushN(Object[] values, int n) {
        int valuesLength = values.length;
        if (n < 0) {
            n = valuesLength;
        }
        for (int i = 0; i < n; i++) {
            if (i < valuesLength) {
                push(values[i]);
            } else {
                push(null);
            }
        }
    }

    public LuaStack getPrev() {
        return prev;
    }

    public void setPrev(LuaStack prev) {
        this.prev = prev;
    }

    public int getPc() {
        return pc;
    }

    public int addPc(int n) {
        return pc += n;
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    public Object[] getVarargs() {
        return varargs;
    }

    public void setVarargs(Object[] varargs) {
        this.varargs = varargs;
    }

    public Map<Integer, Upvalue> getOpenuvs() {
        return openuvs;
    }

    public void setOpenuvs(Map<Integer, Upvalue> openuvs) {
        this.openuvs = openuvs;
    }
}
