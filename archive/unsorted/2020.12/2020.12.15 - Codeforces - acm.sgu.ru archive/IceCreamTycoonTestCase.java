package contest;

import java.util.*;

import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

public class IceCreamTycoonTestCase {
    @TestCase
    public Collection<Test> createTests() {
        List<Test> tests = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
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

    RandomWrapper random = new RandomWrapper(new Random(0));

    public Test create(int testNum) {
        int n = 10;
        String[] type = new String[]{"BUY", "ARRIVE"};
        String[] cmds = new String[n];
        int[] a = new int[n];
        int[] b = new int[n];
        for (int i = 0; i < n; i++) {
            cmds[i] = type[random.nextInt(0, 1)];
            a[i] = random.nextInt(1, 100);
            b[i] = random.nextInt(1, 100);
        }
        String[] ans = solve(cmds, a, b);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < n; i++) {
            printLineObj(in, cmds[i], a[i], b[i]);
        }
        for(String s : ans){
            printLineObj(out, s);
        }
        return new Test(in.toString(), out.toString());
    }

    String[] solve(String[] cmds, int[] a, int[] b) {
        int n = cmds.length;

        List<String> ans = new ArrayList<>();
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i = 0; i < n; i++) {
            if (cmds[i].equals("ARRIVE")) {
                map.put(b[i], map.getOrDefault(b[i], 0) + a[i]);
            } else {
                TreeMap<Integer, Integer> pick = new TreeMap<>();
                int cnt = 0;
                int sum = 0;
                while (!map.isEmpty() && cnt < a[i]) {
                    Map.Entry<Integer, Integer> head = map.pollFirstEntry();
                    int add = Math.min(head.getValue(), a[i] - cnt);
                    cnt += add;
                    sum += head.getKey() * add;
                    if (head.getValue() > add) {
                        map.put(head.getKey(), head.getValue() - add);
                    }
                    pick.put(head.getKey(), add);
                }
                if (cnt < a[i] || sum > b[i]) {
                    ans.add("UNHAPPY");
                    for (Map.Entry<Integer, Integer> entry : pick.entrySet()) {
                        map.put(entry.getKey(), map.getOrDefault(entry.getKey(), 0) + entry.getValue());
                    }
                } else {
                    ans.add("HAPPY");
                }
            }
        }
        return ans.toArray(new String[0]);
    }

}
