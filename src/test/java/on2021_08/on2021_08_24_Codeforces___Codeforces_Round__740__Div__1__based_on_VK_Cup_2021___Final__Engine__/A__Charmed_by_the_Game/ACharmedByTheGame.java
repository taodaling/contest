package on2021_08.on2021_08_24_Codeforces___Codeforces_Round__740__Div__1__based_on_VK_Cup_2021___Final__Engine__.A__Charmed_by_the_Game;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;

public class ACharmedByTheGame {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.ri();
        int b = in.ri();
        IntegerArrayList list = new IntegerArrayList(2 * (a + b));
        int total = (a + b);
        int half = total / 2;
        solve(a, b, half, total - half, list);
        solve(a, b, total - half, half, list);
        list.unique();
        out.println(list.size());
        for(int x : list.toArray()){
            out.append(x).append(' ');
        }
        out.println();
    }

    public void solve(int a, int b, int x, int y, IntegerArrayList col) {
        if (a - y != -(b - x)) {
            return;
        }
        int l = 0;
        int r = y;
        l = Math.max(l, 0 - (a - y));
        r = Math.min(r, x - (a - y));
        for (int i = l; i <= r; i++) {
            int y_ = i;
            int x_ = a - y + y_;
            col.add(x - x_ + y - y_);
        }
    }
}
