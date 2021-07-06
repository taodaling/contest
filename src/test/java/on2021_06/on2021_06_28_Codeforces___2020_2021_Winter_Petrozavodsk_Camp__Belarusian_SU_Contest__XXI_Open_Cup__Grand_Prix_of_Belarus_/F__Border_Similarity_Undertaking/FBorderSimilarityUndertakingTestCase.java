package on2021_06.on2021_06_28_Codeforces___2020_2021_Winter_Petrozavodsk_Camp__Belarusian_SU_Contest__XXI_Open_Cup__Grand_Prix_of_Belarus_.F__Border_Similarity_Undertaking;





import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class FBorderSimilarityUndertakingTestCase {
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
        int n = random.nextInt(1, 100);
        int m = random.nextInt(1, 100);
        StringBuilder in = new StringBuilder();
        printLine(in, n, m);
        for(int i = 0; i < n; i++){
            printLineObj(in, random.nextString('a', 'z', m));
        }
        return new Test(in.toString(), null);
    }
}
