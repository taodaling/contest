package contest;

import java.util.ArrayList;
import java.util.List;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.SequenceUtils;

public class TaskE {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long k = in.readInt();
        long x = in.readInt();
        long y = in.readInt();

        x = x + y;
        y = x - y - y;

        reach(out, x, y, k);
        SequenceUtils.reverse(list, 0, list.size() - 1);
        out.println(list.size());
        for(long[] xy : list){
            out.append(xy[0]).append(' ').append(xy[1]).append('\n');
        }
    }

    public void reach(FastOutput out, long x, long y, long k) {
        if (x == 0 || y == 0) {
            return;
        }
        outputPoint(out, x, y);
        long dist = Math.max(Math.abs(x), Math.abs(y));
        if (dist < k) {
            long nx;
            long ny;
            if (x >= 0) {
                nx = k;
            } else {
                nx = -k;
            }
            if (y >= 0) {
                ny = y - k;
            } else {
                ny = y + k;
            }
            reach(out, nx, ny, k);
        } else {
            long stepNeed = DigitUtils.floorDiv(dist - 1, k);
            if (stepNeed == 0) {
                reach(out, 0, 0, k);
                return;
            }
            long nx;
            long ny;
            if (y > stepNeed * k) {
                ny = stepNeed * k;
            } else if (y < -stepNeed * k) {
                ny = -stepNeed * k;
            } else {
                ny = y;
            }
            if (x > stepNeed * k) {
                nx = stepNeed * k;
            } else if (x < -stepNeed * k) {
                nx = -stepNeed * k;
            } else {
                nx = x;
            }
            reach(out, nx, ny, k);
        }
    }


    List<long[]> list = new ArrayList<>();

    public void outputPoint(FastOutput out, long x, long y) {
        list.add(SequenceUtils.wrapArray((x + y) / 2, (x - y) / 2));
    }

}
