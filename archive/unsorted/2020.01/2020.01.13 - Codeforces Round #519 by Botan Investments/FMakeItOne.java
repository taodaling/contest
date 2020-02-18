package contest;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.IntegerIterator;
import template.primitve.generated.IntegerList;
import template.primitve.generated.IntegerMultiWayStack;

import java.util.Arrays;

public class FMakeItOne {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int limit = 300000;
        int[] cnts = new int[limit + 1];
        for (int i = 0; i < n; i++) {
            cnts[in.readInt()]++;
        }
        if (cnts[1] > 0) {
            out.println(1);
            return;
        }
        IntegerMultiWayStack allPrimeFactors = new IntegerMultiWayStack(limit + 1, limit * 7);
        boolean[] isComp = new boolean[limit + 1];
        for (int i = 2; i <= limit; i++) {
            if (isComp[i]) {
                continue;
            }
            for (int j = i; j <= limit; j += i) {
                isComp[j] = true;
                allPrimeFactors.addLast(j, i);
            }
        }

        int[] hasGcdGreaterThan1 = new int[limit + 1];
        int[] multiples = new int[limit + 1];
        for (int i = 1; i <= limit; i++) {
            for (int j = i; j <= limit; j += i) {
                multiples[i] += cnts[j];
            }
        }

        IntegerList allFactors = new IntegerList(10);
        for (int i = 1; i <= limit; i++) {
            allFactors.clear();
            for (IntegerIterator iterator = allPrimeFactors.iterator(i); iterator.hasNext(); ) {
                allFactors.add(iterator.next());
            }
            hasGcdGreaterThan1[i] = dfs(multiples, allFactors.getData(), 1, 0, allFactors.size() - 1);
        }

        int[] dp = new int[limit + 1];
        Arrays.fill(dp, (int) 1e8);
        for (int i = limit; i >= 1; i--) {
            if (cnts[i] > 0) {
                dp[i] = Math.min(dp[i], 1);
            }
            for (int j = 2; j * i <= limit; j++) {
                if(hasGcdGreaterThan1[j] == n){
                    continue;
                }
                dp[i] = Math.min(dp[i], dp[j * i] + 1);
            }
        }

        if(dp[1] == (int)1e8){
            out.println(-1);
            return;
        }

        out.println(dp[1]);
    }

    public int dfs(int[] multiples, int[] allFactors, int prod, int factors, int i) {
        if (i == -1) {
            if (factors == 0) {
                return 0;
            }
            int ans = multiples[prod];
            if (factors % 2 == 0) {
                ans = -ans;
            }
            return ans;
        }
        return dfs(multiples, allFactors, prod * allFactors[i], factors + 1, i - 1) +
                dfs(multiples, allFactors, prod, factors, i - 1);
    }
}
