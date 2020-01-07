#include <iostream>
#include <fstream>
#include <iomanip>
#include <bits/stdc++.h>
#include <chrono>
#include <random>
#include<ext/rope>

using __gnu_cxx::rope;
using std::cerr;
using std::deque;
using std::endl;
using std::fill;
using std::ios_base;
using std::istream;
using std::iterator;
using std::make_pair;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::set;
using std::sort;
using std::string;
using std::swap;
using std::unordered_map;
using std::vector;
using std::bitset;
using std::uniform_int_distribution;
using std::uniform_real_distribution;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

#define trace(...) __f(#__VA_ARGS__, __VA_ARGS__)
template <typename Arg1>
void __f(const char* name, Arg1&& arg1){
  cerr << name << ": " << arg1 << endl;
}
template <typename Arg1, typename... Args>
void __f(const char* names, Arg1&& arg1, Args&&... args){
  const char* comma = strchr(names + 1, ',');
  cerr.write(names, comma - names) << ": " << arg1 << " |";
  __f(comma + 1, args...);
}

namespace dalt
{
};
namespace other
{
};

using namespace dalt;

namespace dalt
{

#define MOD(a, b) \
    a %= b;       \
    if (a < 0)    \
        a += b;

template <int M>
class Remainder
{
public:
    typedef long long ll;

    Remainder() : _v(0) {}

    Remainder(const int &v) : _v(v){MOD(_v, M)} 
    
    Remainder(ll v)
    {
        MOD(v, M);
        _v = v;
    }

    template <int ANY>
    Remainder(const Remainder<ANY> &x) : _v(x._v) {}

    template <int ANY>
    Remainder<M> &operator=(Remainder<ANY> &x)
    {
        _v = x._v;
        return *this;
    }

    template <int ANY>
    Remainder<M> &operator=(const Remainder<ANY> &x)
    {
        _v = x._v;
        return *this;
    }
    template <int E>
    friend Remainder<E> operator+(const Remainder<E> &a, const Remainder<E> &b);
    template <int E>
    friend Remainder<E> operator-(const Remainder<E> &a, const Remainder<E> &b);
    template <int E>
    friend Remainder<E> operator*(const Remainder<E> &a, const Remainder<E> &b);

    Remainder<M> &operator+=(Remainder<M> &x)
    {
        _v += x._v;
        MOD(_v, M);
        return *this;
    }

    Remainder<M> &operator-=(Remainder<M> &x)
    {
        _v -= x._v;
        MOD(_v, M);
        return *this;
    }

    Remainder<M> &operator+=(const Remainder<M> &x)
    {
        _v += x._v;
        MOD(_v, M);
        return *this;
    }

    Remainder<M> &operator-=(const Remainder<M> &x)
    {
        _v -= x._v;
        MOD(_v, M);
        return *this;
    }

    Remainder<M> &operator*=(Remainder<M> &x)
    {
        ll tmp = (ll)_v * x._v;
        MOD(tmp, M);
        _v = tmp;
        return *this;
    }

    Remainder<M> &operator*=(const Remainder<M> &x)
    {
        ll tmp = (ll)_v * x._v;
        MOD(tmp, M);
        _v = tmp;
        return *this;
    }

    Remainder<M> operator^(int n) const { return pow(_v, n); }

    Remainder<M> operator^(ll n) const { return pow(_v, n); }

    Remainder<M> inverseByFermat() const { return pow(_v, M - 2); }

    Remainder<M> inverseByExtgcd() const { return extgcd(M, _v).second; }

    Remainder<M> operator/(const Remainder<M> &x) const { return (*this) * x.inverseByExtgcd(); }

    Remainder<M> &operator/=(Remainder<M> &x)
    {
        ll tmp = (ll)_v * extgcd(M, _v).second;
        MOD(tmp, M);
        _v = tmp;
        return *this;
    }

    Remainder<M> &operator/=(const Remainder<M> &x)
    {
        ll tmp = (ll)_v * extgcd(M, _v).second;
        MOD(tmp, M);
        _v = tmp;
        return *this;
    }

    int value() const { return _v; }

    template <int E>
    friend bool operator==(const Remainder<E> &a, const Remainder<E> &b);

    template <int E>
    friend bool operator!=(const Remainder<E> &a, const Remainder<E> &b);

private:
    int _v;

    template <typename L>
    static int pow(int x, L n)
    {
        if (n == 0)
        {
            return 1 % M;
        }
        ll ans = pow(x, n >> 1);
        ans = ans * ans % M;
        if (n & 1)
        {
            ans = ans * x % M;
        }
        return ans;
    }

    static pair<ll, ll> extgcd(ll a, ll b)
    {
        if (a >= b)
        {
            return extgcd0(a, b);
        }
        pair<ll, ll> xy = extgcd0(b, a);
        swap(xy.first, xy.second);
        return xy;
    }

    static pair<ll, ll> extgcd0(ll a, ll b)
    {
        if (b == 0)
        {
            return {1, 0};
        }
        pair<ll, ll> xy = extgcd0(b, a % b);
        return {xy.second, xy.first - xy.second * (a / b)};
    }
};

template <int M>
inline Remainder<M> operator+(const Remainder<M> &a, const Remainder<M> &b)
{
    return a._v + b._v;
}
template <int M>
inline Remainder<M> operator-(const Remainder<M> &a, const Remainder<M> &b)
{
    return a._v - b._v;
}
template <int M>
inline Remainder<M> operator*(const Remainder<M> &a, const Remainder<M> &b)
{
    return (ll)a._v * b._v;
}
template <int M>
inline bool operator==(const Remainder<M> &a, const Remainder<M> &b)
{
    return a._v == b._v;
}
template <int M>
inline bool operator!=(const Remainder<M> &a, const Remainder<M> &b)
{
    return a._v != b._v;
}

template <int M>
inline ostream &operator<<(ostream &os, const Remainder<M> &x)
{
    return os << x.value();
}

template <int M>
inline istream &operator>>(istream &is, Remainder<M> &x)
{
    ll val;
    is >> val;
    x = val;
    return is;
}

#undef MOD
} // namespace dalt

template<class T>
struct Fraction
{
    T a, b;
    Fraction(T top, T bot){
        T g = gcd(abs(top), abs(bot));
        a = top / g;
        b = bot / g;
        if(b < 0){
            a = -a;
            b = -b;
        }
    }

    template<class E>
    friend bool operator >= (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b >= b.a * a.b;
    };
    template<class E>
    friend bool operator <= (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b <= b.a * a.b;
    };
    template<class E>
    friend bool operator > (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b > b.a * a.b;
    };
    template<class E>
    friend bool operator < (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b < b.a * a.b;
    };
    template<class E>
    friend bool operator == (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b == b.a * a.b;
    };
    template<class E>
    friend bool operator != (const Fraction<E> &a, const Fraction<E> &b){
        return a.a * b.b != b.a * a.b;
    };
private:
    static T gcd(T a, T b){
        if(a < b){
            swap(a, b);
        }
        return gcd0(a, b);
    }
    static T gcd0(T a, T b){
        return b == 0 ? a : gcd0(b, a % b);
    }
};

typedef Remainder<998244353> rmd;
typedef Fraction<ll> fll;

void solve(istream &in, ostream &out)
{
}

int main()
{
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(NULL);
    std::cout << std::setiosflags(std::ios::fixed);
    std::cout << std::setprecision(15);
    
    solve(std::cin, std::cout);

    return 0;
}
