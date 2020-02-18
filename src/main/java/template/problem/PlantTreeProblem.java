package template.problem;

import template.primitve.generated.datastructure.IntegerBIT;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * A城市有一个巨大的圆形广场，为了绿化环境和净化空气，市政府决定沿圆形广场外圈种一圈树。
 * <br>
 * 园林部门得到指令后，初步规划出n个种树的位置，顺时针编号1到n。并且每个位置都有一个美观度Ai，如果在这里种树就可以得到这Ai的美观度。但由于A城市土壤肥力欠佳，两棵树决不能种在相邻的位置（i号位置和i+1号位置叫相邻位置。值得注意的是1号和n号也算相邻位置！）。
 * <br>
 * 最终市政府给园林部门提供了m棵树苗并要求全部种上，请你帮忙设计种树方案使得美观度总和最大。
 */
public class PlantTreeProblem {
    IntegerBIT bit;
    long ans;
    int n;

    public PlantTreeProblem(long[] weights, int m) {
        n = weights.length;
        bit = new IntegerBIT(n + 1);
        TreeSet<Node> set = new TreeSet<>(Node.sortByW);
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node();
            nodes[i].w = weights[i];
            nodes[i].l = nodes[i].r = i;
        }
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                nodes[i].prev = nodes[i - 1];
            } else {
                nodes[i].prev = nodes[n - 1];
            }
            if (i + 1 < n) {
                nodes[i].next = nodes[i + 1];
            } else {
                nodes[i].next = nodes[0];
            }
        }
        set.addAll(Arrays.asList(nodes));

        for (int i = 0; i < m; i++) {
            Node last = set.pollLast();
            ans += last.w;
            if (last.l <= last.r) {
                bit.update(last.l + 1, 1);
                bit.update(last.r + 2, 1);
            } else {
                bit.update(1, 1);
                bit.update(last.r + 2, 1);
                bit.update(last.l + 1, n + 1);
                bit.update(n, 1);
            }

            if (last.next == last.prev) {
                continue;
            }
            Node prev = last.prev;
            set.remove(last.next);
            set.remove(last.prev);
            prev.r = last.next.r;
            prev.w += last.next.w - last.w;
            prev.next = last.next.next;
            prev.next.prev = prev;
            set.add(prev);
        }
    }

    public long getAnswer() {
        return ans;
    }

    public boolean chooseOrNot(int i) {
        return bit.query(i + 1) % 2 == 1;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < n; i++) {
            builder.append(chooseOrNot(i)).append(',');
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    private static class Node {
        Node prev;
        Node next;
        long w;
        int l;
        int r;
        static Comparator<Node> sortByW = (a, b) -> a.w == b.w ? a.l - b.l : Long.compare(a.w, b.w);

        @Override
        public String toString() {
            return String.format("[%d, %d] => %d", l, r, w);
        }
    }
}
