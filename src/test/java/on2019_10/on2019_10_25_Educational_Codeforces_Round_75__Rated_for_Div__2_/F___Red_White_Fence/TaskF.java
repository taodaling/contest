package on2019_10.on2019_10_25_Educational_Codeforces_Round_75__Rated_for_Div__2_.F___Red_White_Fence;



import template.*;

import java.util.Map;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] b = new int[k];
        for (int i = 0; i < k; i++) {
            b[i] = in.readInt();
        }

        int limit = 300000;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[a[i]]++;
        }

        int[] dbCnt = new int[k];
        int[] sgCnt = new int[k];
        for (int i = 0; i < k; i++) {
            int db = 0;
            int sg = 0;
            for (int j = 0; j < b[i]; j++) {
                if (cnts[j] >= 2) {
                    db++;
                }else if(cnts[j] == 1){
                    sg++;
                }
            }
            dbCnt[i] = db;
            sgCnt[i] = sg;
        }

        NumberTheory.Modular mod = new NumberTheory.Modular(998244353 );
        NumberTheory.Power power = new NumberTheory.Power(mod);
        int q = in.readInt();

        int t = mod.mul(2, power.inverse(9));
        int inv = power.inverse(mod.subtract(t, 1));
        for(int i = 0; i < q; i++){
            int ans = 0;
            int x = in.readInt() / 2;
            for(int j = 0; j < k; j++){
                int y = x - b[j] - 1;
                if(y < 0){
                    continue;
                }
                int sg = sgCnt[j];
                int db = dbCnt[j];
                int dSince = Math.max(0, DigitUtils.ceilDiv(y - sg, 2));
                int dUntil = Math.min(y / 2, db);
                if(dSince > dUntil){
                    continue;
                }
                int contrib = mod.subtract(power.pow(t, dUntil + 1),
                        power.pow(t, dSince));
                contrib = mod.mul(contrib, inv);
                contrib = mod.mul(contrib, power.pow(3, y));
                ans = mod.plus(ans, contrib);
            }

            out.println(ans);
        }
    }
}
