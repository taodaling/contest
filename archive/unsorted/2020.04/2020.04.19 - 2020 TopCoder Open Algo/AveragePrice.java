package contest;

import template.primitve.generated.datastructure.IntegerList;

import java.util.HashSet;
import java.util.Set;

public class AveragePrice {
    public double nonDuplicatedAverage(int[] prices) {
        IntegerList list = new IntegerList();
        Set<Integer> set = new HashSet<>();
        for(int p : prices){
            set.add(p);
        }
        int sum = 0;
        for(int v : set){
            sum += v;
        }
        return (double)sum / set.size();
    }
}
