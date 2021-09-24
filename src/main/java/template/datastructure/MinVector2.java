package template.datastructure;

import java.util.Map;
import java.util.TreeMap;

/**
 * Given N^2 vector V, find a vector (x, y) with minimum x + y and for all vector v in V, v[0] <= x or v[1] <= y
 */
public class MinVector2 {
    MultiSet<Long> costSet = new MultiSet<>();
    TreeMap<Long, Long> pts = new TreeMap<>();

    /**
     * Find min x + y
     */
    public long minCost() {
        return costSet.first();
    }

    private void delete(long x) {
        Map.Entry<Long, Long> cur = pts.floorEntry(x);
        pts.remove(x);
        Map.Entry<Long, Long> floor = pts.floorEntry(x);
        Map.Entry<Long, Long> ceil = pts.ceilingEntry(x);
        breakout(floor, cur);
        breakout(cur, ceil);
        link(floor, ceil);
        assert costSet.size() == pts.size() + 1 || pts.isEmpty();
    }

    private void link(Map.Entry<Long, Long> floor, Map.Entry<Long, Long> ceil) {
        if (floor != null) {
            if (ceil == null) {
                costSet.add(floor.getKey() + 0);
            } else {
                costSet.add(floor.getKey() + ceil.getValue());
            }
        }
        if (ceil != null) {
            if (floor == null) {
                costSet.add(0 + ceil.getValue());
            }
        }
    }

    private void breakout(Map.Entry<Long, Long> floor, Map.Entry<Long, Long> ceil) {
        if (floor != null) {
            if (ceil == null) {
                costSet.remove(floor.getKey() + 0);
            } else {
                costSet.remove(floor.getKey() + ceil.getValue());
            }
        }
        if (ceil != null) {
            if (floor == null) {
                costSet.remove(0 + ceil.getValue());
            }
        }
    }

    /**
     * Add a new vector to V
     * @param x
     * @param y
     */
    public void add(long x, long y) {
        Map.Entry<Long, Long> ceil = pts.ceilingEntry(x);
        if (ceil != null) {
            if (ceil.getValue() >= y) {
                return;
            } else if (ceil.getKey() == x) {
                delete(x);
                ceil = pts.ceilingEntry(x);
            }
        }
        Map.Entry<Long, Long> floor;
        while (true) {
            floor = pts.floorEntry(x);
            if (floor == null || floor.getValue() > y) {
                break;
            }
            delete(floor.getKey());
        }
        pts.put(x, y);
        Map.Entry<Long, Long> entry = pts.floorEntry(x);
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
