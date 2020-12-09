package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class Weed {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] around = new int[n][m];
        Deque<int[]> dq =  new ArrayDeque<>(n * m);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                around[i][j] = in.rc() == 'X' ? 2 : 0;
                if(around[i][j] == 2){
                    dq.add(new int[]{i, j});
                }
            }
        }
        int[][] dirs = new int[][]{
                {1, 0},
                {-1, 0},
                {0, -1},
                {0, 1}
        };
        while(!dq.isEmpty()){
            int[] head = dq.removeFirst();
            for(int[] d : dirs){
                int x = d[0] + head[0];
                int y = d[1] + head[1];
                if(x < 0 || x >= n || y < 0 || y >= m){
                    continue;
                }
                around[x][y]++;
                if(around[x][y] == 2){
                    dq.addLast(new int[]{x, y});
                }
            }
        }

        int ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(around[i][j] >= 2){
                    ans++;
                }
            }
        }
        out.println(ans);
    }
}
