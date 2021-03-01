package contest;

import template.algo.BinarySearch;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerHashMap;
import template.rand.Randomized;

import java.util.Arrays;
import java.util.TreeSet;

public class DCaseOfATopSecret {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int[] x = in.ri(n);
        int[] origin = x.clone();
        Randomized.shuffle(x);
        Arrays.sort(x);
        IntegerHashMap toValue = new IntegerHashMap(n, false);
        IntegerHashMap toIndex = new IntegerHashMap(n, false);
        for(int i = 0; i < n; i++){
            toValue.put(i, origin[i]);
            toIndex.put(origin[i], i);
        }
        for (int i = 0; i < m; i++) {
            int id = BinarySearch.lowerBound(x, 0, n - 1, origin[in.ri() - 1]);
            boolean bot = true;
            int len = in.ri();
            int round = 0;
            while (round < 2) {
                int cur = id;
                if (bot) {
                    int right = BinarySearch.upperBound(x, 0, n - 1, x[id] + len) - 1;
                    if (right <= id) {
                        round++;
                        bot = false;
                        continue;
                    }
                    round = 0;
                    len -= x[right] - x[cur];
                    id = right;
                    bot = false;
                    if (cur == 0 || x[right] - x[cur - 1] > len) {
                        int circle = len / (x[right] - x[cur]);
                        len -= circle * (x[right] - x[cur]);
                        if (circle % 2 == 1) {
                            id = cur;
                            bot = true;
                        }
                    }
                } else {
                    int left = BinarySearch.lowerBound(x, 0, n - 1, x[id] - len);
                    if (left >= id) {
                        round++;
                        bot = true;
                        continue;
                    }
                    round = 0;
                    len -= x[cur] - x[left];
                    id = left;
                    bot = true;
                    if (cur == n - 1 || x[cur + 1] - x[left] > len) {
                        int circle = len / (x[cur] - x[left]);
                        len -= circle * (x[cur] - x[left]);
                        if (circle % 2 == 1) {
                            id = cur;
                            bot = false;
                        }
                    }
                }
            }
            out.println(toIndex.get(x[id]) + 1);
        }
    }


}
