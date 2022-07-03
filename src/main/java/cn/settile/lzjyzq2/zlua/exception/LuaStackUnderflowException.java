package cn.settile.lzjyzq2.zlua.exception;

public class LuaStackUnderflowException extends RuntimeException {
    public LuaStackUnderflowException() {
        super("stack underflow");
    }
}
