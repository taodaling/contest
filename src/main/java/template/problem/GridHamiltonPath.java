package template.problem;

import template.graph.Graph;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.LongDeque;
import template.primitve.generated.datastructure.LongDequeImpl;
import template.primitve.generated.graph.IntegerDinic;
import template.primitve.generated.graph.IntegerFlow;
import template.primitve.generated.graph.IntegerFlowEdge;
import template.primitve.generated.graph.IntegerMaximumFlow;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.List;

public class GridHamiltonPath {
    public static interface Walker {
        public void walk(int dx, int dy);
    }

    Walker walker;

    /**
     * O((nm)^{1.5})
     * @param n
     * @param m
     * @param src
     * @param dst
     * @param walker
     * @return
     */
    public boolean solve(int n, int m, int[] src, int[] dst, Walker walker) {
        flip = udSwap = lrSwap = false;
        flip = m < n;
        if (flip) {
            int tmp = n;
            n = m;
            m = tmp;
            SequenceUtils.reverse(src);
            SequenceUtils.reverse(dst);
        }
        this.src = src;
        this.dst = dst;
        this.n = n;
        this.m = m;
        this.walker = walker;

        if (n == 2 && src[0] != dst[0] && src[1] == dst[1] && src[1] != 0 && src[1] != m - 1) {
            return false;
        }
        if (n == 3 && Math.abs(src[0] - dst[0]) == 2 && Math.abs(src[1] - dst[1]) == 1) {
            if (src[0] == 2) {
                udSwap = true;
                src[0] = 0;
                dst[0] = 2;
            }
            if (src[1] > dst[1]) {
                lrSwap = true;
                src[1] = m - 1 - src[1];
                dst[1] = m - 1 - dst[1];
            }
            if (src[1] % 2 != (m - 1 - dst[1]) % 2) {
                return false;
            }
            if (src[1] % 2 == 1) {
                int nowX = src[0];
                int nowY = src[1];
                while (!(nowX == 1 && nowY == 0)) {
                    dirChar(nowX, nowY, nowX, nowY - 1);
                    nowY--;
                    dirChar(nowX, nowY, nowX ^ 1, nowY);
                    nowX ^= 1;
                }
                //move down
                dirChar(nowX, nowY, nowX + 1, nowY);
                nowX++;
                while (nowY < src[1]) {
                    dirChar(nowX, nowY, nowX, nowY + 1);
                    nowY++;
                }

                dirChar(nowX, nowY, nowX - 1, nowY);
                nowX--;
                dirChar(nowX, nowY, nowX, nowY + 1);
                nowY++;
                dirChar(nowX, nowY, nowX - 1, nowY);
                nowX--;
                while (!(nowX == 1 && nowY == m - 1)) {
                    dirChar(nowX, nowY, nowX, nowY + 1);
                    nowY++;
                    dirChar(nowX, nowY, nowX ^ 1, nowY);
                    nowX ^= 1;
                }

                dirChar(nowX, nowY, nowX + 1, nowY);
                nowX++;
                while (nowY > dst[1]) {
                    dirChar(nowX, nowY, nowX, nowY - 1);
                    nowY--;
                }
                return true;
            } else {
                int nowX = src[0];
                int nowY = src[1];
                while (!(nowY == m - 1)) {
                    dirChar(nowX, nowY, nowX, nowY + 1);
                    nowY++;
                }

                //move down
                dirChar(nowX, nowY, nowX + 1, nowY);
                nowX++;
                while (!(nowX == 1 && nowY == dst[1])) {
                    int nextX = nowX == 1 ? 2 : 1;
                    dirChar(nowX, nowY, nextX, nowY);
                    nowX = nextX;
                    dirChar(nowX, nowY, nowX, nowY - 1);
                    nowY--;
                }
                dirChar(nowX, nowY, nowX, nowY - 1);
                nowY--;
                while (!(nowX == 1 && nowY == 0)) {
                    dirChar(nowX, nowY, nowX, nowY - 1);
                    nowY--;
                    dirChar(nowX, nowY, nowX ^ 1, nowY);
                    nowX ^= 1;
                }

                dirChar(nowX, nowY, nowX + 1, nowY);
                nowX++;
                while (nowY < dst[1]) {
                    dirChar(nowX, nowY, nowX, nowY + 1);
                    nowY++;
                }
                return true;
            }
        }
        List<IntegerFlowEdge>[] g = Graph.createGraph(idOfDst() + 1);
        int leftSum = 0;
        int rightSum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if ((i + j) % 2 == 1) {
                    IntegerFlow.addFlowEdge(g, idOf(i, j), idOfDst(), cap(i, j));
                    rightSum += cap(i, j);
                    continue;
                }
                leftSum += cap(i, j);
                IntegerFlow.addFlowEdge(g, idOfSrc(), idOf(i, j), cap(i, j));
                for (int[] d : dirs) {
                    int x = i + d[0];
                    int y = j + d[1];
                    if (!valid(x, y)) {
                        continue;
                    }
                    IntegerFlow.addFlowEdge(g, idOf(i, j), idOf(x, y), 1);
                }
            }
        }

        if (leftSum != rightSum) {
            return false;
        }

        IntegerMaximumFlow dinic = new IntegerDinic();
        int sentFlow = dinic.apply(g, idOfSrc(), idOfDst(), leftSum);
        if (sentFlow < leftSum) {
            return false;
        }

        next = new long[n][m][];
        visited = new boolean[n][m];
        dq = new LongDequeImpl(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                next[i][j] = new long[cap(i, j)];
                if ((i + j) % 2 == 1) {
                    //right
                    int wpos = 0;
                    for (IntegerFlowEdge e : g[idOf(i, j)]) {
                        if (!e.real && e.rev.flow == 1) {
                            int x = e.to / m;
                            int y = e.to % m;
                            next[i][j][wpos++] = DigitUtils.asLong(x, y);
                        }
                    }
                } else {
                    //right
                    int wpos = 0;
                    for (IntegerFlowEdge e : g[idOf(i, j)]) {
                        if (e.real && e.flow == 1) {
                            int x = e.to / m;
                            int y = e.to % m;
                            next[i][j][wpos++] = DigitUtils.asLong(x, y);
                        }
                    }
                }
            }
        }

        int finalN1 = n;
        int finalM1 = m;
        debug.run(() -> {
            setted = new boolean[finalN1][finalM1];
            color = new char[finalN1][finalM1];
            char c = 'a';
            for (int i = 0; i < finalN1; i++) {
                for (int j = 0; j < finalM1; j++) {
                    if (setted[i][j]) {
                        continue;
                    }
                    paint(i, j, c++);
                }
            }
            debug.debugMatrix("mat", color);
        });

        collect(src[0], src[1]);
        while (!dq.isEmpty()) {
            long head = dq.removeFirst();
            int x = DigitUtils.highBit(head);
            int y = DigitUtils.lowBit(head);
            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                if (!valid(nx, ny) || visited[nx][ny]) {
                    continue;
                }
                boolean ok = false;
                for (int i = 0; i < next[x][y].length && !ok; i++) {
                    for (int j = 0; j < next[nx][ny].length && !ok; j++) {
                        if (!near(next[x][y][i], next[nx][ny][j])) {
                            continue;
                        }
                        collect(nx, ny);
                        replace(DigitUtils.highBit(next[x][y][i]), DigitUtils.lowBit(next[x][y][i]), DigitUtils.asLong(x, y),
                                next[nx][ny][j]);
                        replace(DigitUtils.highBit(next[nx][ny][j]), DigitUtils.lowBit(next[nx][ny][j]), DigitUtils.asLong(nx, ny),
                                next[x][y][i]);
                        next[x][y][i] = DigitUtils.asLong(nx, ny);
                        next[nx][ny][j] = DigitUtils.asLong(x, y);
                        ok = true;
                    }
                }
                if (ok) {
                    int finalN = n;
                    int finalM = m;
                    debug.run(() -> {
                        setted = new boolean[finalN][finalM];
                        color = new char[finalN][finalM];
                        char c = 'a';
                        for (int i = 0; i < finalN; i++) {
                            for (int j = 0; j < finalM; j++) {
                                if (setted[i][j]) {
                                    continue;
                                }
                                paint(i, j, c++);
                            }
                        }
                        debug.debugMatrix("mat", color);
                    });
                }
            }
        }

        //out.println("LLLLLUURDRURDRRRDRUULLL");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!visited[i][j]) {
                    throw new RuntimeException();
                }
            }
        }

        int nowX = src[0];
        int nowY = src[1];
        while (!(nowX == dst[0] && nowY == dst[1])) {
            visited[nowX][nowY] = false;
            for (long to : next[nowX][nowY]) {
                int x = DigitUtils.highBit(to);
                int y = DigitUtils.lowBit(to);
                if (!visited[x][y]) {
                    continue;
                }
                dirChar(nowX, nowY, x, y);
                nowX = x;
                nowY = y;
                break;
            }
        }
        return true;
    }


    int n;
    int m;

    private int idOf(int i, int j) {
        return i * m + j;
    }

    private int idOfSrc() {
        return n * m;
    }

    private int idOfDst() {
        return idOfSrc() + 1;
    }

    int[][] dirs = new int[][]{
            {-1, 0},
            {1, 0},
            {0, -1},
            {0, 1}
    };

    private boolean valid(int i, int j) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    int[] src;
    int[] dst;

    private int cap(int i, int j) {
        return i == src[0] && j == src[1] || i == dst[0] && j == dst[1] ? 1 : 2;
    }

    private boolean near(int i, int j, int a, int b) {
        return Math.abs(i - a) + Math.abs(j - b) == 1;
    }

    private boolean near(long pos1, long pos2) {
        return near(DigitUtils.highBit(pos1), DigitUtils.lowBit(pos1), DigitUtils.highBit(pos2), DigitUtils.lowBit(pos2));
    }

    private void replace(int i, int j, long src, long dst) {
        for (int t = 0; t < next[i][j].length; t++) {
            if (next[i][j][t] == src) {
                next[i][j][t] = dst;
                return;
            }
        }
    }

    boolean flip;
    boolean udSwap;
    boolean lrSwap;

    private void dirChar(int i, int j, int a, int b) {
        if (flip) {
            int tmp = i;
            i = j;
            j = tmp;
            tmp = a;
            a = b;
            b = tmp;
        }
        int dx = 0;
        int dy = 0;
        if (i == a) {
            if (j < b) {
                dy = lrSwap ? -1 : 1;
            } else {
                dy = lrSwap ? 1 : -1;
            }
        } else {
            if (i < a) {
                dx = udSwap ? -1 : 1;
            } else {
                dx = udSwap ? 1 : -1;
            }
        }
        walker.walk(dx, dy);
    }



    long[][][] next;
    LongDeque dq;
    boolean[][] visited;
    char[][] color;
    boolean[][] setted;

    public void paint(int i, int j, char c) {
        if (setted[i][j]) {
            return;
        }
        setted[i][j] = true;
        color[i][j] = c;
        for (long to : next[i][j]) {
            int x = DigitUtils.highBit(to);
            int y = DigitUtils.lowBit(to);
            paint(x, y, c);
        }
    }


    public void collect(int i, int j) {
        if (visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        dq.addLast(DigitUtils.asLong(i, j));
        for (long to : next[i][j]) {
            int x = DigitUtils.highBit(to);
            int y = DigitUtils.lowBit(to);
            collect(x, y);
        }
    }

    Debug debug = new Debug(true);
}
