package on2021_07.on2021_07_05_DMOJ.Canada_Day_Contest_2021___Fine_Art;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

public class CanadaDayContest2021FineArt {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        int[][] dirs = new int[][]{
                {-1, 0, 0},
                {1, 0, 0},
                {0, -1, 0},
                {0, 1, 0},
                {0, 0, -1},
                {0, 0, 1}
        };
        int L = 100;
        int[][][] dists = new int[L + 1][L + 1][L + 1];
        int[][][] nearest = new int[L + 1][L + 1][L + 1];
        int inf = (int) 1e8;
        SequenceUtils.deepFill(dists, inf);
        IntegerDeque dq = new IntegerDequeImpl((L + 1) * (L + 1) * (L + 1));
        for (int i = 0; i < n; i++) {
            int x = in.ri();
            int y = in.ri();
            int z = in.ri();
            dists[x][y][z] = 0;
            nearest[x][y][z] = i;
            dq.addLast((x * 1000 + y) * 1000 + z);
        }
        while (!dq.isEmpty()) {
            int head = dq.removeFirst();
            int z = head % 1000;
            head /= 1000;
            int y = head % 1000;
            head /= 1000;
            int x = head;
            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];
                int nz = z + d[2];
                if (nx < 0 || ny < 0 || nz < 0 || nx > L || ny > L || nz > L) {
                    continue;
                }
                if (dists[nx][ny][nz] > dists[x][y][z] + 1) {
                    dists[nx][ny][nz] = dists[x][y][z] + 1;
                    nearest[nx][ny][nz] = nearest[x][y][z];
                    dq.addLast((nx * 1000 + ny) * 1000 + nz);
                }
            }
        }

        for(int i = 0; i < q; i++){
            int x = in.ri();
            int y = in.ri();
            int z = in.ri();
            out.println(nearest[x][y][z] + 1);
        }
    }
}
