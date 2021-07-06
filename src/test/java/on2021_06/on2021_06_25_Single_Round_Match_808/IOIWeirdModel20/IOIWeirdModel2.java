package on2021_06.on2021_06_25_Single_Round_Match_808.IOIWeirdModel20;



import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IOIWeirdModel2 {
    int[] primes = new int[]{2, 3, 7};
    int target = 5;


    public boolean apply(int[] p, int[] c) {
        BigInteger prod = BigInteger.ONE;
        BigInteger target = BigInteger.ONE;
        for (int i = 0; i < c[0] * c[1]; i++) {
            target = target.multiply(BigInteger.valueOf(this.target));
        }
        for (int i = 0; i < 3; i++) {
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
        return prod.equals(target);
    }

    public int pow(int x, int n) {
        int ans = 1;
        for (int i = 0; i < n; i++) {
            ans *= x;
        }
        return ans;
    }

    public int[] program(int L) {
        List<Integer> ans = new ArrayList<>();
        ans.add(11 * 7);
        ans.add(3 * 7);
        ans.add(1);
        ans.add(7);
        for (int i = 20; i >= 1; i--) {
            if (i < 20) {
                ans.add(pow(3, i));
            } else {
                ans.add(3 * 13);
            }
            ans.add((1 << i) * 11);
        }
        ans.add(pow(3, 19));
        ans.add(13);
        ans.add(10);
        ans.add(3);
        ans.add(1);
        ans.add(11);

        int[] res = ans.stream().mapToInt(Integer::intValue).toArray();
        int[][] testCase = new int[][]{
                {1, 2, 1},
                {0, 0, 0},
                {1, 0, 1},


        };
        for (int[] t : testCase) {
            if (!apply(res, t)) {
                throw new RuntimeException();
            }
        }

        return res;
    }
}
