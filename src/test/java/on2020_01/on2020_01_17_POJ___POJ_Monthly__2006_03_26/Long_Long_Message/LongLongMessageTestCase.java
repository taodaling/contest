package on2020_01.on2020_01_17_POJ___POJ_Monthly__2006_03_26.Long_Long_Message;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;
import template.string.SuffixAutomaton;

public class LongLongMessageTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object...vals){
        for(Object val : vals){
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));
    public Test create(int testNum){
        char[] a = random.nextString('a', 'b', 300).toCharArray();
        char[] b = random.nextString('a', 'b', 300).toCharArray();

        String in = new String(a) + "\n" + new String(b);
        int ans = solve(a, b);
        return new Test(in, "" + ans);
    }

    public int solve(char[] a, char[] b){
        SuffixAutomaton sa = new SuffixAutomaton('a', 'z');
        for(char c : a){
            sa.build(c);
        }
        sa.beginMatch();
        int ans = 0;
        for(char c : b){
            sa.match(c);
            ans = Math.max(ans, sa.lengthMatch());
        }
        return ans;
    }
}
