package contest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChristmasBatteries {
    public int mostFun(int B, int N, int X, int Y, int Z, int M) {
        List<Integer>[] lists = new List[5];
        for (int i = 0; i < 5; i++) {
            lists[i] = new ArrayList<Integer>((N + 4) / 5);
        }
        for (int i = 0; i < N; i++) {
            long v = ((long) i * i * X + (long) Y * i + Z) % M;
            lists[i % 5].add((int) v);
        }
        for (int i = 0; i < 5; i++) {
            lists[i].sort(Comparator.naturalOrder());
            Collections.reverse(lists[i]);
            lists[i].add(0);
        }
        long sum = 0;
        for (long x : lists[0]) {
            sum += x;
        }
        long other = 0;
        for (int i = 0, a = 0; i <= B && i < lists[1].size(); a += lists[1].get(i), i++) {
            for (int j = 0, b = 0; i + j * 2 <= B && j < lists[2].size(); b += lists[2].get(j), j++) {
                for (int k = 0, c = 0; i + j * 2 + k * 3 <= B && k < lists[3].size(); c += lists[3].get(k), k++) {
                    for (int t = 0, d = 0; i + j * 2 + k * 3 + t * 4 <= B && t < lists[4].size(); d += lists[4].get(t), t++) {
                        other = Math.max(other, a + b + c + d);
                    }
                }
            }
        }
        sum += other;
        return (int) sum;
    }
}
