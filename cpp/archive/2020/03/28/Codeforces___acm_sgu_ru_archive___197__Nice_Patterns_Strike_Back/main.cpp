#include "../../libs/bits.h"
#include "../../libs/common.h"
#include "../../libs/modmatrix.h"

void Normalize(vector<int> &bi) {
  while (bi.size() > 1 && bi.back() == 0) {
    bi.pop_back();
  }
}

vector<int> Div2(const vector<int> &bi) {
  vector<int> ans(bi.size());
  int head = 0;
  for (int i = bi.size() - 1; i >= 0; i--) {
    head = head * 10 + bi[i];
    ans[i] = head / 2;
    head %= 2;
  }
  Normalize(ans);
  return ans;
}

vector<int> Sub1(const vector<int> &bi) {
  vector<int> ans(bi);
  for (int i = 0;; i++) {
    if (ans[i] > 0) {
      ans[i]--;
      break;
    }
    ans[i] = 9;
  }
  return ans;
}

int Mod2(const vector<int> &bi) { return bi[0] & 1; }

vector<vector<int>> Pow(const vector<vector<int>> &x, const vector<int> &bi,
                        int mod) {
  if (bi.size() == 1 && bi[0] == 0) {
    return modmatrix::UnitMatrix(x.size(), mod);
  }
  vector<vector<int>> ans = Pow(x, Div2(bi), mod);
  ans = modmatrix::Mul(ans, ans, mod);
  if (Mod2(bi)) {
    ans = modmatrix::Mul(ans, x, mod);
  }
  return ans;
}

vector<int> ReadBi(istream &in) {
  string s;
  in >> s;
  vector<int> bi(s.length());
  for (int i = 0; i < s.length(); i++) {
    bi[i] = s[i] - '0';
  }
  reverse(bi.begin(), bi.end());
  return bi;
}

void solve(int testId, istream &in, ostream &out) {
  vector<int> n = Sub1(ReadBi(in));
  int m, p;
  in >> m >> p;
  int mask = (1 << m) - 1;
  vector<vector<int>> mat(mask + 1, vector<int>(mask + 1));

  for (int i = 0; i <= mask; i++) {
    for (int j = 0; j <= mask; j++) {
      mat[i][j] = 1;
      for (int k = 1; k < m; k++) {
        if (bits::BitAt(i, k - 1) == bits::BitAt(i, k) &&
            bits::BitAt(i, k) == bits::BitAt(j, k - 1) &&
            bits::BitAt(j, k - 1) == bits::BitAt(j, k)) {
          mat[i][j] = 0;
        }
      }
    }
  }

  vector<vector<int>> transpose = modmatrix::Transpose(mat);
  vector<vector<int>> powed = Pow(transpose, n, p);

  vector<int> v0(mask + 1, 1);
  vector<int> vn = modmatrix::Mul(vector<vector<int>>{v0}, powed, p)[0];
  int ans = 0;
  for (int i = 0; i <= mask; i++) {
    ans += vn[i];
  }

  out << ans % p;
}

RUN_ONCE