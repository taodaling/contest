package on2021_07.on2021_07_27_CodeChef___April_Cook_Off_2021_Division_1.Deletion_Sort;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.TreeSet;

public class DeletionSort {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        IntegerArrayList list = new IntegerArrayList(a);
        list.unique();
        for (int i = 0; i < n; i++) {
            a[i] = list.binarySearch(a[i]);
        }

        int m = list.size();
//        RangeTree occur = new RangeTree(m);
        TreeSet<Integer> occur = new TreeSet<>();

        LR[] lrs = new LR[m];
        for (int i = 0; i < m; i++) {
            lrs[i] = new LR();
            lrs[i].l = n + 1;
            lrs[i].r = -1;
        }
        for (int i = 0; i < n; i++) {
            lrs[a[i]].l = Math.min(lrs[a[i]].l, i);
            lrs[a[i]].r = Math.max(lrs[a[i]].r, i);
        }
        int[] cnt = new int[m];
        for (int x : a) {
            cnt[x]++;
        }
        int end = 0;
        for (int i = n - 1; i >= 1; i--) {
            int x = a[i];
            cnt[x]--;
            if (cnt[x] > 0) {
                continue;
            }
            Integer ceil = occur.ceiling(x);
            Integer floor = occur.floor(x);
            if (floor != null && lrs[floor].r >= lrs[x].l ||
                    ceil != null && lrs[ceil].l <= lrs[x].r) {
                cnt[x]++;
                end = i;
                break;
            }
            occur.add(x);
        }

        long ans = n - end;
        int[] right = new int[n];
        right[0] = end;
        for (int i = 1; i < n; i++) {
            int x = a[i - 1];
            cnt[x]--;
            if (cnt[x] == 0) {
                //non-conflict
                occur.add(x);
                while (end < n && occur.contains(x)) {
                    Integer ceil = occur.ceiling(x + 1);
                    Integer floor = occur.floor(x - 1);
                    if (floor != null && lrs[floor].r >= lrs[x].l ||
                            ceil != null && lrs[ceil].l <= lrs[x].r) {
                        end++;
                        if (end < n) {
                            cnt[a[end]]++;
                            if (cnt[a[end]] == 1) {
                                occur.remove(a[end]);
                            }
                        }
                    } else {
                        break;
                    }
                }
            }
            right[i] = end;
            if (end < n) {
                ans += n - end;
            }
        }

        debug.debug("right", right);
        out.println(ans);
    }

    Debug debug = new Debug(false);
}

class LR {
    int l;
    int r;
}
