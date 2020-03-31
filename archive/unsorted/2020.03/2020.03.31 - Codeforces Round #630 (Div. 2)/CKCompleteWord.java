package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class CKCompleteWord {
    public int mirror(int x, int n) {
        return n - 1 - x;
    }

    int charset = 'z' - 'a' + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int k = in.readInt();
        char[][] board = new char[n][charset];
        char[] s = new char[n];
        in.readString(s, 0);

        DSU dsu = new DSU(n);
        for (int i = 0; i < n; i++) {
            dsu.merge(i, mirror(i, n));
            if (i + k < n) {
                dsu.merge(i, i + k);
            }
        }

        for (int i = 0; i < n; i++) {
            board[dsu.find(i)][s[i] - 'a']++;
        }

        int ans = n;
        for(int i = 0; i < n; i++){
            if(dsu.find(i) != i){
                continue;
            }
            int mx = 0;
            for(int c : board[i]){
                mx = Math.max(mx, c);
            }
            ans -= mx;
        }

        out.println(ans);
    }
}
