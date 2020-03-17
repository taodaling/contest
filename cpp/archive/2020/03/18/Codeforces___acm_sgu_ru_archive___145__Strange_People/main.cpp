#include "../../libs/common.h"
#include "../../libs/debug.h"

struct Edge {
  int a;
  int b;
  int w;
  int other(int x) { return a == x ? b : a; }
};

struct State {
  bitset<100> bs;
  int node;
  int prev;
  int dist;
};

ostream &operator<<(ostream &os, const State &state) {
  os << state.node << "=" << state.dist;
  return os;
}

string to_string(vector<State> &states, int final) {
  stringstream ss;
  ss << endl;
  ss << states[final].dist << ' ';
  vector<int> seq;
  for (; final != -1; final = states[final].prev) {
    seq.push_back(states[final].node);
  }
  reverse(seq.begin(), seq.end());
  ss << seq.size() << endl;
  for (int x : seq) {
    ss << x + 1 << ' ';
  }
  return ss.str();
}

void solve(int testId, istream &in, ostream &out) {
  int n, m, k;
  in >> n >> m >> k;
  vector<State> states;
  vector<Edge> edges(m);
  vector<vector<int>> g(n);
  for (int i = 0; i < m; i++) {
    in >> edges[i].a >> edges[i].b >> edges[i].w;
    edges[i].a--;
    edges[i].b--;
    g[edges[i].a].push_back(i);
    g[edges[i].b].push_back(i);
  }

  int src, dst;
  in >> src >> dst;
  src--;
  dst--;

  vector<int> h(n, 1e9);
  vector<bool> inque(n);
  deque<int> dq;
  h[dst] = 0;
  inque[dst] = false;
  dq.push_back(dst);
  while (!dq.empty()) {
    int head = dq.front();
    dq.pop_front();
    inque[head] = false;
    for (int e : g[head]) {
      int node = edges[e].other(head);
      int d = h[head] + edges[e].w;
      if (d < h[node]) {
        h[node] = d;
        if (!inque[node]) {
          inque[node] = true;
          dq.push_back(node);
        }
      }
    }
  }

  dbg(h);
  function<bool(int, int)> comp = [&](int a, int b) {
    return states[a].dist + h[states[a].node] >
           states[b].dist + h[states[b].node];
  };
  priority_queue<int, vector<int>, function<bool(int, int)>> pq(comp);
  states.emplace_back();
  states.back().bs[src] = true;
  states.back().dist = 0;
  states.back().node = src;
  states.back().prev = -1;
  pq.push(0);

  int final = -1;
  int t = 0;
  while (true) {
    int head = pq.top();
    pq.pop();
    dbg(head, states[head]);

    int cur = states[head].node;
    int dist = states[head].dist;
    if (cur == dst) {
      t++;
      dbg2(to_string(states, head));
      if (t == k) {
        final = head;
        break;
      }
    }

    for (int e : g[cur]) {
      int node = edges[e].other(cur);
      if (states[head].bs[node]) {
        continue;
      }
      states.emplace_back();
      states.back().bs = states[head].bs;
      states.back().bs[node] = true;
      states.back().dist = dist + edges[e].w;
      states.back().node = node;
      states.back().prev = head;
      pq.push(states.size() - 1);
    }
  }

  out << states[final].dist << ' ';
  vector<int> seq;
  for (; final != -1; final = states[final].prev) {
    seq.push_back(states[final].node);
  }
  reverse(seq.begin(), seq.end());
  out << seq.size() << endl;
  for (int x : seq) {
    out << x + 1 << ' ';
  }
}

RUN_ONCE