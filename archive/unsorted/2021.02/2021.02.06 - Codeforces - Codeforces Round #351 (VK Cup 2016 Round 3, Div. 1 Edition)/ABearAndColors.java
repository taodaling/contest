package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class ABearAndColors {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.ri() - 1;
        }
        int[] cnts = new int[n];
        int maxIndex = 0;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(cnts, 0);
            maxIndex = 0;
            for (int j = i; j < n; j++) {
                cnts[a[j]]++;
                if (cnts[maxIndex] < cnts[a[j]] ||
                        cnts[maxIndex] == cnts[a[j]] && a[j] < maxIndex) {
                    maxIndex = a[j];
                }
                ans[maxIndex]++;
            }
        }
        for(int x : ans){
            out.append(x).append(' ');
        }
    }
}
