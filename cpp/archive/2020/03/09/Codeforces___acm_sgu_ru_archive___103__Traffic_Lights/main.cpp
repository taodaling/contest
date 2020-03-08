#include "../../libs/common.h"

#define MAX_N 301

vector<pair<int, int>> g[MAX_N];
int init[MAX_N];
int tb[MAX_N];
int tp[MAX_N];
int dist[MAX_N];
bool handled[MAX_N];
int prev[MAX_N];

int color(int now, int x) {
  int cur = (now + init[x]) % (tb[x] + tp[x]);
  return cur >= tb[x] ? 1 : 0;
}

int next(int now, int x) {
  int cur = (now + init[x]) % (tb[x] + tp[x]);
  if (cur < tb[x]) {
    return tb[x] - cur;
  }
  return tb[x] + tp[x] - cur;
}

int waitUntil(int now, int a, int b) {
  int p1 = 0;
  int p2 = 0;
  int l1 = (tb[b] + tp[b]) * 2 + 1;
  int l2 = (tb[a] + tp[a]) * 2 + 1;
  while (color(now, a) != color(now, b)) {
    int n1 = next(now, a);
    int n2 = next(now, b);
    if (n1 < n2) {
      p1++;
    } else {
      p2++;
    }
    if (p1 > l1 || p2 > l2) {
      return -1;
    }
    now += min(n1, n2);
  }
  return now;
}

void solve(int testId, istream &in, ostream &out) {
  int src, dst;
  in >> src >> dst;
  int n, m;
  in >> n >> m;
  for (int i = 1; i <= n; i++) {
    char ci;
    int ri, tib, tip;
    in >> ci >> ri >> tib >> tip;
    int total = tib + tip;
    int time = tib;
    if (ci == 'P') {
      time = tib + tip;
    }
    time = ((time - ri) % total + total) % total;
    init[i] = time;
    tb[i] = tib;
    tp[i] = tip;
  }
  for (int i = 0; i < m; i++) {
    int a, b, len;
    in >> a >> b >> len;
    g[a].emplace_back(b, len);
    g[b].emplace_back(a, len);
  }
  fill(dist, dist + MAX_N, (int)1e9);
  fill(prev, prev + MAX_N, -1);
  dist[src] = 0;
  while (true) {
    int head = -1;
    for (int j = 1; j <= n; j++) {
      if (handled[j]) {
        continue;
      }
      if (head == -1 || dist[head] > dist[j]) {
        head = j;
      }
    }
    if(head == -1){
      break;
    }
    handled[head] = true;

    for (auto &e : g[head]) {
      int to = e.first;
      int len = e.second;
      int until = waitUntil(dist[head], head, to);
      error(dist[head], head, to, until);
      if(until < 0 || until + len >= dist[to]){
        continue;
      }
      dist[to] = until + len;
      prev[to] = head;
    }
  }

  if(prev[dst] == -1){
    out << 0 << endl;
    return;
  }
  
  out << dist[dst] << endl;
  vector<int> ans;
  for(int trace = dst; trace != -1; trace = prev[trace])
  {
    ans.push_back(trace);
  }
  reverse(ans.begin(), ans.end());

  for(int x : ans){
    out << x << ' ';
  }
}

RUN_ONCE