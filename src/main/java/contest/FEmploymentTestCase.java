package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.rand.RandomWrapper;

public class FEmploymentTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        int m = random.nextInt(1, 5000);
        int n = random.nextInt(1, 3);
        int[] a = new int[n];
        int[] b = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = random.nextInt(1, m);
            b[i] = random.nextInt(1, m);
        }

        StringBuilder in = new StringBuilder();
        in.append(m).append(' ').append(n).append('\n');
        for(int i = 0; i < n; i++){
            in.append(a[i]).append(' ');
        }
        in.append('\n');
        for(int i = 0; i < n; i++){
            in.append(b[i]).append(' ');
        }

        long ans = solve(n, m, a.clone(), b.clone());
        return new Test(in.toString(), "" + ans);
    }

    public long solve(int n, int m, int[] a, int[] b){
        Arrays.sort(a);
        Arrays.sort(b);
        long ans = Long.MAX_VALUE;
        for(int i = 0; i < n; i++){
            long local = 0;
            for(int j = 0; j < n; j++){
                int x = a[j];
                int y = b[(i + j) % n];
                int dist = Math.min(DigitUtils.mod(x - y, m), DigitUtils.mod(y - x, m));
                local += dist;
            }
            ans = Math.min(ans, local);
        }
        return ans;
    }
}
