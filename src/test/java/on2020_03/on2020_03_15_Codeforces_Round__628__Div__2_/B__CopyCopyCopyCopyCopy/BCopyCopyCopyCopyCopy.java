package on2020_03.on2020_03_15_Codeforces_Round__628__Div__2_.B__CopyCopyCopyCopyCopy;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerList;

public class BCopyCopyCopyCopyCopy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        IntegerList list = new IntegerList(n);
        for (int i = 0; i < n; i++) {
            list.add(in.readInt());
        }
        list.unique();
        out.println(list.size());
    }
}
