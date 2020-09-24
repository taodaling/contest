package on2020_09.on2020_09_22_Codeforces___Codeforces_Round__356__Div__1_.B__Bear_and_Tower_of_Cubes;



import template.io.FastInput;
import template.io.FastOutput;

public class BBearAndTowerOfCubes {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long[] ans = solve(n);
        out.println(ans[0]).println(ans[1]);
    }

    public long pow3(long n) {
        return n * n * n;
    }

    public long sqrt3(long n) {
        long ans = Math.round(Math.pow(n, 1D / 3));
        while (pow3(ans + 1) <= n) {
            ans++;
        }
        while (pow3(ans) > n) {
            ans--;
        }
        return ans;
    }

    public long[] solve(long n) {
        if (n < 8) {
            return new long[]{n, n};
        }
        long x = sqrt3(n);
        long[] a = solve(n - pow3(x));
        long[] b = solve(pow3(x) - 1 - pow3(x - 1));
        a[1] += pow3(x);
        a[0]++;
        b[1] += pow3(x - 1);
        b[0]++;
        if (a[0] > b[0] || a[0] == b[0] && a[1] >= b[1]) {
            return a;
        }
        return b;
    }
}
