#include "../../libs/binary_search.h"
#include "../../libs/common.h"

const int LOG = 7;
const double PREC = 1e-6;

struct Edge {
  int to;
  int t;
  int c;
};

vector<vector<Edge>> g;
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
  fill(visited.begin(), visited.end(), 0);
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
      if (dists[e.to] < dists[head] + Fix(e, c)) {
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

  double c;
  {
    double l = 0;
    double r = 100;
    while (r - l > 1e-8) {
      double m = (l + r) / 2;
      if (Check(m)) {
        r = m;
      } else {
        l = m;
      }
    }
    c = l;
  }
  
  if (Check(c)) {
    out << 0;
    return;
  }

  int index = -1;
  for (int i = 0; i < n; i++) {
    if (visited[i] > n) {
      index = i;
      break;
    }
  }

  int l, r;
  vector<int> occur(n, -1);
  vector<int> trace;
  while (index != -1 && trace.size() <= n) {
    trace.push_back(index);
    index = prev[index];
  }
  for (int i = 0; i < trace.size(); i++) {
    if (occur[trace[i]] == -1) {
      occur[trace[i]] = i;
    } else {
      l = occur[trace[i]];
      r = i - 1;
      break;
    }
  }

  out << r - l + 1 << endl;
  for (int i = r; i >= l; i--) {
    out << trace[i] + 1 << ' ';
  }
}

RUN_ONCE