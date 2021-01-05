package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.KMAlgo;
import template.RandomWrapper;
import template.SequenceUtils;

public class TaskBTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create());
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create() {
        int n = random.nextInt(1, 3);
        int m = random.nextInt(1, 3);
        int[][] lrs = new int[n][2];
        for (int i = 0; i < n; i++) {
            lrs[i][0] = random.nextInt(0, m);
            lrs[i][1] = random.nextInt(1, m + 1);
            if (lrs[i][0] > lrs[i][1]) {
                SequenceUtils.swap(lrs[i], 0, 1);
            }
            if (lrs[i][0] == lrs[i][1]) {
                lrs[i][0]--;
            }
        }

        KMAlgo km = new KMAlgo(n, m + 1);
        for(int i = 0; i < n; i++){
            for(int j = 1; j <= lrs[i][0]; j++){
                km.addEdge(i, j);
            }
            for(int j = lrs[i][1]; j <= m; j++){
                km.addEdge(i, j);
            }
        }

        int maxMatch = 0;
        for(int i = 0; i < n; i++){
            maxMatch += km.matchLeft(i) ? 1 : 0;
        }

        int ans = n - maxMatch;

        StringBuilder builder = new StringBuilder();
        builder.append(n).append(' ').append(m).append('\n');
        for(int i = 0; i < n; i++){
            builder.append(lrs[i][0]).append(' ').append(lrs[i][1]).append('\n');
        }

        return new Test(builder.toString(), Integer.toString(ans));
    }
}
