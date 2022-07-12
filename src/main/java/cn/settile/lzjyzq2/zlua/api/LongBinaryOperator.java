package cn.settile.lzjyzq2.zlua.api;

import cn.settile.lzjyzq2.zlua.number.Math;

public enum LongBinaryOperator implements java.util.function.LongBinaryOperator {

    ADD {
        @Override
        public long applyAsLong(long left, long right) {
            return Long.sum(left, right);
        }
    },

    SUB {
        @Override
        public long applyAsLong(long left, long right) {
            return left - right;
        }
    },

    MUL {
        @Override
        public long applyAsLong(long left, long right) {
            return left * right;
        }
    },

    MOD {
        @Override
        public long applyAsLong(long left, long right) {
            return Math.IMod(left, right);
        }
    },

    IDIV {
        @Override
        public long applyAsLong(long left, long right) {
            return Math.IFloorDiv(left, right);
        }
    },

    AND {
        @Override
        public long applyAsLong(long left, long right) {
            return left & right;
        }
    },

    OR {
        @Override
        public long applyAsLong(long left, long right) {
            return left | right;
        }
    },

    XOR {
        @Override
        public long applyAsLong(long left, long right) {
            return left ^ right;
        }
    },

    SHL {
        @Override
        public long applyAsLong(long left, long right) {
            return Math.shiftLeft(left, right);
        }
    },

    SHR {
        @Override
        public long applyAsLong(long left, long right) {
            return Math.shiftRight(left, right);
        }
    },

    UNM {
        @Override
        public long applyAsLong(long left, long right) {
            return -left;
        }
    },

    NOT {
        @Override
        public long applyAsLong(long left, long right) {
            return ~left;
        }
    }
}
