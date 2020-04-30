#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/lca.h"
#include "../../libs/version_array.h"

vector<int> size;
vector<int> dfn;
vector<int> parent;
vector<int> vparent;
vector<vector<int>> g;
vector<int> colors;
version_array::VersionArray<bool, 200000> va(false);
vector<vector<int>> regs;
vector<int> sub;
vector<int> vtree;
lca::Lca lcaOnTree;
deque<int> dq;
int order = 0;

void add(int x) {
  if (!va[x]) {
    va[x] = true;
    sub[x] = 0;
    vtree.push_back(x);
  }
}

ll pick2(ll n) { return n * (n - 1) / 2; }

void dfs(int root, int p) {
  dfn[root] = order++;
  size[root]++;
  parent[root] = p;
  for (int node : g[root]) {
    if (node == p) {
      continue;
    }
    dfs(node, root);
    size[root] += size[node];
  }
}

void solve0(int testId, istream &in, ostream &out, int n) {
  dbg(n);
  size.clear();
  dfn.clear();
  parent.clear();
  vparent.clear();
  g.clear();
  colors.clear();
  regs.clear();
  sub.clear();

  size.resize(n);
  dfn.resize(n);
  parent.resize(n);
  vparent.resize(n);
  g.resize(n);
  colors.resize(n);
  regs.resize(n);
  sub.resize(n);

  for (int i = 0; i < n; i++) {
    in >> colors[i];
    colors[i]--;
    regs[colors[i]].push_back(i);
  }

  for (int i = 1; i < n; i++) {
    int a, b;
    in >> a >> b;
    a--;
    b--;
    g[a].push_back(b);
    g[b].push_back(a);
  }

  lcaOnTree.init(g, 0);
  dfs(0, -1);

  ll ans = n * pick2(n);
  function<bool(int, int)> comp = [](int a, int b) { return dfn[a] < dfn[b]; };
  for (int i = 0; i < n; i++) {
    va.clear();
    vtree.clear();
    dq.clear();

    for (int node : regs[i]) {
      add(node);
      for (int next : g[node]) {
        if (next == parent[node]) {
          continue;
        }
        add(next);
      }
    }
    add(0);

    sort(vtree.begin(), vtree.end(), comp);
    for (int i = 1, until = vtree.size(); i < until; i++) {
      add(lcaOnTree(vtree[i - 1], vtree[i]));
    }
    sort(vtree.begin(), vtree.end(), comp);
    for (int node : vtree) {
      while (!dq.empty() && lcaOnTree(dq.back(), node) != dq.back()) {
        dq.pop_back();
      }
      if (dq.empty()) {
        vparent[node] = -1;
      } else {
        vparent[node] = dq.back();
      }
      dq.push_back(node);
    }

    reverse(vtree.begin(), vtree.end());
    for (int node : vtree) {
      if (colors[node] == i) {
        sub[node] = size[node];
      }
      if (vparent[node] != -1) {
        sub[vparent[node]] += sub[node];
      }
      if (colors[node] != i &&
          (vparent[node] == -1 || colors[vparent[node]] == i)) {
        ans -= pick2(size[node] - sub[node]);
      }
    }
  }
  out << "Case #" << testId << ": " << ans << endl;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  while (in >> n) {
    solve0(testId, in, out, n);
    testId++;
  }
}

RUN_ONCE