//
// Created by DALT on 12/9/2019.
//

#ifndef MODULAR_H
#define MODULAR_H
#include "common.h"

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

#endif //MODULAR_H
