package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.LongObjectHashMap;
import template.utils.SequenceUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class ETheCakeIsALie {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        Number[] nums = new Number[n + 1];
        for (int i = 1; i <= n; i++) {
            nums[i] = new Number();
            nums[i].val = i;
        }

        Triangle[] triangles = new Triangle[n - 2];
        for (int i = 0; i < n - 2; i++) {
            triangles[i] = new Triangle();
            triangles[i].id = i;
            for (int j = 0; j < 3; j++) {
                triangles[i].vertexes[j] = in.readInt();
                nums[triangles[i].vertexes[j]].list.add(triangles[i]);
                nums[triangles[i].vertexes[j]].cnt++;
            }
        }

        TreeSet<Number> set = new TreeSet<>((a, b) -> a.cnt == b.cnt ? a.val - b.val : a.cnt - b.cnt);
        for (int i = 1; i <= n; i++) {
            set.add(nums[i]);
        }

        int order = 0;
        while (!set.isEmpty()) {
            Number number = set.pollFirst();
            for (Triangle t : number.list) {
                if (t.order > 0) {
                    continue;
                }
                t.order = ++order;
                for (int i = 0; i < 3; i++) {
                    if (t.vertexes[i] == number.val) {
                        SequenceUtils.swap(t.vertexes, i, 1);
                        break;
                    }
                }
                for (int i = 0; i < 3; i++) {
                    boolean removed = set.remove(nums[t.vertexes[i]]);
                    nums[t.vertexes[i]].cnt--;
                    if (removed) {
                        set.add(nums[t.vertexes[i]]);
                    }
                }
            }
        }

        Arrays.sort(triangles, (a, b) -> a.order - b.order);
        map = new LongObjectHashMap<>(3 * n, false);
        for (Triangle t : triangles) {
            putIfNotExist(map, idOfEdge(t.vertexes[0], t.vertexes[1])).addLast(t);
            putIfNotExist(map, idOfEdge(t.vertexes[0], t.vertexes[2])).addLast(t);
            putIfNotExist(map, idOfEdge(t.vertexes[1], t.vertexes[2])).addLast(t);
        }

        triangles[n - 3].handled = true;
        out.append(triangles[n - 3].vertexes[0]).append(' ');
        dfs(triangles[n - 3].vertexes[0], triangles[n - 3].vertexes[1], out);
        out.append(triangles[n - 3].vertexes[1]).append(' ');
        dfs(triangles[n - 3].vertexes[1], triangles[n - 3].vertexes[2], out);
        out.append(triangles[n - 3].vertexes[2]).append(' ');
        dfs(triangles[n - 3].vertexes[2], triangles[n - 3].vertexes[0], out);

        out.println();

        for (Triangle t : triangles) {
            out.append(t.id + 1).append(' ');
        }
        out.println();
    }

    LongObjectHashMap<Deque<Triangle>> map;

    public void dfs(int a, int b, FastOutput out) {
        Deque<Triangle> list = map.remove(idOfEdge(a, b));
        if (list == null) {
            return;
        }
        while (!list.isEmpty()) {
            dfs(a, b, list.removeFirst(), out);
        }
    }

    public void dfs(int a, int b, Triangle t, FastOutput out) {
        if (t.handled) {
            return;
        }
        t.handled = true;

        boolean flip;
        if (a != t.vertexes[0] && b != t.vertexes[0]) {
            flip = a == t.vertexes[2];
        } else if (a != t.vertexes[1] && b != t.vertexes[1]) {
            flip = a == t.vertexes[2];
        } else {
            flip = a == t.vertexes[1];
        }
        if (flip) {
            SequenceUtils.swap(t.vertexes, 0, 2);
        }
        dfs(t.vertexes[0], t.vertexes[1], out);
        out.append(t.vertexes[1]).append(' ');
        dfs(t.vertexes[1], t.vertexes[2], out);
        dfs(t.vertexes[2], t.vertexes[0], out);
    }

    public long idOfEdge(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        return DigitUtils.asLong(a, b);
    }

    public Deque<Triangle> putIfNotExist(LongObjectHashMap<Deque<Triangle>> map, long key) {
        Deque<Triangle> list = map.get(key);
        if (list == null) {
            list = new LinkedList<>();
            map.put(key, list);
        }
        return list;
    }
}

class Triangle {
    int[] vertexes = new int[3];
    boolean handled;
    int order;
    int id;

    @Override
    public String toString() {
        return Arrays.toString(vertexes);
    }
}

class Number {
    int val;
    int cnt;
    List<Triangle> list = new ArrayList<>();
}