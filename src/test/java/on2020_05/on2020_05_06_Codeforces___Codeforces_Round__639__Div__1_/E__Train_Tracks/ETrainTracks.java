package on2020_05.on2020_05_06_Codeforces___Codeforces_Round__639__Div__1_.E__Train_Tracks;



import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class ETrainTracks {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

    }
}

class Interval {
    int l;
    int r;
}

class Edge {
    Node a;
    Node b;
    List<Interval> intervals = new ArrayList<>();
}

class Node {
    List<Node> next = new ArrayList<>();
    
}
