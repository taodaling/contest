package on2021_08.on2021_08_16_CS_Academy___Virtual_Round__10.Donkey_Paradox;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.GridUtils;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.Deque;

public class DonkeyParadox {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] d1 = dist(n, m, in.ri() - 1, in.ri() - 1);
        int[][] d2 = dist(n, m, in.ri() - 1, in.ri() - 1);
        int ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(d1[i][j] == d2[i][j]){
                    ans++;
                }
            }
        }
        out.println(ans);
    }

    public int[][] dist(int n, int m, int sx, int sy){
        int[][] dist = new int[n][m];
        int inf = (int)1e8;
        SequenceUtils.deepFill(dist, inf);
        dist[sx][sy] = 0;
        Deque<int[]> dq = new ArrayDeque<>(n * m);
        dq.addLast(new int[]{sx, sy});
        while(!dq.isEmpty()){
            int[] head = dq.removeFirst();
            for(int[] d : GridUtils.DIR4){
                int x = head[0] + d[0];
                int y = head[1] + d[1];
                if(x < 0 || x >= n || y < 0 || y >= m || dist[x][y] <= dist[head[0]][head[1]] + 1){
                    continue;
                }
                dist[x][y] = dist[head[0]][head[1]] + 1;
                dq.addLast(new int[]{x, y});
            }
        }
        return dist;
    }
}
