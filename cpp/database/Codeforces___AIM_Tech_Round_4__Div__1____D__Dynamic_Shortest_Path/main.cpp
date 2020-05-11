#include "../../libs/common.h"

const int N = 100000;
const int M = 100000;
const int Q = 1000000 + 1;
const ll INF = 1e18;

struct Node {
  ll dist;
  ll last;
  vector<int> next;
};

struct Edge {
  int to;
  ll w;
  ll reserve;
};

Node nodes[N];
deque<int> dq[Q];
Edge edges[M];

void solve(int testId, istream &in, ostream &out) {
  int n, m, q;
  in >> n >> m >> q;

  for (int i = 0; i < n; i++) {
    nodes[i].dist = INF;
  }
  for (int i = 0; i < m; i++) {
    int a, b, c;
    in >> a >> b >> c;
    a--;
    b--;
    nodes[a].next.push_back(Edge{to : b, w : c, reserve : c});
  }

  priority_queue<pair<int, ll>, vector<pair<int, ll>>,
                 function<bool(pair<int, ll> &, pair<int, ll> &)>>
      pq([&](pair<int, ll> &a, pair<int, ll> &b) {
        return Y(a) == Y(b) ? X(a) > X(b) : Y(a) > Y(b);
      });

  nodes[0].dist = 0;
  pq.push(make_pair(0, 0));
  while (!pq.empty()) {
    auto p = pq.top();
    pq.pop();

    int head = X(p);
    ll dist = Y(p);
    for (auto &e : nodes[head].next) {
      if (nodes[e.to].dist > dist + e.w) {
        nodes[e.to].dist = dist + e.w;
        pq.push(make_pair(e.to, nodes[e.to].dist));
      }
    }
  }

  for (int i = 0; i < q; i++) {
    int t;
    in >> t;
    if (t == 1) {
      int v;
      in >> v;
      v--;
      out << (nodes[v].dist > 1e12 ? -1 : nodes[v].dist) << endl;
      continue;
    }

    int c;
    in >> c;
    for (int j = 0; j < c; j++) {
     
    }
  }
}

RUN_ONCE