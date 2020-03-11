#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/geo2.h"

using namespace ::geo2;
using pt = Point<long double>;

void solve(int testId, istream &in, ostream &out) {
  int n, v1, v2;
  in >> n >> v1 >> v2;
  v1--;
  v2--;
  pt p1, p2;
  in >> p1 >> p2;

  vector<pt> ans;
  ans.emplace_back(0, 0);
  double theta = PI + (n - 2) * PI / n;
  dbg(theta);
  pt step{1, 0};
  dbg(Rotate(step,theta));
  for (int i = 1; i < n; i++) {
    ans.push_back(Rotate(step, theta * (i - 1)) + ans.back());
  }

  dbg(ans);
  pt w1 = ans[v1];
  pt w2 = ans[v2];

  dbg(w1,p1,w2,p2);
  //dbg(LinearTransformTo(w1,p1,w2,p2,w2),w2,p2);
  //assert(LinearTransformTo(w1, p1, w2, p2, w1) == p1);
  //assert(LinearTransformTo(w1, p1, w2, p2, w2) == p2);
  for (int i = 0; i < n; i++) {
    ans[i] = LinearTransformTo(w1, p1, w2, p2, ans[i]);
  }

  for (int i = 0; i < n; i++) {
    out << ans[i].x << ' ' << ans[i].y << endl;
  }
}

RUN_ONCE