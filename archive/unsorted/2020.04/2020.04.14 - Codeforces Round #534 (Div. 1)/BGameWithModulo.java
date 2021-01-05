package contest;

import template.algo.IntBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.rand.RandomWrapper;

import java.util.Random;

public class BGameWithModulo {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        while (true) {
            String cmd = in.readString();
            if (cmd.equals("start")) {
                solve();
                continue;
            }
            if (cmd.equals("mistake")) {
                throw new RuntimeException();
            }
            if (cmd.equals("end")) {
                return;
            }
        }
    }

    FastInput in;
    FastOutput out;

    public boolean less(int x, int y) {
        out.printf("? %d %d", x, y).println().flush();
        String ans = in.readString();
        if (ans.equals("e")) {
            throw new RuntimeException();
        }
        return ans.equals("y");
    }

    public boolean leq(int x, int y) {
        return !less(y, x);
    }

    public boolean geq(int x, int y) {
        return less(y, x);
    }


    public void answer(int a) {
        out.printf("! %d", a).println().flush();
    }

    RandomWrapper rw = new RandomWrapper(0);

    public int nextInt() {
        return rw.nextInt((int) 1, (int) 1e9);
    }

    public void solve() {
        int x = 0;
        int y = 0;
        while (true) {
            x = nextInt();
            y = x + nextInt();
            if (leq(y, x)) {
                break;
            }
        }

        int parents;
        {
            int l = x;
            int r = y;
            while (r - l > 1) {
                int m = (l + r) >>> 1;
                if (geq(m, r)) {
                    l = m;
                } else {
                    //m > l
                    r = m;
                }
            }
            if (r > l) {
                if (geq(r, l)) {
                    r = l;
                } else {
                    l = r;
                }
            }
            parents = l;
        }

        int[] primes = Factorization.factorizeNumberPrime(parents).toArray();
        int ans = 1;
        for (int p : primes) {
            int l = 0;
            int r = log(p, parents);
            IntBinarySearch ibs = new IntBinarySearch() {
                @Override
                public boolean check(int mid) {
                    int cand = parents / pow(p, r - mid);
                    return leq(cand, 0);
                }
            };
            int remain = ibs.binarySearch(l, r);
            ans *= pow(p, remain);
        }

        answer(ans);
    }

    public int pow(int x, int n) {
        return n == 0 ? 1 : x * pow(x, n - 1);
    }

    public int log(int x, int y) {
        int ans = 0;
        while (y % x == 0) {
            y /= x;
            ans++;
        }
        return ans;
    }
}
