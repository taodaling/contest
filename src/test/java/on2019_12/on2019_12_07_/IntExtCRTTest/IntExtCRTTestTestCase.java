package on2019_12.on2019_12_07_.IntExtCRTTest;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.IntList;
import template.math.ExtCRT;
import template.rand.RandomWrapper;

public class IntExtCRTTestTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int p = random.nextInt(2, (int) 2e9);
        int v = random.nextInt(1, p - 1);
        List<int[]> ans = new ArrayList<>();
        for (int i = 2; i * i <= p; i++) {
            if (p % i != 0) {
                continue;
            }
            ans.add(new int[]{v % i, i});
            if(p / i != i){
                ans.add(new int[]{v % (p / i), p / i});
            }
        }

        StringBuilder builder = new StringBuilder();
        builder.append(ans.size()).append('\n');
        ExtCRT crt = new ExtCRT();
        for(int[] pair : ans){
            crt.add(pair[0], pair[1]);
            builder.append(pair[0]).append(' ').append(pair[1]).append('\n');
        }
        return new Test(builder.toString(), Long.toString(crt.getValue()));
    }
}
