package cn.settile.lzjyzq2.zlua.vm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Instruction {

    public static final int MAXARG_Bx = (1 << 18) - 1;

    public static final int MAXARG_sBx = MAXARG_Bx >> 1;

    public static OpCode getOpcode(int instruction) {
        return OpCode.values()[instruction & 0x3F];
    }

    public static int getA(int instruction) {
        return (instruction >> 6) & 0xFF;
    }

    public static int getB(int instruction) {
        return (instruction >> 23) & 0x1FF;
    }

    public static int getC(int instruction) {
        return (instruction >> 14) & 0x1FF;
    }

    public static int getBx(int instruction) {
        return instruction >>> 14;
    }

    public static int getSBx(int instruction) {
        return getBx(instruction) - MAXARG_sBx;
    }

    public static ABC ABC(int instruction) {
        return new ABC(
                getA(instruction),
                getB(instruction),
                getC(instruction)
        );
    }

    public static ABx ABx(int instruction) {
        return new ABx(
                getA(instruction),
                getBx(instruction)
        );
    }

    public static AsBx AsBx(int instruction) {
        return new AsBx(
                getA(instruction),
                getSBx(instruction)
        );
    }

    public static int Ax(int instruction) {
        return instruction >>> 6;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ABC {
        private int A;

        private int B;

        private int C;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ABx {

        private int A;

        private int Bx;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AsBx {

        private int A;

        private int sBx;

    }
}
