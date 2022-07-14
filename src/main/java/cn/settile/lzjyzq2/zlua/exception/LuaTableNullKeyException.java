package cn.settile.lzjyzq2.zlua.exception;

public class LuaTableNullKeyException extends RuntimeException{

    public LuaTableNullKeyException(){
        super("table index is nil!");
    }
}
