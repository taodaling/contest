#include "../../libs/common.h"
#include "../../libs/debug.h"
#include "../../libs/matrix.h"

#define double ld

using namespace ::matrix;

vector<double> operator-(const vector<double> &a, const vector<double> &b) {
  vector<double> ans(a);
  for (int i = 0; i < a.size(); i++) {
    ans[i] -= b[i];
  }
  return ans;
}

double Length2(const vector<double> &xy) {
  double ans = 0;
  for (double x : xy) {
    ans += x * x;
  }
  return ans;
}

double Length(const vector<double> &xy) { return sqrtl(Length2(xy)); }

vector<vector<double>> TransposeRotate(double angle) {
  double cos = std::cos(angle);
  double sin = std::sin(angle);
  return vector<vector<double>>{{cos, -sin}, {sin, -cos}};
}

vector<vector<double>> Create(int n, double side, double angle) {
  double theta = PI - (n - 2) * PI / n;
  vector<vector<double>> ans;
  ans.reserve(n);
  ans.push_back(vector<double>{0, 0});
  vector<vector<double>> base{{side, 0}};
  for (int i = 1; i < n; i++, angle += theta) {
    dbg(i, angle);
    vector<vector<double>> rotate = TransposeRotate(angle);
    vector<vector<double>> ray = base * rotate;
    ans.push_back(
        vector<double>{ans.back()[0] + ray[0][0], ans.back()[1] + ray[0][1]});
  }
  return ans;
}

void solve(int testId, istream &in, ostream &out) {
  int n, v1, v2;
  in >> n >> v1 >> v2;
  v1--;
  v2--;

  vector<double> xy1(2);
  vector<double> xy2(2);
  in >> xy1[0] >> xy1[1] >> xy2[0] >> xy2[1];

  vector<vector<double>> points = Create(n, 1, 0);
  double side = Length(xy2 - xy1) / Length(points[v2] - points[v1]);
  
  double thetaStd =
      atan2(points[v2][0] - points[v1][0], points[v2][1] - points[v1][1]);
  double thetaCustom = atan2(xy2[0] - xy1[0], xy2[1] - xy1[1]);
  dbg(thetaStd, thetaCustom);
  dbg(xy2[0]-xy1[0], xy2[1]-xy1[1]);
  double thetaDelta = (thetaCustom - thetaStd);
  vector<vector<double>> result = Create(n, side, thetaDelta);
  dbg(points, side, result);
  dbg(result);

  std::cout << std::setprecision(6);
  for (vector<double> xy : result) {
    out << xy[0] + xy1[0] << ' ' << xy[1] + xy1[1] << endl;
  }
}

RUN_ONCE