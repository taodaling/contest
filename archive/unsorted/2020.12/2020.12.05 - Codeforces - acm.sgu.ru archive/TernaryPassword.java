package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TernaryPassword {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int a = in.ri();
        int b = in.ri();
        char[] s = new char[n];
        in.rs(s, 0);
        if (a + b > n) {
            out.println(-1);
            return;
        }
        int[] cnts = new int[3];
        for (char c : s) {
            cnts[c - '0']++;
        }
        int[] req = new int[3];
        req[0] = a;
        req[1] = b;
        req[2] = n;
        int[][] cast = new int[3][3];
        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                req[i] = 0;
            }
            if (cnts[i] > req[i]) {
                for (int j = 0; j < 3; j++) {
                    if (i == j) {
                        continue;
                    }
                    if (cnts[j] < req[j]) {
                        int move = Math.min(cnts[i] - req[i], req[j] - cnts[j]);
                        cast[i][j] = move;
                        cnts[i] -= move;
                        cnts[j] += move;
                    }
                }
            }
        }

        int sum = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                sum += cast[i][j];
            }
        }
        out.println(sum);
        for (int i = 0; i < n; i++) {
            int v = s[i] - '0';
            int castTo = v;
            for (int j = 0; j < 3; j++) {
                if (cast[v][j] > 0) {
                    castTo = j;
                    cast[v][j]--;
                    break;
                }
            }
            out.append(castTo);
        }
    }
}
