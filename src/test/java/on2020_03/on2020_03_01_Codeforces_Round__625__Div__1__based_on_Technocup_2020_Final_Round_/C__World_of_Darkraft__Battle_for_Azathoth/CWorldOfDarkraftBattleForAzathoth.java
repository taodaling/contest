package on2020_03.on2020_03_01_Codeforces_Round__625__Div__1__based_on_Technocup_2020_Final_Round_.C__World_of_Darkraft__Battle_for_Azathoth;



import template.graph.DirectedEdge;
import template.graph.Graph;
import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.graph.LongHLPP;
import template.primitve.generated.graph.LongISAP;
import template.primitve.generated.graph.LongMaximumCloseSubGraphAdapter;
import template.primitve.generated.graph.LongMaximumFlow;
import template.utils.SequenceUtils;

import java.util.Arrays;
import java.util.List;

public class CWorldOfDarkraftBattleForAzathoth {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int p = in.readInt();
        weapon = new Item[n + 1];
        armor = new Item[m + 1];
        weapon[n] = new Item();
        weapon[n].w = (long) 1e15;
        weapon[n].price = (long) 1e15;
        armor[m] = weapon[n];
        for (int i = 0; i < n; i++) {
            weapon[i] = new Item();
            weapon[i].w = in.readInt();
            weapon[i].price = in.readInt();
        }
        for (int i = 0; i < m; i++) {
            armor[i] = new Item();
            armor[i].w = in.readInt();
            armor[i].price = in.readInt();
        }
        weapon = trim(weapon);
        armor = trim(armor);

        List<DirectedEdge>[] g = Graph.createDirectedGraph(weapon.length +
                armor.length + p);
        long[] w = new long[weapon.length + armor.length + p];
        long basic = weapon[0].price + armor[0].price;
        for (int i = 1; i < weapon.length; i++) {
            w[idOfWeapon(i)] = weapon[i - 1].price - weapon[i].price;
            Graph.addEdge(g, idOfWeapon(i), idOfWeapon(i - 1));
        }
        for (int i = 1; i < armor.length; i++) {
            w[idOfArmor(i)] = armor[i - 1].price - armor[i].price;
            Graph.addEdge(g, idOfArmor(i), idOfArmor(i - 1));
        }

        for (int i = 0; i < p; i++) {
            int x = in.readInt();
            int y = in.readInt();
            int z = in.readInt();
            int weaponId = binarySearch(weapon, x + 1);
            int armorId = binarySearch(armor, y + 1);
            Graph.addEdge(g, idOfMonster(i), idOfWeapon(weaponId));
            Graph.addEdge(g, idOfMonster(i), idOfArmor(armorId));
            w[idOfMonster(i)] = z;
        }

        // System.err.println(Arrays.toString(w));
        LongMaximumFlow flow = new LongHLPP(w.length + 2);
        LongMaximumCloseSubGraphAdapter adapter = new LongMaximumCloseSubGraphAdapter(flow);
        boolean[] selection = new boolean[w.length];
        long max = adapter.maximumCloseSubGraph(g, w, selection);
        max -= basic;
        out.println(max);
    }

    public int binarySearch(Item[] items, int x) {
        int l = 0;
        int r = items.length - 1;
        while (l < r) {
            int m = (l + r) >> 1;
            if (items[m].w >= x) {
                r = m;
            } else {
                l = m + 1;
            }
        }
        return l;
    }

    Item[] weapon;
    Item[] armor;

    public int idOfWeapon(int i) {
        return i;
    }

    public int idOfArmor(int i) {
        return i + weapon.length;
    }

    public int idOfMonster(int i) {
        return i + weapon.length + armor.length;
    }

    public Item[] trim(Item[] items) {
        int n = items.length;
        int len = 1;
        Arrays.sort(items, (a, b) -> Long.compare(a.w, b.w));
        for (int i = 1; i < n; i++) {
            if (items[i].w == items[len - 1].w) {
                if (items[i].price < items[len - 1].price) {
                    SequenceUtils.swap(items, i, len - 1);
                }
                continue;
            }
            while (len > 0 && items[len - 1].price >= items[i].price) {
                len--;
            }
            SequenceUtils.swap(items, i, len);
            len++;
        }
        return Arrays.copyOf(items, len);
    }
}

class Item {
    long w;
    long price;
}
