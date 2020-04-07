#include "../../libs/common.h"
#include "../../libs/modular.h"
#include "../../libs/gcd.h"
#include "../../libs/maximum_representation.h"

using modular::Mod;

void solve(int testId, istream &in, ostream &out) {
  int n;
  int k;
  in >> n >> k;
  k %= n;

  vector<int> ds(n);
  for (int i = 0; i < n; i++) {
    char c;
    in >> c;
    ds[i] = c - '0';
  }

  int g = gcd::Gcd(n, k);
  
  vector<string> all;
  all.reserve(g);
  for(int i = 0; i < g; i++){
    int index = maximum_representation::MaximumRepresentation([&](int j){return ds[(i + (ll)j * k) % n];}, n / g);
    std::stringstream ss;
    for(int j = 0; j < n / g; j++){
      ss << ds[(i + (ll)(j + index) * k) % n];
    }
    all.push_back(ss.str());
  }

  int index = 0;
  for(int i = 1; i < all.size(); i++){
    if(all[i] > all[index]){
      index = i;
    }
  }

  string &s = all[index];
  for(int i = 0; i < n; i++){
    out << s[i % s.length()];
  }
}

RUN_ONCE