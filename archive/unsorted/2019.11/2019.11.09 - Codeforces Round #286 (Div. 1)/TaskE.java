package contest;

import template.*;

import java.util.ArrayList;
import java.util.List;

public class TaskE {
    Modular mod = new Modular(10007);
    int prime = 10007;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.readString().toCharArray();
        int m = s.length;
        int n = in.readInt();
        int words = 'z' - 'a' + 1;

        List<int[]> indexes = new ArrayList<>(40000);
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= m; j++) {
                if (i + j > m) {
                    continue;
                }
                indexes.add(SequenceUtils.wrapArray(i, j));
            }
        }
        int[][] id2Index = indexes.toArray(new int[0][]);
        int[][] index2Id = new int[m + 1][m + 1];
        for (int i = 0; i < id2Index.length; i++) {
            int[] xy = id2Index[i];
            index2Id[xy[0]][xy[1]] = i;
        }
        int statusCnt = id2Index.length;

        short[][] dp = new short[statusCnt * 2][statusCnt];
        dp[0][index2Id[0][m]] = 1;
        for (int i = 0; i < statusCnt * 2 - 1; i++) {
            for (int j = 0; j < statusCnt; j++) {
                int l = id2Index[j][0];
                int r = l + id2Index[j][1] - 1;
                if (l > r) {
                    dp[i + 1][j] = (short) ((dp[i + 1][j] + dp[i][j] * words) % prime);
                } else if (l == r) {
                    int ns = index2Id[l + 1][r - l];
                    dp[i + 1][ns] = (short) ((dp[i + 1][ns] + dp[i][j]) % prime);
                    dp[i + 1][j] = (short) ((dp[i + 1][j] + (words - 1) * dp[i][j]) % prime);
                } else if (s[l] == s[r]) {
                    int ns = index2Id[l + 1][r - l - 2];
                    dp[i + 1][ns] = (short) ((dp[i + 1][ns] + dp[i][j]) % prime);
                    dp[i + 1][j] = (short) ((dp[i + 1][j] + (words - 1) * dp[i][j]) % prime);
                } else {
                    int ns1 = index2Id[l + 1][r - l];
                    int ns2 = index2Id[l][r - l];
                    dp[i + 1][ns1] = (short) ((dp[i + 1][ns1] + dp[i][j]) % prime);
                    dp[i + 1][ns2] = (short) ((dp[i + 1][ns2] + dp[i][j]) % prime);
                    dp[i + 1][j] = (short) ((dp[i + 1][j] + (words - 2) * dp[i][j]) % prime);
                }
            }
        }

        int[] seq = new int[statusCnt * 2];
        for (int i = 0; i < statusCnt * 2; i++) {
            seq[i] = dp[i][index2Id[0][m]];
        }

        ModLinearFeedbackShiftRegister bm = new ModLinearFeedbackShiftRegister(mod, seq.length);
        for (int i = 0; i < seq.length; i++) {
            bm.add(seq[i]);
        }

       ModLinearFeedbackShiftRegister.Estimator estimator =  bm.newEstimator(1);
        IntList p = new IntList(bm.length());
        for (int i = bm.length(); i >= 1; i--) {
            p.add(mod.valueOf(-bm.codeAt(i)));
        }

        IntList remainder = new IntList();
        Polynomials.module(n / 2, p, remainder, new Power(mod));
        Polynomials.normalize(remainder);

        int[] rem = remainder.toArray();
        int[] finalStatus = new int[statusCnt];
        for(int i = 0; i < rem.length; i++){
            for(int j = 0; j < statusCnt; j++){
                finalStatus[i] = (finalStatus[i] + rem[i] * dp[i][j]) % prime;
            }
        }

        if(n % 2 == 1){
            int[] nextStatus = new int[statusCnt];
            for(int i = 0; i < statusCnt; i++){
                int l = id2Index[i][0];
                int r = id2Index[i][1] + l - 1;

                if (l > r) {
                    nextStatus[i] = (short) ((nextStatus[i] + finalStatus[i] * words) % prime);
                } else if (l == r) {
                    int ns = index2Id[l + 1][r - l];
                    nextStatus[ns] = (short) ((nextStatus[ns] + finalStatus[i]) % prime);
                }
            }
            finalStatus = nextStatus;
        }

        int ans = 0;
        for(int i = 0; i < statusCnt; i++){
            ans = mod.plus(ans, finalStatus[i]);
        }

        out.println(ans);
    }
}
