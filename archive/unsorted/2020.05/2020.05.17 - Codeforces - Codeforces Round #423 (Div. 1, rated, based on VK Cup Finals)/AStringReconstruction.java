package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.TreeSet;

public class AStringReconstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        TreeSet<Integer> set = new TreeSet<>();
        char[] ans = new char[2000001];
        for (int i = 1; i < ans.length; i++) {
            set.add(i);
        }

        char[] s = new char[1000000];
        for (int i = 1; i <= n; i++) {
            int len = in.readString(s, 0);
            int k = in.readInt();
            while (k-- > 0) {
                int index = in.readInt();
                while (true) {
                    Integer next = set.ceiling(index);
                    if (next == null || next - index + 1 > len) {
                        break;
                    }
                    ans[next] = s[next - index];
                    set.remove(next);
                }
            }
        }

        int len = ans.length - 1;
        while (ans[len] == 0) {
            len--;
        }
        for (int i = 1; i <= len; i++) {
            if (ans[i] == 0) {
                ans[i] = 'a';
            }
        }

        for (int i = 1; i <= len; i++) {
            out.append(ans[i]);
        }
    }
}
