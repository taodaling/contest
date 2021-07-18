package on2021_07.on2021_07_11_Kattis.Tarot_Sham_Boast;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.string.KMPAutomaton;
import template.utils.SequenceUtils;
import template.utils.SortUtils;

import java.util.Arrays;
import java.util.Comparator;

public class TarotShamBoast {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int k = in.ri();
        Guess[] guesses = new Guess[k];
        char[] s = new char[(int) 1e6];
        KMPAutomaton kmp = new KMPAutomaton((int) 1e6);
        IntegerArrayList borders = new IntegerArrayList();
        for (int i = 0; i < k; i++) {
            guesses[i] = new Guess();
            int m = in.rs(s);
            guesses[i].seq = Arrays.copyOf(s, m);
            kmp.init();
            for (int c : guesses[i].seq) {
                kmp.build(c);
            }
            borders.clear();
            int cur = m;
            while (cur > 0) {
                int boarder = kmp.maxBorder(cur - 1);
                if (boarder == 0) {
                    break;
                }
                cur = boarder;
                borders.add(boarder);
            }
            while (!borders.isEmpty() && 2 * m - borders.tail() > n) {
                borders.pop();
            }
            guesses[i].borders = borders.toArray();
        }

        Arrays.sort(guesses, Comparator.<Guess, int[]>comparing(x -> x.borders, (x, y) -> {
            for (int i = 0; i < x.length && i < y.length; i++) {
                if (x[i] != y[i]) {
                    return Integer.compare(x[i], y[i]);
                }
            }
            return Integer.compare(x.length, y.length);
        }).thenComparingInt(x -> x.index));
        for(Guess g : guesses){
            out.println(g.seq);
        }
    }
}

class Guess {
    char[] seq;
    int[] borders;
    int index;
}
