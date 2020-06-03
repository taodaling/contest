package on2020_06.on2020_06_04_TopCoder_SRM__752.TokenDoublingGame;



import template.math.GuassianElimination;
import template.math.ModGussianElimination;
import template.math.Modular;
import template.math.Power;
import template.utils.SequenceUtils;

public class TokenDoublingGame {
    public int expectation(int N) {
        int maxLog = 14;
        int[][] dp = new int[maxLog + 1][2 * N + 1];
        Modular mod = new Modular(1e9 + 7);
        Power power = new Power(mod);
        int half = power.inverseByFermat(2);

        ModGussianElimination ge = new ModGussianElimination(maxLog + 1, maxLog + 1, mod);
        int[][] rc = new int[maxLog + 1][2];
        int[][] ids = new int[maxLog + 1][2 * N + 1];
        SequenceUtils.deepFill(ids, -1);


        for (int i = (1 << maxLog) + 2 * N; i >= 0; i--) {
            int cnt = 0;
            for (int j = 0; j <= maxLog; j++) {
                if (i - (1 << j) > 0 && i - (1 << j) < 2 * N) {
                    rc[cnt][0] = j;
                    rc[cnt][1] = i - (1 << j);
                    ids[rc[cnt][0]][rc[cnt][1]] = cnt;
                    cnt++;
                }
            }

            if(cnt == 0){
                continue;
            }
            ge.clear(cnt, cnt);
            for (int j = 0; j < cnt; j++) {
                int r = rc[j][0];
                int c = rc[j][1];
                ge.setLeft(j, j, 1);
                if (r == 0) {
                    for (int k = 0; k <= maxLog; k++) {
                        if (ids[k][j - 1] == -1) {
                            ge.modifyRight(j, mod.mul(half, dp[k][j - 1]));
                        } else {
                            ge.modifyLeft(j, ids[k][j - 1], mod.valueOf(-half));
                        }
                        ge.modifyRight(j, half);
                    }
                } else {
                    //r > 0
                    int val = 0;
                    if(c + (1 << ))
                }
            }
        }
    }
}
