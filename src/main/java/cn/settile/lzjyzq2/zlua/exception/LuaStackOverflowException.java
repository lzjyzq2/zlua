package cn.settile.lzjyzq2.zlua.exception;

public class LuaStackOverflowException extends RuntimeException {
    public LuaStackOverflowException() {
        super("stack overflow");
    }
}
