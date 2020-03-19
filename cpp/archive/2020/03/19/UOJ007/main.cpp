#include "../../libs/binary_search.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"
#define double long double

using binary_search::BinarySearch;

struct Vertex {
  ll l;
  ll p;
  ll q;
};

vector<Vertex> vs;
vector<vector<pair<int, ll>>> adj;
vector<ll> dp;
vector<ll> depths;
vector<ll> dq;

double Slope(double x1, double y1, double x2, double y2) {
  return (y2 - y1) / (x2 - x1);
}

void Dfs(int root, int p, ll depth, int r) {
  dbg2(root);
  dbg2(vector<int>(dq.begin(), dq.begin() + r + 1));
  depths[root] = depth;

  function<bool(int)> check1 = [&](int mid) {
    if (depths[root] - depths[dq[mid]] > vs[root].l) {
      return false;
    }

    if (mid == r) {
      return true;
    }

    int i0 = dq[mid];
    int i1 = dq[mid + 1];
    return !(dp[i1] - dp[i0] <= vs[root].p * (depths[i1] - depths[i0]));
  };

  int l = BinarySearch<int>(0, r, check1);
  dbg(root, l, dq[l]);
  dp[root] = dp[dq[l]] + (depths[root] - depths[dq[l]]) * vs[root].p + vs[root].q;

  function<bool(int)> check2 = [&](int mid) {
    if (mid == 0) {
      return false;
    }
    int i0 = dq[mid - 1];
    int i1 = dq[mid];
    return Slope(depths[i0], dp[i0], depths[i1], dp[i1]) >=
           Slope(depths[i1], dp[i1], depths[root], dp[root]);
  };
  r = BinarySearch<int>(0, r, check2);
  if (check2(r)) {
    r--;
  }
  int recover = dq[r + 1];
  dq[r + 1] = root;
  for (auto &e : adj[root]) {
    if (e.first == p) {
      continue;
    }
    Dfs(e.first, root, depth + e.second, r + 1);
  }
  dq[r + 1] = recover;
}

void solve(int testId, istream &in, ostream &out) {
  int n, t;
  in >> n >> t;

  vs.resize(n);
  adj.resize(n);
  dp.resize(n);
  depths.resize(n);
  dq.resize(n);
  for (int i = 1; i < n; i++) {
    int f;
    ll s, p, q, l;
    in >> f >> s >> p >> q >> l;
    f--;
    vs[i].p = p;
    vs[i].q = q;
    vs[i].l = l;
    adj[i].emplace_back(f, s);
    adj[f].emplace_back(i, s);
  }

  for (auto &e : adj[0]) {
    Dfs(e.first, 0, e.second, 0);
  }

  for (int i = 1; i < n; i++) {
    out << dp[i] << endl;
  }
}

RUN_ONCE