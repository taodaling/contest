package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class BPolycarpsPhoneBookTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
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

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 5);
        String[] s = new String[n];
        Set<String> set = new HashSet<>();
        for (int i = 0; i < n; i++) {
            do {
                s[i] = random.nextString('0', '9', 9);
            } while (set.contains(s[i]));
            set.add(s[i]);
        }

        String[] ans = solve(s);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();

        printLine(in, n);
        for (int i = 0; i < n; i++) {
            printLine(in, s[i]);
            printLine(out, ans[i]);
        }

        return new Test(in.toString(), out.toString());
    }

    public String[] solve(String[] s) {
        Map<String, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < s.length; i++) {
            for (int l = 0; l < 9; l++) {
                for (int r = l; r < 9; r++) {
                    String sub = s[i].substring(l, r + 1);
                    if (!map.containsKey(sub)) {
                        map.put(sub, new HashSet<>());
                    }
                    map.get(sub).add(i);
                }
            }
        }

        String[] ans = new String[s.length];
        for (Map.Entry<String, Set<Integer>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) {
                int val = entry.getValue().iterator().next();
                if (ans[val] == null) {
                    ans[val] = entry.getKey();
                    continue;
                }
                if (ans[val].length() > entry.getKey().length()) {
                    ans[val] = entry.getKey();
                    continue;
                }
                if(ans[val].length() == entry.getKey().length() &&
                s[val].indexOf(ans[val]) > s[val].indexOf(entry.getKey())){
                    ans[val] = entry.getKey();
                    continue;
                }
            }
        }

        return ans;
    }

}
