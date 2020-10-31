package on2020_10.on2020_10_25_CSES___CSES_Problem_Set.Projects;



import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;

import java.io.PrintWriter;

public class Projects {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        Project[] projects = new Project[n];
        IntegerArrayList list = new IntegerArrayList(n * 2);
        for (int i = 0; i < n; i++) {
            projects[i] = new Project(in.readInt(), in.readInt(), in.readInt());
            list.add(projects[i].l);
            list.add(projects[i].r);
        }
        list.unique();
        for (int i = 0; i < n; i++) {
            projects[i].l = 1 + list.binarySearch(projects[i].l);
            projects[i].r = 1 + list.binarySearch(projects[i].r);
        }
        int m = list.size();
        long[] dp = new long[m + 1];
        MultiWayStack<Project> stack = new MultiWayStack<>(m + 1, n);
        for (Project p : projects) {
            stack.addLast(p.r, p);
        }
        for (int i = 1; i <= m; i++) {
            dp[i] = dp[i - 1];
            for (Project p : stack.getStack(i)) {
                dp[i] = Math.max(dp[i], p.reward + dp[p.l - 1]);
            }
        }
        out.println(dp[m]);
    }
}

class Project {
    int l;
    int r;
    int reward;

    public Project(int l, int r, int reward) {
        this.l = l;
        this.r = r;
        this.reward = reward;
    }
}
