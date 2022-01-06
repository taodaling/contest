package on2021_11.on2021_11_15_DMOJ___DMOPC__21_Contest_3.P4___Sorting_Subsequences0;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerIterator;
import template.rand.RandomWrapper;
import template.rand.Randomized;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

public class P4SortingSubsequencesTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int...vals){
        for(int val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long...vals){
        for(long val : vals){
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
    public Test create(int testNum){
        int n = random.nextInt(1, 5);
        int k = random.nextInt(1, n);
        int[] p = IntStream.range(1, n + 1).toArray();
        Randomized.shuffle(p);
        int[] ans = solve(p, k);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, n, k);
        printLine(in, p);
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] p, int k){
        int n = p.length;
        int[][] dp = new int[1 << n][n];
        int inf = (int)1e9;
        SequenceUtils.deepFill(dp, inf);
        SequenceUtils.deepFill(dp[0], 0);
        IntegerArrayList sortBuf = new IntegerArrayList();
        for(int i = 1; i < 1 << n; i++){
            int subset = i + 1;
            while(subset > 0){
                subset = (subset - 1) & i;
                if(subset == 0 || Integer.bitCount(subset) > k){
                    continue;
                }
                int[] cand = dp[i - subset].clone();
                sortBuf.clear();
                for(int j = 0; j < n; j++){
                    if(Bits.get(subset, j) == 1){
                        sortBuf.add(p[j]);
                    }
                }
                sortBuf.sort();
                IntegerIterator iterator = sortBuf.iterator();
                for(int j = 0; j < n; j++){
                    if(Bits.get(subset, j) == 1){
                        cand[j] = iterator.next();
                    }
                }
                dp[i] = SortUtils.<int[]>min(dp[i], cand, (a, b) -> SortUtils.compareArray(a, 0, n - 1, b, 0, n - 1));
            }
        }
        return dp[dp.length - 1];
    }
}
