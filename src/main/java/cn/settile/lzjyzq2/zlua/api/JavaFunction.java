package cn.settile.lzjyzq2.zlua.api;

@FunctionalInterface
public interface JavaFunction extends Function {

    int invoke(LuaState ls);

}
