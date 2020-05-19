package on2020_05.on2020_05_19_Dynamic_Programming_Practice_DIV_1.EmoticonsDiv2;



import template.utils.SequenceUtils;

public class EmoticonsDiv2 {
    public int printSmiles(int smiles) {
        int[][] dp = new int[smiles + 1][smiles + 1];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(dp, inf);
        dp[1][0] = 0;
        for (int i = 1; i <= smiles; i++) {
            for (int j = 0; j <= smiles; j++) {
                //copy
                dp[i][i] = Math.min(dp[i][i], dp[i][j] + 1);
                //paste
                if (i + j <= smiles) {
                    dp[i + j][j] = Math.min(dp[i + j][j], dp[i][j] + 1);
                }
            }
        }

        int ans = inf;
        for(int i = 0; i <= smiles; i++){
            ans = Math.min(ans, dp[smiles][i]);
        }

        return ans;
    }
}
