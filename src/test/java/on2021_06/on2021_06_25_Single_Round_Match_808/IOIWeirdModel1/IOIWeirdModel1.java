package on2021_06.on2021_06_25_Single_Round_Match_808.IOIWeirdModel1;



import template.math.BigInt10;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOIWeirdModel1 {
    int[] primes = new int[]{2, 3, 5, 7, 11};
    int target = 47;


    public boolean apply(int[] p, int[] c) {
        BigInteger prod = BigInteger.ONE;
        int[] median = c.clone();
        Arrays.sort(median);
        long target = 1;
        for (int i = 0; i < median[2]; i++) {
            target *= this.target;
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < c[i]; j++) {
                prod = prod.multiply(BigInteger.valueOf(primes[i]));
            }
        }
        while (true) {
            boolean find = false;
            for (int i = 0; i < p.length; i += 2) {
                if (prod.multiply(BigInteger.valueOf(p[i])).mod(BigInteger.valueOf(p[i + 1])).equals(BigInteger.ZERO)) {
                    find = true;
                    prod = prod.multiply(BigInteger.valueOf(p[i])).divide(BigInteger.valueOf(p[i + 1]));
                    break;
                }
            }
            if (!find) {
                break;
            }
        }
        return prod.equals(BigInteger.valueOf(target));
    }

    public int[] program(int L) {
        int prod = 1;
        for (int i = 0; i < 5; i++) {
            prod *= primes[i];
        }
        List<Integer> ans = new ArrayList<>();
        ans.add(target);
        ans.add(prod);
        for (int r = 0; r < 5; r++) {
            ans.add(target);
            ans.add(prod / primes[r]);
        }
        for (int r = 0; r < 5; r++) {
            for (int c = r + 1; c < 5; c++) {
                if (r == c) {
                    continue;
                }
                ans.add(target);
                ans.add(prod / primes[r] / primes[c]);
            }
        }
        for (int p : primes) {
            ans.add(1);
            ans.add(p);
        }
        int[] res = ans.stream().mapToInt(Integer::intValue).toArray();
//        int[][] testCase = new int[][]{
//                {1, 2, 1, 0, 2},
//                {0, 0, 0, 0, 0},
//                {1, 0, 1, 0, 2},
//
//
//        };
//        for (int[] t : testCase) {
//            if (!apply(res, t)) {
//                throw new RuntimeException();
//            }
//        }
        return res;
    }
}
