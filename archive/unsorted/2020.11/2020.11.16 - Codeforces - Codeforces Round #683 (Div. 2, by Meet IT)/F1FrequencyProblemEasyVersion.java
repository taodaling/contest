package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.Arrays;

public class F1FrequencyProblemEasyVersion {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] cnts = new int[n + 1];
        for (int x : a) {
            cnts[x]++;
        }
        int maxOccur = 0;
        for (int i = 1; i <= n; i++) {
            if (cnts[i] > cnts[maxOccur]) {
                maxOccur = i;
            }
        }
        int sqrt = (int) Math.ceil(Math.sqrt(n));
        int ans = 0;
        int[] reg = new int[n + n + 10];
        int zero = n;
        for (int i = 1; i <= n; i++) {
            if (cnts[i] < sqrt || i == maxOccur) {
                continue;
            }
            Arrays.fill(reg, -2);
            int ps = 0;
            reg[zero] = -1;
            for (int j = 0; j < n; j++) {
                if (a[j] == maxOccur) {
                    ps++;
                } else if (a[j] == i) {
                    ps--;
                }
                if (reg[ps + zero] != -2) {
                    ans = Math.max(ans, j - reg[ps + zero]);
                } else {
                    reg[ps + zero] = j;
                }
            }
        }

        occur = new int[n + 1];
        for (int i = 1; i < sqrt; i++) {
            Arrays.fill(occur, 0);
            exceed = 0;
            threshold = i;
            for (int j = 0, l = 0; j < n; j++) {
                add(a[j], 1);
                while (occur[maxOccur] > threshold) {
                    assert l < j;
                    add(a[l++], -1);
                }
                if (exceed >= 2) {
                    ans = Math.max(ans, j - l + 1);
                }
            }
        }

        out.println(ans);
    }

    int[] occur;
    int exceed;
    int threshold;

    public void add(int x, int d) {
        if (occur[x] >= threshold) {
            exceed--;
        }
        occur[x] += d;
        if (occur[x] >= threshold) {
            exceed++;
        }
    }
}
