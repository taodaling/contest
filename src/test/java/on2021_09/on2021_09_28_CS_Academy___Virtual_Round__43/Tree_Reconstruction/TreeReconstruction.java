package on2021_09.on2021_09_28_CS_Academy___Virtual_Round__43.Tree_Reconstruction;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TreeReconstruction {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.out = out;
        n = in.ri();
        p = in.ri(n);
        for (int i = 0; i < n; i++) {
            p[i] = p[i] < 0 ? p[i] : p[i] - 1;
        }
        if (n == 1) {
            output();
            return;
        }
        cntMap = IntStream.range(0, n).boxed().collect(Collectors.groupingBy(x -> p[x]));
        for (int i = 0; i < n; i++) {
            if (p[i] == i) {
                impossible();
                return;
            }
        }
        Set<Integer> distinct = new HashSet<>(cntMap.keySet());
        distinct.remove(-1);
        ends = distinct.stream().mapToInt(Integer::intValue).toArray();
        if (ends.length > 2) {
            impossible();
            return;
        }
        if (ends.length == 0) {
            case0();
        } else if (ends.length == 1) {
            case1();
        } else {
            case2();
        }
    }

    public List<Integer> get(int x) {
        return cntMap.getOrDefault(x, Collections.emptyList());
    }

    public int size(int x) {
        return get(x).size();
    }

    Map<Integer, List<Integer>> cntMap;
    int n;
    int[] p;
    int[] ends;
    FastOutput out;

    public void impossible() {
        out.println("Impossible");
        return;
    }

    public void possible() {
        out.println("Possible");
        return;
    }

    List<int[]> adj = new ArrayList<>();

    public void output() {
        possible();
        for (int[] e : adj) {
            out.append(e[0] + 1).append(' ').append(e[1] + 1).println();
        }
    }

    private void add(int a, int b) {
        adj.add(new int[]{a, b});
    }

    public void case0() {
        if (n < 4) {
            impossible();
            return;
        }
        for (int i = 1; i < n; i++) {
            add(0, i);
        }
        output();
    }

    public void case1() {
        List<Integer> negCnt = get(-1);
        if (negCnt.size() < 2) {
            impossible();
            return;
        }
        List<Integer> end = get(ends[0]);
        if (end.size() < 3) {
            impossible();
            return;
        }

        int x = build(ends[0], negCnt);

        int a = CollectionUtils.pop(end);
        add(a, x);
        for (int t : end) {
            add(a, t);
        }

        output();
    }

    public int build(int end, List<Integer> pts) {
        pts.remove((Object) end);
        if (pts.isEmpty()) {
            return end;
        }
        int a = CollectionUtils.pop(pts);
        add(a, end);
        if (!pts.isEmpty()) {
            int b = CollectionUtils.pop(pts);
            add(a, b);
            a = b;
        }
        for (int x : pts) {
            add(a, x);
        }
        return a;
    }

    public void case2() {
        List<Integer> negCnt = get(-1);
        List<Integer> end0 = get(ends[0]);
        List<Integer> end1 = get(ends[1]);
        if ((end0.size() >= 3) != (end1.size() >= 3)) {
            impossible();
            return;
        }
        if (end0.size() == 3 || end1.size() == 3) {
            impossible();
            return;
        }
        if (end0.size() < 3 && end0.size() != end1.size()) {
            impossible();
            return;
        }
        if (end0.size() == 1 && negCnt.size() > 1) {
            impossible();
            return;
        }

        int a = build(ends[1], end0);
        int b = build(ends[0], end1);
        if (!negCnt.isEmpty()) {
            int head = CollectionUtils.pop(negCnt);
            add(a, head);
            add(b, head);
            for (int x : negCnt) {
                add(head, x);
            }
        } else {
            add(a, b);
        }

        output();
    }
}
