package on2020_08.on2020_08_20_TopCoder_Open_Round__3B.HorseTicket;



import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.utils.SequenceUtils;

import java.util.*;

public class HorseTicket {
    public String getTicket(String[] races, long index) {
        int[] belong = new int[128];
        Arrays.fill(belong, -1);
        int[] sizes = new int[races.length];
        for (int i = 0; i < races.length; i++) {
            for (char c : races[i].toCharArray()) {
                belong[c] = i;
            }
            sizes[i] = races[i].length();
        }

        index++;
        long total = way(sizes, -1);
        if (total < index) {
            return "!";
        }

        StringBuilder ans = new StringBuilder();
        dfs(sizes, belong, index, ans);
        return ans.toString();
    }

    long inf = (long) 1e18;

    long[] last = new long[100];
    long[] next = new long[100];

    public long way(int[] sizes, int ignore) {
        Arrays.fill(last, 0);
        last[0] = 1;
        for (int i = 0; i < sizes.length; i++) {
            if (i == ignore) {
                continue;
            }
            Arrays.fill(next, 0);
            for (int j = 0; j < 100; j++) {
                long way = last[j];
                if(way == 0){
                    continue;
                }
                next[j] = Math.min(next[j] + way, inf);
                next[j + 1] = Math.min(next[j + 1] + DigitUtils.mul(way, sizes[i], inf), inf);
            }
            long[] tmp = last;
            last = next;
            next = tmp;
        }

        long sum = 0;
        long fact = 1;
        for(int i = 0; i < 100; i++){
            sum = Math.min(sum + DigitUtils.mul(fact, last[i], inf), inf);
            fact = DigitUtils.mul(fact, i + 1, inf);
        }
        return sum;
    }

    public void dfs(int[] sizes, int[] valueIndex, long k, StringBuilder seq) {
        if (k == 1) {
            return;
        }
        k--;
        for (int i = 0; i < valueIndex.length; i++) {
            if (valueIndex[i] == -1) {
                continue;
            }
            long way = way(sizes, valueIndex[i]);
            if (way < k) {
                k -= way;
            } else {
                seq.append((char) i);
                int remove = valueIndex[i];
                for (int j = 0; j < valueIndex.length; j++) {
                    if (valueIndex[j] == remove) {
                        valueIndex[j] = -1;
                    }
                }
                sizes[remove] = 0;
                dfs(sizes, valueIndex, k, seq);
                return;
            }
        }
    }
}
