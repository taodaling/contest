package on2021_10.on2021_10_11_DMOJ___DMOPC__21_Contest_2.P3___Divisions;



import template.binary.Bits;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.RandomWrapper;

public class P3Divisions {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;
        int n = in.ri();
        for (int i = 0; i < n; i++) {
            solve();
        }
    }

    FastInput in;
    FastOutput out;

    public int guess(long x) {
        out.println(x).flush();
        int ans = in.ri();
        if (ans == -1) {
            throw new RuntimeException();
        }
        return ans;
    }

    RandomWrapper rw = new RandomWrapper(0);

    public void solve() {
        int ans = guess(0);
        if (ans == 0) {
            return;
        }
        int len = ans;
        long test = 1L << len - 1;
        int unknown = len - 1;
        while (true) {
            for (int i = 0; i < unknown; i++) {
                test = Bits.set(test, i, rw.nextInt(0, 1) == 0);
            }
            int res = guess(test);
            if (res == 0) {
                return;
            }
            int commonHead = len - res / 2;
            unknown = len - commonHead;
            test = Bits.flip(test, unknown - 1);
            unknown--;
        }

    }
}
