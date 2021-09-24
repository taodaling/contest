package on2021_08.on2021_08_22_Codeforces___Codeforces_Round__320__Div__1___Bayan_Thanks_Round_.E__Walking_0;



import template.datastructure.RangeTree;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class EWalking {

    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] s = in.rs().toCharArray();
        int n = s.length;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            s[i] = (char) (s[i] == 'R' ? 1 : 0);
            sum += s[i];
        }
        int half = (s.length + 1) / 2;
        int[] best = null;
        int score = (int) 1e9;
        if (sum >= half) {
            int[] sol = solve(s, 1);
            int solScore = eval(sol);
            if(solScore < score){
                score = solScore;
                best = sol;
            }
        }
        if(n - sum >= half){
            int[] sol = solve(s, 0);
            int solScore = eval(sol);
            if(solScore < score){
                score = solScore;
                best = sol;
            }
        }
        out.println(score);
        for(int x : best){
            out.append(x + 1).append(' ');
        }
    }

    public int eval(int[] a) {
        int ans = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] < a[i - 1]) {
                ans++;
            }
        }
        return ans;
    }

    public int[] solve(char[] s, int start) {
        int n = s.length;
        RangeTree[] rt = new RangeTree[]{new RangeTree(n), new RangeTree(n)};
        for (int i = 0; i < n; i++) {
            rt[s[i]].add(i);
        }
        IntegerArrayList seq = new IntegerArrayList(n);
        seq.add(rt[start].first());
        rt[start].remove(seq.tail());
        while (seq.size() < n) {
            int tail = seq.tail();
            int type = s[tail] ^ 1;
            int next = rt[type].ceil(tail);
            if (next == -1) {
                next = rt[type].first();
            }
            assert next != -1;
            seq.add(next);
            rt[type].remove(next);
        }
        return seq.toArray();
    }


}
