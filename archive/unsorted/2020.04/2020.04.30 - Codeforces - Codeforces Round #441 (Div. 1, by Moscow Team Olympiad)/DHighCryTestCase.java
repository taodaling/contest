package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class DHighCryTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 30);
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(0, (int) 1e9);
        }
        List<int[]> ans = solve(a);

        StringBuilder in = new StringBuilder();
        printLine(in, n);
        for(int x : a){
            in.append(x).append(' ');
        }
        return new Test(in.toString(), "" + ans.size());
    }

    public List<int[]> solve(int[] x) {
        List<int[]> ans = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            for (int j = i; j < x.length; j++) {
                int or = 0;
                for (int k = i; k <= j; k++) {
                    or |= x[k];
                }
                boolean valid = true;
                for (int k = i; k <= j; k++) {
                    valid = valid && or > x[k];
                }
                if(valid){
                    ans.add(new int[]{i, j});
                }
            }
        }
        return ans;
    }


}
