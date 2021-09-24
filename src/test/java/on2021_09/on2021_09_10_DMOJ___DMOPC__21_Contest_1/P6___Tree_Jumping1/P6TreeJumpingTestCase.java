package on2021_09.on2021_09_10_DMOJ___DMOPC__21_Contest_1.P6___Tree_Jumping1;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P6TreeJumpingTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 0; i++){
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
        int n = random.nextInt(100000, 100000);
        int[] r = new int[n];
        int[] c = new int[n];
        for(int i = 0; i < n; i++){
            r[i] = random.nextInt(1, (int)1e9);
        }
        for(int i = 0; i < n; i++){
            c[i] = random.nextInt(1, (int)1e9);
        }
        int[][] E = new int[n - 1][3];
        for(int i = 0; i < n - 1; i++){
            E[i][0] = random.nextInt(1, i + 1);
            E[i][1] = i + 2;
            E[i][2] = random.nextInt(1, 10);
        }
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        printLine(in, r);
        printLine(in, c);
        for(int[] e : E){
            printLine(in, e);
        }
        return new Test(in.toString());
    }
}
