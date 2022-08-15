package cn.settile.lzjyzq2.zlua.api;

import cn.settile.lzjyzq2.zlua.util.Pair;

public class Operator extends Pair<LongBinaryOperator, DoubleBinaryOperator> {

    private final String metaMethod;

    public Operator(String metaMethod, LongBinaryOperator left, DoubleBinaryOperator right) {
        super(left, right);
        this.metaMethod = metaMethod;
    }

    public String getMetaMethod() {
        return metaMethod;
    }
}
