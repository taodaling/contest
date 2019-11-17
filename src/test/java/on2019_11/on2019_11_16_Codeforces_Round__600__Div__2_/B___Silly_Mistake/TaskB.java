package on2019_11.on2019_11_16_Codeforces_Round__600__Div__2_.B___Silly_Mistake;



import template.FastInput;
import template.FastOutput;
import template.IntVersionArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskB {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        boolean[] enter = new boolean[1000000 + 1];
        int[] lastDay = new int[1000000 + 1];
        Arrays.fill(lastDay, -1);
        int cnt = 0;
        int num = 0;
        List<Integer> ans = new ArrayList<>();
        boolean valid = true;
        for (int i = 0; i < n; i++) {
            int who = Math.abs(a[i]);
            num++;
            if (a[i] > 0) {
                if (lastDay[who] == ans.size() || enter[who]) {
                    valid = false;
                }
                enter[who] = true;
                lastDay[who] = ans.size();
                cnt++;
            } else {
                if (!(lastDay[who] == ans.size() && enter[who])) {
                    valid = false;
                }
                enter[who] = false;
                cnt--;
            }
            if (cnt == 0) {
                ans.add(num);
                num = 0;
            }
        }

        if (cnt != 0) {
            valid = false;
        }

        if (!valid) {
            out.println(-1);
            return;
        }

        out.println(ans.size());
        for (Integer x : ans) {
            out.append(x).append(' ');
        }
    }
}
