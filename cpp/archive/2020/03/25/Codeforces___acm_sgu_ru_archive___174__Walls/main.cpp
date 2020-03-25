#include "../../libs/common.h"
#include "../../libs/discretemap.h"
#include "../../libs/dsu.h"
#include "../../libs/geo2.h"

using pt = geo2::Point<int>;

dsu::DSU<400000> dset;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pair<pt, pt>> edges(n);
  vector<pt> pts;
  pts.reserve(n * 2);
  for (int i = 0; i < n; i++) {
    pt a, b;
    in >> a >> b;
    edges[i].first = a;
    edges[i].second = b;
    pts.push_back(a);
    pts.push_back(b);
  }

  sort(pts.begin(), pts.end());
  discretemap::DiscreteMap<pt> dm(pts);

  for (int i = 0; i < n; i++) {
    int a = dm.apply(edges[i].first);
    int b = dm.apply(edges[i].second);
    if(dset.find(a) == dset.find(b)){
      out << i + 1;
      return;
    }
    dset.merge(a, b);
  }
  out << 0;
}

RUN_ONCE