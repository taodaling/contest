package on2020_04.on2020_04_22_Codeforces___Codeforces_Round__449__Div__1_.B__Ithea_Plays_With_Chtholly;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.util.Arrays;

public class BItheaPlaysWithChtholly {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int c = in.readInt();
        int half = c / 2;

        int[] data = new int[n];
        Arrays.fill(data, -1);

        for (int i = 0; i < m; i++) {
            if (SequenceUtils.indexOf(data, 0, n - 1, -1) == -1 && CompareUtils.notStrictAscending(data, 0, n - 1)) {
                return;
            }
            int val = in.readInt();
            if (val <= half) {
                for (int j = 0; ; j++) {
                    if (data[j] == -1 || data[j] > val) {
                        data[j] = val;
                        out.println(j + 1).flush();
                        break;
                    }
                }
            } else {
                for (int j = n - 1; ; j--) {
                    if (data[j] == -1 || data[j] < val) {
                        data[j] = val;
                        out.println(j + 1).flush();
                        break;
                    }
                }
            }
        }
    }
}
