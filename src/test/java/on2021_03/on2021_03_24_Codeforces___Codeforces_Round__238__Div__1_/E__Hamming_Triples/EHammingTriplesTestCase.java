package on2021_03.on2021_03_24_Codeforces___Codeforces_Round__238__Div__1_.E__Hamming_Triples;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class EHammingTriplesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1,10);
        int m = random.nextInt(1, 10);
        int[][] pairs = new int[m][2];
        for (int i = 0; i < m; i++) {
            pairs[i][0] = random.nextInt(0, 1);
            pairs[i][1] = random.nextInt(1, n);
        }
        long[] ans = solve(n, pairs);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int[] p : pairs){
            printLine(in, p);
        }
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int H(int n, int[] a, int[] b) {
        if (a[0] == b[0]) {
            return Math.abs(a[1] - b[1]);
        }
        return Math.abs(n - Math.abs(a[1] - b[1]));
    }

    public int H(int n, int[] a, int[] b, int[] c){
        return H(n, a, b) + H(n, a, c) + H(n, b, c);
    }

    public long[] solve(int n, int[][] pairs) {
        long best = 0;
        long cnt = 0;
        for(int i = 0; i < pairs.length; i++){
            for(int j = i + 1; j < pairs.length; j++){
                for(int k = j + 1; k < pairs.length; k++){
                    long h = H(n , pairs[i], pairs[j], pairs[k]);
                    if(h > best){
                        best = h;
                        cnt = 0;
                    }
                    if(h == best){
                        cnt++;
                    }
                }
            }
        }
        return new long[]{best, cnt};
    }
}
