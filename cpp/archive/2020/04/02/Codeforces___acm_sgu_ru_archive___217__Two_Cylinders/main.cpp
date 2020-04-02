#include "../../libs/common.h"
#include "../../libs/simpson_integral.h"
#include "../../libs/debug.h"


void solve(int testId, istream &in, ostream &out) {
  double r, R;
  in >> r >> R;

  if (r > R) {
    swap(r, R);
  }

  function<double(double)> area = [&](double x) {
    double h = sqrt(r * r - x * x);
    double alpha = asin(h / R);
    double beta = PI / 2 - alpha;
    double extra = R * R * beta / 2 - R * sin(beta) * h / 2;
    double ans = R * R * PI - extra * 4;

    dbg(x, ans);
    return ans;
  };

  simpson_integeral::SimpsonIntegral<double> si(1e-8, area);
  double volume = si.integral(0, r) * 2;
  out << volume;
}

RUN_ONCE