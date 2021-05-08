package on2021_05.on2021_05_06_Codeforces___Codeforces_Round__190__Div__1_.B__Ciel_and_Duel;



import template.datastructure.MultiSet;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerPreSum;
import template.primitve.generated.datastructure.LongPreSum;

public class BCielAndDuel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        IntegerArrayList atk = new IntegerArrayList(n);
        IntegerArrayList def = new IntegerArrayList(n);
        IntegerArrayList all = new IntegerArrayList(m);
        for (int i = 0; i < n; i++) {
            String t = in.rs();
            (t.equals("ATK") ? atk : def).add(in.ri());
        }
        for(int i = 0; i < m; i++){
            all.add(in.ri());
        }
        atk.sort();
        def.sort();
        all.sort();
        int[] win = new int[m];
        for (int i = 0; i < m; i++) {
            int lb = atk.upperBound(all.get(i));
            win[i] = lb;
        }

        IntegerPreSum psAtk = new IntegerPreSum(i -> atk.get(i), atk.size());
        IntegerPreSum psAll = new IntegerPreSum(i -> all.get(i), all.size());
        int best = 0;
        int match = n;
        for (int i = 1; i <= atk.size() && i <= all.size(); i++) {
            match = Math.min(match, win[m - i]);
            match--;
            if (match < 0) {
                break;
            }
            best = Math.max(best, psAll.post(m - i) - psAtk.prefix(i - 1));
        }

        //kill all
        MultiSet<Integer> set = new MultiSet<>();
        for (int x : all.toArray()) {
            set.add(x);
        }
        boolean ok = true;
        for (int d : def.toArray()) {
            Integer ceil = set.ceil(d + 1);
            if (ceil == null) {
                ok = false;
                break;
            }
            set.remove(ceil);
        }
        atk.reverse();
        int sum = 0;
        for (int x : atk.toArray()) {
            if (set.size() == 0) {
                ok = false;
                break;
            }
            int tail = set.pollLast();
            if (tail < x) {
                ok = false;
                break;
            }
            sum += tail - x;
        }

        while (set.size() > 0) {
            sum += set.pollFirst();
        }
        if (ok) {
            best = Math.max(sum, best);
        }

        out.println(best);
    }
}

