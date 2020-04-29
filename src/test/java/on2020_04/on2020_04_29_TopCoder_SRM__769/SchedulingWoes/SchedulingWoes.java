package on2020_04.on2020_04_29_TopCoder_SRM__769.SchedulingWoes;



import java.util.Arrays;
import java.util.PriorityQueue;

public class SchedulingWoes {
    public int study(int N, int seed, int[] Dprefix, int maxD, int[] Tprefix, int factor) {
        int[] random = new int[2 * N];
        random[0] = seed;
        for (int i = 1; i <= 2 * N - 1; i++) {
            random[i] = (int) ((random[i - 1] * 1103515245L + 12345) % (1L << 31));
        }

        int[] D = new int[N];
        int[] T = new int[N];
        for (int i = 0; i <= Dprefix.length - 1; i++) {
            D[i] = Dprefix[i];
            T[i] = Tprefix[i];
        }

        for (int i = Dprefix.length; i <= N - 1; i++) {
            D[i] = 1 + (random[2 * i] % maxD);
            int maxT = Math.max(1, D[i] / factor);
            T[i] = 1 + (random[2 * i + 1] % maxT);
        }

        Item[] items = new Item[N];
        for (int i = 0; i < N; i++) {
            items[i] = new Item();
            items[i].day = D[i];
            items[i].cost = T[i];
        }
        PriorityQueue<Item> pq = new PriorityQueue<>(N, (a, b) -> -Integer.compare(a.cost, b.cost));
        Arrays.sort(items, (a, b) -> Integer.compare(a.day, b.day));
        int used = 0;
        for (Item item : items) {
            if (item.cost <= item.day - used) {
                used += item.cost;
                pq.add(item);
            } else if (!pq.isEmpty() && pq.peek().cost > item.cost) {
                used -= pq.poll().cost;
                used += item.cost;
                pq.add(item);
            }
        }

        return pq.size();
    }
}


class Item {
    int day;
    int cost;
}