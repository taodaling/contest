#include "../../libs/binary_search.h"
#include "../../libs/common.h"

const int LOG = 7;
const double PREC = 1e-4;

struct Edge {
  int to;
  int t;
  int c;
};

vector<vector<Edge>> g;
vector<int> trace;
vector<int> dists;
vector<int> prev;
vector<bool> inque;
vector<int> visited;

inline double Fix(Edge &e, double c) { return -(e.c - e.t * c); }

bool Check(double c) {
  //找最大权重环
  int n = g.size();
  fill(dists.begin(), dists.end(), 0);
  fill(prev.begin(), prev.end(), -1);
  fill(inque.begin(), inque.end(), true);
  fill(visited.begin(), visited.end(), false);
  deque<int> dq;
  for (int i = 0; i < n; i++) {
    dq.push_back(i);
  }

  while (dq.size()) {
    int head = dq.front();
    dq.pop_front();
    inque[head] = false;
    visited[head]++;
    if (visited[head] > n) {
      return false;
    }
    for (auto &e : g[head]) {
      if (dists[e.to] <= PREC + dists[head] + Fix(e, c)) {
        continue;
      }
      dists[e.to] = dists[head] + Fix(e, c);
      prev[e.to] = head;
      if (!inque[e.to]) {
        inque[e.to] = true;
        dq.push_back(e.to);
      }
    }
  }

  return true;
}

void solve(int testId, istream &in, ostream &out) {
  int n, m;
  in >> n >> m;
  g.resize(n);
  prev.resize(n);
  dists.resize(n);
  inque.resize(n);
  visited.resize(n);

  for (int i = 0; i < m; i++) {
    int u, v, c, t;
    in >> u >> v >> c >> t;
    u--;
    v--;
    g[u].push_back({to : v, t : t, c : c});
  }

  double c = binary_search::BinarySearch<double>(0, 100, Check, 1e-6, 1e-6);
  Check(c);

  int index = 0;
  for (int i = 1; i < n; i++) {
    if (best[LOG - 1][i][i] > best[LOG - 1][index][index]) {
      index = i;
    }
  }

  if (best[LOG - 1][index][index] < 0) {
    out << 0;
    return;
  }

  Record(LOG - 1, index, index);
  trace.pop_back();
  out << trace.size() << endl;
  for (int x : trace) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE