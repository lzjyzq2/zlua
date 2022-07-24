package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.api.Function;
import cn.settile.lzjyzq2.zlua.api.JavaFunction;
import cn.settile.lzjyzq2.zlua.chunk.Prototype;

public class Closure {

    private final Function func;

    public Prototype getProto() {
        if(func instanceof Prototype){
            return (Prototype)func;
        }
        return null;
    }

    public JavaFunction getJavaFunction(){
        if(func instanceof JavaFunction){
            return (JavaFunction)func;
        }
        return null;
    }

    public Closure(Function func){
        this.func = func;
    }

    public boolean isJavaFunction(){
        return func instanceof JavaFunction;
    }

    public boolean isPrototype(){
        return func instanceof Prototype;
    }

}
