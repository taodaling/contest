package on2021_03.on2021_03_26_Google_Coding_Competitions___Qualification_Round_2021___Code_Jam_2021.Cheating_Detection;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.stream.IntStream;

public class CheatingDetection {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int t = in.ri();
        int p = in.ri();
        for (int i = 1; i <= t; i++) {
            solveOne(i, in, out);
        }
    }

    char[][] s = new char[100][(int) 1e4];

    public void solveOne(int testNumber, FastInput in, FastOutput out) {
        int n = 100;
        int m = (int) 1e4;
        for (int i = 0; i < n; i++) {
            in.rs(s[i]);
        }
        int[] difficulty = new int[m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                difficulty[j] += s[i][j] - '0';
            }
        }
        int[] indices = IntStream.range(0, m).toArray();
        SortUtils.quickSort(indices, (i, j) -> Integer.compare(difficulty[i], difficulty[j]), 0, m);
        int[] hardScore = new int[n];
        for (int i = 0; i < 1000; i++) {
            int index = indices[i];
            for (int j = 0; j < n; j++) {
                hardScore[j] += s[j][index] - '0';
            }
        }
        int[] easyScore = new int[n];
        for (int i = 0; i < 1000; i++) {
            int index = indices[m - 1 - i];
            for (int j = 0; j < n; j++) {
                easyScore[j] += s[j][index] - '0';
            }
        }
        int[] range = IntStream.range(0, n).toArray();
        SortUtils.quickSort(range, (i, j) -> Integer.compare(hardScore[j], hardScore[i]), 0, n);
        int[] prefix = Arrays.copyOf(range, 20);
        SortUtils.quickSort(prefix, (i, j) -> Integer.compare(easyScore[i] - hardScore[i], easyScore[j] - hardScore[i]), 0, prefix.length);

        out.printf("Case #%d: %d", testNumber, prefix[0] + 1).println();
    }
}
