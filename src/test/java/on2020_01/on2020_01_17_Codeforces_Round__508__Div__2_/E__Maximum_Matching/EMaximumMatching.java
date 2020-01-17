package on2020_01.on2020_01_17_Codeforces_Round__508__Div__2_.E__Maximum_Matching;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerList;

import javax.annotation.Resource;
import java.util.Arrays;

public class EMaximumMatching {
    IntegerList[][] mat;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        mat = new IntegerList[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat[i][j] = new IntegerList();
            }
        }
        for (int i = 0; i < n; i++) {
            int a = in.readInt() - 1;
            int v = in.readInt();
            int b = in.readInt() - 1;
            mat[a][b].add(v);
            mat[b][a].add(v);
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mat[i][j].sort();
                mat[i][j].reverse();
            }
        }

        int ans = dfs(3);
        out.println(ans);
    }

    boolean[] visited = new boolean[4];


    void check(int root, Result result) {
        if (visited[root]) {
            return;
        }
        visited[root] = true;
        int degree = 0;
        for (int i = 0; i < 4; i++) {
            degree += mat[root][i].size();
        }
        result.odd += degree % 2;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < mat[root][i].size(); j++) {
                result.sum += mat[root][i].get(j);
            }
        }
        for (int i = 0; i < 4; i++) {
            if(mat[root][i].isEmpty()){
                continue;
            }
            check(i, result);
        }
    }

    public int dfs(int step) {
        if (step == 0) {
            Arrays.fill(visited, false);
            int ans = 0;
            for (int i = 0; i < 4; i++) {
                Result result = new Result();
                check(i, result);
                if (result.odd > 2) {
                    continue;
                }
                ans = Math.max(ans, result.sum / 2);
            }
            return ans;
        }
        int ans = dfs(step - 1);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j || mat[i][j].isEmpty()) {
                    continue;
                }
                int val = mat[i][j].pop();
                mat[j][i].pop();
                ans = Math.max(ans, dfs(step - 1));
                mat[i][j].push(val);
                mat[j][i].push(val);
            }
        }
        return ans;
    }
}

class Result {
    int sum;
    int odd;
}