package on2021_03.on2021_03_16_Luogu.P6164__________;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import framework.test.ExternalTestLoader;
import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.rand.RandomWrapper;

public class P6164TestCase {
    @TestCase
    public Collection<Test> createTests() {
        RandomWrapper.INSTANCE.getRandom().setSeed(0);
        List<Test> tests = new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            System.out.println("build  testcase " + i);
//            tests.add(create(i));
//        }
        tests = ExternalTestLoader.load();
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

    RandomWrapper random = new RandomWrapper(1);

    public Test create(int testNum) {
        int q = random.nextInt(1, 10);
        String init = random.nextString('a', 'f', 100);
        int len = init.length();
        String[] type = new String[q];
        String[] args = new String[q];
        for (int i = 0; i < q; i++) {
            String t = random.rangeT( "QUERY");
            type[i] = t;
            if (!t.equals("DEL")) {
                int l = random.nextInt(0, init.length() - 1);
                int r = random.nextInt(0, init.length() -  1);
                if(l > r){
                    l ^= r;
                    r ^= l;
                    l ^= r;
                }

                //args[i] = random.nextString('a', 'b', random.nextInt(1, 10));
                args[i] = random.nextString('a', 'f', random.nextInt(1,5));
                if (t.equals("ADD")) {
                    len += args[i].length();
                }
            } else {
                int del =  random.nextInt(0, Math.min(5, len));
                args[i] = "" + del;
                len -= del;
            }
        }
        int[] ans = solve(init, type, args);
        StringBuilder in = new StringBuilder();
        StringBuilder out = new StringBuilder();
        printLine(in, q);
        printLineObj(in, init);
        for (int i = 0; i < q; i++) {
            printLineObj(in, type[i], args[i]);
        }
        printLine(out, ans);
        return new Test(in.toString(), out.toString());
    }

    public int[] solve(String init, String[] type, String[] args) {
        String now = init;
        IntegerArrayList ans = new IntegerArrayList();
        for (int i = 0; i < type.length; i++) {
            if (type[i].equals("ADD")) {
                now = now + args[i];
            } else if (type[i].equals("DEL")) {
                int del = Integer.parseInt(args[i]);
                now = now.substring(0, now.length() - del);
            } else {
                int cnt = 0;
                for (int j = now.indexOf(args[i]); j != -1; j = now.indexOf(args[i], j + 1)) {
                    cnt++;
                }
                ans.add(cnt);
            }
        }
        return ans.toArray();
    }
}
