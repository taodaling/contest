package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.*;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CMovingPieces {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        m = in.readInt();
        mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.readString(mat[i], 0);
        }

        visited = new boolean[n][m];
        List<int[]> pieces = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == 'o') {
                    pieces.add(new int[]{i, j});
                }
            }
        }
        piece = pieces.size();
        List<IntegerCostFlowEdge>[] g = IntegerFlow.createCostFlow(idOfDst() + 1);
        for (int t = 0; t < piece; t++) {
            int[] xy = pieces.get(t);
            SequenceUtils.deepFill(visited, false);
            dfs(xy[0], xy[1]);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (!visited[i][j]) {
                        continue;
                    }
                    IntegerFlow.addCostEdge(g, idOfPiece(t), idOfCell(i, j), 1, xy[0] + xy[1] - i - j);
                }
            }
        }

        for (int i = 0; i < piece; i++) {
            IntegerFlow.addCostEdge(g, idOfSrc(), idOfPiece(i), 1, 0);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                IntegerFlow.addCostEdge(g, idOfCell(i, j), idOfDst(), 1, 0);
            }
        }

        IntegerMinimumCostFlow mcf = new IntegerDijkstraMinimumCostFlow(g.length);
        int[] ans = mcf.apply(g, idOfSrc(), idOfDst(), (int) 1e9);
//        for(int i = 0; i < n; i++){
//            for(int j = 0; j < m; j++){
//                if(mat[i][j] == '#'){
//                    for(IntegerFlowEdge e : g[idOfCell(i, j)]){
//                        if(e.real && e.flow > 0){
//                            throw new RuntimeException();
//                        }
//                    }
//                }
//            }
//        }
        int move = -ans[1];
        out.println(move);
    }


    boolean[][] visited;
    char[][] mat;

    public void dfs(int i, int j) {
        if (i >= n || j >= m || visited[i][j] || mat[i][j] == '#') {
            return;
        }
        visited[i][j] = true;
        dfs(i + 1, j);
        dfs(i, j + 1);
    }

    int n;
    int m;
    int piece;

    public int idOfCell(int i, int j) {
        return i * m + j;
    }

    public int idOfPiece(int i) {
        return n * m + i;
    }

    public int idOfSrc() {
        return n * m + piece;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }
}
