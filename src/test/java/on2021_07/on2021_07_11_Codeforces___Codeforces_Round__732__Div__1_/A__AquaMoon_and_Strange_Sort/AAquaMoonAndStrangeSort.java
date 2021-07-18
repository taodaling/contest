package on2021_07.on2021_07_11_Codeforces___Codeforces_Round__732__Div__1_.A__AquaMoon_and_Strange_Sort;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerBIT;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AAquaMoonAndStrangeSort {
    int L = (int) 1e5;
    IntegerBIT bit = new IntegerBIT(L);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].index = i;
            items[i].val = in.ri();
        }
        bit.clear();
        for (Item item : items) {
            item.flip += bit.query(item.val + 1, L);
            bit.update(item.val, 1);
        }
        SequenceUtils.reverse(items);
        bit.clear();
        for (Item item : items) {
            item.flip += bit.query(item.val - 1);
            bit.update(item.val, 1);
        }
        for (Item item : items) {
            item.flip &= 1;
        }
        Map<Integer, List<Item>> groupByVal = Arrays.stream(items).collect(Collectors.groupingBy(x -> x.val));
        int[] oe = new int[2];
        for (List<Item> list : groupByVal.values()) {
            list.sort(Comparator.comparingInt(x -> x.index));
            Arrays.fill(oe, 0);
            for (int i = 0; i < list.size(); i++) {
                oe[i & 1] += list.get(i).flip;
            }
            if(oe[0] != oe[1]){
                out.println("NO");
                return;
            }
        }
        out.println("YES");
    }
}

class Item {
    int index;
    int flip;
    int val;
}
