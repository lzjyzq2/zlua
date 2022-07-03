package cn.settile.lzjyzq2.zlua.util;

import java.nio.ByteBuffer;

public class BinaryChunkUtil {

    public static String readLuaString(ByteBuffer buffer) {
        int size = buffer.get() & 0xFF;
        if (size == 0) {
            return "";
        }
        if (size == 0xFF) {
            size = (int) buffer.getLong(); // size_t
        }

        byte[] a = getBytes(buffer, size - 1);
        return new String(a);
    }

    public static byte[] getBytes(ByteBuffer buffer, int size) {
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return bytes;
    }
}
