package on2020_08.on2020_08_24_Codeforces___Codeforces_Round__371__Div__1_.B__Searching_Rectangles;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class BInteractor extends AbstractInteractor {

    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.readInt();
        int[][] rects = new int[2][4];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                rects[i][j] = input.readInt();
            }
        }

        solutionInput.println(n).flush();
        int chance = 200;
        while (chance-- > 0) {
            char c = solutionOutput.readChar();
            if (c == '?') {
                int[] rect = new int[4];
                for (int i = 0; i < 4; i++) {
                    rect[i] = solutionOutput.readInt();
                }
                int ans = contain(rect, rects[0]) + contain(rect, rects[1]);
                solutionInput.println(ans).flush();
            } else {
                int[][] ans = new int[2][4];
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 4; j++) {
                        ans[i][j] = solutionOutput.readInt();
                    }
                }

                if (equal(rects[0], ans[0]) && equal(rects[1], ans[1])) {
                    return Verdict.OK;
                }
                SequenceUtils.swap(ans, 0, 1);
                if (equal(rects[0], ans[0]) && equal(rects[1], ans[1])) {
                    return Verdict.OK;
                }
                return Verdict.WA;
            }
        }
        return Verdict.WA;
    }

    public int contain(int[] a, int[] b) {
        return a[0] <= b[0] && a[1] <= b[1] && a[2] >= b[2] && a[3] >= b[3] ? 1 : 0;
    }

    public boolean equal(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }
}
