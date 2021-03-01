package on2021_03.on2021_03_01_Codeforces___Codeforces_Round__318__RussianCodeCup_Thanks_Round___Div__1_.E__Bear_and_Bowling;



import template.datastructure.MonotoneOrderBeta;
import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class EBearAndBowling {
    Debug debug = new Debug(false);
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        MonotoneOrderBeta<Long, Long> mo = new MonotoneOrderBeta<Long, Long>(Comparator.naturalOrder(), Comparator.naturalOrder(),
                false, true);
        mo.add(0L, 0L);
        List<Map.Entry<Long, Long>> lists = new ArrayList<>(n);
        int[] a = in.ri(n);
        SequenceUtils.reverse(a);
        for (int x : a) {
            lists.clear();
            for (Map.Entry<Long, Long> entry : mo) {
                lists.add(entry);
            }
            for (Map.Entry<Long, Long> entry : lists) {
                mo.add(entry.getKey() + x, entry.getValue() + entry.getKey() + x);
            }
            debug.debug("mo", mo);
        }
        long ans = mo.first().getValue();
        out.println(ans);
    }
}
