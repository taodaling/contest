package on2020_04.on2020_04_16_Codeforces___Codeforces_Round__472__rated__Div__1__based_on_VK_Cup_2018_Round_2_.B__Three_level_Laser;



import template.io.FastInput;
import template.io.FastOutput;

public class BThreeLevelLaser {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        double ans = -1;
        int n = in.readInt();
        int u = in.readInt();
        int[] e = new int[n];
        for (int i = 0; i < n; i++) {
            e[i] = in.readInt();
        }
        int k = 0;
        for (int i = 0; i < n - 2; i++) {
            int j = i + 1;
            k = Math.max(k, j + 1);
            while (k + 1 < n && e[k + 1] - e[i] <= u) {
                k++;
            }
            if(e[k] - e[i] <= u) {
                ans = Math.max((double) (e[k] - e[j]) / (e[k] - e[i]), ans);
            }
        }

        out.println(ans);
    }
}
