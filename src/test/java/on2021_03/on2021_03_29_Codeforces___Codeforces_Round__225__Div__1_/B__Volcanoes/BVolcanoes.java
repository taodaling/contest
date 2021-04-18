package on2021_03.on2021_03_29_Codeforces___Codeforces_Round__225__Div__1_.B__Volcanoes;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

public class BVolcanoes {
    int inf = (int) 1e9 + 10;

    public int nextActive(TreeMap<Integer, Interval> map, int key) {
        Interval floor = CollectionUtils.floorValue(map, key);
        if (floor != null && floor.r >= key) {
            return key;
        }
        Interval ceil = CollectionUtils.ceilValue(map, key);
        if (ceil != null) {
            return ceil.l;
        }
        return inf;
    }

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        TreeMap<Integer, Interval> map = new TreeMap<>();
        List<int[]> obstacles = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            obstacles.add(new int[]{in.ri(), in.ri()});
        }
        map.put(1, new Interval(1, 1));
        obstacles.sort(Comparator.comparingInt(x -> x[1]));
        Map<Integer, List<int[]>> group = obstacles.stream().collect(Collectors.groupingBy(x -> x[0]));
        TreeSet<Integer> set = new TreeSet<>();
        set.add(1);
        for (Integer key : group.keySet()) {
            set.add(key);
            if (key + 1 <= n) {
                set.add(key + 1);
            }
        }


        for (Integer key : set) {
            if (map.isEmpty()) {
                break;
            }
            List<int[]> obstacle = group.getOrDefault(key, Collections.emptyList());
            TreeMap<Integer, Interval> next = new TreeMap<>();
            int last = 0;
            for (int[] x : obstacle) {
                int cur = x[1];
                int begin = nextActive(map, last + 1);
                if (begin < cur) {
                    next.put(begin, new Interval(begin, cur - 1));
                }
                last = cur;
            }

            int cur = n + 1;
            int begin = nextActive(map, last + 1);
            if (begin < cur) {
                next.put(begin, new Interval(begin, cur - 1));
            }

            map = next;
        }

        boolean possible = nextActive(map, n) == n;
        out.println(possible ? 2 * n - 2 : -1);
    }
}

class Interval {
    int l;
    int r;

    public Interval(int l, int r) {
        this.l = l;
        this.r = r;
    }
}
