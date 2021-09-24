package on2021_07.on2021_07_25_Codeforces___Codeforces_Global_Round_15.B__Running_for_Gold1;



import template.io.FastInput;
import template.io.FastOutput;

public class BRunningForGold {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] ranks = new int[n][6];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                ranks[i][j] = in.ri();
            }
            ranks[i][5] = i;
        }
        int[] best = ranks[0];
        for (int i = 1; i < n; i++) {
            int exceed = 0;
            for (int j = 0; j < 5; j++) {
                if (ranks[i][j] < best[j]) {
                    exceed++;
                }
            }
            if (exceed >= 3) {
                best = ranks[i];
            }
        }
        boolean ok = true;
        for (int i = 0; i < n; i++) {
            if(ranks[i] == best){
                continue;
            }
            int exceed = 0;
            for (int j = 0; j < 5; j++) {
                if (ranks[i][j] > best[j]) {
                    exceed++;
                }
            }
            if(exceed < 3){
                ok = false;
            }
        }
        if(ok){
            out.println(best[5] + 1);
        }else{
            out.println(-1);
        }
    }
}


