#include "../../libs/common.h"
#include "../../libs/geo2.h"
#include "../../libs/debug.h"

using pt = geo2::Point<ll>;

void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  vector<pair<pt, pt>> segs(n);
  for (int i = 0; i < n; i++) {
    in >> segs[i].first >> segs[i].second;
  }
  pt ask;
  in >> ask;
  int type = geo2::InPolygon(segs, ask);

  dbg(segs, ask);
  if (type == 0) {
    out << "OUTSIDE";
  } else if (type == 1) {
    out << "INSIDE";
  } else {
    out << "BORDER";
  }
}

RUN_ONCE