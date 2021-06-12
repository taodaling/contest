package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class P1919ABProblemFFT {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] buf = new char[(int) 2e6];
        int n = in.rs(buf, 0);
        NatureBigInt a = NatureBigInt.valueOf(new CharSequence() {
            @Override
            public int length() {
                return n;
            }

            @Override
            public char charAt(int index) {
                return buf[index];
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }
        });
        int m = in.rs(buf, 0);
        NatureBigInt b = NatureBigInt.valueOf(new CharSequence() {
            @Override
            public int length() {
                return m;
            }

            @Override
            public char charAt(int index) {
                return buf[index];
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }
        });

        NatureBigInt c = a.mul(b);
        c.toString(out.getCache());
    }
}
