package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorial;
import template.math.Modular;
import template.primitve.generated.IntegerBIT;
import template.primitve.generated.IntegerMultiWayStack;
import template.problem.PermutationWithDistinctForbiddenMatch;

public class GBeautifulMatrix {
    Modular mod = new Modular(998244353);
    Factorial factorial = new Factorial(2000, mod);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = in.readInt();
            }
        }

        PermutationWithDistinctForbiddenMatch permutation = new PermutationWithDistinctForbiddenMatch(mod, n);
        int[] follow = new int[n + 1];
        follow[0] = 1;
        for (int i = 1; i <= n; i++) {
            follow[i] = mod.mul(follow[i - 1], permutation.get(n, 0));
        }

        int ans = 0;

        int[] indexOfVal = new int[n + 1];
        int[] indexOfDim = new int[n + 1];
        IntegerBIT deleteBit = new IntegerBIT(n);
        IntegerBIT leftBIT = new IntegerBIT(n);
        IntegerBIT rightBIT = new IntegerBIT(n);

        int[][] live = new int[n][n];
        int[][] left = new int[n][n];
        int[][] right = new int[n][n];
        IntegerMultiWayStack s1 = new IntegerMultiWayStack(n, n);
        IntegerMultiWayStack s2 = new IntegerMultiWayStack(n, n);
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                indexOfVal[mat[i][j]] = j;
                indexOfDim[mat[i - 1][j]] = j;
            }

            leftBIT.clear();
            rightBIT.clear();
            s1.clear();
            s2.clear();
            for (int j = n - 1; j >= 0; j--) {
                right[i][j] = rightBIT.query(mat[i][j] - 1);
                if (mat[i - 1][j] < mat[i][j]) {
                    right[i][j] -= rightBIT.query(mat[i - 1][j]) - rightBIT.query(mat[i - 1][j] - 1);
                }
                left[i][j] = leftBIT.query(mat[i][j] - 1);
                if (mat[i - 1][j] < mat[i][j]) {
                    left[i][j] -= leftBIT.query(mat[i - 1][j]) - leftBIT.query(mat[i - 1][j] - 1);
                }
                live[i][j] = rightBIT.query(n);

                if (indexOfVal[mat[i - 1][j]] <= j) {
                    s1.addLast(indexOfVal[mat[i - 1][j]], j);
                } else {
                    rightBIT.update(mat[i - 1][j], 1);
                }

                if (indexOfDim[mat[i][j]] <= j) {
                    s2.addLast(indexOfDim[mat[i][j]], j);
                    leftBIT.update(mat[i][j], 1);
                } else {
                }

                while (!s1.isEmpty(j)) {
                    int tail = s1.removeLast(j);
                    //leftBIT.update(mat[i - 1][tail], -1);
                    rightBIT.update(mat[i - 1][tail], 1);
                }

                while(!s2.isEmpty(j)){
                    int tail = s2.removeLast(j);
                    leftBIT.update(mat[i][tail], -1);
                }
            }

        }

        deleteBit.clear();
        for (int j = 0; j < n; j++) {
            int cnt = mat[0][j] - 1 - deleteBit.query(mat[0][j] - 1);
            int contrib = mod.mul(cnt, factorial.fact(n - j - 1));
            contrib = mod.mul(contrib, follow[n - 1]);
            ans = mod.plus(ans, contrib);
            deleteBit.update(mat[0][j], 1);
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                indexOfVal[mat[i][j]] = j;
                indexOfDim[mat[i - 1][j]] = j;
            }
            for (int j = 0; j < n; j++) {
                //from left
                if (left[i][j] == 0) {
                    continue;
                }
                int contrib = left[i][j];
                int remain = n - 1 - j;
                int l = live[i][j];
                if (indexOfDim[mat[i][j]] > j) {
                    l++;
                }
                contrib = mod.mul(contrib, permutation.get(l, remain - l));
                contrib = mod.mul(contrib, follow[n - 1 - i]);
                ans = mod.plus(ans, contrib);
            }
            for (int j = 0; j < n; j++) {
                //from right
                if (right[i][j] == 0) {
                    continue;
                }
                int contrib = right[i][j];
                int remain = n - 1 - j;
                int l = live[i][j];
                if (indexOfDim[mat[i][j]] > j) {
                    l++;
                }
                contrib = mod.mul(contrib, permutation.get(l - 1, remain - (l - 1)));
                contrib = mod.mul(contrib, follow[n - 1 - i]);
                ans = mod.plus(ans, contrib);
            }
        }

        out.println(ans);
    }
}
