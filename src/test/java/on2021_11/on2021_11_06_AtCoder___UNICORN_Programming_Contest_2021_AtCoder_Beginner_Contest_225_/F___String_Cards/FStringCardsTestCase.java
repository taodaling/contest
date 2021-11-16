package on2021_11.on2021_11_06_AtCoder___UNICORN_Programming_Contest_2021_AtCoder_Beginner_Contest_225_.F___String_Cards;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class FStringCardsTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
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
        String[] ss = new String[n];
        for(int i = 0; i < n; i++){
            ss[i] = random.nextString('a', 'b', random.nextInt(1, 3));
        }
        int k = random.nextInt(1, n);
        String ans = solve(ss, k);
        StringBuilder in = new StringBuilder();
        printLine(in, n, k);
        printLineObj(in, ss);
        return new Test(in.toString(), ans);
    }

    public String solve(String[] ss, int k){
        Arrays.sort(ss, (a, b) -> (a + b).compareTo(b + a));
        String best = null;
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 1 << ss.length; i++){
            if(Integer.bitCount(i) == k){
                builder.setLength(0);
                for(int j = 0; j < ss.length; j++){
                    if(Bits.get(i, j) == 1){
                        builder.append(ss[j]);
                    }
                }
                String cand = builder.toString();
                if(best == null || best.compareTo(cand) > 0){
                    best = cand;
                }
            }
        }
        return best;
    }
}
