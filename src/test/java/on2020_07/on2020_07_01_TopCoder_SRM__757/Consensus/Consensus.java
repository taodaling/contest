package on2020_07.on2020_07_01_TopCoder_SRM__757.Consensus;



import template.math.DoubleLinearFunction;
import template.math.KahanSummation;
import template.math.LinearFunction;
import template.primitve.generated.datastructure.DoubleList;
import template.utils.Debug;

import java.util.Arrays;

public class Consensus {
    public double expectedTime(int[] opinions) {
        int sum = Arrays.stream(opinions).sum();

        DoubleLinearFunction[] funcs = new DoubleLinearFunction[sum + 1];
        funcs[1] = new DoubleLinearFunction(1, 0);
        funcs[0] = DoubleLinearFunction.ZERO;
        double total = (double) sum * (sum - 1);
        for (int i = 1; i <= sum - 1; i++) {
            double change = (double) i * (sum - i) / total;
            double next = change * (i + 1) / i;
            double prev = change * (i - 1) / i;
            double retain = 1 - prev - next;
            //dp[i] = dp[i - 1] * prev + dp[i] * retain + dp[i + 1] * next + 1
            //dp[i + 1] * next = dp[i](1 - retain) - dp[i - 1] * prev - 1
            funcs[i + 1] = DoubleLinearFunction.mul(funcs[i], 1 - retain);
            funcs[i + 1] = DoubleLinearFunction.subtract(funcs[i + 1], DoubleLinearFunction.mul(funcs[i - 1], prev));
            funcs[i + 1] = DoubleLinearFunction.subtract(funcs[i + 1], DoubleLinearFunction.ONE);
            funcs[i + 1] = DoubleLinearFunction.mul(funcs[i + 1], 1 / next);
        }

        double x = DoubleLinearFunction.inverse(funcs[sum]).apply(0);
        KahanSummation exp = new KahanSummation();
        for (int o : opinions) {
            double need = funcs[o].apply(x);
            double prob = (double)o / sum;
            exp.add(need * prob);
        }
        double ans = exp.sum();
        return ans;
    }
}
