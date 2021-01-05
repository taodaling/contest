package contest;

import template.algo.FloydSearchCircle;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.Generator;
import template.utils.Debug;

public class SequenceAnalysis {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a = in.rl();
        long b = in.rl();
        long c = in.rl();
        int max = (int) 2e6;
        int[] res = FloydSearchCircle.search(new FloydSearchCircle.Interactor() {
            Genrator[] gs = new Genrator[3];
            long[] now = new long[3];

            {
                for (int i = 0; i < 3; i++) {
                    gs[i] = new Genrator(a, b, c);
                    now[i] = gs[i].next();
                }
            }

            @Override
            public void next(int i) {
                now[i] = gs[i].next();
            }

            @Override
            public boolean equal(int i, int j) {
                return now[i] == now[j];
            }
        }, max);
        debug.debugArray("res", res);
        long ans = res == null ? (int)1e9 : res[0] + res[1];
        out.println(ans > max ? -1 : ans);
    }
}

class Genrator {
    long a;
    long b;
    long c;
    long x;

    public void reset() {
        x = 1;
    }

    public Genrator(long a, long b, long c) {
        this.a = a;
        this.b = b;
        this.c = c;
        reset();
    }

    public long next() {
        long ans = x;
        x = (a * x + x % b) % c;
        return ans;
    }
}
