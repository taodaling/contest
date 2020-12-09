package contest;

public class IslandInALake {
    int land = 1;
    int sea = 2;
    int river = 3;
    int island = 4;
    int occupy = 5;
    public int build(String[] country) {

        n = country.length;
        m = country[0].length();

        grid = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (country[i].charAt(j) == '.') {
                    grid[i][j] = river;
                } else {
                    grid[i][j] = land;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (i == 0 || i == n - 1 || j == 0 || j == m - 1) {
                    dfs0(i, j);
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                boolean valid = true;
                for(int dx = -1; dx <= 1; dx++){
                    for(int dy = -1; dy <= 1; dy++){
                        int x = i + dx;
                        int y = j + dy;
                        if(x < 0 || x >= n || y < 0 || y >= m){
                            continue;
                        }
                        if(grid[x][y] != river && grid[x][y] != island){
                            valid = false;
                        }
                    }
                }
                if(valid){
                    grid[i][j] = island;
                }
            }
        }

        int ans = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < m; j++){
                ans = Math.max(dfs1(i, j), ans);
            }
        }
        return ans;
    }

    int[][] grid;
    int n;
    int m;
    int[][] dir = new int[][]{
            {0, -1},
            {0, 1},
            {1, 0},
            {-1, 0}
    };

    int dfs0(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] != river) {
            return 0;
        }
        grid[i][j] = sea;
        int ans = 1;
        for (int[] d : dir) {
            ans += dfs0(i + d[0], j + d[1]);
        }
        return ans;
    }

    int dfs1(int i, int j) {
        if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] != island) {
            return 0;
        }
        grid[i][j] = occupy;
        int ans = 1;
        for (int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                ans += dfs1(i + dx, j +dy);
            }
        }
        return ans;
    }
}
