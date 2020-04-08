#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/modular.h"

using modular::Mod;

struct Node {
  int prev;
  int next;
};

Node nodes[2000000];

void solve(int testId, istream& in, ostream& out) {
  int n, q;
  in >> n >> q;

  for (int i = 0; i < n; i++) {
    nodes[i].prev = (i - 1 + n) % n;
    nodes[i].next = (i + 1) % n;
  }

  int now = 0;
  for (int i = 0; i < n - 1; i++) {
    if ((now + 1) & 1) {
      for (int i = 0; i < q - 1; i++) {
        now = nodes[now].next;
      }
    } else {
      for (int i = 0; i < q - 1; i++) {
        now = nodes[now].prev;
      }
    }
    nodes[nodes[now].prev].next = nodes[now].next;
    nodes[nodes[now].next].prev = nodes[now].prev;
    now = nodes[now].next;
  }

  out << now + 1;
}

RUN_ONCE