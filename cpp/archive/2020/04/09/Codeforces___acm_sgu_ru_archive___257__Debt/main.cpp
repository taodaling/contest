#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/max_flow.h"

using namespace max_flow;
using Edge = FlowEdge<int>;

void solve(int testId, istream &in, ostream &out) {
  vector<int> reqs(3);
  in >> reqs[0] >> reqs[1] >> reqs[2];
  int n;
  in >> n;

  vector<int> types(n);
  vector<int> cnts(1 << 3);
  vector<vector<int>> alloc(3, vector<int>(1 << 8));
  for (int i = 0; i < n; i++) {
    int bit = 0;
    for (int j = 0; j < 3; j++) {
      char c;
      in >> c;
      if (c == 'B') {
        bit |= 1 << j;
      }
    }
    types[i] = bit;
    cnts[bit]++;
  }

  int src = 8;
  int dst = 9;
  int offset = dst + 1;

  int inf = 1e8;
  vector<vector<Edge>> g(offset + 3);
  for (int i = 0; i < 8; i++) {
    AddEdge(g, src, i, cnts[i]);
  }

  for (int i = 0; i < 8; i++) {
    for (int j = 0; j < 3; j++) {
      if (bits::BitAt(i, j)) {
        AddEdge(g, i, offset + j, inf);
      }
    }
  }

  for (int i = 0; i < 3; i++) {
    AddEdge(g, offset + i, dst, reqs[i] / 2);
  }

  Dinic<int> dinic(offset + 3);
  dinic.send(g, src, dst, inf);
  dbg(g);

  for (int i = 0; i < 8; i++) {
    for (auto &e : g[i]) {
      if (!e.real) {
        continue;
      }
      int f = e.flow;
      alloc[e.to - offset][i] += f;
      reqs[e.to - offset] -= f * 2;
    }
  }

  dbg(reqs);
  dbg(alloc);
  string ch = "POS";
  stringstream ss;
  for (int i = 0; i < n; i++) {
    int give = -1;
    for (int j = 0; j < 3; j++) {
      if (alloc[j][types[i]] > 0) {
        give = j;
        alloc[j][types[i]]--;
        break;
      }
    }
    if (give == -1) {
      give = 0;
      for (int j = 0; j < 3; j++) {
        if (reqs[j] > 0) {
          give = j;
          reqs[j]--;
          break;
        }
      }
    }
    ss << ch[give];
  }

  for (int i = 0; i < 3; i++) {
    if (reqs[i] > 0) {
      out << "no solution";
      return;
    }
  }

  out << ss.str();
}

RUN_ONCE