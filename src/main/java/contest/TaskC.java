package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;

import java.util.ArrayList;
import java.util.List;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<int[]>[] lists = new List[n];
        for (int i = 0; i < n; i++) {
            int m = in.readInt();
            lists[i] = new ArrayList<>(m);
            for (int j = 0; j < m; j++) {
                int x = in.readInt() - 1;
                int y = in.readInt();
                lists[i].add(new int[]{x, y});
            }
        }

        int max = 0;
        BitOperator bo = new BitOperator();
        for (int i = (1 << n) - 1; i >= 0; i--) {
            boolean valid = true;
            for (int j = 0; valid && j < 15; j++) {
                if (bo.bitAt(i, j) == 0) {
                    continue;
                }
                for (int[] test : lists[j]) {
                    if (bo.bitAt(i, test[0]) != test[1]) {
                        valid = false;
                        break;
                    }
                }
            }
            if(valid){
                max = Math.max(max, Integer.bitCount(i));
            }
        }

        out.println(max);
    }
}
