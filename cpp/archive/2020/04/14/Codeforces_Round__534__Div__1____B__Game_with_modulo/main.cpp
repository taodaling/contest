#include "../../libs/binary_search.h"
#include "../../libs/common.h"

using std::cin;
using std::cout;

bool less(int x, int y) {
  cout << "? " << x << " " <<  y << endl;
  cout.flush();
  string ans;
  cin >> ans;
  if (ans == ("e")) {
    exit(-1);
  }
  return ans == ("y");
}

bool leq(int x, int y) { return !less(y, x); }

bool geq(int x, int y) { return less(y, x); }

void answer(int a) {
  cout << "! " << a << endl;
  cout.flush();
}

int nextInt() { return uniform_int_distribution<int>(1, 1e9)(rng); }

int pow(int x, int n) { return n == 0 ? 1 : x * pow(x, n - 1); }

int log(int x, int y) {
  int ans = 0;
  while (y % x == 0) {
    y /= x;
    ans++;
  }
  return ans;
}

void solve() {
  int x = 0;
  int y = 0;
  while (true) {
    x = nextInt();
    y = x + nextInt();
    if (leq(y, x)) {
      break;
    }
  }

  int parents;
  {
    ll l = x;
    ll r = y;
    while (r - l > 1) {
      ll m = (l + r) >> 1;
      if (geq(m, r)) {
        l = m;
      } else {
        // m > l
        r = m;
      }
    }
    if (r > l) {
      if (geq(r, l)) {
        r = l;
      } else {
        l = r;
      }
    }
    parents = l;
  }

  vector<int> primes;
  int tmp = parents;
  for (int i = 2; i * i <= tmp; i++) {
    if (tmp % i != 0) {
      continue;
    }
    primes.push_back(i);
    while (tmp % i == 0) {
      tmp /= i;
    }
  }

  if (tmp > 1) {
    primes.push_back(tmp);
  }

  int ans = 1;
  for (int p : primes) {
    int l = 0;
    int r = log(p, parents);

    int remain = binary_search::BinarySearch<int>(l, r, [&](int mid) {
      int cand = parents / pow(p, r - mid);
      return leq(cand, 0);
    });
    ans *= pow(p, remain);
  }

  answer(ans);
}

void solve(int testId, istream &in, ostream &out) {
  while (true) {
    string cmd;
    in >> cmd;
    if (cmd == ("start")) {
      solve();
      continue;
    }
    if (cmd == ("mistake")) {
      exit(-1);
    }
    if (cmd == ("end")) {
      return;
    }
  }
}

RUN_ONCE