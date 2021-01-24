package template.graph;

import template.math.DigitUtils;
import template.math.Factorial;
import template.math.ModMatrix;
import template.math.Power;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.function.ToIntFunction;

/**
 * algorithm from https://oi-wiki.org/graph/matrix-tree/
 */
public class MatrixTreeTheoremBeta {
    int mod;
    ToIntFunction<ModMatrix> determine;
    int[][] adj;
    int[] degIn;
    int[] degOut;
    long[] deg;
    Power power;
    int V;

    public MatrixTreeTheoremBeta(int V, int mod, boolean isPrime) {
        this.mod = mod;
        this.V = V;
        power = new Power(mod);
        adj = new int[V][V];
        degIn = new int[V];
        degOut = new int[V];
        deg = new long[V];
        if (isPrime) {
            determine = x -> ModMatrix.determinant(x, power);
        } else {
            determine = x -> ModMatrix.determinant(x, mod);
        }
    }

    public void init() {
        init(adj.length);
    }

    public void init(int V) {
        this.V = V;
        SequenceUtils.deepFill(adj, 0);
        Arrays.fill(degIn, 0);
        Arrays.fill(degOut, 0);
        Arrays.fill(deg, 0);
    }

    /**
     * 增加c条不同的(a,b)无向边
     */
    public void addEdge(int a, int b, int c) {
        deg[a] += c;
        if (a == b) {
            return;
        }

        degIn[a] = DigitUtils.modplus(degIn[a], c, mod);
        degOut[a] = DigitUtils.modplus(degOut[a], c, mod);
        degIn[b] = DigitUtils.modplus(degIn[b], c, mod);
        degOut[b] = DigitUtils.modplus(degOut[b], c, mod);

        adj[a][b] = DigitUtils.modsub(adj[a][b], c, mod);
        adj[b][a] = DigitUtils.modsub(adj[b][a], c, mod);
    }
    /**
     * 增加c条不同的(a,b)有向边
     */
    public void addDirectedEdge(int a, int b, int c) {
        deg[a] += c;
        if (a == b) {
            return;
        }
        degOut[a] = DigitUtils.modplus(degOut[a], c, mod);
        degIn[b] = DigitUtils.modplus(degIn[b], c, mod);
        adj[a][b] = DigitUtils.modsub(adj[a][b], c, mod);
    }

    /**
     * 无向图版本，O(V^3)
     */
    public int countMST() {
        if (V == 1) {
            return 1 % mod;
        }
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        for (int i = 0; i < V; i++) {
            adj[i][i] = degOut[i];
        }
        for (int i = 0; i < V - 1; i++) {
            for (int j = 0; j < V - 1; j++) {
                mat.set(i, j, adj[i][j]);
            }
        }
        int det = determine.applyAsInt(mat);
        return det;
    }

    /**
     * 有向图指定根版本，O(V^3)
     */
    public int countRootMST(int root) {
        if (V == 1) {
            return 1 % mod;
        }
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        for (int i = 0; i < V; i++) {
            adj[i][i] = degOut[i];
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i != root && j != root) {
                    mat.set(i < root ? i : i - 1, j < root ? j : j - 1, adj[i][j]);
                }
            }
        }
        int det = determine.applyAsInt(mat);
        return det;
    }

    /**
     * 有向图指定根版本，O(V^3)
     */
    public int countLeafMST(int root) {
        if (V == 1) {
            return 1 % mod;
        }
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        for (int i = 0; i < V; i++) {
            adj[i][i] = degIn[i];
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (i != root && j != root) {
                    mat.set(i < root ? i : i - 1, j < root ? j : j - 1, adj[i][j]);
                }
            }
        }
        int det = determine.applyAsInt(mat);
        return det;
    }
    /**
     * <pre>
     * BEST theorem
     * O(V^3+E)
     * 统计有向图欧拉环数目， 使用BEST定理
     * 两个欧拉环不同，当且仅当一者的边序列不能通过旋转得到另外一者，比如123和312是同一个欧拉环。
     * fact(E)应该支持
     * </pre>
     */
    public int countEulerTrace(Factorial fact) {
        long t = countRootMST(0);
        for (int i = 0; i < V; i++) {
            if (deg[i] - 1 >= mod) {
                return 0;
            }
            if (deg[i] >= 3) {
                t = t * fact.fact((int) (deg[i] - 1)) % mod;
            }
        }
        return (int) t;
    }

}
