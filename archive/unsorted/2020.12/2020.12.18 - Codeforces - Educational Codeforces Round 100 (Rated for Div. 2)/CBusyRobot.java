package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class CBusyRobot {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Command[] commands = new Command[n];
        for (int i = 0; i < n; i++) {
            commands[i] = new Command(in.ri(), in.ri());
        }
        TreeMap<Long, Long> map = new TreeMap<>();
        map.put(0L, 0L);
        long timeNow = 0;
        long x = 0;
        for (Command c : commands) {
            if (c.t < timeNow) {
                continue;
            }
            map.put((long)c.t, x);
            timeNow = c.t + Math.abs(c.x - x);
            x = c.x;
            map.put(timeNow, x);
        }
        int sum = 0;
        for (int i = 0; i < n; i++) {
            long tl = commands[i].t;
            long tr = i == n - 1 ? (long) 1e18 : commands[i + 1].t;
            Map.Entry<Long, Long> ceil = map.ceilingEntry(tr);
            if (ceil == null) {
                ceil = map.lastEntry();
            }
            boolean cross = false;
            while (ceil != null && ceil.getKey() >= tl) {
                Map.Entry<Long, Long> floor = map.floorEntry(ceil.getKey() - 1);
                if (floor == null) {
                    break;
                }
                long[] lr = range(floor.getValue(), ceil.getValue(), floor.getKey(), ceil.getKey(), tl, tr);
                if (lr[0] <= commands[i].x && commands[i].x <= lr[1]) {
                    cross = true;
                }
                ceil = floor;
            }
            if (cross) {
                sum++;
            }
        }
        out.println(sum);
    }

    public long[] range(long a, long b, long at, long bt, long l, long r) {
        long sign = Long.signum(b - a);
        if (at < l) {
            a += sign * (l - at);
            at = l;
        }
        if (bt > r) {
            b -= sign * (bt - r);
            bt = r;
        }
        if (a > b) {
            long tmp = a;
            a = b;
            b = tmp;
        }
        return new long[]{a, b};
    }
}

class Command {
    long t;
    long x;
    boolean success;
    boolean ignored;

    public Command(int t, int x) {
        this.t = t;
        this.x = x;
    }
}
