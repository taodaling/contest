package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.GuassianElimination;
import template.math.ModGussianElimination;
import template.math.Modular;

public class CentralHeating {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Modular mod = new Modular(2);
        ModGussianElimination mge = new ModGussianElimination(n, n, mod);
        for (int i = 0; i < n; i++) {
            int id;
            while ((id = in.readInt()) != -1) {
                id--;
                mge.setLeft(id, i, 1);
            }
            mge.setRight(i, 1);
        }

        if (!mge.solve()) {
            out.println("No solution");
            return;
        }

        for (int i = 0; i < n; i++) {
            if (mge.getSolutions()[i] == 1) {
                out.append(i + 1).append(' ');
            }
        }
    }
}
