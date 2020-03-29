#include "../../libs/common.h"
#include "../../libs/debug.h"

struct Item {
  int a;
  int b;
  int index;
};

bool operator<(const Item &a, const Item &b) {
  return make_pair(a.a, -a.b) < make_pair(b.a, -b.b);
}

ostream &operator<<(ostream &os, const Item &item) {
  os << '(' << item.a << ',' << item.b << ',' << item.index << ')';
  return os;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Item> items(n);
  for (int i = 0; i < n; i++) {
    in >> items[i].a >> items[i].b;
    items[i].index = i;
  }

  vector<int> prev(n, -1);
  vector<int> dp(n, 0);
  map<int, int> dq;
  sort(items.begin(), items.end());

  for (int i = 0; i < n; i++) {
    auto iter = dq.lower_bound(items[i].b);
    dp[i] = 1;
    if (iter != dq.begin()) {
      iter--;
      prev[i] = iter->second;
      dp[i] += dp[prev[i]];
    }
    while (true) {
      auto lb = dq.lower_bound(items[i].b);
      if (lb == dq.end()) {
        break;
      }
      if (dp[lb->second] > dp[i]) {
        break;
      }
      dq.erase(lb);
    }
    dq[items[i].b] = i;
    //dbg(i, dq);
  }

  dbg(prev);
  dbg(items);
  out << dp[(--dq.end())->second] << endl;
  for (int i = (--dq.end())->second; i != -1; i = prev[i]) {
    out << items[i].index + 1 << ' ';
  }
}

RUN_ONCE