package on2019_11.on2019_11_25_Educational_Codeforces_Round_60__Rated_for_Div__2_.G___Recursive_Queries0;





import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.datastructure.SparseTable;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TaskGTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        int q = random.nextInt(1, 5);
        int[] p = new int[n];
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }
        Randomized.randomizedArray(p, 0, n);
        int[][] qs = new int[q][2];
        for (int i = 0; i < q; i++) {
            for (int j = 0; j < 2; j++) {
                qs[i][j] = random.nextInt(0, n - 1);
            }
            if (qs[i][0] > qs[i][1]) {
                SequenceUtils.swap(qs[i], 0, 1);
            }
        }

        Integer[] indexes = new Integer[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = i;
        }
        SparseTable<Integer> st = new SparseTable<>(indexes, n, (a, b) -> p[a] < p[b] ? b : a);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        in.append(n).append(' ').append(q).append('\n');
        for (int i = 0; i < n; i++) {
            in.append(p[i]).append(' ');
        }
        in.append('\n');
        for (int i = 0; i < q; i++) {
            in.append(qs[i][0] + 1).append(' ');
        }
        in.append('\n');
        for(int i = 0; i < q; i++){
            in.append(qs[i][1] + 1).append(' ');
        }

        for (int i = 0; i < q; i++) {
            long ans = solve(st, qs[i][0], qs[i][1]);
            out.append(ans).append('\n');
        }

        return new Test(in.toString(), out.toString());

    }

    public long solve(SparseTable<Integer> st, int l, int r) {
        if (l > r) {
            return 0;
        }
        int m = st.query(l, r);
        return (r - l + 1) + solve(st, l, m - 1) + solve(st, m + 1, r);
    }
}
