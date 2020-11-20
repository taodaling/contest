package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.Factorization;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerVersionArray;

public class QuizShow {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        char[] s = new char[n];
        in.readString(s, 0);
        IntegerArrayList factors = Factorization.factorizeNumber(n);
        factors.sort();

        IntegerVersionArray va = new IntegerVersionArray(128);
        char[] prefix = new char[n];
        for (int p : factors.toArray()) {
            int retain = 0;
            for (int i = 0; i < p; i++) {
                va.clear();
                int max = 0;
                char maxChar = 0;
                for (int j = i; j < n; j += p) {
                    int now = va.inc(s[j]);
                    if (now > max) {
                        max = now;
                        maxChar = s[j];
                    }
                }
                retain += max;
                prefix[i] = maxChar;
            }
            if(n - retain <= m){
                out.println(p);
                for (int j = 0; j < n / p; j++) {
                    for (int i = 0; i < p; i++) {
                        out.append(prefix[i]);
                    }
                }
                return;
            }
        }
    }
}
