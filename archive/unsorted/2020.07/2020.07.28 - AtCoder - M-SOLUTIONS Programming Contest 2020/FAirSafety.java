package contest;

import dp.Lis;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FAirSafety {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        List<Point>[] upDown = new List[]{new ArrayList(n), new ArrayList(n)};
        List<Point>[] leftRight = new List[]{new ArrayList(n), new ArrayList(n)};
        for (int i = 0; i < n; i++) {
            Point pt = new Point();
            pt.x = in.readInt() * 10;
            pt.y = in.readInt() * 10;
            switch (in.readChar()) {
                case 'U':
                    upDown[0].add(pt);
                    break;
                case 'D':
                    upDown[1].add(pt);
                    break;
                case 'L':
                    leftRight[0].add(pt);
                    break;
                default:
                    leftRight[1].add(pt);
            }
        }

        Comparator<Point> sortByX = (a, b) -> Integer.compare(a.x, b.x);
        for (int i = 0; i < 2; i++) {
            upDown[i].sort(sortByX);
            leftRight[i].sort(sortByX);
        }

        optimizeHorizontal(cast(leftRight[1], x -> x.y, x -> x.x),
                cast(leftRight[0], x -> x.y, x -> x.x));
        optimizeHorizontal(cast(upDown[0], x -> x.x, x -> x.y),
                cast(upDown[1], x -> x.x, x -> x.y));


        //inc
        Map<Integer, List<Point>>[] uDGroupByInc = new Map[2];
        Map<Integer, List<Point>>[] lRGGroupByInc = new Map[2];
        for (int i = 0; i < 2; i++) {
            uDGroupByInc[i] = upDown[i].stream().collect(Collectors.groupingBy(x -> x.y - x.x));
            lRGGroupByInc[i] = leftRight[i].stream().collect(Collectors.groupingBy(x -> x.y - x.x));
        }

        //dec
        Map<Integer, List<Point>>[] uDGroupByDec = new Map[2];
        Map<Integer, List<Point>>[] lRGGroupByDec = new Map[2];
        for (int i = 0; i < 2; i++) {
            uDGroupByDec[i] = upDown[i].stream().collect(Collectors.groupingBy(x -> x.y + x.x));
            lRGGroupByDec[i] = leftRight[i].stream().collect(Collectors.groupingBy(x -> x.y + x.x));
        }

        optimize(uDGroupByInc[0], lRGGroupByInc[0]);
        optimize(lRGGroupByInc[1], uDGroupByInc[1]);

        optimize(uDGroupByDec[1], lRGGroupByDec[0]);
        optimize(lRGGroupByDec[1], uDGroupByDec[0]);

        if (ans == 1e18) {
            out.println("SAFE");
            return;
        }

        out.println(ans);
    }

    long ans = (long) 1e18;

    public Map<Integer, List<Integer>> cast(List<Point> a, Function<Point, Integer> key,
                                            Function<Point, Integer> value) {
        Map<Integer, List<Integer>> map = new HashMap<>(a.size());
        for (Point pt : a) {
            map.computeIfAbsent(key.apply(pt), x -> new ArrayList<>())
                    .add(value.apply(pt));
        }
        return map;
    }

    public void optimizeHorizontal(Map<Integer, List<Integer>> a, Map<Integer, List<Integer>> b) {
        for (Integer key : a.keySet()) {
            List<Integer> l1 = a.get(key);
            List<Integer> l2 = b.getOrDefault(key, Collections.emptyList());
            l1.sort(Comparator.naturalOrder());
            l2.sort(Comparator.naturalOrder());

            int l2Idx = 0;
            for (int x : l1) {
                while (l2Idx < l2.size() && l2.get(l2Idx) < x) {
                    l2Idx++;
                }
                if (l2Idx >= l2.size()) {
                    break;
                }
                ans = Math.min(ans, (l2.get(l2Idx) - x) / 2);
            }
        }
    }

    public void optimize(Map<Integer, List<Point>> a, Map<Integer, List<Point>> b) {
        for (Integer key : a.keySet()) {
            List<Point> l1 = a.get(key);
            List<Point> l2 = b.getOrDefault(key, Collections.emptyList());

            int l2Idx = 0;
            for (Point pt : l1) {
                while (l2Idx < l2.size() && l2.get(l2Idx).x < pt.x) {
                    l2Idx++;
                }
                if (l2Idx >= l2.size()) {
                    break;
                }
                ans = Math.min(ans, l2.get(l2Idx).x - pt.x);
            }
        }
    }
}

class Point {
    int x;
    int y;

}
