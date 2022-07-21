package cn.settile.lzjyzq2.zlua.state;

import cn.settile.lzjyzq2.zlua.chunk.Prototype;

public class Closure {

    private final Prototype proto;

    public Prototype getProto() {
        return proto;
    }

    public Closure(Prototype proto){
        this.proto = proto;
    }
}
