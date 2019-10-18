package on2019_10.on2019_10_17_Codeforces_Round__593__Div__2_.E___Alice_and_the_Unfair_Game0;





import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;

import java.util.*;

public class TaskETestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> ans = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ans.add(createSingle());
        }
        return ans;
    }

    Random random = new Random(0);

    public Test createSingle() {
        int n = random.nextInt(6000) + 1;
        int m = random.nextInt(6000) + 1;
        int[] seq = new int[m];
        for (int i = 0; i < m; i++) {
            seq[i] = random.nextInt(n) + 1;
        }

        long ans = solve(n, m, seq);

        StringBuilder in = new StringBuilder();
        in.append(n).append(' ').append(m).append('\n');
        for (int i = 0; i < m; i++) {
            in.append(seq[i]).append(' ');
        }

        return new Test(in.toString(), Long.toString(ans));
    }

    public long solve(int n, int m, int[] seq) {
        if (n == 1) {
            return 0;
        }
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            int left = i;
            int right = i;
            for (int j = 0; j < m; j++) {
                if (left - 1 == seq[j]) {

                } else {
                    left--;
                }
                if (right + 1 == seq[j]) {

                } else {
                    right++;
                }
            }
            left = Math.max(1, left - 1);
            right = Math.min(n, right + 1);
            ans += right - left + 1;
        }

        return ans;
    }
}
