package contest;

import template.io.FastInput;
import template.io.FastOutput;

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
            int len = Math.min(i, n - 1 - i) * 2 + 1;
            odd.put(a[i], Math.max(len, odd.getOrDefault(a[i], 0)));
            if (i > 0) {
                int min = Math.min(a[i - 1], a[i]);
                int evenLen = Math.min(i - 1, n - 1 - i) * 2 + 2;
                even.put(min, Math.max(evenLen, even.getOrDefault(min, 0)));
            }
        }

        ans[n - 1] = odd.lastEntry().getKey();
        for (int i = n - 2; i >= 0; i--) {
            int remain = n - i - 1;
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

        for(int x : ans){
            out.append(x).append(' ');
        }
    }
}
