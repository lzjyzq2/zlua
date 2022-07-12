package cn.settile.lzjyzq2.zlua.number;

public class Math {

    public static long IFloorDiv(long a, long b) {
        if (a > 0 && b > 0 || a < 0 && b < 0 || (a % b) == 0) {
            return a / b;
        } else {
            return a / b - 1;
        }
    }

    public static double FFloorDiv(double a, double b) {
        return java.lang.Math.floor(a / b);
    }

    public static long IMod(long a, long b) {
        return a - IFloorDiv(a, b) * b;
    }

    public static double FMod(double a, double b) {
        return a - FFloorDiv(a, b) * b;
    }

    public static long shiftLeft(long a, long n) {
        return n >= 0 ? a << n : a >>> -n;
    }

    public static long shiftRight(long a, long n) {
        return n >= 0 ? a >>> n : a << -n;
    }

}
