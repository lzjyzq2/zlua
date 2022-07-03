package cn.settile.lzjyzq2.zlua;

import cn.settile.lzjyzq2.zlua.chunk.BinaryChunk;
import cn.settile.lzjyzq2.zlua.chunk.LocVar;
import cn.settile.lzjyzq2.zlua.chunk.Prototype;
import cn.settile.lzjyzq2.zlua.chunk.Upvalue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Program {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            toUnDumpChunk(args[0]);
        }
    }

    public static void toUnDumpChunk(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        Prototype proto = BinaryChunk.unDump(data);
        list(proto);
    }

    private static void list(Prototype prototype) {
        printHeader(prototype);
        printCode(prototype);
        printDetail(prototype);
        for (Prototype proto : prototype.getProtos()) {
            list(proto);
        }
    }

    public static void printHeader(Prototype prototype) {
        String funcType = "main";
        if (prototype.getLineDefined() > 0) {
            funcType = "function";
        }
        String varargFlag = "";
        if (prototype.getIsVararg() > 0) {
            varargFlag = "+";
        }
        System.out.printf(
                "\n%s <%s:%d,%d> (%d instructions) \n",
                funcType,
                prototype.getSource(),
                prototype.getLineDefined(),
                prototype.getLastLineDefined(),
                prototype.getCode().length
        );
        System.out.printf(
                "%d%s params, %d slots, %d upvalues, ",
                prototype.getNumParams(),
                varargFlag,
                prototype.getMaxStackSize(),
                prototype.getUpValues().length
        );
        System.out.printf(
                "%d locals, %d constants, %d functions\n",
                prototype.getLocVars().length,
                prototype.getConstants().length,
                prototype.getProtos().length
        );
    }

    public static void printCode(Prototype prototype) {
        for (int i = 0; i < prototype.getCode().length; i++) {
            String line = "-";
            if (prototype.getLineInfo().length > 0) {
                line = String.format("%d", prototype.getLineInfo()[i]);
            }
            System.out.printf("\t%d\t[%s]\t0x%08X\n", i + 1, line, prototype.getCode()[i]);
        }
    }

    public static void printDetail(Prototype prototype) {
        System.out.printf("constants (%d):\n", prototype.getConstants().length);
        for (int i = 0; i < prototype.getConstants().length; i++) {
            System.out.printf("\t%d\t%s\n", i + 1, constantToString(prototype.getConstants()[i]));
        }
        System.out.printf("locals (%d):\n", prototype.getLocVars().length);
        for (int i = 0; i < prototype.getLocVars().length; i++) {
            LocVar locVar = prototype.getLocVars()[i];
            System.out.printf("\t%d\t%s\t%d\t%d\n", i, locVar.getVarName(), locVar.getStartPC() + 1, locVar.getEndPC() + 1);
        }
        System.out.printf("upvalues (%d):\n", prototype.getUpValues().length);
        for (int i = 0; i < prototype.getUpValues().length; i++) {
            Upvalue upvalue = prototype.getUpValues()[i];
            System.out.printf("\t%d\t%s\t%d\t%d\n", i, upvalName(prototype, i), upvalue.getInstack(), upvalue.getIdx());
        }

    }

    private static Object upvalName(Prototype prototype, int i) {
        if (prototype.getUpValueNames().length > 0) {
            return prototype.getUpValueNames()[i];
        }
        return "-";
    }

    private static Object constantToString(Object constant) {
        if (constant == null) {
            return "nil";
        } else if (constant instanceof String) {
            return "\"" + constant + "\"";
        } else {
            return constant.toString();
        }
    }


}
