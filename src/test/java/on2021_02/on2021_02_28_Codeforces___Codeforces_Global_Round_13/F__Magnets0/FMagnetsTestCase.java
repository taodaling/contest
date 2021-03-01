package on2021_02.on2021_02_28_Codeforces___Codeforces_Global_Round_13.F__Magnets0;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.rand.Randomized;

public class FMagnetsTestCase {
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
        int n = random.nextInt(3, 8);
        int[] a = new int[n];
        a[0] = 0;
        a[1] = random.nextInt(2) * 2 - 1;
        a[2] = random.nextInt(2) * 2 - 1;
        for(int i = 3; i < n; i++){
            a[i] = random.nextInt(-1, 1);
        }
        Randomized.shuffle(a);
        StringBuilder in = new StringBuilder();
        printLine(in, 1);
        printLine(in, n);
        printLine(in, a);
        return new Test(in.toString(), null);
    }
}
