package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.math.LinearProgramming;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class EPolitics {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int x = in.readInt();
        int y = in.readInt();

        int[] income = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            income[i] = in.readInt();
        }

        Node[] a = new Node[n + 1];
        Node[] b = new Node[n + 1];

        for (int i = 1; i <= n; i++) {
            a[i] = new Node();
            b[i] = new Node();
            a[i].id = b[i].id = i;
        }

        for (int i = 1; i < n; i++) {
            Node u = a[in.readInt()];
            Node v = a[in.readInt()];
            u.next.add(v);
            v.next.add(u);
        }

        for (int i = 1; i < n; i++) {
            Node u = b[in.readInt()];
            Node v = b[in.readInt()];
            u.next.add(v);
            v.next.add(u);
        }

        dfsForParent(a[x], null);
        dfsForParent(b[y], null);

        int q1 = in.readInt();
        int[][] constrains1 = new int[q1][2];
        for (int i = 0; i < q1; i++) {
            for (int j = 0; j < 2; j++) {
                constrains1[i][j] = in.readInt();
            }
            SequenceUtils.swap(constrains1[i], 0, 1);
        }

        int q2 = in.readInt();
        int[][] constrains2 = new int[q2][2];
        for (int i = 0; i < q2; i++) {
            for (int j = 0; j < 2; j++) {
                constrains2[i][j] = in.readInt();
            }
            SequenceUtils.swap(constrains2[i], 0, 1);
        }

        dlp = new LinearProgramming.DualLinearProgramming(2 * q1 + 2 * q2 + n, n, 1e-8);

        for (int i = 1; i <= n; i++) {
            dlp.setTargetCoefficient(i, -income[i]);
        }

        int constraintIndex = 1;
        for (int i = 0; i < q1; i++, constraintIndex += 2) {
            dlp.setConstraintConstant(constraintIndex, constrains1[i][0]);
            dlp.setConstraintConstant(constraintIndex + 1, -constrains1[i][0]);
            dfsForConstraint(constraintIndex, constraintIndex + 1, a[constrains1[i][1]]);
        }

        for (int i = 0; i < q2; i++, constraintIndex += 2) {
            dlp.setConstraintConstant(constraintIndex, constrains2[i][0]);
            dlp.setConstraintConstant(constraintIndex + 1, -constrains2[i][0]);
            dfsForConstraint(constraintIndex, constraintIndex + 1, b[constrains2[i][1]]);
        }

        for (int i = 1; i <= n; i++, constraintIndex += 1) {
            dlp.setConstraintConstant(constraintIndex, -1);
            //dlp.setConstraintConstant(constraintIndex + 1, 0);
            dlp.setConstraintCoefficient(constraintIndex, i, -1);
            //dlp.setConstraintCoefficient(constraintIndex + 1, i, -1);
        }

        dlp.solve();
        if (dlp.isInfeasible()) {
            out.println(-1);
            return;
        }

        out.println(DigitUtils.round(-dlp.minSolution()));
    }

    public void dfsForParent(Node root, Node p) {
        root.p = p;
        for (Node node : root.next) {
            if (node == p) {
                continue;
            }
            dfsForParent(node, root);
        }
    }

    public void dfsForConstraint(int i, int j, Node root) {
        dlp.setConstraintCoefficient(i, root.id, 1);
        dlp.setConstraintCoefficient(j, root.id, -1);

        for (Node node : root.next) {
            if (node == root.p) {
                continue;
            }
            dfsForConstraint(i, j, node);
        }
    }

    LinearProgramming.DualLinearProgramming dlp;

    static class Node {
        Node p;
        List<Node> next = new ArrayList<>();
        int id;
    }
}

