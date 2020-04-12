package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class DMinimumEulerCycle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        long l = in.readLong();
        long r = in.readLong();
        Machine m = new Machine(0, n, n, 1);
        m.go(l);
        for (long i = l; i <= r; i++) {
            out.append(m.value()).append(' ');
            m.go(1);
        }
        out.println();
    }
}

class Machine {
    int x;
    int y;
    int n;
    int deg;

    public int value() {
        return deg == 0 ? x : y;
    }

    public Machine(int x, int y, int n, int deg) {
        this.x = x;
        this.y = y;
        this.n = n;
        this.deg = deg;

    }

    public void go(long k) {
        if (k == 0) {
            return;
        }
        if (k == 1) {
            deg++;
            if (deg == 2) {
                deg = 0;
                y++;
                if (y > n) {
                    x = x + 1;
                    y = x + 1;
                    if(y > n){
                        x = 1;
                    }
                }
            }
            return;
        }
        if (k % 2 == 1) {
            go(1);
            go(k - 1);
            return;
        }
        long remain = Math.min(k / 2, n - y);
        y += remain;
        k -= remain * 2;
        if (k > 0) {
            go(1);
            go(k - 1);
        }
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", deg = " + deg;
    }
}

