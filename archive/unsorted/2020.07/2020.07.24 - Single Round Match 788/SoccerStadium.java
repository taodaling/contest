package contest;

import template.datastructure.DSU;

public class SoccerStadium {

    public int maximumGames(int H, int W, String[] placement) {
        DSU dsu = new DSU(H * W);
        int[][] mat = new int[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                mat[i][j] = i * W + j;
            }
        }
        for (int i = 2, level = 0; i < placement.length - 1; i += 2, level++) {
            for (int j = 1, left = 0; j < placement[i].length() - 1; j += 2, left++) {
                if (placement[i].charAt(j) == '.') {
                    dsu.merge(mat[level][left], mat[level + 1][left]);
                }
            }
        }
        for (int i = 1, level = 0; i < placement.length - 1; i += 2, level++) {
            for (int j = 2, left = 0; j < placement[i].length() - 1; j += 2, left++) {
                if (placement[i].charAt(j) == '.') {
                    dsu.merge(mat[level][left], mat[level][left + 1]);
                }
            }
        }

        int[][] dir = new int[][]{
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        while (true) {
            boolean find = false;
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    for (int d1 = 0; d1 < 4; d1++) {
                        for (int d2 = d1 + 1; d2 < 4; d2++) {
                            int x1 = i + dir[d1][0];
                            int y1 = j + dir[d1][1];
                            int x2 = i + dir[d2][0];
                            int y2 = j + dir[d2][1];
                            if (x1 < 0 || y1 < 0 || x2 < 0 || y2 < 0 ||
                                    x1 >= H || x2 >= H || y1 >= W || y2 >= W) {
                                continue;
                            }
                            if (dsu.find(mat[x1][y1]) == dsu.find(mat[x2][y2]) &&
                                    dsu.find(mat[x1][y1]) != dsu.find(mat[i][j])) {
                                dsu.merge(mat[x1][y1], mat[i][j]);
                                find = true;
                            }
                        }
                    }
                }
            }

            if(!find){
                break;
            }
        }

        int ans = 0;
        for(int i = 0; i < H * W; i++){
            if(dsu.find(i) == i){
                ans++;
            }
        }
        return ans;
    }
}
