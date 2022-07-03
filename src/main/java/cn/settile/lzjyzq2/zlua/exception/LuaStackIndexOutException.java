package cn.settile.lzjyzq2.zlua.exception;

public class LuaStackIndexOutException extends RuntimeException {
    public LuaStackIndexOutException() {
        super("invalid index");
    }
}
