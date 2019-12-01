package on2019_12.on2019_12_01_Sumitomo_Mitsui_Trust_Bank_Programming_Contest_2019.F___Interval_Running;



import template.io.FastInput;
import template.io.FastOutput;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long t1 = in.readInt();
        long t2 = in.readInt();
        long[] a = new long[]{in.readLong(), in.readLong()};
        long[] b = new long[]{in.readLong(), in.readLong()};

        long aDist = a[0] * t1 + a[1] * t2;
        long bDist = b[0] * t1 + b[1] * t2;
        if (aDist == bDist) {
            out.println("infinity");
            return;
        }
        if (aDist > bDist) {
            {
                long tmp = aDist;
                aDist = bDist;
                bDist = tmp;
            }
            {
                long[] tmp = a;
                a = b;
                b = tmp;
            }
        }

        long maxExcess = a[0] * t1 - b[0] * t1;
        long single = bDist - aDist;
        if (maxExcess == 0) {
            out.println(t1);
            return;
        }

        if (maxExcess < 0) {
            out.println(0);
            return;
        }

        long ans = maxExcess / single * 2 + 1;
        if (maxExcess % single == 0) {
            ans--;
        }
        out.println(ans);
    }
}
