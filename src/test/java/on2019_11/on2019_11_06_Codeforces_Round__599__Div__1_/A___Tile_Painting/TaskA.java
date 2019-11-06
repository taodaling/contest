package on2019_11.on2019_11_06_Codeforces_Round__599__Div__1_.A___Tile_Painting;



import template.FastInput;
import template.FastOutput;
import template.IntList;
import template.NumberTheory;

public class TaskA {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        long n = in.readLong();
        long gcd = n;
        NumberTheory.Gcd g = new NumberTheory.Gcd();
        for (long i = 2; i * i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            gcd = g.gcd(gcd, i);
            gcd = g.gcd(gcd, n / i);
        }

        out.println(gcd);
    }
}
