package on2020_06.on2020_06_01_TopCoder_SRM__751.AllInclusiveString;



import net.egork.chelper.task.NewTopCoderTest;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class AllInclusiveStringTestCase {
    @TestCase
    public Collection<NewTopCoderTest> createTests() {
        List<NewTopCoderTest> tests = new ArrayList<>();
        for (int i = 1; i <= 1; i++) {
            tests.add(create(i));
        }
        return tests;
    }

    private void printLine(StringBuilder builder, Object... vals) {
        for (Object val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(new Random(0));

    public NewTopCoderTest create(int testNum) {
        List<String> list = new ArrayList<>();
        for (int i = 'a'; i <= 'p'; i++) {
            for (int j = 'a'; j <= 'p'; j++) {
                list.add("" + (char) i + (char) j);
            }
        }
        String[] args = list.toArray(new String[0]);
        return new NewTopCoderTest(args);
    }
}
