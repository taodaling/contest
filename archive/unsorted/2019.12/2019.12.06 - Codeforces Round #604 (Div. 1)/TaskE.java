package contest;

import template.io.FastInput;
import template.io.FastOutput;

public class TaskE {
    int n;

    public int idOfNode(int i) {
        return i;
    }

    public int idOfPair(int i, int j) {
        if (i > j) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        return n + i * n + j;
    }

    public int idOfSrc() {
        return n * n;
    }

    public int idOfDst() {
        return idOfSrc() + 1;
    }


    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();

        LongMinCostMaxFlow mcmf = new LongMinCostMaxFlow(idOfDst() + 1,
                idOfSrc(), idOfDst());
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                mcmf.getChannel(idOfSrc(), idOfPair(i, j), 0)
                        .modify(1, 0);
                mcmf.getChannel(idOfPair(i, j), idOfNode(i), 0)
                        .modify(1, 0);
                mcmf.getChannel(idOfPair(i, j), idOfNode(j), 0)
                        .modify(1, 0);
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mcmf.getChannel(idOfNode(i), idOfDst(), j)
                        .modify(1, 0);
            }
        }
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            mcmf.getChannel(idOfPair(a, b), idOfNode(a), 0)
                    .modify(-1, 0);
        }

        long[] flow = mcmf.send(n * n);
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                LongMinCostMaxFlow.DirectFeeChannel channel =
                        mcmf.getChannel(idOfPair(i, j), idOfNode(i), 0);
                mat[i][j] = (int) (1 - channel.getFlow());
                mat[j][i] = (int) channel.getFlow();
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                out.append(mat[i][j]);
            }
            out.append('\n');
        }
    }
}
