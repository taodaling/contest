package on2020_10.on2020_10_25_Luogu.P2397_yyy_loves_Maths_VI__mode_;



import template.io.FastInput;
import template.io.FastOutput;

public class P2397YyyLovesMathsVIMode {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = 0;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            int y = in.readInt();
            if (y == x) {
                cnt++;
            } else {
                if (cnt > 0) {
                    cnt--;
                } else {
                    x = y;
                    cnt = 1;
                }
            }
        }
        out.println(x);
    }
}
