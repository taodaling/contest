package contest;

import template.math.GCDs;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class ReturnedKnight {

    public void print() {
        for (int i = 0; i < 30; i++) {
            System.err.println(String.valueOf(grids[i]));
        }
        System.err.println("-------------------");
    }

    char[][] grids = new char[30][30];
    int remainE;
    int[][] dirs = new int[][]{
            {2, -1},
            {-1, 2},
            {-1, -2},
            {1, 2},
            {1, -2},
            {2, 1},
            {-2, -1},
            {-2, 1}
    };

    int around(int x, int y) {
        int ans = 0;
        for (int[] d : dirs) {
            int nx = d[0] + x;
            int ny = d[1] + y;
            if (nx < 0 || nx >= 30 || ny < 0 || ny >= 30) {
                continue;
            }
            if (grids[nx][ny] != '#') {
                ans++;
            }
        }
        return ans;
    }

    public void setIfPossible(int x, int y) {
        if (x < 0 || x >= 30 || y < 0 || y >= 30 || grids[x][y] != '#') {
            return;
        }
        int around = around(x, y);
        if (remainE >= around && around > 0) {
            remainE -= around(x, y);
            grids[x][y] = '.';
        }
    }

    public void build(int row) {
        int col = 1;
        while (col < 30) {
            setIfPossible(row, col);
            setIfPossible(row + 2, col - 1);
            col += 2;
        }
        setIfPossible(row + 1, 2);
        col = 0;
        while (col < 30) {
            setIfPossible(row, col);
            setIfPossible(row + 2, col + 1);
            col += 2;
        }
    }

    public String[] construct(int P, int Q) {
        int g = GCDs.gcd(P, Q);
        P /= g;
        Q /= g;
        if (P % 2 == 1) {
            P *= 2;
            Q *= 2;
        }
        if (Q > 8 || P < Q * 2) {
            return new String[0];
        }
        remainE = P / 2;
        int x = 2;
        int y = 2;

        SequenceUtils.deepFill(grids, '#');
        grids[x][y] = 'N';
        for (int i = 0; i < Q; i++) {
            int nx = x + dirs[i][0];
            int ny = y + dirs[i][1];
            setIfPossible(nx, ny);
        }
        print();
        setIfPossible(6, 0);
        print();
        build(8);
        print();
        for (int i = 8; i < 30; i++) {
            for (int j = 0; j < 30; j++) {
                setIfPossible(i, j);
            }
        }
        print();
        if (remainE > 0) {
            return new String[0];
        }
        return Arrays.stream(grids).map(String::valueOf).toArray(n -> new String[n]);
    }
}
