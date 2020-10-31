package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.TreeMap;
import java.util.TreeSet;

public class BShurikens {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        TreeSet<Integer> put = new TreeSet<>();
        TreeSet<Integer> remove = new TreeSet<>();
        int[] invIndex = new int[n + 1];
        TreeMap<Integer, Integer> alloc = new TreeMap<>();

        for (int i = 0; i < 2 * n; i++) {
            char c = in.readChar();
            if (c == '+') {
                put.add(i);
            } else {
                int x = in.readInt();
                invIndex[x] = i;
                remove.add(i);
            }
        }

        for (int i = 1; i <= n; i++) {
            int index = invIndex[i];
            remove.remove(index);
            Integer floorRemove = remove.floor(index);
            Integer floorPut = put.floor(index);
            if (floorPut == null || floorRemove != null && floorRemove > floorPut) {
                out.println("NO");
                return;
            }
            put.remove(floorPut);
            alloc.put(floorPut, i);
        }
        out.println("YES");
        for(Integer val : alloc.values()){
            out.append(val).append(' ');
        }
    }
}

