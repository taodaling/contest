package contest;

import java.util.Arrays;

import template.FastInput;
import template.FastOutput;
import template.IntegerList;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = 40;
        int n = k * k - k + 1;

        out.append(n).append(' ').append(k).println();

        boolean[][] forbiden = new boolean[n][n];
        int[] used = new int[n];

        IntegerList picked = new IntegerList(k);
        boolean[] localForbiden = new boolean[n];
        for (int i = 0; i < n; i++) {
            picked.clear();
            Arrays.fill(localForbiden, false);
            for (int j = 0; j < n && picked.size() < k; j++) {
                if (used[j] == k || localForbiden[j]) {
                    continue;
                }
                picked.add(j);
                used[j]++;
                for (int t = 0; t < n; t++) {
                    localForbiden[t] = localForbiden[t] || forbiden[j][t];
                }
            }

            for(int j = 0; j < picked.size(); j++){
                for(int t = j + 1; t < picked.size(); t++){
                    int vj = picked.get(j);
                    int vt = picked.get(t);
                    forbiden[vj][vt] = forbiden[vt][vj] = true;
                }
            }

            for(int j = 0; j < picked.size(); j++){
                out.append(picked.get(j) + 1).append(' ');
            }
            out.println();
        }

        return;
    }
}
