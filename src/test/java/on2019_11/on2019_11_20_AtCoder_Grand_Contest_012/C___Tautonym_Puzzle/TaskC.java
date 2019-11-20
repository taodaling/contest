package on2019_11.on2019_11_20_AtCoder_Grand_Contest_012.C___Tautonym_Puzzle;



import java.util.Random;

import template.Buffer;
import template.FastInput;
import template.FastOutput;
import template.RandomWrapper;
import template.SimulatedAnnealing;

public class TaskC {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        RandomWrapper rw = new RandomWrapper(new Random(0));
        SimulatedAnnealing<int[]> sa = new SimulatedAnnealing<int[]>(1e-5, 1e-5, 0.99, rw.getRandom()) {
            @Override
            public int[] next(int[] old, double temperature) {
                int[] ans = buffer.alloc();
                System.arraycopy(old, 0, ans, 0, 100);
                int change = Math.min((int) (temperature * 10), 50);
                change = Math.max(1, change);

                for (int i = 0; i < 100; i++) {
                    if (rw.nextInt(0, 1) == 0) {
                        continue;
                    }
                    ans[i] += rw.nextInt(1, change);
                    ans[i] = Math.max(0, ans[i]);
                    ans[i] = Math.min(50, ans[i]);
                }
                return ans;
            }

            Buffer<int[]> buffer = new Buffer<>(() -> new int[100]);

            @Override
            public void abandon(int[] old) {
                buffer.release(old);
            }

            @Override
            public double eval(int[] status) {
                long cnt = 0;
                for (int i = 0; i < 100; i++) {
                    if (status[i] == 0) {
                        continue;
                    }
                    cnt += (1L << (status[i] - 1)) - 1;
                }
                int sum = 0;
                for (int i = 0; i < 100; i++) {
                    sum += status[i];
                }
                if (sum > 200) {
                    return sum * -1e18;
                }
                return -Math.abs(cnt - n);
            }
        };

        long now = System.nanoTime();
        while (System.nanoTime() - now <= 1.8e9) {
            int[] data = new int[100];
            sa.optimize(100, data);
        }

        int[] ans = sa.getBest();
        if (Math.abs(sa.weightOfBest()) > 1e-5) {
            throw new IllegalStateException();
        }

        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum += ans[i];
        }
        out.println(sum);
        for (int i = 0; i < 100; i++) {
            if (ans[i] == 0) {
                continue;
            }
            for (int j = 0; j < ans[i]; j++) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
