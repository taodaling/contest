package contest;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GroupTheNumbers {
    public String calculate(int[] a) {
        List<Integer> pos = new ArrayList<>();
        List<Integer> neg = new ArrayList<>();
        List<Integer> zero = new ArrayList<>();
        List<Integer> one = new ArrayList<>();
        List<Integer> minusOne = new ArrayList<>();
        for (int x : a) {
            if (x == 1) {
                one.add(x);
            } else if (x == -1) {
                minusOne.add(-x);
            } else if (x > 0) {
                pos.add(x);
            } else if (x == 0) {
                zero.add(x);
            } else {
                neg.add(-x);
            }
        }


        if (neg.size() % 2 == 1 && minusOne.size() > 0) {
            neg.add(1);
            minusOne.remove(minusOne.size() - 1);
        }
        neg.sort(Comparator.naturalOrder());
        while (neg.size() > 1) {
            int tail = neg.remove(neg.size() - 1);
            int tail2 = neg.remove(neg.size() - 1);
            pos.add(tail);
            pos.add(tail2);
        }

        BigInteger sum = BigInteger.valueOf(one.size() + minusOne.size() / 2);
        if (!pos.isEmpty()) {
            BigInteger prod = BigInteger.ONE;
            for (int x : pos) {
                prod = prod.multiply(BigInteger.valueOf(x));
            }
            sum = sum.add(prod);
        }
        if (!neg.isEmpty() && zero.isEmpty()) {
            BigInteger val = BigInteger.valueOf(neg.get(0));
            sum = sum.subtract(val);
        }
        if(minusOne.size() % 2 == 1 && zero.isEmpty()){
            sum = sum.subtract(BigInteger.ONE);
        }

        String ans = sum.toString(10);
        if (ans.length() <= 203) {
            return ans;
        }
        return ans.substring(0, 100) + "..." + ans.substring(ans.length() - 100);
    }


}
