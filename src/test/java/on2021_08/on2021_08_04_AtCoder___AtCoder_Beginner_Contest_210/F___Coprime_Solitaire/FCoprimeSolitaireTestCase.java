package on2021_08.on2021_08_04_AtCoder___AtCoder_Beginner_Contest_210.F___Coprime_Solitaire;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.binary.Bits;
import template.math.GCDs;
import template.rand.RandomWrapper;

public class FCoprimeSolitaireTestCase {
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
        int L = (int)20;
        int n = random.nextInt(1, 2);
        int[][] pts = new int[n][2];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < 2; j++){
                pts[i][j] = random.nextInt(1, L);
            }
        }
        String ans = solve(pts) ? "Yes" : "No";
        StringBuilder in = new StringBuilder();
        printLine(in,  n);
        for(int[] pt : pts){
            printLine(in, pt);
        }
        return new Test(in.toString(), ans);
    }

    public boolean solve(int[][] pts){
        int n = pts.length;
        for(int i = 0; i < 1 << n;  i++){
            int[] v =  new int[n];
            for(int j = 0; j < n; j++){
                v[j] = pts[j][Bits.get(i, j)];
            }
            boolean ok = true;
            for(int a = 0; a < n && ok; a++){
                for(int b = a + 1; b < n && ok; b++){
                    if (GCDs.gcd(v[a], v[b]) > 1) {
                        ok = false;
                    }
                }
            }
            if(ok){
                return true;
            }
        }
        return false;
    }
}
