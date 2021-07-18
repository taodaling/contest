package on2021_07.on2021_07_10_Kattis.Secret_Chamber_at_Mount_Rushmore;



import template.io.FastInput;
import template.io.FastOutput;

public class SecretChamberAtMountRushmore {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();
        int charset = 'z' - 'a' + 1;
        boolean[][] adj = new boolean[charset][charset];
        for(int i = 0; i < charset; i++){
            adj[i][i] = true;
        }
        for (int i = 0; i < m; i++) {
            int a = in.rc() - 'a';
            int b = in.rc() - 'a';
            adj[a][b] = true;
        }
        for (int k = 0; k < charset; k++) {
            for (int i = 0; i < charset; i++) {
                for (int j = 0; j < charset; j++) {
                    adj[i][j] = adj[i][j] || adj[i][k] && adj[k][j];
                }
            }
        }
        for (int i = 0; i < n; i++) {
            String a = in.rs();
            String b = in.rs();
            if (a.length() == b.length()) {
                boolean ok = true;
                for (int j = 0; j < a.length(); j++) {
                    if (!adj[a.charAt(j) - 'a'][b.charAt(j) - 'a']) {
                        ok = false;
                    }
                }
                if (ok) {
                    out.println("yes");
                    continue;
                }
            }
            out.println("no");
        }
    }
}
