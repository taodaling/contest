package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;

public class DMOPC20Contest3P2BobAndParallelKs {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.ri();
        int n = in.ri();
        int k = in.ri();
        int[][] mat = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.ri();
            }
        }
        long ans = 0;
        IntegerHashMap map = new IntegerHashMap(m, false);
        for (int j = 0; j < n - 1; j++) {
            map.clear();
            for (int i = 0; i < m; i++) {
                map.put(mat[i][j], i);
            }
            for(int i = 0; i < m; i++){
                int sub = map.getOrDefault(mat[i][j] - k, -1);
                if(sub == -1){
                    continue;
                }
                if(mat[i][j + 1] - mat[sub][j + 1] == k){
                    ans++;
                }
            }
        }
        out.println(ans);
    }
}
