package on2020_07.on2020_07_10_Codeforces___Codeforces_Round__385__Div__1_.B__Hongcow_s_Game;



import chelper.AbstractInteractor;
import net.egork.chelper.tester.Verdict;
import net.egork.chelper.tester.State;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class BInteractor extends AbstractInteractor {
    @Override
    public Verdict interact(FastInput input, FastInput solutionOutput, FastOutput solutionInput) throws Throwable {
        int n = input.readInt();
        int[][] mat = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = input.readInt();
            }
        }

        solutionInput.println(n).flush();
        int time = 0;
        while (true) {
            int t = solutionOutput.readInt();
            if (t >= 0) {
                time++;
                if (time > 20) {
                    return Verdict.WA;
                }
                int[] ans = new int[n];
                Arrays.fill(ans, (int) 1e9);
                for (int i = 0; i < t; i++) {
                    int index = solutionOutput.readInt() - 1;
                    for (int j = 0; j < n; j++) {
                        ans[j] = Math.min(ans[j], mat[j][index]);
                    }
                }
                for (int i = 0; i < n; i++) {
                    solutionInput.append(ans[i]).append(' ');
                }
                solutionInput.println().flush();
            } else if (t == -1) {
                for (int i = 0; i < n; i++) {
                    int ans = (int) 1e9;
                    for (int j = 0; j < n; j++) {
                        if (i == j) {
                            continue;
                        }
                        ans = Math.min(ans, mat[i][j]);
                    }
                    int row = solutionOutput.readInt();
                    if (row != ans) {
                        return Verdict.WA;
                    }
                }
                return Verdict.OK;
            } else {
                return Verdict.WA;
            }
        }
    }
}
