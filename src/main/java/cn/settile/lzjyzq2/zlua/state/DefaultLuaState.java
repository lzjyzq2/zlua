package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.*;
import cn.settile.lzjyzq2.zlua.chunk.BinaryChunk;
import cn.settile.lzjyzq2.zlua.chunk.Prototype;
import cn.settile.lzjyzq2.zlua.chunk.UpvalueInfo;
import cn.settile.lzjyzq2.zlua.exception.*;
import cn.settile.lzjyzq2.zlua.vm.Instruction;
import cn.settile.lzjyzq2.zlua.vm.OpCode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultLuaState implements LuaState, LuaVM {

    private LuaStack stack;

    private LuaTable registry;

    public DefaultLuaState() {
        this(LUA_MINSTACK);
    }

    public DefaultLuaState(int size) {
        this.stack = new LuaStack(size);
        this.registry = new LuaTable(0, 0);
        this.registry.put(LUA_RIDX_GLOBALS, new LuaTable(0, 0));

        pushLuaStack(new LuaStack(LUA_MINSTACK, this));
    }

    public LuaStack getStack() {
        return stack;
    }

    public void setStack(LuaStack stack) {
        this.stack = stack;
    }

    public LuaTable getRegistry() {
        return registry;
    }

    @Override
    public void setRegistry(LuaTable registry) {
        this.registry = registry;
    }

    @Override
    public void pushGlobalTable() {
        Object global = this.registry.get(LUA_RIDX_GLOBALS);
        stack.push(global);
    }

    @Override
    public LuaType getGlobal(String name) {
        Object t = this.registry.get(LUA_RIDX_GLOBALS);
        return getTable(t, name);
    }

    @Override
    public void setGlobal(String name) {
        Object t = this.registry.get(LUA_RIDX_GLOBALS);
        Object val = stack.pop();
        setTable(t, name, val);
    }

    @Override
    public void register(String name, JavaFunction javaFunc) {
        pushJavaFunction(javaFunc);
        setGlobal(name);
    }

    @Override
    public void pushJavaClosure(JavaFunction javaFunc, int n) {
        Closure closure = new Closure(javaFunc, n);
        for (int i = n; i > 0; i--) {
            Object val = stack.pop();
            closure.getUpvalues()[n - 1] = new Upvalue(val);
        }
        stack.push(closure);
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
    public void newTable() {
        createTable(0, 0);
    }

    @Override
    public void createTable(int nArr, int nRec) {
        stack.push(new LuaTable(nArr, nRec));
    }

    @Override
    public LuaType getTable(int idx) {
        Object t = stack.get(idx);
        Object k = stack.pop();
        return getTable(t, k);
    }

    private LuaType getTable(Object t, Object k) {
        if (t instanceof LuaTable) {
            Object val = ((LuaTable) t).get(k);
            stack.push(val);
            return LuaValue.typeOf(val);
        }
        throw new LuaTypeCastException(LuaValue.typeOf(t) + " not a table!");
    }

    @Override
    public LuaType getField(int idx, String k) {
        Object t = stack.get(idx);
        return getTable(t, k);
    }

    @Override
    public LuaType getI(int idx, int i) {
        Object t = stack.get(idx);
        return getTable(t, i);
    }

    @Override
    public void setTable(int idx) {
        Object t = stack.get(idx);
        Object v = stack.pop();
        Object k = stack.pop();
        setTable(t, k, v);
    }

    private void setTable(Object t, Object k, Object v) {
        if (t instanceof LuaTable) {
            ((LuaTable) t).put(k, v);
            return;
        }
        throw new LuaTypeCastException(LuaValue.typeOf(t) + " not a table!");
    }

    @Override
    public void setField(int idx, String k) {
        Object t = stack.get(idx);
        Object v = stack.pop();
        setTable(t, k, v);
    }

    @Override
    public void setI(int idx, long n) {
        Object t = stack.get(idx);
        Object v = stack.pop();
        setTable(t, n, v);
    }

    @Override
    public int load(byte[] chunk, String chunkName, String mode) {
        Prototype proto = BinaryChunk.unDump(chunk);
        Closure closure = new Closure(proto, 0);
        stack.push(closure);
        if (proto.getUpValues().length > 0) {
            Object env = registry.get(LUA_RIDX_GLOBALS);
            closure.getUpvalues()[0] = new Upvalue(env);
        }
        return 0;
    }

    @Override
    public void call(int nArgs, int nResults) {
        Object value = stack.get(-(nArgs + 1));
        if (value instanceof Closure) {
            Closure c = (Closure) value;
            if (c.isPrototype()) {
                callLuaClosure(nArgs, nResults, c);
            } else if (c.isJavaFunction()) {
                callJavaClosure(nArgs, nResults, c);
            } else {
                throw new LuaUnknownFunctionTypeException();
            }

        } else {
            throw new LuaNotFunctionException();
        }
    }

    private void callJavaClosure(int nArgs, int nResults, Closure c) {
        LuaStack newStack = new LuaStack(nArgs + 20);
        newStack.setClosure(c);
        Object[] args = this.stack.popN(nArgs);
        newStack.pushN(args, nArgs);
        this.stack.pop();

        pushLuaStack(newStack);
        int r = c.getJavaFunction().invoke(this);
        popLuaStack();

        if (nResults != 0) {
            Object[] results = newStack.popN(r);
            stack.check(results.length);
            stack.pushN(results, nResults);
        }
    }

    @Override
    public void pushJavaFunction(JavaFunction javaFunc) {
        stack.push(new Closure(javaFunc, 0));
    }

    @Override
    public boolean isJavaFunction(int idx) {
        Object val = stack.get(idx);
        if (val instanceof Closure) {
            Closure closure = (Closure) val;
            return closure.isJavaFunction();
        }
        return false;
    }

    @Override
    public JavaFunction toJavaFunction(int idx) {
        Object val = stack.get(idx);
        if (val instanceof Closure) {
            Closure closure = (Closure) val;
            return closure.getJavaFunction();
        }
        return null;
    }

    private void callLuaClosure(int nArgs, int nResults, Closure c) {
        int nRegs = c.getProto().getMaxStackSize();
        int nParams = c.getProto().getNumParams();
        boolean isVararg = c.getProto().getIsVararg() == 1;

        LuaStack newStack = new LuaStack(nRegs + 20);
        newStack.setClosure(c);

        Object[] funcAndArgs = stack.popN(nArgs + 1);
        Object[] args = new Object[funcAndArgs.length - 1];
        System.arraycopy(funcAndArgs, 1, args, 0, funcAndArgs.length - 1);
        newStack.pushN(args, nParams);
        newStack.top(nRegs);
        if (nArgs > nParams && isVararg) {
            int varargsLength = funcAndArgs.length - 1 - nParams;
            Object[] varargs = new Object[varargsLength];
            System.arraycopy(funcAndArgs, 1 + nParams, varargs, 0, varargsLength);
            newStack.setVarargs(varargs);
        }

        pushLuaStack(newStack);
        runLuaClosure();
        popLuaStack();

        if (nResults != 0) {
            Object[] results = newStack.popN(newStack.top() - nRegs);
            stack.check(results.length);
            stack.pushN(results, nResults);
        }
    }

    private void runLuaClosure() {
        while (true) {
            int inst = fetch();
            OpCode opCode = Instruction.getOpcode(inst);
            opCode.getOpAction().execute(inst, this);
            if (opCode == OpCode.RETURN) {
                break;
            }
        }
    }

    @Override
    public int getPC() {
        return stack.getPc();
    }

    @Override
    public void addPC(int n) {
        stack.addPc(n);
    }

    @Override
    public int fetch() {
        int i = stack.getClosure().getProto().getCode()[stack.getPc()];
        addPC(1);
        return i;
    }

    @Override
    public void getConst(int idx) {
        Object c = stack.getClosure().getProto().getConstants()[idx];
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

    @Override
    public void loadProto(int idx) {
        Prototype proto = stack.getClosure().getProto().getProtos()[idx];
        Closure closure = new Closure(proto, 0);
        stack.push(closure);

        int nUpValues = proto.getUpValues().length;
        for (int i = 0; i < nUpValues; i++) {

            UpvalueInfo upvalueInfo = proto.getUpValues()[i];
            int uvIdx = upvalueInfo.getIdx();

            if (upvalueInfo.getInstack() == 1) {
                if (stack.getOpenuvs() == null) {
                    stack.setOpenuvs(new HashMap<>());
                }
                Map<Integer, Upvalue> openUVs = stack.getOpenuvs();
                Upvalue openUV = openUVs.get(uvIdx);
                if (openUV != null) {
                    closure.getUpvalues()[i] = openUV;
                } else {
                    int _idx = uvIdx + 1;
                    closure.getUpvalues()[i] = new Upvalue(stack.get(_idx), stack, _idx);
                    openUVs.put(uvIdx, closure.getUpvalues()[i]);
                }
            } else {
                closure.getUpvalues()[i] = stack.getClosure().getUpvalues()[uvIdx];
            }
        }
    }

    @Override
    public void loadVararg(int i) {
        if (i < 0) {
            i = stack.getVarargs().length;
        }
        stack.check(i);
        stack.pushN(stack.getVarargs(), i);
    }

    @Override
    public int registerCount() {
        return stack.getClosure().getProto().getMaxStackSize();
    }

    @Override
    public void closeUpvalues(int a) {
        if (stack.getOpenuvs() != null) {
            for (Iterator<Map.Entry<Integer, Upvalue>> it = stack.getOpenuvs().entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, Upvalue> upvalueEntry = it.next();
                if (upvalueEntry.getKey() >= a - 1) {
                    upvalueEntry.getValue().close(stack.get(upvalueEntry.getKey()+1));
                    it.remove();
                }
            }
        }
    }

    public void pushLuaStack(LuaStack stack) {
        stack.setPrev(this.stack);
        this.stack = stack;
    }

    public void popLuaStack() {
        LuaStack top = this.stack;
        this.stack = top.getPrev();
        top.setPrev(null);
    }


}
