package contest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.utils.SequenceUtils;

public class IntervalBooleanMapTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int q = random.nextInt(100, 1000);
        List<int[]> query = new ArrayList<>();
        for (int i = 0; i < q; i++) {
            int t = random.nextInt(0, 2);
            int l = random.nextInt(0, 1000);
            int r = random.nextInt(0, 1000);
            if (l > r) {
                int tmp = l;
                l = r;
                r = tmp;
            }
            if (t != 2) {
                query.add(SequenceUtils.wrapArray(t, l, r));
            } else {
                query.add(SequenceUtils.wrapArray(t));
            }
        }

        StringBuilder in = new StringBuilder(q);
        StringBuilder out = new StringBuilder();
        in.append(q).append('\n');
        for (int[] qu : query) {
            for(int x : qu){
                in.append(x).append(' ');
            }
            in.append('\n');
        }
        for(int x : solve(query)){
            out.append(x).append('\n');
        }
        return new Test(in.toString(), out.toString());
    }

    public List<Integer> solve(List<int[]> qs) {
        int[] cnts = new int[1000 + 1];
        List<Integer> ans = new ArrayList<>();
        for (int[] q : qs) {
            if (q[0] != 2) {
                for (int i = q[1]; i <= q[2]; i++) {
                    cnts[i] = 1 - q[0];
                }
            } else {
                int total = 0;
                for (int x : cnts) {
                    total += x;
                }
                ans.add(total);
            }
        }

        return ans;
    }
}
