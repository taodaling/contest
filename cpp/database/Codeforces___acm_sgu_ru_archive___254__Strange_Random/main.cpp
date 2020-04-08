#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/modular.h"

using modular::Mod;

struct Node {
  int prev;
  int next;
  int val;
};

Node nodes[2000000];

void solve(int testId, istream& in, ostream& out) {
  int n, q;
  in >> n >> q;

  for (int i = 0; i < n; i++) {
    nodes[i].val = i;
  }
  for (int i = 1; i < n; i++) {
    nodes[i].prev = i - 1;
  }
  for (int i = 0; i < n - 1; i++) {
    nodes[i].next = i + 1;
  }
  nodes[0].prev = n - 1;
  nodes[n - 1].next = 0;

  int now = 0;
  for (int i = 0; i < n - 1; i++) {
    int rm = -1;
    if (nodes[now].val & 1) {
      for (int i = 0; i < q; i++) {
        now = nodes[now].next;
      }
      rm = nodes[now].prev;
    } else {
      for (int i = 0; i < q; i++) {
        now = nodes[now].prev;
      }
      rm = nodes[now].next;
    }
    nodes[nodes[rm].prev].next = nodes[rm].next;
    nodes[nodes[rm].next].prev = nodes[rm].prev;
  }

  out << nodes[now].val;
}

RUN_ONCE