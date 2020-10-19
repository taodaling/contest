#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target("sse4.2")
#pragma GCC optimize("inline")
#ifdef LOCAL
#define dbg(x) cerr << #x << " is " << x << endl;
#else
#define dbg(x) 
#endif
#include <bits/stdc++.h>
using namespace std;

typedef long long ll;
typedef vector<int> vi;
typedef vector<vector<int>> vii;
typedef pair<int, int> pii;
typedef long double ld;
typedef unsigned int uint;
typedef unsigned long long ull;

const int limit = 1e6;
int mod = (int)(1e9 + 7);
bool isComp[limit + 1];
vector<int> primes[limit + 1];
int choose[10];
int ornot[10];
int fact[2 * limit + 1];
int invFact[2 * limit + 1];
int r;

ll comb(int n, int m) {
    if (m > n || m < 0) {
        return 0;
    }
    return (ll)fact[n] * invFact[n - m] % mod * invFact[m] % mod;
}

int pow(int x, int n) {
    if (n == 0) {
        return 1;
    }
    ll ans = pow(x, n / 2);
    ans = ans * ans % mod;
    if (n % 2 == 1) {
        ans = ans * x % mod;
    }
    return ans;
}

void init() {
    isComp[1] = true;
    for (int i = 2; i <= limit; i++) {
        if (isComp[i]) {
            continue;
        }
        for (int j = i; j <= limit; j += i) {
            isComp[j] = true;
            primes[j].push_back(i);
        }
        isComp[i] = false;
    }

    fact[0] = 1;
    for (int i = 1; i <= limit * 2; i++) {
        fact[i] = (ll)i * fact[i - 1] % mod;
    }
    invFact[2 * limit] = pow(fact[2 * limit], mod - 2);
    for (int i = 2 * limit; i >= 1; i--) {
        invFact[i - 1] = invFact[i] * (ll)i % mod;
    }
}

void solveOne() {
    int n;
    cin >> r >> n;
    int cur = n;
    
    ll prod = 1;
    for (int i = 0; i < primes[n].size(); i++) {
        int p = primes[n][i];
        int x = 0;
        while (cur % p == 0) {
            cur /= p;
            x++;
        }
        
        dbg(x);
        prod = prod * ((comb(x - 1 + r, r) * (ll)2 + comb(x + r - 1, x)) % mod) % mod;    
    }
     
    cout << prod << '\n';
}

void solve() {
    int q;
    cin >> q;
    while (q-- > 0) {
        solveOne();
    }
}

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(0);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);

    init();
    solve();

    return 0;
}
