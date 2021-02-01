package contest;

import template.binary.Bits;
import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BerlandChess {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.ri();
        m = in.ri();
        char[][] mat = new char[n][m];
        for (int i = 0; i < n; i++) {
            in.rs(mat[i]);
        }
        Item[][] cast = new Item[n][m];
        List<Item> items = new ArrayList<>();
        Item whiteKing = null;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == '*') {
                    whiteKing = new Item(i, j);
                } else if (mat[i][j] != '.') {
                    int id = items.size();
                    Item item = new Item(i, j);
                    item.type = mat[i][j];
                    item.id = id;
                    items.add(item);
                    cast[i][j] = item;
                }
            }
        }

        int[][] rookDirs = new int[][]{
                {-1, 0},
                {1, 0},
                {0, 1},
                {0, -1}
        };

        int[][] knightDirs = new int[][]{
                {-1, 2},
                {1, 2},
                {2, 1},
                {2, -1},
                {-1, -2},
                {1, -2},
                {-2, 1},
                {-2, -1}
        };

        int[][] bishopDirs = new int[][]{
                {-1, -1},
                {1, 1},
                {1, -1},
                {-1, 1}
        };
        Item[] itemArray = items.toArray(new Item[0]);
        stateCnt = 1 << itemArray.length;
        boolean[][][] attackGraph = new boolean[n][m][stateCnt];
        for (int i = 0; i < stateCnt; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    //as rook
                    for (int[] d : rookDirs) {
                        if (attackGraph[j][k][i]) {
                            break;
                        }
                        for (int x = j + d[0], y = k + d[1]; valid(x, y); x += d[0], y += d[1]) {
                            if (cast[x][y] != null && Bits.get(i, cast[x][y].id) == 1) {
                                if (cast[x][y].type == 'R') {
                                    attackGraph[j][k][i] = true;
                                }
                                break;
                            }
                        }
                    }
                    //as bishop
                    for (int[] d : bishopDirs) {
                        if (attackGraph[j][k][i]) {
                            break;
                        }
                        for (int x = j + d[0], y = k + d[1]; valid(x, y); x += d[0], y += d[1]) {
                            if (cast[x][y] != null && Bits.get(i, cast[x][y].id) == 1) {
                                if (cast[x][y].type == 'B') {
                                    attackGraph[j][k][i] = true;
                                }
                                break;
                            }
                        }
                    }
                    //as knight
                    for (int[] d : knightDirs) {
                        if (attackGraph[j][k][i]) {
                            break;
                        }
                        int x = j + d[0];
                        int y = k + d[1];
                        if (!valid(x, y)) {
                            continue;
                        }
                        if (cast[x][y] != null && Bits.get(i, cast[x][y].id) == 1 && cast[x][y].type == 'K') {
                            attackGraph[j][k][i] = true;
                        }
                    }
                }
            }
        }

        int inf = (int) 1e8;
        int[][][] dist = new int[n][m][stateCnt];
        SequenceUtils.deepFill(dist, inf);
        dist[whiteKing.x][whiteKing.y][stateCnt - 1] = 0;
        Deque<int[]> dq = new ArrayDeque<>(n * m * stateCnt);
        dq.add(new int[]{whiteKing.x, whiteKing.y, stateCnt - 1});
        while (!dq.isEmpty()) {
            int[] event = dq.removeFirst();
            int i = event[0];
            int j = event[1];
            int s = event[2];
            if (s == 0) {
                out.println(dist[i][j][s]);
                return;
            }
            if (attackGraph[i][j][s]) {
                continue;
            }

            int cand = dist[i][j][s] + 1;
            for (int h = -1; h <= 1; h++) {
                for (int v = -1; v <= 1; v++) {
                    if (h == 0 && v == 0) {
                        continue;
                    }
                    int x = i + h;
                    int y = j + v;
                    if (!valid(x, y)) {
                        continue;
                    }
                    int nextState = s;
                    if (cast[x][y] != null) {
                        nextState = Bits.clear(nextState, cast[x][y].id);
                    }
                    if (dist[x][y][nextState] > cand) {
                        dist[x][y][nextState] = cand;
                        dq.addLast(new int[]{x, y, nextState});
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int s = 0; s < stateCnt; s++) {
                    if (attackGraph[i][j][s]) {
                        continue;
                    }

                }
            }
        }

        out.println(-1);
    }


    int n;
    int m;
    int stateCnt;

    public boolean valid(int i, int j) {
        return i >= 0 && j >= 0 && i < n && j < m;
    }

    public int get(int i, int j, int s) {
        return (i * m + j) * stateCnt + s;
    }
}

class Item {
    int x;
    int y;
    int id;
    int type;

    public Item(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
