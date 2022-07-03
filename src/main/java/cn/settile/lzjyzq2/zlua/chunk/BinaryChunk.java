package cn.settile.lzjyzq2.zlua.chunk;

import cn.settile.lzjyzq2.zlua.util.BinaryChunkUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public final class BinaryChunk {

    private static final byte[] LUA_SIGNATURE = new byte[]{0x1B, 'L', 'u', 'a'};

    private static final byte LUAC_VERSION = 0x53;

    private static final byte LUAC_FORMAT = 0;

    private static final byte[] LUAC_DATA = new byte[]{0x19, (byte) 0x93, '\r', '\n', 0x1A, '\n'};

    private static final int CINT_SIZE = 4;

    private static final int CSIZET_SIZE = 8;

    private static final int INSTRUCTION_SIZE = 4;

    private static final int LUA_INTEGER_SIZE = 8;

    private static final int LUA_NUMBER_SIZE = 8;

    private static final int LUAC_INT = 0x5678;

    private static final double LUAC_NUM = 370.5;

    public static Prototype unDump(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        checkHeader(buffer);
        buffer.get();
        return Prototype.readProto(buffer,"");
    }

    public static void checkHeader(ByteBuffer buffer) {
        if (!Arrays.equals(LUA_SIGNATURE, BinaryChunkUtil.getBytes(buffer, 4))) {
            throw new RuntimeException("not a precompiled chunk!");
        }
        if (buffer.get() != LUAC_VERSION) {
            throw new RuntimeException("version mismatch!");
        }
        if (buffer.get() != LUAC_FORMAT) {
            throw new RuntimeException("format mismatch!");
        }
        if (!Arrays.equals(LUAC_DATA, BinaryChunkUtil.getBytes(buffer, 6))) {
            throw new RuntimeException("corrupted!");
        }
        if (buffer.get() != CINT_SIZE) {
            throw new RuntimeException("int size mismatch!");
        }
        if (buffer.get() != CSIZET_SIZE) {
            throw new RuntimeException("size_t size mismatch!");
        }
        if (buffer.get() != INSTRUCTION_SIZE) {
            throw new RuntimeException("instruction size mismatch!");
        }
        if (buffer.get() != LUA_INTEGER_SIZE) {
            throw new RuntimeException("lua_Integer size mismatch!");
        }
        if (buffer.get() != LUA_NUMBER_SIZE) {
            throw new RuntimeException("lua_Number size mismatch!");
        }
        if (buffer.getLong() != LUAC_INT) {
            throw new RuntimeException("endianness mismatch!");
        }
        if (buffer.getDouble() != LUAC_NUM) {
            throw new RuntimeException("float format mismatch!");
        }
    }

}
