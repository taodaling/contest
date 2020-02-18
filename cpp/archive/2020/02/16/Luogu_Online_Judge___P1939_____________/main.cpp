#include "../../libs/common.h"
#include "../../libs/modular.h"
#include "../../libs/matrix.h"

using Mint = modular::Modular<int, (int)1e9 + 7>;
using namespace matrix;
vector<Mint> coes{1, 0, 1};
vector<vector<Mint>> a{{1, 1, 1}};
vector<vector<Mint>> mat = Transpose(vector<vector<Mint>>{
    {1, 0, 1},
    {1, 0, 0},
    {0, 1, 0}
});

void solve(int testId, istream &in, ostream &out)
{
    int n;
    in >> n;
    vector<vector<Mint>> pow = mat ^ (n - 1);
    vector<vector<Mint>> state = (a * pow);
    Mint ans = state[0][3 - 1];
    //error(n, pow, state);
    out << ans << endl;
}

#include "../../libs/run_multi.h"