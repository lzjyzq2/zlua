package cn.settile.lzjyzq2.zlua.util;

import java.util.Objects;
import java.util.function.BiFunction;

public class Pair<L, R> {

    private final L left;

    private final R right;


    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }


    public <T> T get(BiFunction<L, R, T> biFunction) {
        return biFunction.apply(left, right);
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }
}
