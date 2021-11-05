package on2021_10.on2021_10_30_AtCoder___Exawizards_Programming_Contest_2021_AtCoder_Beginner_Contest_222_.G___222;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.primitve.generated.datastructure.IntegerHashSet;
import template.rand.RandomWrapper;

public class G222TestCase {
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
        int m = random.nextInt(1, 100);
        int ans = solve(m);
        return new Test("1 " + m, "" + ans);
    }

    public int solve(int m){
        HashSet<Long> set = new HashSet();
        long cur = 0;
        for(int i = 1; ; i++){
            cur = cur * 10 + 2;
            cur %= m;
            if(cur == 0){
                return i;
            }
            if(set.contains(cur)){
                return -1;
            }
            set.add(cur);
        }
    }
}
