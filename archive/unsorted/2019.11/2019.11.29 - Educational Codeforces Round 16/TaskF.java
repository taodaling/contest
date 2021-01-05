package contest;

import template.algo.BitDecomposeFramework;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TaskF {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        BitDecomposeFramework<Item> plus = new BitDecomposeFramework<>(this::merge);
        BitDecomposeFramework<Item> sub = new BitDecomposeFramework<>(this::merge);
        int m = in.readInt();

        char[] buf = new char[300000];
        for (int i = 0; i < m; i++) {
            int t = in.readInt();
            if (t == 1) {
                String s = in.readString();
                Item item = getItem(s);
                plus.add(item);
            } else if (t == 2) {
                String s = in.readString();
                Item item = getItem(s);
                sub.add(item);
            } else {
                int len = in.readString(buf, 0);
                long add = 0;
                long del = 0;
                for (Item item : plus) {
                    item.automaton.beginMatching();
                    for (int j = 0; j < len; j++) {
                        item.automaton.match(buf[j]);
                        add += item.automaton.getMatchLast().getPreSum();
                    }
                }
                for (Item item : sub) {
                    item.automaton.beginMatching();
                    for (int j = 0; j < len; j++) {
                        item.automaton.match(buf[j]);
                        del += item.automaton.getMatchLast().getPreSum();
                    }
                }
                out.println(add - del);
                out.flush();
            }
        }
    }

    private Item getItem(String s) {
        Item item = new Item();
        item.list = new ArrayList<>();
        item.list.add(s);
        ACAutomaton automaton = new ACAutomaton('a', 'z');
        item.automaton = automaton;
        automaton.beginBuilding();
        for (int i = 0, until = s.length(); i < until; i++) {
            automaton.build(s.charAt(i));
        }
        automaton.getBuildLast().increaseCnt();
        automaton.endBuilding();
        return item;
    }

    private Item merge(Item a, Item b) {
        a.list.addAll(b.list);
        ACAutomaton automaton = new ACAutomaton('a', 'z');
        a.automaton = automaton;
        for (String s : a.list) {
            automaton.beginBuilding();
            for (int i = 0, until = s.length(); i < until; i++) {
                automaton.build(s.charAt(i));
            }
            automaton.getBuildLast().increaseCnt();
        }
        automaton.endBuilding();
        return a;
    }
}

class Item {
    ACAutomaton automaton;
    List<String> list;
}