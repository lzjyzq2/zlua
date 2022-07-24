package cn.settile.lzjyzq2.zlua.exception;

public class LuaUnknownFunctionTypeException extends RuntimeException{

    public LuaUnknownFunctionTypeException(){
        super("unknown function type!");
    }
}
