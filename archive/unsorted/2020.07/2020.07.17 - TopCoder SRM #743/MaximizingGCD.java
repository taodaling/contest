package contest;

import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.util.HashMap;
import java.util.Map;

public class MaximizingGCD {
    public int maximumGCDPairing(int[] A) {
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 1; i < A.length; i++) {
            int sum = A[0] + A[i];
            Factorization.factorizeNumber(sum, list);
        }
        list.unique();
        list.reverse();
        for (int i = 0; i < list.size(); i++) {
            if (check(A, list.get(i))) {
                return list.get(i);
            }
        }
        return 1;
    }

    public boolean check(int[] A, int g) {
        Map<Integer, Integer> cntMap = new HashMap<>(A.length);
        for (int i = 0; i < A.length; i++) {
            cntMap.put(A[i] % g, cntMap.getOrDefault(A[i] % g, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : cntMap.entrySet()) {
            if (entry.getKey().intValue() == 0 && entry.getValue() % 2 == 1) {
                return false;
            }
            if (entry.getKey().intValue() != 0 && entry.getValue().intValue() != cntMap.getOrDefault(g - entry.getKey(), 0)) {
                return false;
            }
        }
        return true;
    }
}
