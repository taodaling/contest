#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/dsu.h"

struct Edge {
  int a;
  int b;
  bool visited;
};

vector<vector<int>> g;
vector<int> types;
vector<vector<int>> typeG;
vector<int> trace;
vector<Edge> edges;
vector<bool> occur;
vector<vector<int>> pend;
dsu::DSU<10000> dset;

void Dfs(int e) {
  int type = types[edges[e].b];
  while (!typeG[type].empty()) {
    int back = typeG[type].back();
    typeG[type].pop_back();
    if (edges[back].visited) {
      continue;
    }
    if (types[edges[back].a] == types[edges[back].b]) {
      continue;
    }
    edges[back].visited = true;
    if (types[edges[back].a] != type) {
      swap(edges[back].a, edges[back].b);
    }
    Dfs(back);
  }
  trace.push_back(e);
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  g.resize(n);
  types.resize(n, -1);
  trace.reserve(n + 1);
  edges.resize(m);
  typeG.reserve(n);
  occur.resize(n);
  pend.resize(n);

  for (int i = 0; i < m; i++) {
    int a, b;
    in >> a >> b;
    a--;
    b--;
    edges[i].a = a;
    edges[i].b = b;
    g[edges[i].a].push_back(i);
    g[edges[i].b].push_back(i);
  }

  for (int i = 0; i < n; i++) {
    if (types[i] != -1) {
      continue;
    }
    types[i] = typeG.size();
    typeG.emplace_back();
    if (g[i].size() == 2) {
      continue;
    }
    for (int e : g[i]) {
      int node = edges[e].a == i ? edges[e].b : edges[e].a;
      if (g[node].size() == 2) {
        continue;
      }
      types[node] = types[i];
    }
  }

  for (int i = 0; i < n; i++) {
    typeG[types[i]].insert(typeG[types[i]].end(), g[i].begin(), g[i].end());
  }
  dbg(typeG);

  for (auto &e : edges) {
    dset.merge(e.a, e.b);
  }
  bool valid = true;
  for (int i = 0; i < n; i++) {
    if (dset.find(i) != dset.find(0)) {
      valid = false;
    }
  }

  for (int i = 0; i < typeG.size(); i++) {
    if (typeG[i].size() % 2) {
      valid = false;
    }
  }

  if (!valid) {
    out << -1;
    return;
  }

  for (int i = 0; i < edges.size(); i++) {
    if (types[edges[i].a] != types[edges[i].b]) {
      edges[i].visited = true;
      Dfs(i);
      break;
    }
  }
  reverse(trace.begin(), trace.end());

  dbg(g);
  dbg(typeG);
  dbg(types);
  dbg(trace);
  for (int t : trace) {
    occur[edges[t].a] = true;
    occur[edges[t].b] = true;
    dbg(edges[t].a, edges[t].b);
  }
  for (int i = 0; i < n; i++) {
    if (!occur[i]) {
      pend[types[i]].push_back(i);
    }
  }

  vector<int> ans;
  ans.reserve(n);
  ans.push_back(edges[trace[0]].a);
  ans.push_back(edges[trace[0]].b);
  for (int i = 1; i < trace.size(); i++) {
    if (edges[trace[i - 1]].b != edges[trace[i]].a) {
      int t = types[edges[trace[i]].a];
      if (!pend[t].empty()) {
        ans.insert(ans.end(), pend[t].begin(), pend[t].end());
        pend[t].clear();
      }
      ans.push_back(edges[trace[i]].a);
    }
    ans.push_back(edges[trace[i]].b);
  }

  if (ans.size() > 1 && ans[0] == ans[ans.size() - 1]) {
    ans.pop_back();
  }
  for (int i = 0; i < pend.size(); i++) {
    if (!pend[i].empty()) {
      ans.insert(ans.end(), pend[i].begin(), pend[i].end());
    }
  }

  for (int x : ans) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE