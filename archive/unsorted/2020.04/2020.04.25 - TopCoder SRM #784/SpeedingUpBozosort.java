package contest;

import dp.Lis;
import template.math.Matrix;
import template.math.Radix;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.SortUtils;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpeedingUpBozosort {
    Radix radix = new Radix(10);
//    Debug debug = new Debug(true);

    public double expectedComparisons(int[] A, int numSwaps) {
        int n = A.length;
        IntegerList permList = new IntegerList(720);
        getAllPerm(0, new boolean[n], 0, permList);
        permList.sort();
        permList.reverse();
        int[] perms = permList.toArray();
        int m = perms.length;
        int[] permToIndex = new int[1000000];
        Arrays.fill(permToIndex, -1);
        for (int i = 0; i < perms.length; i++) {
            permToIndex[perms[i]] = i;
        }

//        debug.debug("perm", perms);

        double prob = 1.0 / n / n;
        double[][] transfer = new double[numSwaps + 1][m];
        transfer[0][0] = 1;

        List<Integer>[] next = new List[m];
        for (int i = 0; i < m; i++) {
            next[i] = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    int index = permToIndex[swap(perms[i], j, k)];
                    next[i].add(index);
                }
            }
        }

        for (int i = 0; i < numSwaps; i++) {
            for (int j = 0; j < m; j++) {
                for (int x : next[j]) {
                    transfer[i + 1][x] += transfer[i][j] * prob;
                }
            }
        }

//        debug.debug("transfer", transfer);

        double[][] matData = new double[m + 1][m + 1];
        for (int i = 0; i <= m; i++) {
            matData[i][i] = 1;
        }
        for (int i = 0; i < m; i++) {
            int[] B = apply(A, perms[i], n);
            if (!SortUtils.notStrictAscending(B, 0, B.length - 1)) {
                for (int j = 0; j < m; j++) {
                    int k = permToIndex[apply(perms[j], perms[i], n)];
                    matData[i][k] -= transfer[numSwaps][j];
                }
            }
            matData[i][m] -= cost(B);
        }

        Matrix mat = new Matrix(matData);
        Matrix inv = Matrix.inverse(mat);
        Matrix b = new Matrix(m + 1, 1);
        b.set(m, 0, 1);
        Matrix x = Matrix.mul(inv, b);
        double exp = x.get(0, 0);
        return exp;
    }

    public int swap(int x, int i, int j) {
        int xi = radix.get(x, i);
        int xj = radix.get(x, j);
        x = (int) radix.setBit(x, i, xj);
        x = (int) radix.setBit(x, j, xi);
        return x;
    }

    public int apply(int x, int p, int n) {
        int ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            ans = (int) radix.setBit(ans, i, radix.get(x, radix.get(p, i)));
        }
        return ans;
    }

    public int[] apply(int[] x, int p, int n) {
        int[] ans = new int[x.length];
        for (int i = n - 1; i >= 0; i--) {
            ans[i] = x[radix.get(p, i)];
        }
        return ans;
    }

    public int cost(int[] x) {
        for (int i = 1; i < x.length; i++) {
            if (x[i] < x[i - 1]) {
                return i;
            }
        }
        return x.length - 1;
    }

    public void getAllPerm(int val, boolean[] used, int i, IntegerList list) {
        if (i == used.length) {
            list.add(val);
            return;
        }
        for (int j = 0; j < used.length; j++) {
            if (used[j]) {
                continue;
            }
            used[j] = true;
            getAllPerm(val * 10 + j, used, i + 1, list);
            used[j] = false;
        }
    }

}