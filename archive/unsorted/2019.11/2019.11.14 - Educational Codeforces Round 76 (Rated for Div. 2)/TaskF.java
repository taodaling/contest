package contest;

import template.DigitUtils;
import template.FastInput;
import template.FastOutput;
import template.RandomWrapper;
import template.SimulatedAnnealing;

import java.util.Random;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] a = new int[n];
        for(int i = 0; i < n; i++){
            a[i] = in.readInt();
        }

        RandomWrapper random = new RandomWrapper(new Random());
        DigitUtils.BitOperator bo = new DigitUtils.BitOperator();
        SimulatedAnnealing<Integer> sa = new SimulatedAnnealing<Integer>(1e-5, 1, 0.99, random.getRandom()) {
            @Override
            public Integer next(Integer old, double temperature) {
                int x = old;
               do{
                    int i = random.nextInt(0, 29);
                    x = bo.setBit(x, i, bo.bitAt(x, i) == 0);
                    temperature /= 10;
                }while (temperature >= 1);
                return x;
            }

            @Override
            public double eval(Integer status) {
                double total = 0;
                for(int i = 0; i < n; i++){
                    total += Integer.bitCount(a[i] ^ status);
                }
                double avg = total / n;
                double ans = 0;
                for(int i = 0; i < n; i++){
                    double diff = Integer.bitCount(a[i] ^ status) - avg;
                    ans += diff * diff;
                }
                return -ans;
            }
        };


        long now = System.nanoTime();
        while(System.nanoTime() - now <= 3.5 * 1e9){
            sa.optimize(10000, random.nextInt(0, (1 << 30) - 1));
        }

        if(sa.weightOfBest() > -1e-6){
            out.println(sa.getBest());
        }else{
            out.println(-1);
        }
    }
}
