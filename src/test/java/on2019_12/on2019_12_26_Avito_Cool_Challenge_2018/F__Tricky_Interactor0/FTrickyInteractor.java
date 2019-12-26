package on2019_12.on2019_12_26_Avito_Cool_Challenge_2018.F__Tricky_Interactor0;




import template.io.FastInput;
import template.io.FastOutput;

public class FTrickyInteractor {
    FastInput in;
    FastOutput out;
    int n;
    int t;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        this.in = in;
        this.out = out;

        n = in.readInt();
        t = in.readInt();

        int[] ans = new int[n + 1];
        if(n == 1){
            ans[1] = t;
            output(ans);
            return;
        }

        if(n % 2 == 0){
            ans[1] = queryPrefix(1, t);
        }else{
            ans[1] = t - querySuffix(2, t);
        }

        int preSum = ans[1];
        for(int i = 2; i <= n; i++){
            ans[i] = queryPrefix(i, t) - preSum;
            preSum += ans[i];
        }

        output(ans);
    }

    public void output(int[] ans) {
        out.append("! ");
        for (int i = 1; i <= n; i++) {
            out.append(ans[i]);
        }
        out.flush();
    }

    public int querySuffix(int s, int t) {
        int p = s;
        boolean sufOdd = (n - s + 1) % 2 == 1;
        boolean prevOdd = !sufOdd;
        if (prevOdd != (p % 2 == 1)) {
            p++;
        }
        boolean lFlip = false;
        boolean rFlip = false;
        int ans = t;
        while (!(!lFlip && rFlip)) {
            int val = query(s, p);
            if ((Math.abs(ans - val) % 2 == 1) == prevOdd) {
                lFlip = !lFlip;
            } else {
                rFlip = !rFlip;
            }
            ans = val;
        }
        int ret = (n - s + 1 + t - ans) / 2;
        while (lFlip || rFlip) {
            int val = query(s, p);
            if ((Math.abs(ans - val) % 2 == 1) == prevOdd) {
                lFlip = !lFlip;
            } else {
                rFlip = !rFlip;
            }
            ans = val;
        }
        return ret;
    }

    public int queryPrefix(int p, int t) {
        int s = p;
        boolean prevOdd = p % 2 == 1;
        boolean sufOdd = !prevOdd;
        if (sufOdd != ((n - s + 1) % 2 == 1)) {
            s--;
        }
        boolean lFlip = false;
        boolean rFlip = false;
        int ans = t;
        while (!(lFlip && !rFlip)) {
            int val = query(s, p);
            if ((Math.abs(ans - val) % 2 == 1) == prevOdd) {
                lFlip = !lFlip;
            } else {
                rFlip = !rFlip;
            }
            ans = val;
        }
        int ret = (p + t - ans) / 2;
        while (lFlip || rFlip) {
            int val = query(s, p);
            if ((Math.abs(ans - val) % 2 == 1) == prevOdd) {
                lFlip = !lFlip;
            } else {
                rFlip = !rFlip;
            }
            ans = val;
        }
        return ret;
    }

    public int query(int l, int r) {
        out.append("? ").append(l).append(' ').append(r).println();
        out.flush();
        return in.readInt();
    }
}
