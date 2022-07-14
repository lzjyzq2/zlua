package cn.settile.lzjyzq2.zlua.exception;

public class LuaTableNaNKeyException extends RuntimeException{

    public LuaTableNaNKeyException(){
        super("table index is NaN!");
    }
}
