package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.*;
import java.util.stream.Collectors;

public class Dumbbells {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].a = in.ri();
            items[i].b = in.ri();
        }
        Map<Integer, List<Item>> itemByA = Arrays.stream(items)
                .collect(Collectors.groupingBy(x -> x.a));
        List<List<Item>> values = new ArrayList<>(itemByA.values());
        values.sort(Comparator.comparingInt(x -> x.size()));
        if (values.size() < k) {
            out.println(0);
            out.println(0);
            return;
        }
        int setNum = values.get(values.size() - k).size();
        values = values.stream().filter(x -> x.size() >= setNum).collect(Collectors.toList());
        List<Integer> costs = new ArrayList<>(n);
        for (List<Item> list : values) {
            list.sort(Comparator.comparingInt(x -> x.b));
            int sum = 0;
            int m = list.size();
            for (int i = 1; i <= setNum; i++) {
                sum += list.get(m - i).b;
            }
            costs.add(sum);
        }
        costs.sort(Comparator.naturalOrder());
        int ans = 0;
        for (int i = 1, size = costs.size(); i <= k; i++) {
            ans += costs.get(size - i);
        }
        out.println(setNum);
        out.println(ans);
    }
}

class Item {
    int a;
    int b;
}
