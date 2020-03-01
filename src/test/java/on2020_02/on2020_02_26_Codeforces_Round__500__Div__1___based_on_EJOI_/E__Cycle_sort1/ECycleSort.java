package on2020_02.on2020_02_26_Codeforces_Round__500__Div__1___based_on_EJOI_.E__Cycle_sort1;





import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.PermutationUtils;
import template.primitve.generated.datastructure.IntegerList;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.rand.Randomized;
import template.utils.CompareUtils;
import template.utils.SequenceUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ECycleSort {
//    public void solve(int testNumber, FastInput in, FastOutput out) {
//        int n = in.readInt();
//        int s = in.readInt();
//        int[] a = new int[n];
//        for (int i = 0; i < n; i++) {
//            a[i] = in.readInt();
//        }
//        int[] b = a.clone();
//        Randomized.shuffle(b);
//        Arrays.sort(b);
//        byte[] same = new byte[n];
//        int sum = 0;
//        for (int i = 0; i < n; i++) {
//            if (a[i] == b[i]) {
//                same[i] = 1;
//            }
//        }
//        for (byte x : same) {
//            sum += x;
//        }
//        if (n - sum > s) {
//            out.println(-1);
//            return;
//        }
//        IntegerList permList = new IntegerList(n);
//        for (int i = 0; i < n; i++) {
//            if (same[i] == 0) {
//                permList.add(i);
//            }
//        }
//        int[] perm = permList.toArray();
//        CompareUtils.quickSort(perm, (x, y) -> Integer.compare(a[x], a[y]), 0, perm.length);
//        DSU dsu = new DSU(n);
//        for (int i = 0; i < perm.length; i++) {
//            int from = perm[i];
//            int to = permList.get(i);
//            dsu.merge(from, to);
//        }
//        for (int i = 1; i < perm.length; i++) {
//            if (a[perm[i]] != a[perm[i - 1]]) {
//                continue;
//            }
//            if (dsu.find(perm[i]) == dsu.find(perm[i - 1])) {
//                continue;
//            }
//            dsu.merge(perm[i], perm[i - 1]);
//            SequenceUtils.swap(perm, i, i - 1);
//        }
//        int[] index = new int[n];
//        for (int i = 0; i < n; i++) {
//            if (same[i] == 1) {
//                index[i] = i;
//            }
//        }
//        for (int i = 0; i < perm.length; i++) {
//            index[perm[i]] = i;
//        }
//
//        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(index);
//        List<IntegerList> circles = pp.extractCircles(2);
//        out.println(circles.size());
//        for (IntegerList list : circles) {
//            out.println(list.size());
//            for (int i = 0; i < list.size(); i++) {
//                out.append(list.get(i) + 1).append(' ');
//            }
//        }
//    }

    int n;
    int[] a;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int s = in.readInt();
        a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
        }
        int[] b = a.clone();
        Randomized.shuffle(b);
        Arrays.sort(b);
        int[] same = new int[n];
        int sum = 0;
        for (int i = 0; i < n; i++) {
            if (a[i] == b[i]) {
                same[i] = 1;
            }
        }
        for (int x : same) {
            sum += x;
        }
        if (n - sum > s) {
            out.println(-1);
            return;
        }
        IntegerList permList = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (same[i] == 0) {
                permList.add(i);
            }
        }
        int[] perm = permList.toArray();
        CompareUtils.quickSort(perm, (x, y) -> Integer.compare(a[x], a[y]), 0, perm.length);
        DSU dsu = new DSU(n);
        for (int i = 0; i < perm.length; i++) {
            int from = perm[i];
            int to = permList.get(i);
            dsu.merge(from, to);
        }
        for (int i = 1; i < perm.length; i++) {
            if (a[perm[i]] != a[perm[i - 1]]) {
                continue;
            }
            if (dsu.find(perm[i]) == dsu.find(perm[i - 1])) {
                continue;
            }
            dsu.merge(perm[i], perm[i - 1]);
            SequenceUtils.swap(perm, i, i - 1);
        }
        IntegerList first = new IntegerList();
        if(perm.length > 0) {
            int remain = s - (n - sum) - 1;
            first.add(perm[0]);
            for (int i = 1; remain > 0 && i < perm.length; i++) {
                if (dsu.find(perm[i - 1]) != dsu.find(perm[i])) {
                    remain--;
                    first.add(perm[i]);
                    dsu.merge(perm[i - 1], perm[i]);
                }
            }

            int last = a[first.get(0)];
            for (int i = 1; i < first.size(); i++) {
                int y = first.get(i);
                int tmp = a[y];
                a[y] = last;
                last = tmp;
            }
            a[first.get(0)] = last;
            //System.err.println(Arrays.toString(a));
        }

        List<IntegerList> circles = new ArrayList<>();
        if(first.size() > 1){
            circles.add(first);
        }
        circles.addAll(solve());
        out.println(circles.size());
        for (IntegerList list : circles) {
            out.println(list.size());
            for (int i = 0; i < list.size(); i++) {
                out.append(list.get(i) + 1).append(' ');
            }
            out.println();
        }
    }

    public List<IntegerList> solve() {
        int[] b = a.clone();
        Randomized.shuffle(b);
        Arrays.sort(b);
        int[] same = new int[n];
        for (int i = 0; i < n; i++) {
            if (a[i] == b[i]) {
                same[i] = 1;
            }
        }
        IntegerList permList = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            if (same[i] == 0) {
                permList.add(i);
            }
        }
        int[] perm = permList.toArray();
        CompareUtils.quickSort(perm, (x, y) -> Integer.compare(a[x], a[y]), 0, perm.length);
        DSU dsu = new DSU(n);
        for (int i = 0; i < perm.length; i++) {
            int from = perm[i];
            int to = permList.get(i);
            dsu.merge(from, to);
        }
        for (int i = 1; i < perm.length; i++) {
            if (a[perm[i]] != a[perm[i - 1]]) {
                continue;
            }
            if (dsu.find(perm[i]) == dsu.find(perm[i - 1])) {
                continue;
            }
            dsu.merge(perm[i], perm[i - 1]);
            SequenceUtils.swap(perm, i, i - 1);
        }

        int[] index = new int[n];
        for (int i = 0; i < n; i++) {
            if (same[i] == 1) {
                index[i] = i;
            }
        }
        for (int i = 0; i < perm.length; i++) {
            index[perm[i]] = permList.get(i);
        }
        PermutationUtils.PowerPermutation pp = new PermutationUtils.PowerPermutation(index);
        List<IntegerList> circles = pp.extractCircles(2);

        return circles;
    }

}
