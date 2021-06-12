package on2021_06.on2021_06_11_Library_Checker.Bitwise_Xor_Convolution;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.polynomial.FastWalshHadamardTransform;
import template.rand.RandomWrapper;

public class BitwiseXorConvolutionTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
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
    int mod = 998244353;
    public Test create(int testNum){
        int l = 2;
        int[] a = new int[1 << l];
        int[] b = new int[1 << l];
        for(int i = 0; i < a.length; i++){
            a[i] = random.nextInt(0, mod - 1);
            b[i] = random.nextInt(0, mod - 1);
        }
        int[] ans = solve(a, b);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, l);
        printLine(in, a);
        printLine(in, b);
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(int[] a, int[] b){
        a = a.clone();
        b = b.clone();
        FastWalshHadamardTransform.xorFWT(a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.xorFWT(b, 0, a.length - 1, mod);
        FastWalshHadamardTransform.dotMul(a, b, a, 0, a.length - 1, mod);
        FastWalshHadamardTransform.xorIFWT(a, 0, a.length - 1, mod);
        return a;
    }
}
