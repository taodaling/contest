#include "../../libs/common.h"
#include "../../libs/util.h"

struct Node {
  int id;
  Node *l;
  Node *r;
  Node *p;
  int a;
  int b;
} NIL;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Node> nodes(n);
  for (int i = 0; i < n; i++) {
    nodes[i].id = i + 1;
    in >> nodes[i].a >> nodes[i].b;
    nodes[i].p = nodes[i].l = nodes[i].r = &NIL;
  }

  vector<int> index = util::Range(0, n - 1);
  sort(index.begin(), index.end(),
       [&](auto &a, auto &b) { return nodes[a].a < nodes[b].a; });

  deque<int> dq;
  for (int i : index) {
    while (!dq.empty() && nodes[dq.back()].b > nodes[i].b) {
      int back = dq.back();
      dq.pop_back();
      nodes[back].r = nodes[i].l;
      nodes[i].l = &nodes[back];
    }
    dq.push_back(i);
  }
  while (dq.size() > 1) {
    int back = dq.back();
    dq.pop_back();
    nodes[dq.back()].r = &nodes[back];
  }

  for (int i = 0; i < n; i++) {
    nodes[i].l->p = &nodes[i];
    nodes[i].r->p = &nodes[i];
  }


  out << "YES" << endl;
  for (int i = 0; i < n; i++) {
    out << nodes[i].p->id << ' ' << nodes[i].l->id << ' ' << nodes[i].r->id
        << endl;
  }
}

RUN_ONCE