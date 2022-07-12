package cn.settile.lzjyzq2.zlua.api;

import cn.settile.lzjyzq2.zlua.util.Pair;

public class Operator extends Pair<LongBinaryOperator,DoubleBinaryOperator> {
    public Operator(LongBinaryOperator left, DoubleBinaryOperator right) {
        super(left, right);
    }
}
