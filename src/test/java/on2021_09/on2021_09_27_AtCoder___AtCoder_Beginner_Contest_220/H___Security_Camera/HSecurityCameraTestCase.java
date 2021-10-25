package on2021_09.on2021_09_27_AtCoder___AtCoder_Beginner_Contest_220.H___Security_Camera;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.rand.RandomWrapper;

public class HSecurityCameraTestCase {
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
        int n = random.nextInt(1, 4);
        List<int[]> adj = new ArrayList<>();
        for(int i = 1; i <= n; i++){
            for(int j = i + 1; j <= n; j++){
                if(random.nextInt(1, 2) == 1){
                    adj.add(new int[]{i, j});
                }
            }
        }

        int ans = solve(n, adj);
        StringBuilder in = new StringBuilder();
        printLine(in, n, adj.size());
        for(int[] e : adj){
            printLine(in, e);
        }

        return new Test(in.toString(), "" + ans);
    }

    public int solve(int n, List<int[]> adj){
        int ans = 0;
        for(int i = 0; i < 1 << n; i++){
            int xor = 0;
            for(int[] e : adj){
                if(Bits.get(i, e[0] - 1) == 1 || Bits.get(i, e[1] - 1) == 1){
                    xor ^= 1;
                }
            }
            if(xor == 0){
                ans++;
            }
        }
        return ans;
    }
}
