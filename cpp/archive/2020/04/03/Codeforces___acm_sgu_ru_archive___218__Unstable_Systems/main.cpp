#include "../../libs/binary_search.h"
#include "../../libs/common.h"
#include "../../libs/max_flow.h"
#include "../../libs/debug.h"

using namespace max_flow;
typedef max_flow::FlowEdge<int> fe;

int n;
inline int Left(int i) { return i; }
inline int Right(int i) { return i + n; }
inline int Src() { return n + n; }
inline int Dst() { return Src() + 1; }

void solve(int testId, istream &in, ostream &out) {
  in >> n;
  vector<vector<int>> mat(n, vector<int>(n));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      in >> mat[i][j];
    }
  }

  vector<vector<fe>> g(Dst() + 1);
  max_flow::ISAP<int> dinic;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      AddEdge(g, Left(i), Right(j), 1);
    }
  }
  for (int i = 0; i < n; i++) {
    AddEdge(g, Src(), Left(i), 1);
    AddEdge(g, Right(i), Dst(), 1);
  }

  function<bool(int)> checker = [&](int mid) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        auto &e = g[i][j];
        if (mat[i][j] <= mid) {
          e.flow = 0;
          g[e.to][e.rev].flow = 1;
        } else {
          e.flow = 0;
          g[e.to][e.rev].flow = 0;
        }
      }
    }
    for(auto &e : g[Src()]){
      e.flow = 0;
      g[e.to][e.rev].flow = 1;
    }
    for(auto &e : g[Dst()]){
      e.flow = 1;
      g[e.to][e.rev].flow = 0;
    }
dbg(g);
    
    int flow = dinic.send(g, Src(), Dst(), n);
    return flow >= n;
  };

  int mid = binary_search::BinarySearch((int)-1e6, (int)1e6, checker);
  checker(mid);

  out << mid << endl;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (g[i][j].flow > 0) {
        out << i + 1 << ' ' << j + 1 << endl;
      }
    }
  }
}

RUN_ONCE