package on2020_04.on2020_04_26_Codeforces___Codeforces_Round__443__Div__1_.C__Tournament;



import template.io.FastInput;
import template.io.FastOutput;
import template.utils.Debug;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class CTournament {

    TreeMap<Integer, Node>[] tops;
    TreeMap<Integer, Node>[] bots;
    int k;

    Debug debug = new Debug(true);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        k = in.readInt();

        Node min = new Node(k);
        tops = new TreeMap[k];
        bots = new TreeMap[k];
        for (int i = 0; i < k; i++) {
            tops[i] = new TreeMap<>();
            bots[i] = new TreeMap<>();
            tops[i].put(0, min);
            bots[i].put(0, min);
        }

        for (int i = 0; i < n; i++) {
            Node newer = new Node(k);
            for (int j = 0; j < k; j++) {
                newer.bot[j] = newer.top[j] = in.readInt();
            }

            Node top = null;
            Node bot = null;
            for (int j = 0; j < k; j++) {
                Map.Entry<Integer, Node> floor = tops[j].ceilingEntry(newer.top[j]);
                if (floor != null) {
                    top = min(top, floor.getValue());
                }
                Map.Entry<Integer, Node> ceil = bots[j].floorEntry(newer.top[j]);
                if (ceil != null) {
                    bot = max(bot, ceil.getValue());
                }
            }


            debug.debug("newer", newer);
            debug.debug("bot", bot);
            debug.debug("top", top);
            if (top == null) {
                install(newer);
            } else if (bot.top[0] < top.top[0]) {
                //insert into the middle
                install(newer);
            } else {
                //merge all
                uninstall(bot);
                while (bot != top) {
                    Node prev = tops[0].floorEntry(bot.top[0]).getValue();
                    uninstall(prev);
                    prev.mergeInto(bot);
                    bot = prev;
                }
                bot.mergeInto(newer);
                install(bot);
            }

            int ans = tops[0].lastEntry().getValue().size;
            out.println(ans);

            debug.debug("tops[0]", tops[0]);
        }
    }

    public void uninstall(Node node) {
        for (int i = 0; i < k; i++) {
            tops[i].remove(node.top[i]);
            bots[i].remove(node.bot[i]);
        }
    }

    public void install(Node node) {
        for (int i = 0; i < k; i++) {
            tops[i].put(node.top[i], node);
            bots[i].put(node.bot[i], node);
        }
    }

    public Node min(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.top[0] < b.top[0] ? a : b;
    }

    public Node max(Node a, Node b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return a.top[0] > b.top[0] ? a : b;
    }
}

class Node {
    int[] top;
    int[] bot;
    int size = 1;

    public Node(int k) {
        top = new int[k];
        bot = new int[k];
    }

    void mergeInto(Node other) {
        int k = top.length;
        for (int i = 0; i < k; i++) {
            top[i] = Math.max(top[i], other.top[i]);
            bot[i] = Math.min(bot[i], other.bot[i]);
        }
        size += other.size;
    }

    @Override
    public String toString() {
        return Arrays.toString(top) + " > " + Arrays.toString(bot);
    }
}