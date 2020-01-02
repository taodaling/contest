//
// Created by DALT on 12/9/2019.
//

#ifndef MODULAR_H
#define MODULAR_H
#include "common.h"
#include "gcd.h"

namespace dalt
{

#define MOD(a, b) \
    a %= b;       \
    if (a < 0)    \
        a += b;

class IntModular
{
public:
    typedef long long ll;

    IntModular(int m) : _m(m) {}

    int operator()(int x) const
    {
        MOD(x, _m);
        return x;
    }

    int operator()(ll x) const
    {
        MOD(x, _m);
        return x;
    }

    int plus(int a, int b) const { return (*this)(a + b); }

    int plus(ll a, ll b) const { return (*this)(a + b); }

    int subtract(int a, int b) const { return (*this)(a - b); }

    int subtract(ll a, ll b) const { return (*this)(a - b); }

    int mul(int a, int b) const { return (*this)((ll)a * b); }

    int mul(ll a, ll b) const { return mul((*this)(a), (*this)(b)); }

private:
    int _m;
};

class LongModular
{
public:
    typedef long long ll;
    typedef long double ld;

    ll operator()(ll x)
    {
        MOD(x, _m);
        return x;
    }

    ll plus(ll a, ll b) { return (*this)(a + b); }

    ll subtract(ll a, ll b) { return (*this)(a - b); }

    ll mul(ll a, ll b)
    {
        ll k = ll((ld)a * b / _m);
        ll ans = (a * b - k * _m);
        MOD(ans, _m);
        return ans;
    }

private:
    ll _m;
};

#undef MOD
} // namespace dalt
#endif //MODULAR_H
