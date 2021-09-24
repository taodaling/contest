package on2021_09.on2021_09_02_Single_Round_Match_812.GuessForMoney;



import java.util.HashMap;
import java.util.Map;

public class GuessForMoney {
    public double balance(long N) {
        double exp = round(N);
        return exp;
    }

    Map<Long, Double> cache = new HashMap<>();


    public double round(long N) {
        if (N <= 0) {
            return 0;
        }
        Double ans = cache.get(N);
        if (ans == null) {
            long mid = (N + 1) / 2;
            ans = round(mid - 1) * (mid - 1) / N + round(N - mid) * (N - mid) / N + 1;
            cache.put(N, ans);
        }
        return ans;
    }
}
