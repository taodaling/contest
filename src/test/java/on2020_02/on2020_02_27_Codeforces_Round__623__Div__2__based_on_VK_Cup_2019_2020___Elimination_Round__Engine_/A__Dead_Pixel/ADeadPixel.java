package on2020_02.on2020_02_27_Codeforces_Round__623__Div__2__based_on_VK_Cup_2019_2020___Elimination_Round__Engine_.A__Dead_Pixel;



import template.io.FastInput;
import template.io.FastOutput;

public class ADeadPixel {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int a = in.readInt();
        int b = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        int area = Math.max(Math.max(x, a - x - 1) * b, Math.max(y, b - y - 1) * a);
        out.println(area);
    }
}
