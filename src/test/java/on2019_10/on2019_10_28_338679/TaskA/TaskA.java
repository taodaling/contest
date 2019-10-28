package on2019_10.on2019_10_28_338679.TaskA;



import template.DigitUtils;
import template.FastInput;
import template.FastOutput;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        boolean[] forOne = new boolean[11];
        long val = 1;
        for (int i = 0; i <= 10; i++) {
            forOne[i] = ask(in, out, val);
            val *= 10;
        }
        boolean[] forTwo = new boolean[11];
        val = 2;
        for (int i = 0; i <= 10; i++) {
            forTwo[i] = ask(in, out, val);
            val *= 10;
        }

        int digit = 0;
        boolean forOneMerge = true;
        for (int i = 0; i <= 10; i++) {
            forOneMerge = forOneMerge && forOne[i];
        }
        if (forOneMerge) {
            for (int j = 0; j <= 10; j++) {
                if (forTwo[j]) {
                    digit = j + 1;
                    break;
                }
            }
        }else{
            for (int j = 0; j <= 10; j++) {
                if (!forOne[j]) {
                    digit = j;
                    break;
                }
            }
        }

        long l = DigitUtils.setDigitOn(0, digit - 1, 1);
        long r = DigitUtils.setDigitOn(0, digit, 1) - 1;
        while (l < r) {
            long m = (l + r) >> 1;
            if (ask(in, out, m * 10)) {
                r = m;
            } else {
                l = m + 1;
            }
        }

        out.append("! ").append(l);
        out.flush();
    }

    public boolean ask(FastInput in, FastOutput out, Object o) {
        out.append("? ").println(o);
        out.flush();
        return in.readString().equals("Y");
    }
}
