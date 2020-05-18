package contest;

import template.math.Modular;
import template.math.Power;

import java.util.Arrays;

public class ModCounters {
    int limit = 512;
    Modular mod = new Modular(1e9 + 7);
    Power power = new Power(mod);


    public int[] mul(int[] a, int[] b) {
        int[] ans = new int[limit];
        for (int i = 0; i < limit; i++) {
            for (int j = 0; j < limit; j++) {
                int k = (i + j) % limit;
                ans[k] = mod.plus(ans[k], mod.mul(a[i], b[j]));
            }
        }
        return ans;
    }

    public int[] pow(int[] x, int n) {
        if (n == 0) {
            int[] ans = new int[limit];
            ans[0] = 1;
            return ans;
        }
        int[] ans = pow(x, n / 2);
        ans = mul(ans, ans);
        if (n % 2 == 1) {
            ans = mul(ans, x);
        }
        return ans;
    }

    public int findExpectedSum(int[] P, int A0, int X, int Y, int N, int K) {
        int[] A = new int[N];
        A[0] = A0;
        for (int i = 1; i <= N - 1; i++) {
            A[i] = (int) (((long) A[i - 1] * X + Y) % 1812447359);
        }
        int[] S = Arrays.copyOf(P, N);
        for (int i = P.length; i <= N - 1; i++) {
            S[i] = A[i] % 512;
        }


        int limit = 512;
        int p = power.inverseByFermat(N);
        int q = mod.subtract(1, p);

        int[] cnt = new int[limit];
        for (int s : S) {
            cnt[s]++;
        }

        int[] poly = new int[limit];
        poly[0] = q;
        poly[1] = p;

        poly = pow(poly, K);
        int[] probs = mul(cnt, poly);

        int ans = 0;
        for (int i = 0; i < limit; i++) {
            ans = mod.plus(ans, mod.mul(probs[i], i));
        }

        return ans;
    }


}
