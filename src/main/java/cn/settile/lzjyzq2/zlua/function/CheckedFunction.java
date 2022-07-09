package cn.settile.lzjyzq2.zlua.function;

@FunctionalInterface
public interface CheckedFunction<T, R> {

    R apply(T t) throws Exception;
}
