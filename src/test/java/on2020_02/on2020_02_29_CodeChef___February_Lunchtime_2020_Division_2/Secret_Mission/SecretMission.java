package on2020_02.on2020_02_29_CodeChef___February_Lunchtime_2020_Division_2.Secret_Mission;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class SecretMission {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int l = in.readInt();
        int[] seq = new int[l];
        for (int i = 0; i < l; i++) {
            seq[i] = in.readInt() - 1;
        }
        int[][] edges = new int[n][n];
        int inf = (int) 1e9;
        SequenceUtils.deepFill(edges, inf);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            int w = in.readInt();
            edges[a][b] = edges[b][a] = Math.min(w, edges[a][b]);
        }
        int[][] dist = new int[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                dist[i][j] = edges[i][j];
            }
            dist[i][i] = 0;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    dist[j][k] = Math.min(dist[j][k],
                            dist[j][i] + dist[i][k]);
                }
            }
        }

        //System.err.println(Arrays.deepToString(dist));
        int k = 1;
        for (int i = 1, r = 0; i < l; i = r + 1) {
            int head = seq[r];
            int total = 0;
            while (r + 1 < l && dist[head][seq[r + 1]] == total + edges[seq[r]][seq[r + 1]]) {
                r++;
                total = dist[head][seq[r]];
            }
            k++;
            if(r < i){
                out.println(-1);
                return;
            }
        }

        out.println(k);
    }
}
