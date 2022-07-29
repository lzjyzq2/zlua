package cn.settile.lzjyzq2.zlua.state;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class Upvalue {

    private Object val;

    private LuaStack luaStack;

    private int idx;

    public Upvalue(Object val){
        this.val = val;
        this.idx = 0;
    }

    public void setVal(Object val) {
        this.val = val;
        if (luaStack!=null) {
            luaStack.set(idx, val);
        }
    }
}
