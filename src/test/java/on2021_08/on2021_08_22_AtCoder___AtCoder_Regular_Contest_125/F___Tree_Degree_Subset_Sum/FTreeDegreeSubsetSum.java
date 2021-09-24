package on2021_08.on2021_08_22_AtCoder___AtCoder_Regular_Contest_125.F___Tree_Degree_Subset_Sum;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerComparator;
import template.primitve.generated.datastructure.IntegerMinQueue;

import java.util.Arrays;

public class FTreeDegreeSubsetSum {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] deg = new int[n];
        for (int i = 0; i < n - 1; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            deg[a]++;
            deg[b]++;
        }

        int sumDeg = (n - 1) * 2 - n;
        int[] prevMin = new int[sumDeg + 1];
        int[] nextMin = new int[sumDeg + 1];
        int[] prevMax = new int[sumDeg + 1];
        int[] nextMax = new int[sumDeg + 1];
        int inf = (int) 1e9;
        Arrays.fill(prevMin, inf);
        Arrays.fill(prevMax, -inf);
        int[] cnt = new int[sumDeg + 1];
        for (int i = 0; i < n; i++) {
            deg[i]--;
            cnt[deg[i]]++;
        }
        prevMin[0] = 0;
        prevMax[0] = cnt[0];
        IntegerMinQueue minQueue = new IntegerMinQueue(sumDeg + 1, IntegerComparator.NATURE_ORDER);
        IntegerMinQueue maxQueue = new IntegerMinQueue(sumDeg + 1, IntegerComparator.REVERSE_ORDER);
        for (int i = 1; i <= sumDeg; i++) {
            if (cnt[i] == 0) {
                continue;
            }
            int x = cnt[i];
            Arrays.fill(nextMin, inf);
            Arrays.fill(nextMax, -inf);
            //calc min
            for (int j = 0; j < i; j++) {
                minQueue.clear();
                maxQueue.clear();
                int t;
                for (int step = 0; (t = step * i + j) <= sumDeg; step++) {
                    //i - j + prevMin[j]
                    minQueue.addLast(prevMin[t] - step);
                    maxQueue.addLast(prevMax[t] - step);
                    if (minQueue.size() > x + 1) {
                        minQueue.removeFirst();
                        maxQueue.removeFirst();
                    }
                    nextMin[t] = minQueue.min() + step;
                    nextMax[t] = maxQueue.min() + step;
                }
            }
            int[] tmp = prevMin;
            prevMin = nextMin;
            nextMin = tmp;
            tmp = prevMax;
            prevMax = nextMax;
            nextMax = tmp;
        }

        long ans = 0;
        for(int i = 0; i <= sumDeg; i++){
            int l = prevMin[i];
            int r = prevMax[i];
            if(l > r){
                continue;
            }
            ans += r - l + 1;
        }
        out.println(ans);
    }
}
