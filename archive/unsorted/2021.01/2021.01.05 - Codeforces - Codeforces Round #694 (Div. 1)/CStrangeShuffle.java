package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.rand.HashData;
import template.rand.IntRangeHash;
import template.rand.RandomWrapper;
import template.rand.RangeHash;

import java.util.Arrays;

public class CStrangeShuffle {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        test();
        this.in = in;
        this.out = out;
        n = in.ri();
        long k = in.rl();
        query(0);
        int index;
        long ans;

        while (true) {
            index = RandomWrapper.INSTANCE.nextInt(n);
            ans = query(index);
            if (ans != k) {
                break;
            }
        }
        if (time < 450) {
            while (ans < k) {
                index = (index + 1) % n;
                ans = query(index);
            }
            while (ans > k) {
                index = (index - 1 + n) % n;
                ans = query(index);
            }
            answer(index);
            return;
        }

        //binary search
        int l = index;
        int r = index;
        if (ans < k) {
            while (query(r) < k) {
                int step = Math.min(n / 4, time / 4);
                r += step;
            }
        } else {
            while (query(l) > k) {
                int step = Math.min(n / 4, time / 4);
                l -= step;
            }
        }
        while (l < r) {
            int mid = DigitUtils.floorDiv(l + r, 2);
            if (query(mid) < k) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }

        answer(r);
    }

    int n;
    FastInput in;
    FastOutput out;
    int time;

    public void answer(int i) {
        i = DigitUtils.mod(i, n);
        out.append("! ").println(i + 1).flush();
    }

    public long query(int i) {
        i = DigitUtils.mod(i, n);
        time++;
        out.append("? ").append(i + 1).println().flush();
        return in.rl();
    }

    private void test() {
        int k = 11;
        int round = 20;
        int n = 10;
        int[] cards = new int[n];
        Arrays.fill(cards, k);
        for (int i = 0; i < round; i++) {
            System.err.println(Arrays.toString(cards));
            int[] next = new int[n];
            for (int j = 0; j < n; j++) {
                if (j == 0) {
                    next[(j + 1) % n] += cards[j];
                    continue;
                }
                int half = cards[j] / 2;
                next[(j + n - 1) % n] += half;
                next[(j + 1) % n] += cards[j] - half;
            }
            cards = next;
        }
    }

}
