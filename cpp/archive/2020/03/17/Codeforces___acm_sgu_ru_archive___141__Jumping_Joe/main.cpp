#include "../../libs/common.h"
#include "../../libs/gcd.h"
#include "../../libs/modular.h"

bool solve(ll x1, ll x2, ll a, ll b, ll k, ostream &out) {
  ll r = k - abs(a) - abs(b);
  if (r < 0 || (r % 2 == 1)) {
    return false;
  }
  out << "YES" << endl;

  ll p1 = 0;
  ll n1 = 0;
  ll p2 = 0;
  ll n2 = 0;
  if (a > 0) {
    p1 += a;
  } else {
    n1 -= a;
  }
  if (b > 0) {
    p2 += b;
  } else {
    n2 -= b;
  }

  p1 += r / 2;
  n1 += r / 2;

  out << p1 << ' ' << n1 << ' ' << p2 << ' ' << n2;
  return true;
}

void solve(int testId, istream &in, ostream &out) {
  ll x1, x2, p, k;
  in >> x1 >> x2 >> p >> k;
  ll a, b;
  ll g = gcd::Extgcd(x1, x2, a, b);
  if (p % g != 0) {
    out << "NO" << endl;
    return;
  }
  a *= p / g;
  b *= p / g;
  x1 /= g;
  x2 /= g;

  // case 1
  vector<pair<ll, ll>> possible;
  {
    ll ta = a;
    ll tb = b;
    ta = modular::Mod(ta, x2);
    tb -= (ta - a) / x2 * x1;
    possible.emplace_back(ta, tb);
  }

  {
    ll ta = a;
    ll tb = b;
    tb = modular::Mod(tb, x1);
    ta -= (tb - b) / x1 * x2;
    possible.emplace_back(ta, tb);
  }

  for (auto &ab : possible) {
    if (solve(x1, x2, ab.first, ab.second, k, out)) {
      return;
    }
    if (solve(x1, x2, ab.first + x2, ab.second - x1, k, out)) {
      return;
    }
    if (solve(x1, x2, ab.first - x2, ab.second + x1, k, out)) {
      return;
    }
  }

  out << "NO";
  return;
}

RUN_ONCE