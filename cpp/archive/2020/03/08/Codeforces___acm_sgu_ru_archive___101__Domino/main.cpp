#include "../../libs/common.h"

#define V 7
#define N 100

struct Edge {
  int s;
  int t;
  bool visit;
  int other(int x) { return x == s ? t : s; }
};

vector<int> options[V][V];
Edge edges[N];
vector<int> et;
vector<int> g[V];

void dfs(int root) {
  while (!g[root].empty()) {
    int e = g[root].back();
    g[root].pop_back();
    if (edges[e].visit) {
      continue;
    }
    edges[e].visit = true;
    dfs(edges[e].other(root));
  }
  et.push_back(root);
}

bool findET() {
  vector<int> odd;
  for (int i = 0; i < V; i++) {
    if (g[i].size() % 2 == 1) {
      odd.push_back(i);
    }
  }

  if (odd.size() == 0) {
    dfs(edges[0].s);
  } else if (odd.size() == 2) {
    dfs(odd.front());
  } else {
    return false;
  }

  for (int i = 0; i < V; i++) {
    if (!g[i].empty()) {
      return false;
    }
  }
  return true;
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  for (int i = 0; i < n; i++) {
    int a, b;
    in >> a >> b;
    edges[i].s = a;
    edges[i].t = b;
    options[a][b].push_back(i + 1);
    g[a].push_back(i);
    g[b].push_back(i);
  }
  if (!findET()) {
    out << "No solution";
    return;
  }

  for (int i = 1; i < et.size(); i++) {
    int last = et[i - 1];
    int cur = et[i];
    if (!options[last][cur].empty()) {
      out << options[last][cur].back() << " +" << endl;
      options[last][cur].pop_back();
    } else {
      out << options[cur][last].back() << " -" << endl;
      options[cur][last].pop_back();
    }
  }
}

RUN_ONCE