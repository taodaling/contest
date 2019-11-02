package on2019_11.on2019_11_01_Codeforces_Round__597__Div__2_.B___Restricted_RPS;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int r = in.readInt();
        int p = in.readInt();
        int sc = in.readInt();
        char[] s = in.readString().toCharArray();

        int winTime = 0;
        char[] strategy = new char[n];

        for (int i = 0; i < n; i++) {
            if (s[i] == 'P' && sc > 0) {
                sc--;
                strategy[i] = 'S';
                winTime++;
            }
            if (s[i] == 'R' && p > 0) {
                p--;
                strategy[i] = 'P';
                winTime++;
            }
            if (s[i] == 'S' && r > 0) {
                r--;
                strategy[i] = 'R';
                winTime++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (strategy[i] == 0) {
                if (r > 0) {
                    r--;
                    strategy[i] = 'R';
                } else if (p > 0) {
                    p--;
                    strategy[i] = 'P';
                } else {
                    sc--;
                    strategy[i] = 'S';
                }
            }
        }

        if (winTime < DigitUtils.ceilDiv(n, 2)) {
            out.println("NO");
            return;
        }
        out.println("YES");
        for (int i = 0; i < n; i++) {
            out.append(strategy[i]);
        }
        out.println();
    }
}
