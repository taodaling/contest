package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.utils.SequenceUtils;

import java.util.ArrayList;
import java.util.List;

public class CNewLanguage {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        char[] types = in.rs().toCharArray();
        for (int i = 0; i < types.length; i++) {
            types[i] = (char) (types[i] == 'V' ? 0 : 1);
        }
        int n = in.ri();
        int m = in.ri();
        Node[][] nodes = new Node[n][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 2; j++) {
                nodes[i][j] = new Node();
                nodes[i][j].type = j;
                nodes[i][j].index = i;
            }
        }
        for (int i = 0; i < m; i++) {
            int p1 = in.ri() - 1;
            int t1 = in.rc() == 'V' ? 0 : 1;
            int p2 = in.ri() - 1;
            int t2 = in.rc() == 'V' ? 0 : 1;
            nodes[p1][t1].out.add(nodes[p2][t2]);
            nodes[p2][t2].in.add(nodes[p1][t1]);
        }

        char[] lower = in.rs().toCharArray();
        for (int i = 0; i < n; i++) {
            lower[i] -= 'a';
        }


        int[] prefers = new int[n];
        int[] status = new int[n];
        int NOT_SET = -1;
        int V = 0;
        int C = 1;
        int DIRTY_V = 2;
        int DIRTY_C = 3;
        IntegerDeque dq = new IntegerDequeImpl(n);
        for (int start = n; start >= 0; start--) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < 2; j++) {
                    nodes[i][j].possible = false;
                }
            }
            for (int i = 0; i < n; i++) {
                int l = 0;
                int r = types.length - 1;
                if (i < start) {
                    l = r = lower[i];
                } else if (i == start) {
                    l = lower[i] + 1;
                }
                for (int j = l; j <= r; j++) {
                    nodes[i][types[j]].possible = true;
                }
                if (l < types.length) {
                    prefers[i] = types[l];
                }
            }
            SequenceUtils.deepFill(status, NOT_SET);
            boolean valid = true;
            for (int i = 0; i < n && valid; i++) {
                //greedy
                int prefer = prefers[i];
                if (status[i] != NOT_SET) {
                    continue;
                }
                for (int tryTime = 0; tryTime < 2; tryTime++, prefer ^= 1) {
                    if (!nodes[i][prefer].possible) {
                        continue;
                    }
                    status[i] = prefer + 2;
                    dq.clear();
                    dq.addLast(i);
                    boolean possible = true;
                    while (!dq.isEmpty() && possible) {
                        int head = dq.removeFirst();
                        Node set = nodes[head][status[head] % 2];
                        Node notSet = nodes[head][status[head] % 2 ^ 1];
                        if (!set.possible) {
                            possible = false;
                            break;
                        }

                        for (Node x : set.out) {
                            if (status[x.index] == -1) {
                                status[x.index] = x.type + 2;
                                dq.addLast(x.index);
                                continue;
                            }
                            if (status[x.index] % 2 != x.type) {
                                possible = false;
                                break;
                            }
                        }
                        for (Node x : notSet.in) {
                            if (status[x.index] == -1) {
                                status[x.index] = (x.type ^ 1) + 2;
                                dq.addLast(x.index);
                                continue;
                            }
                            if (status[x.index] % 2 == x.type) {
                                possible = false;
                                break;
                            }
                        }
                    }

                    if (possible) {
                        for (int j = 0; j < n; j++) {
                            if (status[j] >= 2) {
                                status[j] -= 2;
                            }
                        }
                        break;
                    } else {
                        for (int j = 0; j < n; j++) {
                            if (status[j] >= 2) {
                                status[j] = -1;
                            }
                        }
                    }
                }

                if (status[i] == -1) {
                    valid = false;
                }
            }

            if (valid) {
                for (int i = 0; i < n; i++) {
                    int l = 0;
                    int r = types.length - 1;
                    if (i < start) {
                        l = r = lower[i];
                    } else if (i == start) {
                        l = lower[i] + 1;
                    }
                    for (int j = l; j <= r; j++) {
                        if (types[j] == status[i]) {
                            out.append((char) (j + 'a'));
                            break;
                        }
                    }
                }
                return;
            }
        }

        out.println(-1);
    }

}

class Node {
    List<Node> in = new ArrayList<>();
    List<Node> out = new ArrayList<>();
    int type;
    int index;
    boolean possible;
}
