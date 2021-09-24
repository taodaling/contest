package on2021_09.on2021_09_23_AtCoder___Sciseed_Programming_Contest_2021_AtCoder_Beginner_Contest_219_.F___Cleaning_Robot1;



import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.math.DigitUtils;
import template.rand.RandomWrapper;

public class FCleaningRobotTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
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
        int n = random.nextInt(1, 5);
        String s = random.nextString("LRUD".toCharArray(), n);
        int k = random.nextInt(1, 5);
        int ans = solve(s, k);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLineObj(in, s);
        printLine(in, k);
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int solve(String s, int k){
        int curX = 0;
        int curY = 0;
        Set<Long> set = new HashSet<>();
        for(int i = 0; i < k; i++) {
            for (char c : s.toCharArray()) {
                set.add(DigitUtils.asLong(curX, curY));
                switch (c) {
                    case 'L':
                        curY--;
                        break;
                    case 'R':
                        curY++;
                        break;
                    case 'U':
                        curX++;
                        break;
                    case 'D':
                        curX--;
                        break;
                }
            }
        }
        set.add(DigitUtils.asLong(curX, curY));
        return set.size();
    }
}
