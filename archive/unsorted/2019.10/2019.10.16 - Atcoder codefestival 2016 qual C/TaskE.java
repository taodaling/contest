package contest;

import template.BIT;
import template.FastInput;
import template.FastOutput;
import template.NumberTheory;

public class TaskE {
    NumberTheory.Modular mod = new NumberTheory.Modular(1e9 + 7);
    NumberTheory.Factorial fact = new NumberTheory.Factorial(500000, mod);
    NumberTheory.Composite comp = new NumberTheory.Composite(fact);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] s = new int[n + 1];
        for(int i = 1; i <= n; i++){
            s[i] = in.readInt();
        }

        BIT specified = new BIT(n);
        for (int i = 1; i <= n; i++) {
            if(s[i] > 0) {
                specified.update(s[i], 1);
            }
        }

        int k = n - specified.query(n);

        int ans1 = fact.fact(k);
        int ans2 = 0;
        int ans3 = 0;


        int notFixSum = 0;
        for (int i = 1; i <= n; i++) {
            notFixSum = mod.plus(notFixSum, i - 1);
        }


        for (int i = 1; i <= n; i++) {
            if (s[i] != 0) {
                notFixSum = mod.subtract(notFixSum, s[i] - 1);
            }
        }

        for (int i = 1; i <= n; i++) {
            int cnt = 1;
            if (s[i] == 0) {
                cnt = mod.mul(cnt, notFixSum);
                cnt = mod.mul(cnt, fact.fact(k - 1));
            } else {
                cnt = mod.mul(cnt, s[i] - 1);
                cnt = mod.mul(cnt, fact.fact(k));
            }
            cnt = mod.mul(cnt, fact.fact(n - i));
            ans2 = mod.plus(ans2, cnt);
        }

        int[] prefixSubstitute = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            prefixSubstitute[i] = prefixSubstitute[i - 1];
            if (s[i] == 0) {
                prefixSubstitute[i]++;
            }
        }

        BIT bit = new BIT(n);
        int freeGreaterThanCnt = 0;
        for (int i = 1; i <= n; i++) {
            int cnt = 0;
            int rep = prefixSubstitute[i - 1];
            int cnt1 = 0;
            if (s[i] > 0) {
                int free = s[i] - 1 - bit.query(s[i] - 1);
                if (k > 0) {
                    cnt1 = mod.mul(free, fact.fact(k - 1));
                }
                cnt = mod.plus(cnt, mod.mul(bit.query(s[i]), fact.fact(k)));
            } else {
                if (k > 1) {
                    cnt1 = mod.mul(comp.composite(k, 2), fact.fact(k - 2));
                }
                cnt = mod.plus(cnt, mod.mul(freeGreaterThanCnt, fact.fact(k - 1)));
            }
            cnt1 = mod.mul(cnt1, rep);
            cnt = mod.plus(cnt, cnt1);
            cnt = mod.mul(cnt, fact.fact(n - i));
            ans3 = mod.plus(ans3, cnt);
            if (s[i] > 0) {
                bit.update(s[i], 1);
                int blank = specified.query(n) - specified.query(s[i]);
                blank = (n - s[i]) - blank;
                freeGreaterThanCnt = mod.plus(freeGreaterThanCnt, blank);
            }
        }

        int ans = 0;
        ans = mod.plus(ans, ans1);
        ans = mod.plus(ans, ans2);
        ans = mod.subtract(ans, ans3);

        out.println(ans);
    }
}
