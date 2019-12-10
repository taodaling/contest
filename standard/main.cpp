
#include <map>
#include <cmath>
#include <cstdio>
#include <iostream>
#include <unordered_map>

#define ll long long

using namespace std ; 
unordered_map<ll, int> H ;
int N, M, P, ans ; // N ^x = M (mod P)

inline ll gcd(ll a, ll b){
    if (!b) return a ;
    return gcd(b, a % b) ;
}
inline ll expow(ll a, ll b, ll mod){
    ll res = 1 ;
    while (b) res = ((b & 1)?res * a % mod : res), a = a * a % mod, b >>= 1 ;
    return res ;
}
inline ll exgcd(ll &x, ll &y, ll a, ll b){
    if (!b){ x = 1, y = 0 ; return a ; }
    ll t = exgcd(y, x, b, a % b) ; y -= x * (a / b) ; return t ;
}
inline ll BSGS(ll a, ll b, ll mod, ll qaq){
    H.clear() ; ll Q, p = ceil(sqrt(mod)), x, y ; 
    exgcd(x, y, qaq, mod), b = (b * x % mod + mod) % mod, 
    Q = expow(a, p, mod), exgcd(x, y, Q, mod), Q = (x % mod + mod) % mod ;
    for (ll i = 1, j = 0 ; j <= p ; ++ j, i = i * a % mod)  if (!H.count(i)) H[i] = j ;
    for (ll i = b, j = 0 ; j <= p ; ++ j, i = i * Q % mod)  if (H[i]) return j * p + H[i] ; return -1 ;
}
inline ll exBSGS(){
    ll qaq = 1 ;
    ll k = 0, qwq = 1 ; 
    if (M == 1) return 0 ; 
    while ((qwq = gcd(N, P)) > 1){
        if (M % qwq) return -1 ;
        ++ k, M /= qwq, P /= qwq, qaq = qaq * (N / qwq) % P ;
        if (qaq == M) return k ;
    }
    return (qwq = BSGS(N, M, P, qaq)) == -1 ? -1 : qwq + k ;
}                    
int main(){
    while(cin >> N){
        scanf("%d%d", &P, &M); if (!N && !M && !P) return 0 ;
        N %= P, M %= P, ans = exBSGS() ; if (ans < 0) puts("No Solution") ; else cout << ans << '\n' ;
    }
}