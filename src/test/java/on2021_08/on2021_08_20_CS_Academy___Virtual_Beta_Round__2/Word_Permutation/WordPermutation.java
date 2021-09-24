package on2021_08.on2021_08_20_CS_Academy___Virtual_Beta_Round__2.Word_Permutation;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.Arrays;
import java.util.Comparator;

public class WordPermutation {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        Word[] words = new Word[n];
        for (int i = 0; i < n; i++) {
            words[i] = new Word();
            words[i].s = in.rs();
            words[i].index = i;
        }
        Arrays.sort(words, Comparator.comparing(x -> x.s));

        for (int i = 0; i < n; i++) {
            words[i].rank = i;
        }
        int[] sigma = new int[n];
        for (int i = 0; i < n; i++) {
            sigma[words[i].rank] = words[i].index;
        }
        for (int s : sigma) {
            out.append(s + 1).println(' ');
        }
    }
}

class Word {
    String s;
    int rank;
    int index;
}