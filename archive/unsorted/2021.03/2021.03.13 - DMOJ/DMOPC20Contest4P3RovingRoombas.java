package contest;

import template.datastructure.Range2DequeAdapter;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerBIT;
import template.primitve.generated.datastructure.IntegerBinaryFunction;

import java.util.Arrays;
import java.util.Comparator;

public class DMOPC20Contest4P3RovingRoombas {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList allx = new IntegerArrayList(n + m);
        int[][] row = new int[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                row[i][j] = in.ri();
            }
            allx.add(row[i][0]);
        }
        int[][] col = new int[m][2];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < 2; j++) {
                col[i][j] = in.ri();
            }
            allx.add(col[i][0]);
        }
        allx.unique();
        for (int i = 0; i < n; i++) {
            row[i][0] = allx.binarySearch(row[i][0]) + 1;
        }
        for (int i = 0; i < m; i++) {
            col[i][0] = allx.binarySearch(col[i][0]) + 1;
        }
        long sum = 0;
        Arrays.sort(row, Comparator.comparingInt(x -> -x[1]));
        Arrays.sort(col, Comparator.comparingInt(x -> -x[1]));
        IntegerBIT bit = new IntegerBIT(allx.size());
        SimplifiedDeque<int[]> dq = new Range2DequeAdapter<>(i -> col[i], 0, col.length - 1);
        for (int[] r : row) {
            while (!dq.isEmpty() && dq.peekFirst()[1] >= r[1]) {
                bit.update(dq.removeFirst()[0], 1);
            }
            sum += bit.query(r[0]);
        }
        out.println(sum);
    }
}
