package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__223__Div__1_.A__Sereja_and_Prefixes;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeMap;

public class ASerejaAndPrefixes {


    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        TreeMap<Long, Interval> map = new TreeMap<>();
        long last = -1;
        for (int i = 0; i < m; i++) {
            int t = in.ri();
            Interval interval = new Interval();
            if (t == 1) {
                Add add = new Add();
                add.x = in.ri();
                interval.op = add;
                interval.l = interval.r = ++last;
            } else {
                Copy copy = new Copy();
                copy.l = in.ri();
                copy.c = in.ri();
                interval.op = copy;
                interval.l = ++last;
                last = interval.r = interval.l + (long) copy.l * copy.c - 1;
            }
            map.put(interval.l, interval);
        }

        int n = in.ri();
        for (int i = 0; i < n; i++) {
            long x = in.rl() - 1;
            while (true) {
                Interval interval = map.floorEntry(x).getValue();
                if (interval.op instanceof Add) {
                    Add add = (Add) interval.op;
                    out.println(add.x);
                    break;
                } else {
                    Copy copy = (Copy) interval.op;
                    x = (x - interval.l) % copy.l;
                }
            }
        }
    }
}

class Op {
}

class Add extends Op {
    int x;
}

class Copy extends Op {
    int l;
    int c;
}

class Interval {
    long l;
    long r;
    Op op;
}
