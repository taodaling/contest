package on2021_06.on2021_06_25_Single_Round_Match_808.IOIVoting;



import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class IOIVoting {
    int[][] adj;

    public int[] winners(int N, int[] votes) {
        adj = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                adj[i][j] = votes[i * N + j] - votes[j * N + i];
            }
        }
        int inf = (int) 1e8;
        int[][] dest = new int[N][N];
        SequenceUtils.deepFill(dest, -1);
        for (int i = 0; i < N; i++) {
            dest[i][i] = inf;
            boolean[] visited = new boolean[N];
            for (int j = 0; j < N; j++) {
                int head = -1;
                for (int k = 0; k < N; k++) {
                    if (visited[k]) {
                        continue;
                    }
                    if (head == -1 || dest[i][k] > dest[i][head]) {
                        head = k;
                    }
                }
                visited[head] = true;
                for (int k = 0; k < N; k++) {
                    if (adj[head][k] <= 0) {
                        continue;
                    }
                    dest[i][k] = Math.max(dest[i][k], Math.min(dest[i][head], adj[head][k]));
                }
            }
        }

        List<Integer> ok = new ArrayList<>(N);
        for(int i = 0; i < N; i++){
            boolean valid = true;
            for(int j = 0; j < N; j++){
                if(dest[i][j] < dest[j][i]){
                    valid = false;
                }
            }
            if(valid){
                ok.add(i);
            }
        }
        return ok.stream().mapToInt(Integer::intValue).toArray();
    }
}
