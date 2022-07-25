package cn.settile.lzjyzq2.zlua;

import cn.settile.lzjyzq2.zlua.api.LuaState;
import cn.settile.lzjyzq2.zlua.api.LuaType;
import cn.settile.lzjyzq2.zlua.chunk.BinaryChunk;
import cn.settile.lzjyzq2.zlua.chunk.LocVar;
import cn.settile.lzjyzq2.zlua.chunk.Prototype;
import cn.settile.lzjyzq2.zlua.chunk.UpvalueInfo;
import cn.settile.lzjyzq2.zlua.lib.IO;
import cn.settile.lzjyzq2.zlua.state.DefaultLuaState;
import cn.settile.lzjyzq2.zlua.vm.Instruction;
import cn.settile.lzjyzq2.zlua.vm.OpArg;
import cn.settile.lzjyzq2.zlua.vm.OpCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Program {

    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            switch (args[0]) {
                case "-c":
                    toUnDumpChunk(args[1]);
                    break;
                case "-r":
                    runLuaMain(args[1]);
                    break;
            }
        }
    }

    public static void runLuaMain(String path) throws IOException {
        byte[] data = Files.readAllBytes(Paths.get(path));
        LuaState luaState = new DefaultLuaState();
        luaState.register("print", IO::print);
        luaState.load(data, path, "b");
        luaState.call(0, 0);
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
            int instruction = prototype.getCode()[i];
            OpCode opCode = Instruction.getOpcode(instruction);
            System.out.printf("\t%d\t[%s]\t%-10s \t", i + 1, line, opCode.name());
            printOperands(instruction);
            System.out.println();
        }
    }

    private static void printOperands(int i) {
        OpCode opCode = Instruction.getOpcode(i);
        switch (opCode.getOpMode()) {
            case IABC: {
                Instruction.ABC abc = Instruction.ABC(i);
                System.out.printf("%d", abc.getA());
                if (opCode.getArgBMode() != OpArg.OpArgN) {
                    System.out.printf(" %d", abc.getB() > 0xFF ? -1 - (abc.getB() & 0xFF) : abc.getB());
                }
                if (opCode.getArgCMode() != OpArg.OpArgN) {
                    System.out.printf(" %d", abc.getC() > 0xFF ? -1 - (abc.getC() & 0xFF) : abc.getC());
                }
                break;
            }
            case IABx: {
                Instruction.ABx aBx = Instruction.ABx(i);
                System.out.printf("%d", aBx.getA());
                if (opCode.getArgBMode() == OpArg.OpArgK) {
                    System.out.printf(" %d", -1 - aBx.getBx());
                } else if (opCode.getArgBMode() == OpArg.OpArgU) {
                    System.out.printf(" %d", aBx.getBx());
                }
                break;
            }
            case IAsBx: {
                Instruction.AsBx asBx = Instruction.AsBx(i);
                System.out.printf("%d %d", asBx.getA(), asBx.getSBx());
                break;
            }
            case IAx: {
                System.out.printf("%d", -1 - Instruction.Ax(i));
                break;
            }

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
            UpvalueInfo upvalueInfo = prototype.getUpValues()[i];
            System.out.printf("\t%d\t%s\t%d\t%d\n", i, upvalName(prototype, i), upvalueInfo.getInstack(), upvalueInfo.getIdx());
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

    static void printStack(LuaState luaState) {
        int top = luaState.getTop();
        for (int i = 1; i <= top; i++) {
            LuaType type = luaState.type(i);
            switch (type) {
                case LUA_TBOOLEAN:
                    System.out.printf("[%b]", luaState.toBoolean(i));
                    break;
                case LUA_TNUMBER:
                    if (luaState.isInteger(i)) {
                        System.out.printf("[%d]", luaState.toInteger(i));
                    } else {
                        System.out.printf("[%f]", luaState.toNumber(i));
                    }
                    break;
                case LUA_TSTRING:
                    System.out.printf("[\"%s\"]", luaState.toString(i));
                    break;
                default:
                    System.out.printf("[%s]", luaState.typeName(type));
            }
        }
        System.out.println();
    }
}
