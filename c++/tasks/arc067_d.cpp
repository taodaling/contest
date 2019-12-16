#include "../library/common.h"


vector<ll> dists;

class arc067_d {
public:

    void solve(std::istream &in, std::ostream &out) {
        int n, m;
        in >> n >> m;
        dists.resize(n);
        for(int i = 0; i < n; i++){
            in >> dists[i];
        }
    }

private:
};
