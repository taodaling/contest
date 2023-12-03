package template.math;

public class PureRadix {
    private long[] base;

    public PureRadix(long x) {
        assert x > 1;
        int log = 0;
        long max = Long.MAX_VALUE;
        while (max > 0) {
            max /= x;
            log++;
        }
        //2^log > max
        //2^{log-1} <= max
        base = new long[log];
        base[0] = 1;
        for (int i = 1; i < log; i++) {
            base[i] = base[i - 1] * x;
        }
    }

    public long get(long x, int i) {
        return x / base[i] % base[1];
    }

    Number buffer = new Number();

    public Number NewNumber() {
        return new Number();
    }

    public Number NewNumber(long v) {
        return new Number(v);
    }

    public Number GetNumber(long v) {
        buffer.value = v;
        return buffer;
    }

    public class Number {
        public long value;

        private Number() {
            this(0);
        }

        private Number(long value) {
            this.value = value;
        }

        public long get(int i) {
            return PureRadix.this.get(value, i);
        }

        public Number add(int i, long v) {
            value += v * base[i];
            return this;
        }

        public Number sub(int i, long v) {
            add(i, -v);
            return this;
        }

        public Number replace(int i, long v) {
            add(i, v - get(i));
            return this;
        }
    }
}
