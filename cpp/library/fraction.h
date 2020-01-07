#ifndef FRACTION_H
#define FRACTION_H

#include "common.h"

namespace dalt
{
template <class T>
struct Fraction
{
    T a, b;
    Fraction(T top, T bot)
    {
        T g = 1; //gcd(abs(top), abs(bot));
        a = top / g;
        b = bot / g;
        if (b < 0)
        {
            a = -a;
            b = -b;
        }
    }

    Fraction()
    {
        Fraction(0, 1);
    }

    template <class E>
    friend bool operator>=(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b >= b.a * a.b;
    };
    template <class E>
    friend bool operator<=(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b <= b.a * a.b;
    };
    template <class E>
    friend bool operator>(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b > b.a * a.b;
    };
    template <class E>
    friend bool operator<(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b < b.a * a.b;
    };
    template <class E>
    friend bool operator==(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b == b.a * a.b;
    };
    template <class E>
    friend bool operator!=(const Fraction<E> &a, const Fraction<E> &b)
    {
        return a.a * b.b != b.a * a.b;
    };

private:
    static T gcd(T a, T b)
    {
        if (a < b)
        {
            swap(a, b);
        }
        return gcd0(a, b);
    }
    static T gcd0(T a, T b)
    {
        return b == 0 ? a : gcd0(b, a % b);
    }
};
} // namespace dalt

#endif