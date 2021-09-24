package on2021_09.on2021_09_13_Codeforces___Codeforces_Global_Round_16.G__Four_Vertices;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.graph.EdgeMap;
import template.rand.RandomWrapper;

public class GFourVerticesTestCase {
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
        int n = (int)1e5;
        int m = n;
        int q = n;
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        EdgeMap map = new EdgeMap(m + q);
        for(int i = 0; i < m; ){
            int a = random.nextInt(1, n);
            int b = random.nextInt(1, n);
            if(a == b){
                continue;
            }
            if(map.exist(a, b)){
                continue;
            }
            i++;
            int w = random.nextInt(1, (int)1e9);
            printLine(in, a, b, w);
        }
        printLine(in, q);
        for(int i = 0; i < q; ){
            int a = random.nextInt(1, n);
            int b = random.nextInt(1, n);
            if(a == b){
                continue;
            }
            if(map.exist(a, b)){
                continue;
            }
            i++;
            int w = random.nextInt(1, (int)1e9);
            printLine(in, 1, a, b, w);
        }
        return new Test(in.toString());
    }
}
