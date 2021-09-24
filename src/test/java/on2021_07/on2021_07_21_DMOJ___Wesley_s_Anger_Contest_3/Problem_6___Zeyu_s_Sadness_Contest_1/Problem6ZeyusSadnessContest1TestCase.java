package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_6___Zeyu_s_Sadness_Contest_1;



import net.egork.chelper.task.Test;
import net.egork.chelper.tester.TestCase;
import template.rand.RandomWrapper;

import java.util.*;

public class Problem6ZeyusSadnessContest1TestCase {
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

    private static void printLine(StringBuilder builder, int... vals) {
        for (int val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private static void printLine(StringBuilder builder, long... vals) {
        for (long val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    private static <T> void printLineObj(StringBuilder builder, T... vals) {
        for (T val : vals) {
            builder.append(val).append(' ');
        }
        builder.append('\n');
    }

    RandomWrapper random = new RandomWrapper(0);

    public Test create(int testNum) {
        int n = random.nextInt(1, 10);
        int m = random.nextInt(1, 3);
        Set<Integer> set = new HashSet<>();
        int[][][] state = new int[n][m][];
        List<Event> events = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                //solve or not
                if (random.nextInt(0, 1) == 1) {
                    int time = random.nextInt(1, (int) 1e7);
                    while (set.contains(time)) {
                        time = random.nextInt(1, (int) 1e7);
                    }
                    set.add(time);
                    state[i][j] = new int[]{time, random.nextInt(0, (int) 1e7 - 1)};
                    events.add(new Event(i, j, state[i][j][0], state[i][j][1]));
                }
            }
        }

        String in = handleEvent(n, m, events);
        return new Test(in, null);
    }

    public static String handleEvent(int n, int m, List<Event> events){
        events.sort(Comparator.comparingInt(x -> x.t));
        Contestant[] cs = new Contestant[n];
        for (int i = 0; i < n; i++) {
            cs[i] = new Contestant();
            cs[i].id = i;
        }
        Contestant[] sorted = cs.clone();
        StringBuilder in = new StringBuilder();
        printLine(in, n, m, events.size());
        for (Event e : events) {
            cs[e.c].solved++;
            cs[e.c].p += e.t + e.cost * 10L;
            int rank = recalc(sorted, cs[e.c]);
            printLine(in, e.c + 1, e.p + 1, rank + 1);
        }
        return in.toString();
    }

    public static int recalc(Contestant[] sorted, Contestant target) {
        Arrays.sort(sorted, Comparator.<Contestant>comparingInt(x -> -x.solved).thenComparingLong(x -> x.p)
                .thenComparingInt(x -> x.id));
        int rank = -1;
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i] == target) {
                rank = i;
            }
        }
        return rank;
    }

    public static class Contestant {
        int solved;
        long p;
        int id;
    }

    public static class Event {
        int c;
        int p;
        int t;
        int cost;

        public Event(int c, int p, int t, int cost) {
            this.c = c;
            this.p = p;
            this.t = t;
            this.cost = cost;
        }
    }
}
