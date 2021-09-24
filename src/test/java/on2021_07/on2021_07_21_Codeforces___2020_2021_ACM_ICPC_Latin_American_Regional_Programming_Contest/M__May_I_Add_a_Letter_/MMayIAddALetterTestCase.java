package on2021_07.on2021_07_21_Codeforces___2020_2021_ACM_ICPC_Latin_American_Regional_Programming_Contest.M__May_I_Add_a_Letter_;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;
import template.string.IntFunctionIntSequenceAdapter;
import template.string.IntSequence;
import template.string.SuffixArrayDC3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MMayIAddALetterTestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            System.out.println("build  testcase " + i);
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
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

    public Test create(int testNum) {
        String charset = "ab";
        String s = random.nextString(charset.toCharArray(), 10);
        String cmd = random.nextString((charset + "-").toCharArray(), 10);
        int[] ans = solve(s, cmd);
        StringBuilder out = new StringBuilder();
        printLine(out, ans);
        return new Test(s + "\n" + cmd, out.toString());
    }

    public int solve(String s) {
        if(s.length() <= 1){
            return 0;
        }
        IntSequence seq = new IntFunctionIntSequenceAdapter(i -> s.charAt(i), 0, s.length() - 1);
        int[] sa = SuffixArrayDC3.suffixArray(seq);
        int[] rank = SuffixArrayDC3.rank(sa);
        int[] lcp = SuffixArrayDC3.lcp(sa, rank, seq);
        int ans = 0;
        for (int i = 1; i < lcp.length; i++) {
            ans += Math.max(lcp[i] - lcp[i - 1], 0);
        }
        ans += lcp[0];
        return ans;
    }

    public int[] solve(String s, String cmd) {
        IntegerArrayList list = new IntegerArrayList();
        list.add(solve(s));
        for (char c : cmd.toCharArray()) {
            if (c == '-') {
                s = s.substring(0, s.length() - 1);
            } else {
                s = s + c;
            }
            list.add(solve(s));
        }
        return list.toArray();
    }
}
