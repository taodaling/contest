package on2021_09.on2021_09_23_AtCoder___Sciseed_Programming_Contest_2021_AtCoder_Beginner_Contest_219_.F___Cleaning_Robot0;





import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.string.FastHash;
import template.utils.Debug;

import java.util.*;
import java.util.stream.Collectors;

public class FCleaningRobot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        long k = in.rl();
        Set<Long> set = new HashSet<>(s.length + 1);
        int curX = 0;
        int curY = 0;
        for (char c : s) {
            set.add(DigitUtils.asLong(curX, curY));
            switch (c) {
                case 'L':
                    curY--;
                    break;
                case 'R':
                    curY++;
                    break;
                case 'U':
                    curX++;
                    break;
                case 'D':
                    curX--;
                    break;
            }
        }
        long dx = curX;
        long dy = curY;
        if (dx == 0 && dy == 0) {
            out.println(set.size());
            return;
        }
        Event[] events = set.stream().map(x -> {
            Event e = new Event();
            e.x = DigitUtils.highBit(x);
            e.y = DigitUtils.lowBit(x);
            return e;
        }).toArray(n -> new Event[n]);
        if (dx == 0) {
            //swap
            long tmp = dx;
            dx = dy;
            dy = tmp;
            for (Event e : events) {
                tmp = e.x;
                e.x = e.y;
                e.y = tmp;
            }
        }
        if (dx < 0) {
            dx = -dx;
            for (Event e : events) {
                e.x = -e.x;
            }
        }

        for (Event e : events) {
            //x + dx * k >= 0
            //x + dx * (k - 1) < 0
            // k >= ceil(-x / dx)
            // k < floor(-x, dx) + 1
            long step = DigitUtils.ceilDiv(-e.x, dx);
            e.signX = e.x + dx * step;
            e.signY = e.y + dy * step;
            e.offset = -step;
        }

        debug.debug("dx", dx);
        debug.debug("dy", dy);
        debug.debug("events", events);
        FastHash fh = new FastHash();
        Map<Long, List<Event>> groupByHash = Arrays.stream(events)
                .collect(Collectors.groupingBy(x -> fh.hash(DigitUtils.highBit(x.signX), DigitUtils.lowBit(x.signX), DigitUtils.highBit(x.signY),
                        DigitUtils.lowBit(x.signY))));
        long ans = 0;
        for (List<Event> elist : groupByHash.values()) {
            elist.sort(Comparator.comparingLong(x -> x.offset));
            debug.debug("elist", elist);
            Event last = null;
            for (Event e : elist) {
                if (last != null) {
                    long possible = e.offset - last.offset;
                    possible = Math.min(possible, k + (last.zero() ? 1 : 0));
                    ans += possible;
                }
                last = e;
            }
            ans += k + (last.zero() ? 1 : 0);
        }
        out.println(ans);
    }
    Debug debug = new Debug(false);

}

class Event {
    long x;
    long y;
    long signX;
    long signY;
    long offset;

    public boolean zero() {
        return x == 0 && y == 0;
    }

    @Override
    public String toString() {
        return "Event{" +
                "x=" + x +
                ", y=" + y +
                ", signX=" + signX +
                ", signY=" + signY +
                ", offset=" + offset +
                '}';
    }
}
