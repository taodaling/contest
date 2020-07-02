package on2020_07.on2020_07_02_AtCoder___AtCoder_Regular_Contest_078.E___Awkward_Response;



import template.algo.LongBinarySearch;
import template.io.FastInput;
import template.io.FastOutput;

public class EAwkwardResponse {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        this.in = in;

        long limit = (long) 1e15;
        long x = 1;
        while (ask(x) && x < limit) {
            x = x * 10;
        }

        if (x >= limit) {
            //all one
            x = 1;
            while (!ask(x + 1)) {
                x = x * 10;
            }
            answer(x);
            return;
        }

        int len = Long.toString(x).length();
        long min = Math.round(Math.pow(10, len));
        long max = min * 10 - 1;

        LongBinarySearch lbs = new LongBinarySearch() {
            @Override
            public boolean check(long mid) {
                return ask(mid);
            }
        };

        long ans = lbs.binarySearch(min, max);
        ans /= 100;
        answer(ans);
    }

    FastOutput out;
    FastInput in;

    public boolean ask(long x) {
        out.printf("? %d\n", x);
        out.flush();
        return in.readChar() == 'Y';
    }

    public void answer(long x) {
        out.printf("! %d\n", x);
        out.flush();
    }
}
