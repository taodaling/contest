package on2019_10.on2019_10_11_Atcoder_AGC039.TaskC;



import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import template.FastInput;
import template.NumberTheory;

public class TaskC {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        NumberTheory.Modular mod = new NumberTheory.Modular(998244353);

        int n = in.readInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = in.readChar() - '0';
        }

        int[] prefixVal = new int[n];
        prefixVal[0] = x[0];
        for (int i = 1; i < n; i++) {
            prefixVal[i] = mod.mul(prefixVal[i - 1], 2);
            prefixVal[i] = mod.plus(prefixVal[i], x[i]);
        }

        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            if (n % i == 0 && n / i % 2 == 1) {
                factors.add(i);
            }
        }


        TreeMap<Integer, Integer> cntMap = new TreeMap<>();
        for (int d : factors) {
            int cnt = prefixVal[d - 1];
            repeat(y, x, d);
            if (compare(x, y) >= 0) {
                cnt = mod.plus(cnt, 1);
            }
            cntMap.put(d, cnt);
        }


        for (int d : factors) {
            int cnt = cntMap.get(d);
            for (int dd : factors) {
                if (dd < d && d % dd == 0) {
                    cnt = mod.subtract(cnt, cntMap.get(dd));
                }
            }
            cntMap.put(d, cnt);
        }

        int ans = 0;
        int total = mod.plus(prefixVal[n - 1], 1);
        for (int d : factors) {
            int cnt = cntMap.get(d);
            ans = mod.plus(ans, mod.mul(2 * d, cnt));
            total = mod.subtract(total, cnt);
        }

        ans = mod.plus(ans, mod.mul(total, 2 * n));

        out.println(ans);
    }

    public int compare(int[] a, int[] b) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return 0;
    }

    public void repeat(int[] target, int[] s, int k) {
        for (int i = 0; i < target.length; i += k) {
            if (i / k % 2 == 0) {
                for (int j = 0; j < k; j++) {
                    target[i + j] = s[j];
                }
            } else {
                for (int j = 0; j < k; j++) {
                    target[i + j] = 1 - s[j];
                }
            }
        }
    }

}
