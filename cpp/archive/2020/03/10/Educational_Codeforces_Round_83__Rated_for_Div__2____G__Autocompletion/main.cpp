#include "../../libs/common.h"
#include "../../libs/debug.h"

const int charset = 'z' - 'a' + 1;

#ifndef SEGMENT_H
#define SEGMENT_H
namespace segment {
class Segment {
 public:
  Segment(int l, int r) {
    _dirty = 1e9;
    int m = (l + r) >> 1;
    if (l < r) {
      _l = new Segment(l, m);
      _r = new Segment(m + 1, r);
      pushUp();
    } else {
      _index = l;
    }
  }

#define NO_INTERSECTION ql > r || qr < l
#define COVER ql <= l &&qr >= r
#define RANGE (min(qr, r) - max(l, ql) + 1)

  void update(int ql, int qr, int l, int r, int x) {
    if (NO_INTERSECTION) {
      return;
    }
    if (COVER) {
      modify(x);
      return;
    }
    pushDown();
    int m = (l + r) >> 1;
    _l->update(ql, qr, l, m, x);
    _r->update(ql, qr, m + 1, r, x);
    pushUp();
  }

  int query(int ql, int qr, int l, int r) {
    if (NO_INTERSECTION) {
      return 1e9;
    }
    if (COVER) {
      return _dirty + _index;
    }
    pushDown();
    int m = (l + r) >> 1;
    return min(_l->query(ql, qr, l, m), _r->query(ql, qr, m + 1, r));
  }

#undef NO_INTERSECTION
#undef COVER

 private:
  Segment *_l, *_r;
  int _index;
  int _dirty;

  void modify(int d) { _dirty = min(_dirty, d); }

  void pushDown() {
    _l->modify(_dirty);
    _r->modify(_dirty);
  }

  void pushUp() {}
};
}  // namespace segment

#endif  // SEGMENT_H

using segment::Segment;

struct Node {
  Node() {
    memset(next, 0, sizeof(next));
    l = 0;
    r = 0;
    word = 0;
    dist = 0;
    c = 0;
    parent = 0;
  }

  int l;
  int r;
  bool word;
  int dist;
  char c;
  Node *parent;
  Node *next[charset];
  Node *get(int i) {
    if (next[i] == 0) {
      next[i] = new Node();
      next[i]->c = i + 'a';
      next[i]->parent = this;
    }
    return next[i];
  }

  string to_string(){
    if(parent == 0){
      return "";
    }
    return parent->to_string() + c;
  }

};

int order = 0;
void dfs(Node *root) {
  if (root == 0) {
    return;
  }
  if (root->word) {
    root->l = order++;
  } else {
    root->l = order;
  }
  for (int i = 0; i < charset; i++) {
    dfs(root->next[i]);
  }
  root->r = order - 1;
}

void dfs2(Node *root, Segment *seg, int step) {
  if (root == 0) {
    return;
  }
  if (root->word) {
    step = min(step, seg->query(root->l, root->l, 0, order));
    root->dist = step;
  }
  int plus = 0;
  if (root->word) {
    plus++;
  }
  seg->update(root->l, root->r, 0, order, plus + step + 1 - root->l);
  for (int i = 0; i < charset; i++) {
    dfs2(root->next[i], seg, step + 1);
  }
}

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<Node *> nodes(n + 1);
  nodes[0] = new Node();
  for (int i = 1; i <= n; i++) {
    int last;
    char c;
    in >> last >> c;
    nodes[i] = nodes[last]->get(c - 'a');
  }
  int k;
  in >> k;
  vector<Node *> qs(k);
  for (int i = 0; i < k; i++) {
    int x;
    in >> x;
    qs[i] = nodes[x];
    qs[i]->word = true;
    //dbg(qs[i]->to_string());
  }

  dbg(0, order);
  dfs(nodes[0]);
  Segment *seg = new Segment(0, order);
  dfs2(nodes[0], seg, 0);
  for (int i = 0; i < k; i++) {
    out << qs[i]->dist << endl;
  }
}

RUN_ONCE