#include "../../libs/common.h"
#include "../../libs/debug.h"

struct Item {
  int cnt;
  int id;
};

ostream &operator<<(ostream &os, const Item &item) {
  os << item.id << ':' << item.cnt;
  return os;
}

bool operator<(const Item &a, const Item &b) {
  return make_tuple(a.cnt, a.id) < make_tuple(b.cnt, b.id);
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<int> deg(n);
  int sum = 0;
  for (int i = 0; i < n; i++) {
    in >> deg[i];
    sum += deg[i];
  }
  out << sum / 2 << endl;
  if (sum == 0) {
    return;
  }

  set<Item> itemSet;
  for (int i = 0; i < n; i++) {
    itemSet.insert({cnt : deg[i], id : i});
  }

  Item cur = *itemSet.rbegin();
  itemSet.erase(cur);
  while (cur.cnt) {
    dbg(itemSet);
    sum -= 2;
    if (sum / 2 < itemSet.rbegin()->cnt) {
      Item next = *itemSet.rbegin();
      itemSet.erase(next);
      out << next.id + 1 << ' ' << cur.id + 1 << endl;
      next.cnt--;
      cur.cnt--;
      if (cur.cnt > 0) {
        itemSet.insert(cur);
      }
      cur = next;
      
      continue;
    }
    if (cur.cnt == 1) {
      Item next = *itemSet.rbegin();
      itemSet.erase(next);
      out << next.id + 1 << ' ' << cur.id + 1 << endl;
      cur = next;
      cur.cnt--;
      continue;
    }

    Item index;
    if (itemSet.begin()->cnt == 1) {
      index = *itemSet.begin();
    } else {
      index = *itemSet.rbegin();
    }
    itemSet.erase(index);
    out << cur.id + 1 << ' ' << index.id + 1 << endl;
    cur.cnt--;
    index.cnt--;
    if (index.cnt > 0) {
      itemSet.insert(index);
    }
  }
}

RUN_ONCE