package cn.settile.lzjyzq2.zlua.vm;

import static cn.settile.lzjyzq2.zlua.vm.OpArg.*;
import static cn.settile.lzjyzq2.zlua.vm.OpMode.*;

public enum OpCode {


    /*       T  A    B       C     mode */
    MOVE(0, 1, OpArgR, OpArgN, IABC, InstMisc::move), // R(A) := R(B)
    LOADK(0, 1, OpArgK, OpArgN, IABx, InstLoad::loadK), // R(A) := Kst(Bx)
    LOADKX(0, 1, OpArgN, OpArgN, IABx, InstLoad::loadKx), // R(A) := Kst(extra arg)
    LOADBOOL(0, 1, OpArgU, OpArgU, IABC, InstLoad::loadBool), // R(A) := (bool)B; if (C) pc++
    LOADNIL(0, 1, OpArgU, OpArgN, IABC, InstLoad::loadNil), // R(A), R(A+1), ..., R(A+B) := nIl
    GETUPVAL(0, 1, OpArgU, OpArgN, IABC, null), // R(A) := UpValue[B]
    GETTABUP(0, 1, OpArgU, OpArgK, IABC, null), // R(A) := UpValue[B][RK(C)]
    GETTABLE(0, 1, OpArgR, OpArgK, IABC, InstTable::getTable), // R(A) := R(B)[RK(C)]
    SETTABUP(0, 0, OpArgK, OpArgK, IABC, null), // UpValue[A][RK(B)] := RK(C)
    SETUPVAL(0, 0, OpArgU, OpArgN, IABC, null), // UpValue[B] := R(A)
    SETTABLE(0, 0, OpArgK, OpArgK, IABC, InstTable::setTable), // R(A)[RK(B)] := RK(C)
    NEWTABLE(0, 1, OpArgU, OpArgU, IABC, InstTable::newTable), // R(A) := {} (size = B,C)
    SELF(0, 1, OpArgR, OpArgK, IABC, null), // R(A+1) := R(B); R(A) := R(B)[RK(C)]
    ADD(0, 1, OpArgK, OpArgK, IABC, InstOperators::add), // R(A) := RK(B) + RK(C)
    SUB(0, 1, OpArgK, OpArgK, IABC, InstOperators::sub), // R(A) := RK(B) - RK(C)
    MUL(0, 1, OpArgK, OpArgK, IABC, InstOperators::mul), // R(A) := RK(B) * RK(C)
    MOD(0, 1, OpArgK, OpArgK, IABC, InstOperators::mod), // R(A) := RK(B) % RK(C)
    POW(0, 1, OpArgK, OpArgK, IABC, InstOperators::pow), // R(A) := RK(B) ^ RK(C)
    DIV(0, 1, OpArgK, OpArgK, IABC, InstOperators::div), // R(A) := RK(B) / RK(C)
    IDIV(0, 1, OpArgK, OpArgK, IABC, InstOperators::idiv), // R(A) := RK(B) // RK(C)
    BAND(0, 1, OpArgK, OpArgK, IABC, InstOperators::band), // R(A) := RK(B) & RK(C)
    BOR(0, 1, OpArgK, OpArgK, IABC, InstOperators::bor), // R(A) := RK(B) | RK(C)
    BXOR(0, 1, OpArgK, OpArgK, IABC, InstOperators::bxor), // R(A) := RK(B) ~ RK(C)
    SHL(0, 1, OpArgK, OpArgK, IABC, InstOperators::shl), // R(A) := RK(B) << RK(C)
    SHR(0, 1, OpArgK, OpArgK, IABC, InstOperators::shr), // R(A) := RK(B) >> RK(C)
    UNM(0, 1, OpArgR, OpArgN, IABC, InstOperators::unm), // R(A) := -R(B)
    BNOT(0, 1, OpArgR, OpArgN, IABC, InstOperators::bnot), // R(A) := ~R(B)
    NOT(0, 1, OpArgR, OpArgN, IABC, InstOperators::not), // R(A) := not R(B)
    LEN(0, 1, OpArgR, OpArgN, IABC, InstOperators::len), // R(A) := length of R(B)
    CONCAT(0, 1, OpArgR, OpArgR, IABC, InstOperators::concat), // R(A) := R(B).. ... ..R(C)
    JMP(0, 0, OpArgR, OpArgN, IAsBx, InstMisc::jmp), // pc+=sBx; if (A) close all upvalues >= R(A - 1)
    EQ(1, 0, OpArgK, OpArgK, IABC, InstOperators::eq), // if ((RK(B) == RK(C)) ~= A) then pc++
    LT(1, 0, OpArgK, OpArgK, IABC, InstOperators::lt), // if ((RK(B) <  RK(C)) ~= A) then pc++
    LE(1, 0, OpArgK, OpArgK, IABC, InstOperators::le), // if ((RK(B) <= RK(C)) ~= A) then pc++
    TEST(1, 0, OpArgN, OpArgU, IABC, InstOperators::test), // if not (R(A) <=> C) then pc++
    TESTSET(1, 1, OpArgR, OpArgU, IABC, InstOperators::testSet), // if (R(B) <=> C) then R(A) := R(B) else pc++
    CALL(0, 1, OpArgU, OpArgU, IABC, null), // R(A), ... ,R(A+C-2) := R(A)(R(A+1), ... ,R(A+B-1))
    TAILCALL(0, 1, OpArgU, OpArgU, IABC, null), // return R(A)(R(A+1), ... ,R(A+B-1))
    RETURN(0, 0, OpArgU, OpArgN, IABC, null), // return R(A), ... ,R(A+B-2)
    FORLOOP(0, 1, OpArgR, OpArgN, IAsBx, InstOperators::forLoop), // R(A)+=R(A+2); if R(A) <?= R(A+1) then { pc+=sBx; R(A+3)=R(A) }
    FORPREP(0, 1, OpArgR, OpArgN, IAsBx, InstOperators::forPrep), // R(A)-=R(A+2); pc+=sBx
    TFORCALL(0, 0, OpArgN, OpArgU, IABC, null), // R(A+3), ... ,R(A+2+C) := R(A)(R(A+1), R(A+2));
    TFORLOOP(0, 1, OpArgR, OpArgN, IAsBx, null), // if R(A+1) ~= nil then { R(A)=R(A+1); pc += sBx }
    SETLIST(0, 0, OpArgU, OpArgU, IABC, InstTable::setList), // R(A)[(C-1)*FPF+i] := R(A+i), 1 <= i <= B
    CLOSURE(0, 1, OpArgU, OpArgN, IABx, null), // R(A) := closure(KPROTO[Bx])
    VARARG(0, 1, OpArgU, OpArgN, IABC, null), // R(A), R(A+1), ..., R(A+B-2) = vararg
    EXTRAARG(0, 0, OpArgU, OpArgU, IAx, null) // extra (larger) argument for previous opcode
    ;
    private final int testFlag; // operator is a test (next instruction must be a jump)
    private final int setAFlag; // instruction set register A
    private final OpArg argBMode; // B arg mode
    private final OpArg argCMode; // C arg mode
    private final OpMode opMode; // op mode

    private final OpAction opAction;

    OpCode(int testFlag, int setAFlag, OpArg argBMode, OpArg argCMode, OpMode opMode, OpAction opAction) {
        this.testFlag = testFlag;
        this.setAFlag = setAFlag;
        this.argBMode = argBMode;
        this.argCMode = argCMode;
        this.opMode = opMode;
        this.opAction = opAction;
    }

    public int getTestFlag() {
        return testFlag;
    }

    public int getSetAFlag() {
        return setAFlag;
    }

    public OpArg getArgBMode() {
        return argBMode;
    }

    public OpArg getArgCMode() {
        return argCMode;
    }

    public OpMode getOpMode() {
        return opMode;
    }

    public OpAction getOpAction() {
        return opAction;
    }
}
