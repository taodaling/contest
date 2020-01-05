package on2020_01.on2020_01_04_Hello_2020.D__New_Year_and_Conference;



import template.datastructure.Array2DequeAdapter;
import template.datastructure.IntervalBooleanMap;
import template.datastructure.SimplifiedDeque;
import template.io.FastInput;
import template.io.FastOutput;

import java.io.LineNumberReader;
import java.util.Arrays;

public class DNewYearAndConference {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item();
            items[i].a.l = in.readInt();
            items[i].a.r = in.readInt();
            items[i].b.l = in.readInt();
            items[i].b.r = in.readInt();
        }

        boolean valid = check(items);
        for(Item item : items){
            Interval tmp = item.a;
            item.a = item.b;
            item.b = tmp;
        }
        valid = valid && check(items);

        if(valid){
            out.println("YES");
        }else{
            out.println("NO");
        }
    }

    public boolean check(Item[] items) {
        int n = items.length;
        IntervalBooleanMap map = new IntervalBooleanMap();
        Item[] sortedByAL = items.clone();
        Item[] sortedByAR = items.clone();
        Arrays.sort(sortedByAL, (a, b) -> a.a.l - b.a.l);
        Arrays.sort(sortedByAR, (a, b) -> a.a.r - b.a.r);
        SimplifiedDeque<Item> deque = new Array2DequeAdapter<>(sortedByAR);
        for (Item item : sortedByAL) {
            while (!deque.isEmpty() && deque.peekFirst().a.r < item.a.l) {
                Item head = deque.removeFirst();
                map.setTrue(head.b.l, head.b.r);
            }
            if (map.or(item.b.l, item.b.r)) {
                return false;
            }
        }
        return true;
    }
}

class Item {
    Interval a = new Interval();
    Interval b = new Interval();
}

class Interval {
    int l;
    int r;
}
