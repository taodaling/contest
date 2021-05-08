package on2021_05.on2021_05_06_Codeforces___Codeforces_Round__190__Div__1_.A__Ciel_and_Robot;



import template.io.FastInput;
import template.io.FastOutput;

public class ACielAndRobot {
    int any = Integer.MAX_VALUE;

    int div(int a, int b) {
        if (a == 0) {
            if (b == 0) {
                return any;
            }
            return 0;
        }
        if (b == 0 || a % b != 0) {
            return -1;
        }
        return a / b;
    }

    boolean equal(int a, int b){
        return a == any || b == any || a == b;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        char[] s = in.rs().toCharArray();
        int n = s.length;
        int[][] pos = new int[n + 1][2];
        for (int i = 1; i <= n; i++) {
            char c = s[i - 1];
            pos[i] = pos[i - 1].clone();
            if (c == 'L') {
                pos[i][0]--;
            } else if (c == 'R') {
                pos[i][0]++;
            } else if (c == 'U') {
                pos[i][1]++;
            } else {
                pos[i][1]--;
            }
        }

        for (int i = 0; i < n; i++) {
            int dx = a - pos[i][0];
            int dy = b - pos[i][1];
            int sx = div(dx, pos[n][0]);
            int sy = div(dy, pos[n][1]);
            if (sx < 0 || sy < 0 || !equal(sx, sy)) {
                continue;
            }
            out.println("Yes");
            return;
        }
        out.println("No");
    }
}
