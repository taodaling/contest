package on2021_07.on2021_07_16_DMOJ___DMOPC__20_Contest_1.P5___Victor_Takes_Over_Canada;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P5VictorTakesOverCanadaTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1; i++){
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
        int n = (int)5e5;
        int k = n;
        int m = (int)3e5;
        StringBuilder in = new StringBuilder();
        printLine(in, n, k, m);
        for(int i = 0; i < n; i++){
            in.append(random.nextInt(1, k)).append(' ');
        }
        for(int i = 2; i <= n; i++){
            int p = random.nextInt(1, i - 1);
            in.append(p).append(' ').append(i).append('\n');
        }
        for(int i = 0; i < m; i++){
            int t = random.nextInt(1, 2);
            int c = random.nextInt(1, n);
            if(t == 1){
                printLine(in, t, c, random.nextInt(1, k));
            }else{
                printLine(in, t, c);
            }
        }
        return new Test(in.toString());
    }
}
