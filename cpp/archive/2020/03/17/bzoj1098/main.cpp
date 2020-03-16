#include "../../libs/common.h"
#include "../../libs/hash.h"

unordered_map<ll, bool, hash::CustomHash> umap;

ll Merge(ll a, ll b) { return (a << 32) | (b & (((ll)1 << 32) - 1)); }

ll Key(ll a, ll b) {
  if (a > b) {
    swap(a, b);
  }
  return Merge(a, b);
}

bool Exist(ll a, ll b) {
  ll key = Key(a, b);
  return umap.find(key) != umap.end();
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  vector<int> ans;
  deque<int> unhandled;
  for (int i = 0; i < n; i++) {
    unhandled.push_back(i);
  }

  for (int i = 0; i < m; i++) {
    int u, v;
    in >> u >> v;
    u--;
    v--;
    umap[Key(u, v)] = true;
  }

  while (!unhandled.empty()) {
    deque<int> wait;
    wait.push_back(unhandled.front());
    unhandled.pop_front();
    int size = 0;

    while (!wait.empty()) {
      int head = wait.front();
      wait.pop_front();
      size++;

      deque<int> skip;
      while (!unhandled.empty()) {
        int cand = unhandled.front();
        unhandled.pop_front();
        if (!Exist(head, cand)) {
          wait.push_back(cand);
        } else {
          skip.push_back(cand);
        }
      }
      unhandled.swap(skip);
    }

    ans.push_back(size);
  }
  sort(ans.begin(), ans.end());
  out << ans.size() << endl;
  for (int i = 0; i < ans.size(); i++) {
    out << ans[i] << ' ';
  }
}

RUN_ONCE