package cn.settile.lzjyzq2.zlua.api;

import cn.settile.lzjyzq2.zlua.number.Math;

public enum DoubleBinaryOperator implements java.util.function.DoubleBinaryOperator {


    ADD {
        @Override
        public double applyAsDouble(double left, double right) {
            return Double.sum(left, right);
        }
    },

    SUB {
        @Override
        public double applyAsDouble(double left, double right) {
            return left - right;
        }
    },

    MUL {
        @Override
        public double applyAsDouble(double left, double right) {
            return left * right;
        }
    },

    MOD {
        @Override
        public double applyAsDouble(double left, double right) {
            return Math.FMod(left, right);
        }
    },

    POW {
        @Override
        public double applyAsDouble(double left, double right) {
            return java.lang.Math.pow(left, right);
        }
    },

    DIV {
        @Override
        public double applyAsDouble(double left, double right) {
            return left / right;
        }
    },

    IDIV {
        @Override
        public double applyAsDouble(double left, double right) {
            return Math.FFloorDiv(left, right);
        }
    },

    UNM {
        @Override
        public double applyAsDouble(double left, double right) {
            return -left;
        }
    }
}
