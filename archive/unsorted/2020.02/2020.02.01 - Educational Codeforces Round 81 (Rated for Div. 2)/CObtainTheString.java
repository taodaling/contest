package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class CObtainTheString {
    char[] s = new char[100000 + 1];
    char[] t = new char[100000 + 1];
    int[][] nextIndex = new int[26][100000 + 1];

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readString(s, 0);
        int m = in.readString(t, 0);
        for (int i = 0; i < n; i++) {
            s[i] -= 'a';
        }
        for (int i = 0; i < m; i++) {
            t[i] -= 'a';
        }

        for (int i = 0; i < 26; i++) {
            nextIndex[i][n] = n;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < 26; j++) {
                nextIndex[j][i] = nextIndex[j][i + 1];
            }
            nextIndex[s[i]][i] = i;
        }

        int turn = 1;
        int cur = 0;
        int scan = 0;
        while (scan < m && turn <= m) {
            if (nextIndex[t[scan]][cur] >= n) {
                turn++;
                cur = 0;
                continue;
            }
            cur = nextIndex[t[scan]][cur] + 1;
            scan++;
        }

        if(scan == m){
            out.println(turn);
        }else{
            out.println(-1);
        }
    }
}
