package on2020_05.on2020_05_08_TopCoder_SRM__748.Rectoggle0;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerVersionArray;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

import java.util.*;

public class RectoggleTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for (int i = 1; i <= 1000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public NewTopCoderTest create(int testNum) {
        int n = random.nextInt(1, 20);
        int[] row = new int[n];
        int[] col = new int[n];
        for (int i = 0; i < n; i++) {
            row[i] = random.nextInt(0, 30);
            col[i] = random.nextInt(0, 30);
        }
        int maxRow = random.nextInt(1, 10);
        int maxCol = random.nextInt(1, 10);

        int ans = solve(row, col, maxRow, maxCol);

        return new NewTopCoderTest(SequenceUtils.wrapObjectArray(row, col, maxRow, maxCol),
                ans);
    }

    int[][] sg;
    IntegerVersionArray iva = new IntegerVersionArray(100000);
    int[][] rect;
    int maxRow;
    int maxCol;

    public int rect(int i, int j) {
        if (i < 0 || j < 0) {
            return 0;
        }
        if (rect[i][j] == -1) {
            rect[i][j] = rect(i - 1, j) ^ rect(i, j - 1) ^ rect(i - 1, j - 1) ^ sg(i, j);
        }
        return rect[i][j];
    }

    public int rect(int rl, int rr, int cl, int cr) {
        if(rl > rr || cl > cr){
            return 0;
        }
        return rect(rr, cr) ^ rect(rl - 1, cr) ^ rect(rr, cl - 1) ^ rect(rl - 1, cl - 1);
    }

    public int sg(int i, int j) {
        if (sg[i][j] == -1) {

            for (int k = 1; k <= maxRow; k++) {
                for (int t = 1; t <= maxCol; t++) {
                    int a = i - k + 1;
                    int b = j - t + 1;
                    if (a < 0 || b < 0) {
                        continue;
                    }
                    rect(a, i - 1, b, j - 1);
                    rect(a, i, b, j - 1);
                    rect(a, i - 1, b, j);
                }
            }

            iva.clear();
            for (int k = 1; k <= maxRow; k++) {
                for (int t = 1; t <= maxCol; t++) {
                    int a = i - k + 1;
                    int b = j - t + 1;
                    if (a < 0 || b < 0) {
                        continue;
                    }
                    int v = rect(a, i - 1, b, j - 1) ^
                            rect(a, i, b, j - 1) ^
                            rect(a, i - 1, b, j);
                    iva.set(v, 1);
                }
            }

            sg[i][j] = 0;
            while (iva.get(sg[i][j]) == 1) {
                sg[i][j]++;
            }
        }

        return sg[i][j];
    }

    public int solve(int[] row, int[] col, int maxRow, int maxCol) {
        this.maxCol = maxCol;
        this.maxRow = maxRow;
        int r = 0;
        int c = 0;
        for (int x : row) {
            r = Math.max(r, x);
        }
        for (int x : col) {
            c = Math.max(c, x);
        }

        sg = new int[r + 1][c + 1];
        rect = new int[r + 1][c + 1];
        SequenceUtils.deepFill(sg, -1);
        SequenceUtils.deepFill(rect, -1);

        int xorsum = 0;
        for (int i = 0; i < row.length; i++) {
            int v = sg(row[i], col[i]);
            xorsum ^= v;
        }

        return xorsum == 0 ? 2 : 1;
    }
}
