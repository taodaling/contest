package on2020_11.on2020_11_15_CSES___CSES_Problem_Set.Visiting_Cities1;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.problem.DirectedModifiableMinDistProblem;

public class VisitingCities {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        int m = in.readInt();

        DirectedModifiableMinDistProblem problem = new DirectedModifiableMinDistProblem(2 * n, n + m, enterId(0), leaveId(n - 1));
        for (int i = 0; i < n; i++) {
            problem.addEdge(enterId(i), leaveId(i), 0);
        }
        for (int i = 0; i < m; i++) {
            problem.addEdge(leaveId(in.readInt() - 1), enterId(in.readInt() - 1), in.readInt());
        }
        IntegerArrayList list = new IntegerArrayList(n);
        for (int i = 0; i < n; i++) {
            if (problem.minDist() != problem.queryOnDeleteEdge(i)) {
                list.add(i);
            }
        }
        out.println(list.size());
        for(int x : list.toArray()){
            out.append(x + 1).append(' ');
        }
    }

    int n;

    int enterId(int i) {
        return i * 2;
    }

    int leaveId(int i) {
        return i * 2 + 1;
    }
}
