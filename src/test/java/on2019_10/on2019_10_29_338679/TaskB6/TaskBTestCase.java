package on2019_10.on2019_10_29_338679.TaskB6;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.IntList;
import template.RandomWrapper;

public class TaskBTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create());
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create() {
        int x = random.nextInt(1, 100000000);
        int k = random.nextInt(1, 200);
        IntList list = new IntList();
        for (int i = 0; i < k; i++) {
            list.add(random.nextInt(1, x));
        }
        list.unique();
        k = list.size();
        int[] r = list.toArray();
        int q = random.nextInt(1, 3);
        int[] qs = new int[q];
        int[] as = new int[q];
        for (int i = 0; i < q; i++) {
            qs[i] = random.nextInt(0, (int) 1e9);
            as[i] = random.nextInt(0, x);
        }

        StringBuilder in = new StringBuilder();
        StringBuilder ans = new StringBuilder();

        int[] rs = new int[k + 1];
        System.arraycopy(r, 0, rs, 1, k);
        for (int i = 0; i < q; i++) {
            int ti = qs[i];
            int ai = as[i];

            int j = 1;
            while (j <= k && rs[j] <= ti) {
                if (j % 2 == 1) {
                    ai = Math.max(0, ai - (rs[j] - rs[j - 1]));
                } else {
                    ai = Math.min(x, ai + (rs[j] - rs[j - 1]));
                }
                j++;
            }

            int more = ti - rs[j - 1];
            if(j % 2 == 1){
                ai = Math.max(0, ai - more);
            }else{
                ai = Math.min(x, ai + more);
            }
            ans.append(ai).append('\n');
        }

        in.append(x).append('\n').append(k).append('\n');
        for(int i = 0; i < k; i++){
            in.append(r[i]).append(' ');
        }
        in.append('\n').append(q).append('\n');
        for(int j = 0; j < q; j++){
            in.append(qs[j]).append(' ').append(as[j]).append('\n');
        }

        return new Test(in.toString(), ans.toString());
    }
}
