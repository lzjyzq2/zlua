package cn.settile.lzjyzq2.zlua.number;

public class Number {

    public static Long parseInteger(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double parseFloat(String str) {
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static long floatToInteger(double d) {
        return Double.valueOf(d).longValue();
    }

}
