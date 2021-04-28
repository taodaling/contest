package on2021_04.on2021_04_28_Codeforces___Codeforces_Round__194__Div__1_.D__Characteristics_of_Rectangles;



import template.algo.BinarySearch;
import template.datastructure.BitSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.SequenceUtils;

import java.util.function.IntPredicate;

public class DCharacteristicsOfRectangles {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[][] mat = new int[n][m];
        IntegerArrayList unique = new IntegerArrayList(n * m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = in.ri();
                unique.add(mat[i][j]);
            }
        }
        int[] seq = new int[m];
        boolean[][] visited = new boolean[m][m];
        unique.unique();
        IntPredicate predicate = mid -> {
            SequenceUtils.deepFill(visited, false);
            int v = unique.get(mid);
            for (int i = 0; i < n; i++) {
                int tail = 0;
                for (int j = 0; j < m; j++) {
                    if (mat[i][j] >= v) {
                        seq[tail++] = j;
                    }
                }
                for (int j = 0; j < tail; j++) {
                    for (int k = 0; k < j; k++) {
                        if (visited[seq[k]][seq[j]]) {
                            return true;
                        }
                        visited[seq[k]][seq[j]] = true;
                    }
                }
            }
            return false;
        };

        int index = BinarySearch.lastTrue(predicate, 0, unique.size() - 1);
        out.println(unique.get(index));
    }
}
