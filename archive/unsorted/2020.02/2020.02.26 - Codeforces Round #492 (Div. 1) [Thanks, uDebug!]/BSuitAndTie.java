package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.utils.SequenceUtils;

public class BSuitAndTie {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] line = new int[n * 2];
        for (int i = 0; i < line.length; i++) {
            line[i] = in.readInt();
        }
        int cnt = 0;
        for (int i = 0; i < line.length; i += 2) {
            if (line[i] == line[i + 1]) {
                continue;
            }
            int j;
            for (j = i + 1; line[j] != line[i]; j++) ;
            for (; j > i + 1; j--) {
                cnt++;
                SequenceUtils.swap(line, j, j - 1);
            }
        }
        out.println(cnt);
    }
}
