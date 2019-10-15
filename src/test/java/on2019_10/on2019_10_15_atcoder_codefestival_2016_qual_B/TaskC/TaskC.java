package on2019_10.on2019_10_15_atcoder_codefestival_2016_qual_B.TaskC;



import java.io.PrintWriter;
import java.util.Arrays;

import template.FastInput;
import template.Randomized;

public class TaskC {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int w = in.readInt();
        int h = in.readInt();
        int[] rowCost = new int[h];
        int[] colCost = new int[w];

        for (int i = 0; i < w; i++) {
            colCost[i] = in.readInt();
        }

        for (int i = 0; i < h; i++) {
            rowCost[i] = in.readInt();
        }

        Randomized.randomizedArray(rowCost, 0, rowCost.length);
        Randomized.randomizedArray(colCost, 0, colCost.length);
        Arrays.sort(rowCost);
        Arrays.sort(colCost);

        int rIndex = 0;
        int cIndex = 0;

        long cost = 0;
        while (rIndex < h || cIndex < w) {
            if (rIndex < h && (cIndex >= w || rowCost[rIndex] <= colCost[cIndex])) {
                long need = w + 1 - cIndex;
                cost += need * rowCost[rIndex];
                rIndex++;
            } else {
                long need = h + 1 - rIndex;
                cost += need * colCost[cIndex];
                cIndex++;
            }
        }

        out.println(cost);
    }
}
