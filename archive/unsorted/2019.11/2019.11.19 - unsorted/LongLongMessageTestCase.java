package contest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import org.apache.commons.lang3.RandomUtils;
import template.RandomWrapper;
import template.SuffixAutomaton;

public class LongLongMessageTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            tests.add(create(i));
        }
        return tests;
    }

    RandomWrapper random = new RandomWrapper(0);
    public Test create(int testNum){
        char[] s = new char[random.nextInt(1, 100000)];
        char[] t = new char[random.nextInt(1, 100000)];
        for(int i = 0; i < s.length; i++){
            s[i] = (char) random.nextInt('a', 'c');
        }
        for(int i = 0; i < t.length; i++){
            t[i] = (char) random.nextInt('a', 'c');
        }

        SuffixAutomaton sa = new SuffixAutomaton('a', 'c');
        for(int i = 0; i < s.length; i++){
            sa.build(s[i]);
        }
        int ans = 0;
        sa.beginMatch();
        for(int i = 0; i < t.length; i++){
            sa.match(t[i]);
            ans = Math.max(ans, sa.lengthMatch());
        }

        StringBuilder builder = new StringBuilder();
        builder.append(String.valueOf(s)).append(' ')
                .append(String.valueOf(t));
        return new Test(builder.toString(), Integer.toString(ans));
    }
}
