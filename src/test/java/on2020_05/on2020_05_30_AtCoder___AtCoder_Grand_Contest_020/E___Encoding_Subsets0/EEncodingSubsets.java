package on2020_05.on2020_05_30_AtCoder___AtCoder_Grand_Contest_020.E___Encoding_Subsets0;



import template.io.FastInput;
import template.io.FastOutput;
import template.math.Modular;

import java.util.HashMap;
import java.util.Map;

public class EEncodingSubsets {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int ans = dp(in.readString());
        out.println(ans);
    }

    Modular mod = new Modular(998244353);
    Map<String, Integer> dp = new HashMap<>((int) 1e6);

    private String merge(String a, String b) {
        int n = a.length();
        StringBuilder builder = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            builder.append(a.charAt(i) == '1' && b.charAt(i) == '1' ? 1 : 0);
        }
        return builder.toString();
    }

    public int dp(String bs) {
        Integer ans = dp.get(bs);
        if (ans == null) {
            if (bs.length() == 0) {
                ans = 1;
                dp.put(bs, ans);
                return ans;
            }

            int normal = dp(bs.substring(1));
            if (bs.charAt(0) == '1') {
                normal = mod.mul(normal, 2);
            }

            int special = 0;
            for (int i = 1; i <= bs.length() / 2; i++) {
                for (int k = 2; k * i <= bs.length(); k++) {
                    String state = bs.substring(0, i);
                    for (int j = i; j < k * i; j += i) {
                        state = merge(state, bs.substring(j, j + i));
                    }
                    special = mod.plus(special, mod.mul(dp(state), dp(bs.substring(k * i))));
                }
            }

            ans = mod.plus(normal, special);
            dp.put(bs, ans);
        }

        return ans;
    }
}
