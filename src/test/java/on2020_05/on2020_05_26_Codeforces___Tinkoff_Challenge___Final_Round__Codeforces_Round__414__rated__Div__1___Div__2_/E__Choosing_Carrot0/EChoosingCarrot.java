package on2020_05.on2020_05_26_Codeforces___Tinkoff_Challenge___Final_Round__Codeforces_Round__414__rated__Div__1___Div__2_.E__Choosing_Carrot0;




import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.TreeMap;

public class EChoosingCarrot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);

        int[] ans = new int[n];

        //odd
        TreeMap<Integer, Integer> odd = new TreeMap<>();
        TreeMap<Integer, Integer> even = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            if (i > 0 && i + 1 < n) {
                int len = Math.min(i - 1, n - 1 - (i + 1)) * 2 + 3;
                int val = Math.max(Math.min(a[i], a[i - 1]), Math.min(a[i], a[i + 1]));
                odd.put(val, Math.max(len, odd.getOrDefault(val, 0)));
            }
            if (i > 0) {
                int max = Math.max(a[i - 1], a[i]);
                int evenLen = Math.min(i - 1, n - 1 - i) * 2 + 2;
                even.put(max, Math.max(evenLen, even.getOrDefault(max, 0)));
            }
        }

        ans[n - 1] = Arrays.stream(a).max().getAsInt();
        for (int i = n - 2; i >= 0; i--) {
            int remain = n - i;
            if (remain % 2 == 1) {
                while (odd.lastEntry().getValue() < remain) {
                    odd.pollLastEntry();
                }
                ans[i] = odd.lastKey();
            } else {
                while (even.lastEntry().getValue() < remain) {
                    even.pollLastEntry();
                }
                ans[i] = even.lastKey();
            }
        }

        for (int x : ans) {
            out.append(x).append(' ');
        }
    }
}
