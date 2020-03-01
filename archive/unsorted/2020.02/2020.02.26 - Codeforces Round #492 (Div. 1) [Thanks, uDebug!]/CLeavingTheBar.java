package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class CLeavingTheBar {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Vector[] vecs = new Vector[n];
        for (int i = 0; i < n; i++) {
            vecs[i] = new Vector(in.readInt(), in.readInt());
        }

        Vector[] sorted = vecs.clone();
        long end = System.currentTimeMillis() + 1500;
        while (System.currentTimeMillis() < end) {
            Randomized.shuffle(sorted, 0, n);
            Vector now = new Vector(0, 0);
            for (Vector vec : sorted) {
                Vector v1 = new Vector(now.x + vec.x, now.y + vec.y);
                Vector v2 = new Vector(now.x - vec.x, now.y - vec.y);
                if (v1.length() < v2.length()) {
                    now = v1;
                    vec.sign = 1;
                } else {
                    now = v2;
                    vec.sign = -1;
                }
            }
            if (now.length() <= 2e12) {
                break;
            }
        }

        for (int i = 0; i < n; i++) {
            out.println(vecs[i].sign);
        }
    }

}

class Vector {
    long x;
    long y;
    int sign;

    public Vector(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public long length() {
        return x * x + y * y;
    }
}
