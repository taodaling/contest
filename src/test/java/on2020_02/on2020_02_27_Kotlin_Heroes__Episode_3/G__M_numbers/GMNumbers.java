package on2020_02.on2020_02_27_Kotlin_Heroes__Episode_3.G__M_numbers;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;

public class GMNumbers {
    public int inf = (int) 1e9 + 1;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int m = in.readInt();
        int k = in.readInt();

        int mm = m;
        for (int i = 2; i < 10; i++) {
            while (mm % i == 0) {
                mm /= i;
            }
        }
        if (mm != 1) {
            out.println(-1);
            return;
        }

        List<Integer> factorList = new ArrayList<>(10000);
        for (int i = 1; i * i <= m; i++) {
            if (m % i != 0) {
                continue;
            }
            factorList.add(i);
            if (m / i != i) {
                factorList.add(m / i);
            }
        }
        int[] factors = factorList.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(factors);
        Map<Integer, Integer> valToIndex = new HashMap<>(factors.length);
        for (int i = 0; i < factors.length; i++) {
            valToIndex.put(factors[i], i);
        }
        int[][] transfer = new int[factors.length][10];
        for(int i = 0; i < factors.length; i++){
            for(int j = 0; j < 10; j++){
                transfer[i][j] = -1;
            }
        }
        for (int i = 0; i < factors.length; i++) {
            for (int j = 1; j < 10; j++) {
                if (factors[i] % j == 0) {
                    transfer[i][j] = valToIndex.get(factors[i] / j);
                }
            }
        }

        List<int[]> dp = new ArrayList<>(100000);
        int[] firstState = new int[factors.length];
        for (int i = 0; i < firstState.length; i++) {
            if (factors[i] < 10) {
                firstState[i] = 1;
            } else {
                firstState[i] = 0;
            }
        }
        dp.add(firstState);
        while (dp.get(dp.size() - 1)[factors.length - 1] < k) {
            k -= dp.get(dp.size() - 1)[factors.length - 1];
            int[] last = dp.get(dp.size() - 1);
            int[] next = new int[factors.length];
            for (int i = 0; i < factors.length; i++) {
                for (int j = 1; j < 10; j++) {
                    if (transfer[i][j] != -1) {
                        next[i] = Math.min(next[i] + last[transfer[i][j]], inf);
                    }
                }
                next[i] = Math.min(next[i], inf);
            }
            dp.add(next);
        }

        int len = dp.size() - 1;
        int state = factors.length - 1;
        while (len > 0) {
            int[] last = dp.get(len - 1);
            for (int i = 1; i < 10; i++) {
                if (transfer[state][i] != -1) {
                    if (last[transfer[state][i]] < k) {
                        k -= last[transfer[state][i]];
                    } else {
                        state = transfer[state][i];
                        len--;
                        out.append(i);
                        break;
                    }
                }
            }
        }

        out.append(factors[state]);
    }
}
