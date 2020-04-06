#include "../../libs/common.h"
#include "../../libs/geo2.h"
#include "../../libs/debug.h"

using pt = geo2::Point<double>;

vector<pt> Gen(int n) {
  vector<pt> ans;
  ans.resize(n);

  double angle = PI + (n - 2) * PI / n;
  ans[0] = pt(0, 0);
  ans[1] = pt(1, 0);
  for (int i = 2; i < n; i++) {
    auto& b1 = ans[i - 1];
    auto& b2 = ans[i - 2];

    pt extend = geo2::Scale(b2, 2.0, b1);
    pt b3 = geo2::Rotate(b1, extend, angle);
    ans[i] = b3;
  }
  return ans;
}

void solve(int testId, istream& in, ostream& out) {
  int n, a, b;
  in >> n >> a >> b;
  a--;
  b--;
  pt pa, pb;
  in >> pa >> pb;

  vector<pt> vec = Gen(n);

dbg(vec);
  vector<pt> ans(n);
  for (int i = 0; i < n; i++) {
    ans[i] = geo2::LinearTransformTo(vec[a], pa, vec[b], pb, vec[i]);
  }

  for(int i = 0; i < n; i++){
    out << ans[i].x << ' ' << ans[i].y << endl;
  }
}

RUN_ONCE