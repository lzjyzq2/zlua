package cn.settile.lzjyzq2.zlua.chunk;

import cn.settile.lzjyzq2.zlua.util.BinaryChunkUtil;
import lombok.*;

import java.nio.ByteBuffer;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Prototype {


    private static final int TAG_NIL = 0x00;

    private static final int TAG_BOOLEAN = 0x01;
    private static final int TAG_NUMBER = 0x03;
    private static final int TAG_INTEGER = 0x13;
    private static final int TAG_SHORT_STR = 0x04;
    private static final int TAG_LONG_STR = 0x14;

    private String source;

    private int lineDefined;

    private int lastLineDefined;

    private byte numParams;

    private byte isVararg;

    private byte maxStackSize;

    private int[] code;

    private Object[] constants;

    private Upvalue[] upValues;

    private Prototype[] protos;

    private int[] lineInfo;

    private LocVar[] locVars;

    private String[] upValueNames;

    public static Prototype readProto(ByteBuffer buffer, String parentSource) {
        String source = BinaryChunkUtil.readLuaString(buffer);
        if (source.equals("")) {
            source = parentSource;
        }
        return Prototype.builder()
                .source(source)
                .lineDefined(buffer.getInt())
                .lastLineDefined(buffer.getInt())
                .numParams(buffer.get())
                .isVararg(buffer.get())
                .maxStackSize(buffer.get())
                .code(readCode(buffer))
                .constants(readConstants(buffer))
                .upValues(readUpvalues(buffer))
                .protos(readProtos(buffer,source))
                .lineInfo(readLineInfo(buffer))
                .locVars(readLocVars(buffer))
                .upValueNames(readUpvalueNames(buffer))
                .build();
    }


    private static int[] readCode(ByteBuffer buffer) {
        int[] code = new int[buffer.getInt()];
        for (int i = 0; i < code.length; i++) {
            code[i] = buffer.getInt();
        }
        return code;
    }

    private static Object[] readConstants(ByteBuffer buffer) {
        Object[] constants = new Object[buffer.getInt()];
        for (int i = 0; i < constants.length; i++) {
            constants[i] = readConstant(buffer);
        }
        return constants;
    }

    private static Object readConstant(ByteBuffer buffer) {
        switch (buffer.get()) {
            case TAG_NIL:
                return null;
            case TAG_BOOLEAN:
                return buffer.get() != 0;
            case TAG_INTEGER:
                return buffer.getLong();
            case TAG_NUMBER:
                return buffer.getDouble();
            case TAG_SHORT_STR:
            case TAG_LONG_STR:
                return BinaryChunkUtil.readLuaString(buffer);
            default:
                throw new RuntimeException("corrupted!");
        }
    }

    private static Upvalue[] readUpvalues(ByteBuffer buffer) {
        Upvalue[] upvalues = new Upvalue[buffer.getInt()];
        for (int i = 0; i < upvalues.length; i++) {
            upvalues[i] = Upvalue.read(buffer);
        }
        return upvalues;
    }

    private static Prototype[] readProtos(ByteBuffer buffer, String parentSource) {
        Prototype[] protos = new Prototype[buffer.getInt()];
        for (int i = 0; i < protos.length; i++) {
            protos[i] = Prototype.readProto(buffer, parentSource);
        }
        return protos;
    }

    private static int[] readLineInfo(ByteBuffer buffer) {
        int[] lineInfo = new int[buffer.getInt()];
        for (int i = 0; i < lineInfo.length; i++) {
            lineInfo[i] = buffer.getInt();
        }
        return lineInfo;
    }

    private static LocVar[] readLocVars(ByteBuffer buffer) {
        LocVar[] locVars = new LocVar[buffer.getInt()];
        for (int i = 0; i < locVars.length; i++) {
            locVars[i] = LocVar.read(buffer);
        }
        return locVars;
    }

    private static String[] readUpvalueNames(ByteBuffer buffer) {
        String[] upvalueNames = new String[buffer.getInt()];
        for (int i = 0; i < upvalueNames.length; i++) {
            upvalueNames[i] = BinaryChunkUtil.readLuaString(buffer);
        }
        return upvalueNames;
    }
}
