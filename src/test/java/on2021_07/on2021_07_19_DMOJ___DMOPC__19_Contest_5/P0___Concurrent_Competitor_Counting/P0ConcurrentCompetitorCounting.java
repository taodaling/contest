package on2021_07.on2021_07_19_DMOJ___DMOPC__19_Contest_5.P0___Concurrent_Competitor_Counting;



import template.io.FastInput;
import template.io.FastOutput;

public class P0ConcurrentCompetitorCounting {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int cut = in.ri();
        for (int i = 0; i < n; i++) {
            String name = in.rs();
            if (in.ri() > cut) {
                out.append(name).println(" will advance");
            } else {
                out.append(name).println(" will not advance");
            }
        }
    }
}
