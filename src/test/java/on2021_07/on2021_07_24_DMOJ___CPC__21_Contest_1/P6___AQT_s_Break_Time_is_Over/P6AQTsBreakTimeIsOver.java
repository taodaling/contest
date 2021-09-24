package on2021_07.on2021_07_24_DMOJ___CPC__21_Contest_1.P6___AQT_s_Break_Time_is_Over;



import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class P6AQTsBreakTimeIsOver {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[][] data = new int[n][3];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 3; j++) {
                data[i][j] = in.ri();
            }
        }
        Arrays.sort(data, Comparator.comparingInt(x -> x[2]));
        debug.debugMatrix("mat", data);
        int best = data[n - 1][2];
        int[] xyz = new int[]{0, 0, best};
        Plane p = new Plane();
        for (int i = n - 1; i >= 0; i--) {
            p.add(data[i][0], data[i][1]);
            debug.debug("i", i);
            debug.debug("p", p);
            int z = i == 0 ? 0 : data[i - 1][2];
            long minCost = p.minCost();
            int cost = z + DigitUtils.highBit(minCost);
            debug.debug("cost", cost);
            debug.debug("minCost", DigitUtils.highBit(minCost));
            if (best > cost) {
                best = cost;
                xyz[0] = DigitUtils.lowBit(minCost);
                xyz[1] = DigitUtils.highBit(minCost) - DigitUtils.lowBit(minCost);
                xyz[2] = z;
            }
        }
        for (int x : xyz) {
            out.append(x).append(' ');
        }
    }
}

class Plane {
    MultiSet<Long> costSet = new MultiSet<>();
    int[] minCost = new int[(int) 1e6];
    int low = 0;
    public void mod(int x, int y, int v) {
        minCost[x + y] += v;
    }

    public int query(){
        while(minCost[low] == 0){
            low++;
        }
        return low;
    }

    TreeMap<Integer, Integer> pts = new TreeMap<>();

    public long minCost() {
        return costSet.first();
    }

    public long maxCost() {
        return costSet.last();
    }

    private long encode(int x, int y) {
        return DigitUtils.asLong(x + y, x);
    }

    public void delete(int x) {
        Map.Entry<Integer, Integer> cur = pts.floorEntry(x);
        pts.remove(x);
        Map.Entry<Integer, Integer> floor = pts.floorEntry(x);
        Map.Entry<Integer, Integer> ceil = pts.ceilingEntry(x);
        breakout(floor, cur);
        breakout(cur, ceil);
        link(floor, ceil);
        assert costSet.size() == pts.size() + 1 || pts.isEmpty();
    }

    public void link(Map.Entry<Integer, Integer> floor, Map.Entry<Integer, Integer> ceil) {
        if (floor != null) {
            if (ceil == null) {
                costSet.add(encode(floor.getKey(), 0));
            } else {
                costSet.add(encode(floor.getKey(), ceil.getValue()));
            }
        }
        if (ceil != null) {
            if (floor == null) {
                costSet.add(encode(0, ceil.getValue()));
            }
        }
    }

    public void breakout(Map.Entry<Integer, Integer> floor, Map.Entry<Integer, Integer> ceil) {
        if (floor != null) {
            if (ceil == null) {
                costSet.remove(encode(floor.getKey(), 0));
            } else {
                costSet.remove(encode(floor.getKey(), ceil.getValue()));
            }
        }
        if (ceil != null) {
            if (floor == null) {
                costSet.remove(encode(0, ceil.getValue()));
            }
        }
    }

    public void add(int x, int y) {
        Map.Entry<Integer, Integer> ceil = pts.ceilingEntry(x);
        if (ceil != null) {
            if (ceil.getValue() >= y) {
                return;
            } else if (ceil.getKey() == x) {
                delete(x);
                ceil = pts.ceilingEntry(x);
            }
        }
        Map.Entry<Integer, Integer> floor;
        while (true) {
            floor = pts.floorEntry(x);
            if (floor == null || floor.getValue() > y) {
                break;
            }
            delete(floor.getKey());
        }
        pts.put(x, y);
        Map.Entry<Integer, Integer> entry = pts.floorEntry(x);
        breakout(floor, ceil);
        link(floor, entry);
        link(entry, ceil);
        assert costSet.size() == pts.size() + 1 || pts.isEmpty();
    }

    @Override
    public String toString() {
        return pts.toString();
    }
}