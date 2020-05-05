package on2020_05.on2020_05_05_Codeforces___Codeforces_Round__434__Div__1__based_on_Technocup_2018_Elimination_Round_1_.C__Tests_Renumeration;



import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerDeque;
import template.primitve.generated.datastructure.IntegerDequeImpl;
import template.primitve.generated.datastructure.IntegerList;
import template.utils.Debug;

import java.util.*;

public class CTestsRenumeration {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        File[] files = new File[n];

        int example = 0;
        for (int i = 0; i < n; i++) {
            files[i] = new File();
            files[i].name = in.readString();
            files[i].type = 1 - in.readInt();
            if (files[i].type == 0) {
                example++;
            }
            files[i].id = getId(files[i].name);
        }

        box = new File[n + 1];
        for (File file : files) {
            box[file.id] = file;
        }


        cmds = new ArrayList<>(n * 2);
        if (box[0] == null) {
            //if all right
            boolean ok = true;
            for (int i = 1; i <= example; i++) {
                if (box[i].type == 1) {
                    ok = false;
                }
            }
            if (ok) {
                out.println(0);
                return;
            }

            for (int i = 1; i <= n; i++) {
                if ((box[i].type == 0) == (i <= example)) {
                    continue;
                }
                box[i].name = "dalt";
                cmds.add(new String[]{"" + i, box[i].name});
                box[i].id = 0;
                box[0] = box[i];
                box[i] = null;
                break;
            }
        }


        Deque<File> zero = new ArrayDeque<>(n);
        Deque<File> one = new ArrayDeque<>(n);
        Deque<File> otherZero = new ArrayDeque<>(n);
        Deque<File> otherOne = new ArrayDeque<>(n);
        IntegerDeque nullIndex = new IntegerDequeImpl(n);

        for (File file : files) {
            if (file.id == 0) {
                if (file.type == 0) {
                    otherZero.addLast(file);
                } else {
                    otherOne.addLast(file);
                }
            }
        }

        for (int i = 1; i <= n; i++) {
            if (box[i] == null) {
                nullIndex.addLast(i);
                continue;
            }
            if (box[i].type == 0 && i > example) {
                zero.addLast(box[i]);
            }
            if (box[i].type == 1 && i <= example) {
                one.addLast(box[i]);
            }
        }


        while (!nullIndex.isEmpty()) {
            debug.debug("box", box);
            int index = nullIndex.removeFirst();
            if (index <= example) {
                if (!zero.isEmpty()) {
                    File file = zero.removeFirst();
                    nullIndex.addLast(file.id);
                    move(file, index);
                } else {
                    File file = otherZero.removeFirst();
                    addBox(file, index);
                }
            } else {
                if (!one.isEmpty()) {
                    File file = one.removeFirst();
                    nullIndex.addLast(file.id);
                    move(file, index);
                } else {
                    File file = otherOne.removeFirst();
                    addBox(file, index);
                }
            }
        }

        out.println(cmds.size());
        for (String[] cmd : cmds) {
            out.append("move ").append(cmd[0]).append(' ').append(cmd[1]).println();
        }
    }

    File[] box;
    List<String[]> cmds;
    int n;

    public void addBox(File file, int i) {
        box[i] = file;
        String[] move = new String[2];
        move[0] = file.getName();
        file.id = i;
        move[1] = file.getName();
        cmds.add(move);
    }

    public void move(File file, int to) {
        box[to] = file;
        box[file.id] = null;
        String[] move = new String[2];
        move[0] = file.getName();
        file.id = to;
        move[1] = file.getName();
        cmds.add(move);
    }

    public int getId(String name) {
        int ans = 0;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c == '0' && ans == 0) {
                return 0;
            }
            if (c < '0' || c > '9') {
                return 0;
            }
            ans = ans * 10 + c - '0';
        }

        if (ans <= 0 || ans > n) {
            return 0;
        }
        return ans;
    }
}

class File {
    String name;
    int id;
    int type;

    public String getName() {
        if (id == 0) {
            return name;
        }
        return Integer.toString(id);
    }

    @Override
    public String toString() {
        return getName();
    }
}
