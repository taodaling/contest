package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CTooHeavy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int[] a = in.ri(n);
        int[] b = in.ri(n);
        int[] p = in.ri(n);
        for (int i = 0; i < n; i++) {
            p[i]--;
        }
        Item[] items = new Item[n];
        for (int i = 0; i < n; i++) {
            items[i] = new Item(b[i], i);
        }
        ps = new Person[n];
        for (int i = 0; i < n; i++) {
            ps[i] = new Person(a[i], i, items[p[i]]);
        }
        Person[] sorted = ps.clone();
        Arrays.sort(sorted, Comparator.comparingInt(x -> x.w));
        for (int i = n - 1; i >= 0; i--) {
            Person person = sorted[i];
            if (person.carry.id == person.id) {
                continue;
            }
            if (person.carry.w >= person.w) {
                out.println(-1);
                return;
            }
            swap(person.id, person.carry.id);
            i++;
        }
        if (!valid) {
            out.println(-1);
            return;
        }
        out.println(op.size());
        for (int[] x : op) {
            out.append(x[0] + 1).append(' ').append(x[1] + 1).println();
        }
    }

    List<int[]> op = new ArrayList<>();
    Person[] ps;
    boolean valid = true;

    public void swap(int i, int j) {
        if (ps[i].carry.w >= ps[i].w || ps[j].carry.w >= ps[j].w) {
            valid = false;
        }
        Item tmp = ps[i].carry;
        ps[i].carry = ps[j].carry;
        ps[j].carry = tmp;
        op.add(new int[]{i, j});
    }
}

class Person {
    int w;
    int id;
    Item carry;

    public Person(int w, int id, Item carry) {
        this.w = w;
        this.id = id;
        this.carry = carry;
    }
}

class Item {
    int w;
    int id;

    public Item(int w, int id) {
        this.w = w;
        this.id = id;
    }
}


