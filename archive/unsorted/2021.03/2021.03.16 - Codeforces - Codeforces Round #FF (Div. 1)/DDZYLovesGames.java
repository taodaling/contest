package contest;

import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.Matrix;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

public class DDZYLovesGames {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int k = in.ri();
        int[][] g = new int[n][n];
        int[] degs = new int[n];
        int[] trap = new int[n];
        IntegerArrayList trapPos = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            trap[i] = in.ri();
            if (trap[i] == 1) {
                trapPos.add(i);
            }
        }
        int t = trapPos.size();
        int[] allTrapPos = trapPos.toArray();
        debug.debug("trapPos", trapPos);
        for (int i = 0; i < m; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            g[a][b]++;
            g[b][a]++;
            degs[a]++;
            degs[b]++;
        }
        debug.debugArray("degs", degs);
        double[][] toTrap = new double[n][n];
        for (int i = 0; i < n; i++) {
            toTrap[i][i]++;
            if (trap[i] == 0) {
                for (int j = 0; j < n; j++) {
                    toTrap[j][i] -= g[i][j] / (double) degs[i];
                }
            }
        }
        debug.debugMatrix("toTrap", toTrap);
        Matrix toTrapMat = new Matrix(toTrap);
        Matrix invToTrapMat = Matrix.inverse(toTrapMat);
        debug.debug("invToTrapMat", invToTrapMat);
        double[][] invToTrap = invToTrapMat.toArray();
        Matrix probMatrix = new Matrix(t, 1);
        for (int i = 0; i < t; i++) {
            probMatrix.set(i, 0, invToTrapMat.get(allTrapPos[i], 0));
        }
        debug.debug("probMatrix", probMatrix);
        double[][] transfer = new double[t][t];

        for (int i = 0; i < t; i++) {
            int x = allTrapPos[i];
            double prob = 1d / degs[x];
            for (int j = 0; j < n; j++) {
                if (g[x][j] == 0) {
                    continue;
                }
                for (int z = 0; z < t; z++) {
                    transfer[z][i] += g[x][j] * invToTrap[allTrapPos[z]][j] * prob;
                }
            }
        }
        debug.debugMatrix("transfer", transfer);
        int remainBlood = k - 1 - 1;
        debug.debug("remainBlood", remainBlood);
        Matrix transferMat = new Matrix(transfer);
        Matrix multiTransferMat = Matrix.pow(transferMat, remainBlood);
        Matrix probAfterTransfer = Matrix.mul(multiTransferMat, probMatrix);
        debug.debug("multiTransferMat", multiTransferMat);
        debug.debug("probAfterTransfer", probAfterTransfer);
        double ans = probAfterTransfer.get(t - 1, 0);
        out.println(ans);
    }
}
