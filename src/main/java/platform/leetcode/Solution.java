package platform.leetcode;


import template.math.LongRadix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Solution {
    public static void main(String[] args) {
        new Solution().kMirror(0, 0);
    }

    int type;
    LongRadix radix = new LongRadix(10);

    public void calc(int len, long v, int k, List<Long> ans) {
        if (k < 0) {
            String s = Long.toString(v, type);
            if (new StringBuilder(s).reverse().toString().equals(s)) {
                ans.add(v);
            }
            return;
        }
        for (int i = k == 0 ? 1 : 0; i < 10; i++) {
            calc(len, radix.set(radix.set(v, k, i), len - 1 - k, i), k - 1, ans);
        }
    }

    public long kMirror(int k, int n) {
        for (int i = 2; i < 10; i++) {
            List<Long> ans = new ArrayList<>();
            type = i;
            for (int len = 1; ans.size() < 30 ; len++) {
                calc(len, 0, (len + 1) / 2 - 1, ans);
            }
            ans.sort(Comparator.naturalOrder());
            for(int j = 0; j < 30 && j < ans.size(); j++){
                System.out.println("c[" + i + "][" + j + "] = " + ans.get(j) + "L;");
            }
        }
        return -1;
    }

}
