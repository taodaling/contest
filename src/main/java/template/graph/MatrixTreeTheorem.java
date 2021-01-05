package template.graph;

import template.math.*;
import template.utils.Debug;

public class MatrixTreeTheorem {
    /**
     * <pre>
     * O(V(V+E))
     * 统计无向图生成树的数目，mod必须为素数
     * </pre>
     */
    public static int countUndirectedGraphMSTSparse(int V, int[] edgeU, int[] edgeV, int mod) {
        if (V == 1) {
            return 1 % mod;
        }
        int E = edgeU.length;
        ModSparseMatrix mat = new ModSparseMatrix(V - 1, E * 2 + V - 1);

        int wpos = 0;
        int one = 1 % mod;
        int inv = mod - 1;
        int[] deg = new int[V];
        for (int i = 0; i < E; i++) {
            int u = edgeU[i];
            int v = edgeV[i];
            if (u == v) {
                continue;
            }
            deg[u]++;
            deg[v]++;
            if (u < V - 1 && v < V - 1) {
                mat.set(wpos++, u, v, inv);
                mat.set(wpos++, v, u, inv);
            }
        }
        for (int i = 0; i < V - 1; i++) {
            mat.set(wpos++, i, i, deg[i] % mod);
        }
        int det = mat.determinant(new Power(mod));
        return det;
    }

    /**
     * <pre>
     * O(V^3+E)
     * 统计无向图生成树的数目
     * </pre>
     */
    public static int countUndirectedGraphMSTDensity(int V, int[] edgeU, int[] edgeV, int mod, boolean isPrime) {
        if (V == 1) {
            return 1 % mod;
        }
        int E = edgeU.length;
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        int one = 1 % mod;
        int inv = mod - 1;
        for (int i = 0; i < E; i++) {
            int u = edgeU[i];
            int v = edgeV[i];
            if (u == v) {
                continue;
            }
            if (u < V - 1) {
                mat.increment(u, u, one, mod);
            }
            if (v < V - 1) {
                mat.increment(v, v, one, mod);
            }
            if (u < V - 1 && v < V - 1) {
                mat.increment(u, v, inv, mod);
                mat.increment(v, u, inv, mod);
            }
        }
        int det = isPrime ? ModMatrix.determinant(mat, new Power(mod)) : ModMatrix.determinant(mat, mod);
        return det;
    }

    /**
     * <pre>
     * O(V^3+E)
     * 统计有向图以root作为根的生成树数目。
     * </pre>
     */
    public static int countDirectedGraphMSTDensityRoot(int root, int V, int[] edgeU, int[] edgeV, int mod, boolean isPrime) {
        if (V == 1) {
            return 1 % mod;
        }
        int E = edgeU.length;
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        int one = 1 % mod;
        int inv = mod - 1;
        for (int i = 0; i < E; i++) {
            int u = edgeU[i];
            int v = edgeV[i];
            if (u == v) {
                continue;
            }
            int castU = u == root ? -1 : u < root ? u : u - 1;
            int castV = v == root ? -1 : v < root ? v : v - 1;
            if (u != root) {
                mat.increment(castU, castU, one, mod);
            }
            if (u != root && v != root) {
                mat.increment(castU, castV, inv, mod);
            }
        }
        int det = isPrime ? ModMatrix.determinant(mat, new Power(mod)) : ModMatrix.determinant(mat, mod);
        return det;
    }

    /**
     * <pre>
     * O(V^3+E)
     * 统计有向图以root作为叶子的生成树数目。
     * </pre>
     */
    public static int countDirectedGraphMSTDensityLeaf(int root, int V, int[] edgeU, int[] edgeV, int mod, boolean isPrime) {
        if (V == 1) {
            return 1 % mod;
        }
        int E = edgeU.length;
        ModMatrix mat = new ModMatrix(V - 1, V - 1);
        int one = 1 % mod;
        int inv = mod - 1;
        for (int i = 0; i < E; i++) {
            int u = edgeU[i];
            int v = edgeV[i];
            if (u == v) {
                continue;
            }
            int castU = u == root ? -1 : u < root ? u : u - 1;
            int castV = v == root ? -1 : v < root ? v : v - 1;
            if (v != root) {
                mat.increment(castV, castV, one, mod);
            }
            if (u != root && v != root) {
                mat.increment(castU, castV, inv, mod);
            }
        }
        int det = isPrime ? ModMatrix.determinant(mat, new Power(mod)) : ModMatrix.determinant(mat, mod);
        return det;
    }

    /**
     * <pre>
     * O(V^3+E)
     * 统计有向图欧拉环数目， 使用BEST定理
     * 两个欧拉环不同，当且仅当一者的边序列不能通过旋转得到另外一者，比如123和312是同一个欧拉环。
     * </pre>
     */
    public static int countDirectedGraphEulerTour(int V, int[] edgeU, int[] edgeV, int mod, boolean isPrime) {
        int[] deg = new int[V];
        for (int i = 0; i < edgeU.length; i++) {
            deg[edgeU[i]]++;
        }
        long t = countDirectedGraphMSTDensityRoot(0, V, edgeU, edgeV, mod, isPrime);
        for (int i = 0; i < V; i++) {
            for (int j = 2; j <= deg[i] - 1; j++) {
                t = t * j % mod;
            }
        }
        return (int) t;
    }


}
