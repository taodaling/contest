package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;
import template.utils.Debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EChessChampionship {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {

        int m = in.readInt();
        int n = in.readInt();
        int[] top = new int[n];

        in.populate(top);
        for (int i = 0; i < n; i++) {
            top[i] -= m - 1;
        }
        int[] indeg = new int[m];
        int[] outdeg = new int[m];
        int sum = Arrays.stream(top).sum();
        if (n == m && sum != 0) {
            out.println("no");
            return;
        }
        if (sum < 0) {
            out.println("no");
            return;
        }
        if(m - n != 0) {
            for (int i = n; i < m; i++) {
                indeg[i] += sum / (m - n);
            }
            for (int i = n; i < n + sum % (m - n); i++) {
                indeg[i]++;
            }
        }

        for (int i = 0; i < n; i++) {
            if (top[i] < 0) {
                indeg[i] = -top[i];
            } else {
                outdeg[i] = top[i];
            }
        }

        int avg = indeg[m - 1];
        for (int i = 0; i < n; i++) {
            if (indeg[i] < avg && outdeg[i] < m - i) {
                int add = avg - indeg[i];
                add = Math.min(m - i - outdeg[i], add);
                indeg[i] += add;
                outdeg[i] += add;
            }
        }

        debug.debug("indeg", indeg);
        debug.debug("outdeg", outdeg);
        boolean[][] edge = new boolean[m][m];

        int[] indices = new int[m];
        for (int i = 0; i < m; i++) {
            indices[i] = i;
        }
        for (int i = 0; i < m; i++) {
            SortUtils.radixSort(indices, 0, m - 1, x -> indeg[x]);
            for (int j = 1; j <= outdeg[i]; j++) {
                int use = indices[m - j];
                if (indeg[use] <= 0) {
                    out.println("no");
                    return;
                }
                indeg[use]--;
                edge[i][use] = true;
            }
        }

        out.println("yes");
        char[][] mat = new char[m][m];
        for(int i = 0; i < m; i++){
            for(int j = 0; j < m; j++){
                if(i == j){
                    mat[i][j] = 'X';
                    continue;
                }
                if(edge[i][j] == edge[j][i]){
                    mat[i][j] = 'D';
                }else if(edge[i][j]){
                    mat[i][j] = 'W';
                }else{
                    mat[i][j] = 'L';
                }
            }
        }

        for(int i = 0; i < m; i++){
            for(int j = 0; j < m; j++){
                out.append(mat[i][j]);
            }
            out.println();
        }
    }
}


