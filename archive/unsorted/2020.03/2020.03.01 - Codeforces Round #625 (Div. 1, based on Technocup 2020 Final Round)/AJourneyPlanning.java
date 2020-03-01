package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AJourneyPlanning {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Item[] b = new Item[n];
        for (int i = 0; i < n; i++) {
            b[i] = new Item();
            b[i].val = in.readInt();
            b[i].index = i;
        }
        Map<Integer, List<Item>> group = Arrays.stream(b)
                .collect(Collectors.groupingBy(x -> x.index - x.val));

        long ans = 0;
        for(List<Item> items : group.values()){
            long local = 0;
            for(Item item : items){
                local += item.val;
            }
            ans = Math.max(ans, local);
        }

        out.println(ans);
    }
}

class Item {
    int index;
    int val;
}
