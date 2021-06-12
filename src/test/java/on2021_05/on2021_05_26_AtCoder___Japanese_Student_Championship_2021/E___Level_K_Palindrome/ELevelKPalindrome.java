package on2021_05.on2021_05_26_AtCoder___Japanese_Student_Championship_2021.E___Level_K_Palindrome;



import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ELevelKPalindrome {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.ri();
        level = Graph.createGraph(k + 1);
        int L = (int) 1e6;
        s = new char[L];
        int n = in.rs(s);
        mark(0, n - 1, k);
        if (!level[0].isEmpty() && length(level[0].get(0)) == 1) {
            valid = false;
        }
        if (!valid) {
            out.println("impossible");
            return;
        }
        int[] cnt = new int[charset];
        int retain = 0;
        for (int i = 1; i <= k; i++) {
            count(level[i], 0, cnt);
            int max = cnt[argmax(cnt)];
            retain += max;
        }

        int[] lcnt = new int[charset];
        int[] rcnt = new int[charset];
        if (!level[0].isEmpty()) {
            boolean allSame = true;
            int minReduce = (int) 1e9;
            int len = length(level[0].get(0));
            int sign = 0;
            for (int[] lr : level[0]) {
                sign ^= 1;
                if (sign == 0) {
                    SequenceUtils.reverse(s, lr[0], lr[1]);
                }
            }
            int l = 0;
            int r = len - 1;
            while (l < r) {
                count(level[0], l, lcnt);
                count(level[0], r, rcnt);
                int[] a = getMaxAndSecond(lcnt);
                int[] b = getMaxAndSecond(rcnt);
                int c1 = Math.max(lcnt[a[0]] + rcnt[b[1]], lcnt[a[1]] + rcnt[b[0]]);
                int c2 = lcnt[a[0]] + rcnt[b[0]];
                retain += c2;
                if (a[0] == b[0]) {
                    minReduce = Math.min(minReduce, c2 - c1);
                } else {
                    allSame = false;
                }
                l++;
                r--;
            }
            if (l == r) {
                count(level[0], l, lcnt);
                int best = lcnt[argmax(lcnt)];
                retain += best;
            }

            if (allSame) {
                retain -= minReduce;
            }
        }

        int cost = n - retain;
        out.println(cost);
    }

    int charset = 'z' - 'a' + 1;

    char[] s;

    public int argmax(int[] cnt) {
        int maxIndex = 0;
        for (int i = 0; i < cnt.length; i++) {
            if (cnt[i] > cnt[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int[] getMaxAndSecond(int[] cnt) {
        int maxIndex = argmax(cnt);
        int secondIndex = maxIndex == 0 ? 1 : 0;
        for (int i = 0; i < cnt.length; i++) {
            if (i == maxIndex) {
                continue;
            }
            if (cnt[i] > cnt[secondIndex]) {
                secondIndex = i;
            }
        }
        return new int[]{maxIndex, secondIndex};
    }

    public void count(List<int[]> intervals, int offset, int[] cnt) {
        Arrays.fill(cnt, 0);
        for (int[] lr : intervals) {
            cnt[s[lr[0] + offset] - 'a']++;
        }
    }

    public int length(int[] lr) {
        return lr[1] - lr[0] + 1;
    }

    boolean valid = true;
    List<int[]>[] level;

    void mark(int l, int r, int k) {
        if (l > r) {
            if (k != 0) {
                valid = false;
            }
            return;
        }
        if (k == 0) {
            level[k].add(new int[]{l, r});
            return;
        }
        int len = r - l + 1;
        int mid = (l + r) / 2;
        if (len % 2 == 1) {
            level[k].add(new int[]{mid, mid});
            mark(l, mid - 1, k - 1);
            mark(mid + 1, r, k - 1);
        } else {
            mark(l, mid, k - 1);
            mark(mid + 1, r, k - 1);
        }
    }
}
