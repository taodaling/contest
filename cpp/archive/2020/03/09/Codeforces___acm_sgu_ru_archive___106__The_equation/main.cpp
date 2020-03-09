#include "../../libs/common.h"
#include "../../libs/linear_integer_range.h"
#include "../../libs/gcd.h"
using namespace linear_integer_range;

ll Count(int l, int r){
  return l > r ? 0 : (r - l + 1);
}

void solve(int testId, istream &in, ostream &out) {
  ll a, b, c, x1, x2, y1, y2;
  in >> a >> b >> c >> x1 >> x2 >> y1 >> y2;
  pair<ll, ll> interval(-1e18, 1e18);
  c = -c;

  if(a == 0 && b == 0){
    if(c == 0){
      out << Count(x1, x2) * Count(y1, y2) << endl;
      return;
    }
    out << 0 << endl;
    return;
  }

  ll x0, y0;
  ll g = gcd::Extgcd(abs(a), abs(b), x0, y0);
  error(g, x0, y0);
  if (a < 0) {
    x0 = -x0;
  }
  if (b < 0) {
    y0 = -y0;
  }
  if (c % g != 0) {
    out << 0 << endl;
    return;
  }
  x0 *= c / g;
  y0 *= c / g;
  error(g, x0, y0);

  Between(b / g, x0, x1, x2, interval);
  error(interval.first, interval.second);
  Between(-a / g, y0, y1, y2, interval);
  error(interval.first, interval.second);
  ll ans = Length(interval);
  out << ans << endl;
}

RUN_ONCE