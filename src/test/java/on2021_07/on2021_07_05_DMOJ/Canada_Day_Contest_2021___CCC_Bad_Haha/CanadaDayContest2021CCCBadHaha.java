package on2021_07.on2021_07_05_DMOJ.Canada_Day_Contest_2021___CCC_Bad_Haha;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;

public class CanadaDayContest2021CCCBadHaha {
    char[] s = new char[(int) 2e5];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.rs(s);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }
        int k = in.ri();
        k = Math.min(n, k);
        boolean[] include = new boolean[n];
        int[][] next = new int[10][n + 1];
        for (int i = 0; i < 10; i++) {
            next[i][n] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 10; j++) {
                next[j][i] = next[j][i + 1];
            }
            next[s[i]][i] = i;
        }
        int pick = 0;
        int target = n - k;
        int head = 0;
        while (pick < target) {
            for (int i = 1; i < 10; i++) {
                int go = next[i][head];
                int maxPick = n - go + pick;
                if (maxPick >= target) {
                    //pick
                    pick++;
                    head = go + 1;
                    include[go] = true;
                    break;
                }
            }
        }
        int[] cnts = new int[10];
        for (int i = 0; i < n; i++) {
            if (!include[i]) {
                cnts[s[i]]++;
            } else {
                out.append((int) s[i]);
            }
        }
        for(int i = 1; i < 10; i++){
            while(cnts[i] > 0){
                cnts[i]--;
                out.append(i);
            }
        }
        out.println();
    }
}
