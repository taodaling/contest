package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FElectricSchemeTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 6);
        TreeSet<int[]> set = new TreeSet<>((a, b) -> a[0] == b[0] ? a[1] - b[1] : a[0] - b[0]);
        for (int i = 0; i < n; i++) {
            set.add(new int[]{
                    random.nextInt(1, (int)5), random.nextInt(1, (int)5)
            });
        }

        StringBuilder in = new StringBuilder();
        in.append(set.size()).append('\n');
        for(int[] pt : set){
            in.append(pt[0]).append(' ').append(pt[1]).append('\n');
        }
        return new Test(in.toString(), "");
    }
}
