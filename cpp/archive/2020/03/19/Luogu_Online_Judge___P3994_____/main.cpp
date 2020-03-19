#include "../../libs/binary_search.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"

using binary_search::BinarySearch;

vector<int> dq;
vector<vector<pair<int, ll>>> adj;
vector<pair<ll, ll>> factors;
vector<ll> depths;
vector<ll> dp;

bool LessThan(double x1, double y1, double x2, double y2, double x3,
              double y3) {
  return (x2 - x1) / (y2 - y1) < (x3 - x2) / (y3 - y2);
}

void Dfs(int root, int p, ll depth, int l, int r) {
  dbg(root, l, r, p, depth);
  dbg2(vector<int>(dq.begin() + l, dq.begin() + r + 1));

  depths[root] = depth;

  l = BinarySearch<int>(l, r, [&](int mid) {
    if (mid == r) {
      return true;
    }
    int p1 = dq[mid + 1];
    int p0 = dq[mid];
    return (dp[p1] - dp[p0]) > (depths[p1] - depths[p0]) * factors[root].first;
  });

  int from = dq[l];
  dbg(root, from);
  dp[root] = dp[from] + (depths[root] - depths[from]) * factors[root].first +
             factors[root].second;

  function<bool(int)> checker2 = [&](int mid) {
    if (mid == l) {
      return false;
    }
    int p1 = dq[mid];
    int p0 = dq[mid - 1];
    return !LessThan(dp[p0], depths[p0], dp[p1], depths[p1], dp[root],
                     depths[root]);
  };
  r = BinarySearch<int>(l, r, checker2);
  if (checker2(r)) {
    r--;
  }
  int recover = dq[r + 1];
  dq[r + 1] = root;

  for (auto &e : adj[root]) {
    if (e.first == p) {
      continue;
    }
    Dfs(e.first, root, depth + e.second, l, r + 1);
  }

  dq[r + 1] = recover;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  dq.resize(n);
  adj.resize(n);
  factors.resize(n);
  depths.resize(n);
  dp.resize(n);

  for (int i = 1; i < n; i++) {
    int f;
    ll s, p, q;
    in >> f >> s >> p >> q;
    f--;
    adj[i].emplace_back(f, s);
    adj[f].emplace_back(i, s);
    factors[i].first = p;
    factors[i].second = q;
  }

  for (auto &e : adj[0]) {
    Dfs(e.first, 0, e.second, 0, 0);
  }

  for (int i = 1; i < n; i++) {
    out << dp[i] << endl;
  }
}

RUN_ONCE