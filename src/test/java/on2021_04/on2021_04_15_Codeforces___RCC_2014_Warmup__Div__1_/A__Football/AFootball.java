package on2021_04.on2021_04_15_Codeforces___RCC_2014_Warmup__Div__1_.A__Football;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SortUtils;

import java.util.stream.IntStream;

public class AFootball {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        if (k * n > n * (n - 1) / 2) {
            out.println(-1);
            return;
        }
        out.println(k * n);
        int[] win = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int remain = k - win[i];
            int[] indices = IntStream.range(0, i).toArray();
            SortUtils.quickSort(indices, (a, b) -> -Integer.compare(win[a], win[b]), 0, i);
            for (int j = 0; j < remain; j++) {
                out.append(i + 1).append(' ').append(j + 1).append(' ').println();
            }
            for (int j = remain; j < i; j++) {
                if(win[j] < k) {
                    out.append(j + 1).append(' ').append(i + 1).append(' ').println();
                    win[j]++;
                }
            }
        }
    }
}
