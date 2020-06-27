package template.math;

import template.utils.Debug;

public class HandyLongModular implements ILongModular {
    public static void main(String[] args) {
        Debug debug = new Debug(true);
        {
            BigLongModular mod = new BigLongModular((long) 1e18);
            long a = (long) 1e18;
            long b = (long) 1e18;
            for (int i = 0; i < 10000000; i++) {
                mod.mul(a, b);
            }
        }

        debug.elapse("BigLongModular");

        {
            HandyLongModular mod = new HandyLongModular((long) 1e18);
            long a = (long) 1e18;
            long b = (long) 1e18;
            for (int i = 0; i < 10000000; i++) {
                mod.mul(a, b);
            }
        }

        debug.elapse("HandyLongModular");


        {
            LongModular mod = new LongModular((long) 1e18);
            long a = (long) 1e18;
            long b = (long) 1e18;
            for (int i = 0; i < 10000000; i++) {
                mod.mul(a, b);
            }
        }

        debug.elapse("LongModular");

        {
            LongModularDanger mod = new LongModularDanger((long) 1e18);
            long a = (long) 1e18;
            long b = (long) 1e18;
            for (int i = 0; i < 10000000; i++) {
                mod.mul(a, b);
            }
        }

        debug.elapse("LongModularDanger");
    }

    public HandyLongModular(long mod) {
        this.mod = mod;
    }

    private long mod;
    private long accumulator;

    @Override
    public long getMod() {
        return mod;
    }

    @Override
    public long plus(long a, long b) {
        return valueOf(a + b);
    }

    @Override
    public long subtract(long a, long b) {
        return valueOf(a - b);
    }

    @Override
    public long valueOf(long x) {
        x %= mod;
        if (x < 0) {
            x += mod;
        }
        return x;
    }

    private static long mask = (1L << 32) - 1;

    private void accumulate(long x) {
        for (int i = 63; i >= 0; i -= 4) {
            accumulator = (accumulator << 1) | ((x >> i) & 1);
            if (accumulator >= mod) {
                accumulator -= mod;
            }
            accumulator = (accumulator << 1) | ((x >> i - 1) & 1);
            if (accumulator >= mod) {
                accumulator -= mod;
            }
            accumulator = (accumulator << 1) | ((x >> i - 2) & 1);
            if (accumulator >= mod) {
                accumulator -= mod;
            }
            accumulator = (accumulator << 1) | ((x >> i - 3) & 1);
            if (accumulator >= mod) {
                accumulator -= mod;
            }
        }
    }

    @Override
    public long mul(long a, long b) {
        long a0 = a & mask;
        long a1 = (a >>> 32) & mask;
        long b0 = b & mask;
        long b1 = (b >>> 32) & mask;
        long a0b0 = a0 * b0;
        long a0b1 = a0 * b1;
        long a1b0 = a1 * b0;
        long a1b1 = a1 * b1;

        //00
        long d0 = DigitUtils.lowBit(a0b0) & mask;
        long d1 = (DigitUtils.highBit(a0b0) & mask) + (DigitUtils.lowBit(a1b0) & mask) +
                (DigitUtils.lowBit(a0b1) & mask);
        long d2 = (DigitUtils.highBit(a1b0) & mask) + (DigitUtils.highBit(a0b1) & mask) +
                (DigitUtils.lowBit(a1b1) & mask) + (DigitUtils.highBit(d1) & mask);
        long d3 = (DigitUtils.highBit(a1b1) & mask) + (DigitUtils.highBit(d2) & mask);
        d2 = DigitUtils.lowBit(d2) & mask;
        d1 = DigitUtils.lowBit(d1) & mask;

        accumulator = 0;
        accumulate((d3 << 32) | d2);
        accumulate((d1 << 32) | d0);
        return accumulator;
    }
}
