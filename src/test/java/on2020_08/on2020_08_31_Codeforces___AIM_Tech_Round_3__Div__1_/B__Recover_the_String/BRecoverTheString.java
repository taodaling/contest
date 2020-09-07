package on2020_08.on2020_08_31_Codeforces___AIM_Tech_Round_3__Div__1_.B__Recover_the_String;



import com.sun.xml.internal.fastinfoset.util.PrefixArray;
import template.io.FastInput;
import template.io.FastOutput;

public class BRecoverTheString {
    String noWay = "Impossible";

    public void solve(int testNumber, FastInput in, FastOutput out) {
        long a00 = in.readLong();
        long a01 = in.readLong();
        long a10 = in.readLong();
        long a11 = in.readLong();

        int n = -1;
        int m = -1;

        for (int i = 0; i <= 1000000; i++) {
            if (choose(i) == a00) {
                n = i;
            }
            if (choose(i) == a11) {
                m = i;
            }
        }

        String ans = null;
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                if (ans != null) {
                    continue;
                }
                ans = notNull(ans, solve(a00, a01, a10, a11, n ^ i, m ^ j));
            }
        }

        if (ans == null) {
            out.println(noWay);
        } else {
            out.println(ans);
        }
    }

    String notNull(String a, String b) {
        return a == null ? b : a;
    }

    String solve(long a00, long a01, long a10, long a11, int n, int m) {
        if (n < 0 || m < 0) {
            return null;
        }
        if (a00 != choose(n) || a11 != choose(m)) {
            return null;
        }
        if (a01 + a10 != n * (long) m) {
            return null;
        }
        StringBuilder ans = new StringBuilder();
        int cur = 0;

        int[] after = new int[n];
        for (int i = 0; i < n; i++) {
            after[i] = i == 0 ? m : after[i - 1];
            while (a01 < after[i]) {
                after[i]--;
            }
            a01 -= after[i];
        }

        if (a01 != 0) {
            return null;
        }

        for (int i = n - 1; i >= 0; i--) {
            while (cur < after[i]) {
                cur++;
                ans.append(1);
            }
            ans.append(0);
        }

        while (cur < m) {
            cur++;
            ans.append(1);
        }

        return ans.reverse().toString();
    }


    public long choose(long n) {
        return n * (n - 1) / 2;
    }
}
