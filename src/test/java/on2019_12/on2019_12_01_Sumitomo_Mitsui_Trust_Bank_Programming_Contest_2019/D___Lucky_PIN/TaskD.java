package on2019_12.on2019_12_01_Sumitomo_Mitsui_Trust_Bank_Programming_Contest_2019.D___Lucky_PIN;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskD {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        boolean[] dp1 = new boolean[10];
        boolean[][] dp2 = new boolean[10][10];
        boolean[][][] dp3 = new boolean[10][10][10];
        int n = in.readInt();
        int[] s = new int[n];
        in.readString(s, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= '0';
        }

        for (int i = 0; i < n; i++) {
            for (int a = 0; a < 10; a++) {
                for (int b = 0; b < 10; b++) {
                    dp3[a][b][s[i]] = dp3[a][b][s[i]] ||
                            dp2[a][b];
                }
            }
            for (int a = 0; a < 10; a++) {
                dp2[a][s[i]] = dp2[a][s[i]] || dp1[a];
            }
            dp1[s[i]] = true;
        }

        int sum = 0;
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                for(int k = 0; k < 10; k++){
                    sum += dp3[i][j][k] ? 1 : 0;
                }
            }
        }

        out.println(sum);
    }

}
