package contest;

import template.math.EulerSieve;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;
import template.utils.SequenceUtils;

public class EllysThreePrimes {
    //Debug debug = new Debug(true);
    public int[] getPrimes(int[] sums) {

        IntegerArrayList list = new IntegerArrayList(10000);
        for (int i = (int) 1e4; i < 1e5; i++) {
            if (sieve.isPrime(i)) {
                list.add(i);
            }
        }

        SequenceUtils.reverse(sums);
        int[] data = list.toArray();
        for (int i = 0; i < data.length; i++) {
            for (int j = i + 1; j < data.length; j++) {
                valid = true;
                int k = get(sums, sums.length - 1, data[i], data[j]);
//                if(data[i] == 20533 && data[j] == 44987){
//                    k = get(sums, sums.length - 1, data[i], data[j]);
//                    debug.debug("k", k);
//                }
                if (valid && k > data[j] && sieve.isPrime(k)) {
                    return new int[]{data[i], data[j], k};
                }
            }
        }

        return new int[0];
    }

    boolean valid;

    public int get(int[] sum, int i, int a, int b) {
        if (i < 0) {
            return 0;
        }
        int d = a % 10 + b % 10;
        int x = sum[i] - d;
        if (x < 0 || x > 9 || (i == 0 && x == 0)) {
            valid = false;
        }
        return get(sum, i - 1, a / 10, b / 10) * 10 + x;
    }

    EulerSieve sieve = new EulerSieve((int) 1e5);

}
