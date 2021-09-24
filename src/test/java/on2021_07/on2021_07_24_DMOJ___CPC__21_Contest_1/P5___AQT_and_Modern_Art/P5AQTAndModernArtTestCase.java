package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P5___AQT_and_Modern_Art;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class P5AQTAndModernArtTestCase {
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
        int n = random.nextInt(500, 500);
        StringBuilder in = new StringBuilder();
        printLine(in, n);
        int L = (int)1e9;
        for(int i = 0; i < n; i++){
            int l = random.nextInt(1, L);
            int r = random.nextInt(1, L);
            int b = random.nextInt(1, L);
            int t = random.nextInt(1, L);
            if(l > r){
                int tmp = l;
                l = r;
                r = tmp;
            }
            if(b > t){
                int tmp = b;
                b = t;
                t = tmp;
            }
            printLine(in, l, r, b, t);
        }
        return new Test(in.toString());
    }
}
