package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.DoubleKMAlgo;

public class UVA1411Ants {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] ants = new int[2][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                ants[j][i] = in.readInt();
            }
        }
        int[][] trees = new int[2][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                trees[j][i] = in.readInt();
            }
        }

        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double dx = ants[0][i] - trees[0][j];
                double dy = ants[1][i] - trees[1][j];
                dist[i][j] = -Math.sqrt(dx * dx + dy * dy);
            }
        }

        DoubleKMAlgo kmAlgo = new DoubleKMAlgo(dist);
        kmAlgo.solve();
        for (int i = 0; i < n; i++) {
            int tree = kmAlgo.getLeftPartner(i);
            out.append(tree + 1).println();
        }
    }
}
