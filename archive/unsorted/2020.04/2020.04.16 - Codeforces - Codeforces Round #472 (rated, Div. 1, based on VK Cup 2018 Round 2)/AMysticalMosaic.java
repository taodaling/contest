package contest;

import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class AMysticalMosaic {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        int[][] mat = new int[n][m];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                mat[i][j] = in.readChar() == '#' ? 1 : 0;
            }
        }

        DSU dsu = new DSU(n + m);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(mat[i][j] == 1){
                    dsu.merge(idOfRow(i), idOfCol(j));
                }
            }
        }

        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                if(mat[i][j] == 0 && dsu.find(idOfRow(i)) == dsu.find(idOfCol(j))){
                    out.println("No");
                    return;
                }
            }
        }
        out.println("Yes");
    }
    int n;
    int m;
    public int idOfRow(int i){
        return i;
    }
    public int idOfCol(int i){
        return n + i;
    }
}
