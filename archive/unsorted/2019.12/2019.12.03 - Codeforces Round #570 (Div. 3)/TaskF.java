package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.rand.Randomized;

import java.util.Arrays;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntList list = new IntList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.readInt());
        }
        list.unique();
        int[] a = list.toArray();
        n = a.length;

        int ans = 0;
        for (int i = n - 1; i >= 0; i--) {
            IntList pick = new IntList(3);
            for (int j = i; j >= 0 && pick.size() < 3; j--) {
                boolean flag = false;
                for (int k = 0; k < pick.size(); k++) {
                    flag = flag || pick.get(k) % a[j] == 0;
                }
                if (!flag) {
                    pick.add(a[j]);
                }
            }

            int sum = 0;
            for(int j = 0; j < pick.size(); j++){
                sum += pick.get(j);
            }

            ans = Math.max(ans, sum);
        }

        out.println(ans);
    }
}
