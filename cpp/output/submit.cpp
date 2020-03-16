#ifndef COMMON_H
#define COMMON_H

#include <bits/stdc++.h>
#include <algorithm>
#include <chrono>
#include <complex>
#include <ext/rope>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <queue>
#include <random>
#ifndef COMPILER_MACRO_H
#define COMPILER_MACRO_H

#ifndef LOCAL

#pragma GCC diagnostic error "-std=c++14"
#pragma GCC target("avx")
#pragma GCC optimize(3)
#pragma GCC optimize("Ofast")
#pragma GCC target("sse4.2")
#pragma GCC optimize("inline")

#endif

#endif

using __gnu_cxx::rope;
using std::array;
using std::bitset;
using std::cerr;
using std::complex;
using std::deque;
using std::endl;
using std::fill;
using std::function;
using std::ios_base;
using std::istream;
using std::istream_iterator;
using std::iterator;
using std::make_pair;
using std::make_tuple;
using std::map;
using std::max;
using std::min;
using std::numeric_limits;
using std::ostream;
using std::pair;
using std::priority_queue;
using std::queue;
using std::reverse;
using std::rotate;
using std::set;
using std::sort;
using std::string;
using std::stringstream;
using std::swap;
using std::uniform_int_distribution;
using std::uniform_real_distribution;
using std::unique;
using std::unordered_map;
using std::vector;
using std::tuple;
using std::get;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
std::mt19937 rng(std::chrono::steady_clock::now().time_since_epoch().count());

const double E = 2.7182818284590452354;
const double PI = 3.14159265358979323846;

#define mp make_pair

#ifdef LOCAL
#define PREPARE_INPUT                                  \
  {                                                    \
    std::cout << "Input file name:";                   \
    string file;                                       \
    std::cin >> file;                                  \
    if (file != "stdin") {                             \
      file = string(__FILE__) + "/../" + file + ".in"; \
      std::cout << "Open file:" << file << std::endl;  \
      freopen(file.data(), "r", stdin);                \
    }                                                  \
  }
#else
#define PREPARE_INPUT
#endif

#define RUN_ONCE                                    \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    solve(1, std::cin, std::cout);                  \
    return 0;                                       \
  }

#define RUN_MULTI                                   \
  int main() {                                      \
    PREPARE_INPUT;                                  \
    std::ios_base::sync_with_stdio(false);          \
    std::cin.tie(0);                                \
    std::cout << std::setiosflags(std::ios::fixed); \
    std::cout << std::setprecision(15);             \
    int t;                                          \
    std::cin >> t;                                  \
    for (int i = 1; i <= t; i++) {                  \
      solve(i, std::cin, std::cout);                \
    }                                               \
    return 0;                                       \
  }

#endif

#define C0(x) memset(x, 0, sizeof(x))
#define C1(x) memset(x, -1, sizeof(x))
#ifndef MODULAR_H
#define MODULAR_H


#ifndef GCD_H
#define GCD_H



namespace gcd {
template <typename T>
T Gcd0(T a, T b) {
  return b ? Gcd0(b, a % b) : a;
}

template <typename T>
T Gcd(T a, T b) {
  if (a < b) {
    swap(a, b);
  }
  return Gcd0(a, b);
}

template <typename T>
T Extgcd0(T a, T b, T &x, T &y) {
  if (!b) {
    x = 1;
    y = 0;
    return a;
  }
  T ans = Extgcd0(b, a % b, y, x);
  y = y - x * (a / b);
  return ans;
}

/**
 * Find gcd(a, b) and expression xa+yb=g
 */
template <typename T>
T Extgcd(T a, T b, T &x, T &y) {
  if (a >= b) {
    return Extgcd0(a, b, x, y);
  }
  return Extgcd0(b, a, y, x);
}

/**
 * O(n + logn)
 */
template <class T>
T Extgcd(vector<T> &arg, vector<T> &coes) {
  int n = arg.size();
  if (n == 0) {
    return 0;
  }
  coes.resize(n);
  vector<T> gs(n);
  gs[0] = arg[0];
  for (int i = 1; i < n; i++) {
    gs[i] = Gcd(gs[i - 1], arg[i]);
  }
  T prod = 1;
  for (int i = n - 1; i >= 1; i--) {
    T a, b;
    Extgcd0(gs[i - 1], arg[i], a, b);
    coes[i] = b * prod;
    prod *= a;
  }
  coes[0] = prod;
  return gs[n - 1];
}

}  // namespace gcd

#endif

namespace modular {
template <class T>
inline T Mod(T x, T m) {
  x %= m;
  if (x < 0) {
    x += m;
  }
  return x;
}

template <class T>
inline T Modmul(T a, T b, T m) {
  T k = (T)((long double)a / m * b + 0.5);
  return Mod<T>(a * b - k * m, m);
}

template <>
inline int Modmul<int>(int a, int b, int m) {
  return Mod<long long>((long long)a * b, m);
}

template <class T>
inline T Modpow(T x, long long n, T m) {
  if (n == 0) {
    return Mod<T>(1, m);
  }
  T ans = Modpow<T>(x, n >> 1, m);
  ans = Modmul<T>(ans, ans, m);
  if (n & 1) {
    ans = Modmul<T>(ans, x, m);
  }
  return ans;
}

template <typename T>
T Inverse(T a, T m) {
  int x, y;
  gcd::Extgcd(a, m, x, y);
  return Mod(x, m);
}

/**
 * O(n + logn)
 */
template <class T>
T Extgcd(vector<T> &arg, vector<T> &coes, T mod) {
  int n = arg.size();
  if (n == 0) {
    return 0;
  }
  coes.resize(n);
  vector<T> gs(n);
  gs[0] = arg[0];
  for (int i = 1; i < n; i++) {
    gs[i] = gcd::Gcd(gs[i - 1], arg[i]);
  }
  T prod = 1;
  for (int i = n - 1; i >= 1; i--) {
    T a, b;
    gcd::Extgcd0(gs[i - 1], arg[i], a, b);
    coes[i] = Modmul(b, prod, mod);
    prod = Modmul(prod, a, mod);
  }
  coes[0] = prod;
  return gs[n - 1];
}

template <class T, T M>
class Modular {
 public:
  Modular() { set(0); }
  Modular(const T &val) { set(val); }
  void set(const T &x) { _v = Mod(x, M); }
  Modular(const Modular<T, M> &val) { _v = val._v; }
  Modular<T, M> &operator=(const Modular<T, M> &y) {
    _v = y._v;
    return *this;
  }
  const T &operator()() const { return _v; }
  T &operator()() { return _v; }
  Modular<T, M> &operator-=(const Modular<T, M> &y) {
    _v = Mod(_v - y._v, M);
    return *this;
  }
  Modular<T, M> &operator+=(const Modular<T, M> &y) {
    _v = Mod(_v + y._v, M);
    return *this;
  }

  Modular<T, M> &operator*=(const Modular<T, M> &y) {
    _v = Modmul(_v, y._v, M);
    return *this;
  }
  Modular<T, M> &operator/=(const Modular<T, M> &y) {
    (*this) *= y.inverse();
    return *this;
  }
  Modular<T, M> pow(long long exp) const { return Modpow(_v, exp, M); }
  Modular<T, M> inverse() const { return modular::Inverse(_v, M); }

 private:
  T _v;
};

template <class T, T M>
Modular<T, M> operator+(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator+(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator+(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans += b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator-(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans -= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator*(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans *= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const Modular<T, M> &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const T &a, const Modular<T, M> &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator/(const Modular<T, M> &a, const T &b) {
  Modular<T, M> ans = a;
  ans /= b;
  return ans;
}
template <class T, T M>
Modular<T, M> operator==(const Modular<T, M> &a, const Modular<T, M> &b) {
  return a() == b();
}
template <class T, T M>
Modular<T, M> operator==(const Modular<T, M> &a, const T &b) {
  return a() == Modular<T, M>(b);
}
template <class T, T M>
Modular<T, M> operator==(const T &a, const Modular<T, M> &b) {
  return Modular<T, M>(a) == b;
}
template <class T, T M>
Modular<T, M> operator!=(const Modular<T, M> &a, const Modular<T, M> &b) {
  return a() != b();
}
template <class T, T M>
Modular<T, M> operator!=(const Modular<T, M> &a, const T &b) {
  return a() != Modular<T, M>(b);
}
template <class T, T M>
Modular<T, M> operator!=(const T &a, const Modular<T, M> &b) {
  return Modular<T, M>(a) != b;
}
template <class T, T M>
std::ostream &operator<<(std::ostream &out, const Modular<T, M> &v) {
  return out << v();
}
template <class T, T M>
std::istream &operator>>(std::istream &in, const Modular<T, M> &v) {
  T x;
  in >> x;
  v.set(x);
  return in;
}
}  // namespace modular

#endif
#ifndef DEBUG_H
#define DEBUG_H
//          Copyright Louis Delacroix 2010 - 2014.
// Distributed under the Boost Software License, Version 1.0.
//    (See accompanying file LICENSE_1_0.txt or copy at
//          http://www.boost.org/LICENSE_1_0.txt)
//
// A pretty printing library for C++
//
// Usage:
// Include this header, and operator<< will "just work".

#ifndef H_PRETTY_PRINT
#define H_PRETTY_PRINT

#include <cstddef>
#include <iterator>
#include <memory>
#include <ostream>
#include <set>
#include <tuple>
#include <type_traits>
#include <unordered_set>
#include <utility>
#include <valarray>

namespace pretty_print
{
    namespace detail
    {
        // SFINAE type trait to detect whether T::const_iterator exists.

        struct sfinae_base
        {
            using yes = char;
            using no  = yes[2];
        };

        template <typename T>
        struct has_const_iterator : private sfinae_base
        {
        private:
            template <typename C> static yes & test(typename C::const_iterator*);
            template <typename C> static no  & test(...);
        public:
            static const bool value = sizeof(test<T>(nullptr)) == sizeof(yes);
            using type =  T;
        };

        template <typename T>
        struct has_begin_end : private sfinae_base
        {
        private:
            template <typename C>
            static yes & f(typename std::enable_if<
                std::is_same<decltype(static_cast<typename C::const_iterator(C::*)() const>(&C::begin)),
                             typename C::const_iterator(C::*)() const>::value>::type *);

            template <typename C> static no & f(...);

            template <typename C>
            static yes & g(typename std::enable_if<
                std::is_same<decltype(static_cast<typename C::const_iterator(C::*)() const>(&C::end)),
                             typename C::const_iterator(C::*)() const>::value, void>::type*);

            template <typename C> static no & g(...);

        public:
            static bool const beg_value = sizeof(f<T>(nullptr)) == sizeof(yes);
            static bool const end_value = sizeof(g<T>(nullptr)) == sizeof(yes);
        };

    }  // namespace detail


    // Holds the delimiter values for a specific character type

    template <typename TChar>
    struct delimiters_values
    {
        using char_type = TChar;
        const char_type * prefix;
        const char_type * delimiter;
        const char_type * postfix;
    };


    // Defines the delimiter values for a specific container and character type

    template <typename T, typename TChar>
    struct delimiters
    {
        using type = delimiters_values<TChar>;
        static const type values; 
    };


    // Functor to print containers. You can use this directly if you want
    // to specificy a non-default delimiters type. The printing logic can
    // be customized by specializing the nested template.

    template <typename T,
              typename TChar = char,
              typename TCharTraits = ::std::char_traits<TChar>,
              typename TDelimiters = delimiters<T, TChar>>
    struct print_container_helper
    {
        using delimiters_type = TDelimiters;
        using ostream_type = std::basic_ostream<TChar, TCharTraits>;

        template <typename U>
        struct printer
        {
            static void print_body(const U & c, ostream_type & stream)
            {
                using std::begin;
                using std::end;

                auto it = begin(c);
                const auto the_end = end(c);

                if (it != the_end)
                {
                    for ( ; ; )
                    {
                        stream << *it;

                    if (++it == the_end) break;

                    if (delimiters_type::values.delimiter != NULL)
                        stream << delimiters_type::values.delimiter;
                    }
                }
            }
        };

        print_container_helper(const T & container)
        : container_(container)
        { }

        inline void operator()(ostream_type & stream) const
        {
            if (delimiters_type::values.prefix != NULL)
                stream << delimiters_type::values.prefix;

            printer<T>::print_body(container_, stream);

            if (delimiters_type::values.postfix != NULL)
                stream << delimiters_type::values.postfix;
        }

    private:
        const T & container_;
    };

    // Specialization for pairs

    template <typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    template <typename T1, typename T2>
    struct print_container_helper<T, TChar, TCharTraits, TDelimiters>::printer<std::pair<T1, T2>>
    {
        using ostream_type = typename print_container_helper<T, TChar, TCharTraits, TDelimiters>::ostream_type;

        static void print_body(const std::pair<T1, T2> & c, ostream_type & stream)
        {
            stream << c.first;
            if (print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter != NULL)
                stream << print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter;
            stream << c.second;
        }
    };

    // Specialization for tuples

    template <typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    template <typename ...Args>
    struct print_container_helper<T, TChar, TCharTraits, TDelimiters>::printer<std::tuple<Args...>>
    {
        using ostream_type = typename print_container_helper<T, TChar, TCharTraits, TDelimiters>::ostream_type;
        using element_type = std::tuple<Args...>;

        template <std::size_t I> struct Int { };

        static void print_body(const element_type & c, ostream_type & stream)
        {
            tuple_print(c, stream, Int<0>());
        }

        static void tuple_print(const element_type &, ostream_type &, Int<sizeof...(Args)>)
        {
        }

        static void tuple_print(const element_type & c, ostream_type & stream,
                                typename std::conditional<sizeof...(Args) != 0, Int<0>, std::nullptr_t>::type)
        {
            stream << std::get<0>(c);
            tuple_print(c, stream, Int<1>());
        }

        template <std::size_t N>
        static void tuple_print(const element_type & c, ostream_type & stream, Int<N>)
        {
            if (print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter != NULL)
                stream << print_container_helper<T, TChar, TCharTraits, TDelimiters>::delimiters_type::values.delimiter;

            stream << std::get<N>(c);

            tuple_print(c, stream, Int<N + 1>());
        }
    };

    // Prints a print_container_helper to the specified stream.

    template<typename T, typename TChar, typename TCharTraits, typename TDelimiters>
    inline std::basic_ostream<TChar, TCharTraits> & operator<<(
        std::basic_ostream<TChar, TCharTraits> & stream,
        const print_container_helper<T, TChar, TCharTraits, TDelimiters> & helper)
    {
        helper(stream);
        return stream;
    }


    // Basic is_container template; specialize to derive from std::true_type for all desired container types

    template <typename T>
    struct is_container : public std::integral_constant<bool,
                                                        detail::has_const_iterator<T>::value &&
                                                        detail::has_begin_end<T>::beg_value  &&
                                                        detail::has_begin_end<T>::end_value> { };

    template <typename T, std::size_t N>
    struct is_container<T[N]> : std::true_type { };

    template <std::size_t N>
    struct is_container<char[N]> : std::false_type { };

    template <typename T>
    struct is_container<std::valarray<T>> : std::true_type { };

    template <typename T1, typename T2>
    struct is_container<std::pair<T1, T2>> : std::true_type { };

    template <typename ...Args>
    struct is_container<std::tuple<Args...>> : std::true_type { };


    // Default delimiters

    template <typename T> struct delimiters<T, char> { static const delimiters_values<char> values; };
    template <typename T> const delimiters_values<char> delimiters<T, char>::values = { "[", ", ", "]" };
    template <typename T> struct delimiters<T, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename T> const delimiters_values<wchar_t> delimiters<T, wchar_t>::values = { L"[", L", ", L"]" };


    // Delimiters for (multi)set and unordered_(multi)set

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::set<T, TComp, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::set<T, TComp, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::set<T, TComp, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::set<T, TComp, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::multiset<T, TComp, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::multiset<T, TComp, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename TComp, typename TAllocator>
    struct delimiters< ::std::multiset<T, TComp, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename TComp, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::multiset<T, TComp, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::unordered_set<T, THash, TEqual, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, char> { static const delimiters_values<char> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<char> delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, char>::values = { "{", ", ", "}" };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    struct delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, wchar_t> { static const delimiters_values<wchar_t> values; };

    template <typename T, typename THash, typename TEqual, typename TAllocator>
    const delimiters_values<wchar_t> delimiters< ::std::unordered_multiset<T, THash, TEqual, TAllocator>, wchar_t>::values = { L"{", L", ", L"}" };


    // Delimiters for pair and tuple

    template <typename T1, typename T2> struct delimiters<std::pair<T1, T2>, char> { static const delimiters_values<char> values; };
    template <typename T1, typename T2> const delimiters_values<char> delimiters<std::pair<T1, T2>, char>::values = { "(", ", ", ")" };
    template <typename T1, typename T2> struct delimiters< ::std::pair<T1, T2>, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename T1, typename T2> const delimiters_values<wchar_t> delimiters< ::std::pair<T1, T2>, wchar_t>::values = { L"(", L", ", L")" };

    template <typename ...Args> struct delimiters<std::tuple<Args...>, char> { static const delimiters_values<char> values; };
    template <typename ...Args> const delimiters_values<char> delimiters<std::tuple<Args...>, char>::values = { "(", ", ", ")" };
    template <typename ...Args> struct delimiters< ::std::tuple<Args...>, wchar_t> { static const delimiters_values<wchar_t> values; };
    template <typename ...Args> const delimiters_values<wchar_t> delimiters< ::std::tuple<Args...>, wchar_t>::values = { L"(", L", ", L")" };


    // Type-erasing helper class for easy use of custom delimiters.
    // Requires TCharTraits = std::char_traits<TChar> and TChar = char or wchar_t, and MyDelims needs to be defined for TChar.
    // Usage: "cout << pretty_print::custom_delims<MyDelims>(x)".

    struct custom_delims_base
    {
        virtual ~custom_delims_base() { }
        virtual std::ostream & stream(::std::ostream &) = 0;
        virtual std::wostream & stream(::std::wostream &) = 0;
    };

    template <typename T, typename Delims>
    struct custom_delims_wrapper : custom_delims_base
    {
        custom_delims_wrapper(const T & t_) : t(t_) { }

        std::ostream & stream(std::ostream & s)
        {
            return s << print_container_helper<T, char, std::char_traits<char>, Delims>(t);
        }

        std::wostream & stream(std::wostream & s)
        {
            return s << print_container_helper<T, wchar_t, std::char_traits<wchar_t>, Delims>(t);
        }

    private:
        const T & t;
    };

    template <typename Delims>
    struct custom_delims
    {
        template <typename Container>
        custom_delims(const Container & c) : base(new custom_delims_wrapper<Container, Delims>(c)) { }

        std::unique_ptr<custom_delims_base> base;
    };

    template <typename TChar, typename TCharTraits, typename Delims>
    inline std::basic_ostream<TChar, TCharTraits> & operator<<(std::basic_ostream<TChar, TCharTraits> & s, const custom_delims<Delims> & p)
    {
        return p.base->stream(s);
    }


    // A wrapper for a C-style array given as pointer-plus-size.
    // Usage: std::cout << pretty_print_array(arr, n) << std::endl;

    template<typename T>
    struct array_wrapper_n
    {
        typedef const T * const_iterator;
        typedef T value_type;

        array_wrapper_n(const T * const a, size_t n) : _array(a), _n(n) { }
        inline const_iterator begin() const { return _array; }
        inline const_iterator end() const { return _array + _n; }

    private:
        const T * const _array;
        size_t _n;
    };


    // A wrapper for hash-table based containers that offer local iterators to each bucket.
    // Usage: std::cout << bucket_print(m, 4) << std::endl;  (Prints bucket 5 of container m.)

    template <typename T>
    struct bucket_print_wrapper
    {
        typedef typename T::const_local_iterator const_iterator;
        typedef typename T::size_type size_type;

        const_iterator begin() const
        {
            return m_map.cbegin(n);
        }

        const_iterator end() const
        {
            return m_map.cend(n);
        }

        bucket_print_wrapper(const T & m, size_type bucket) : m_map(m), n(bucket) { }

    private:
        const T & m_map;
        const size_type n;
    };

}   // namespace pretty_print


// Global accessor functions for the convenience wrappers

template<typename T>
inline pretty_print::array_wrapper_n<T> pretty_print_array(const T * const a, size_t n)
{
    return pretty_print::array_wrapper_n<T>(a, n);
}

template <typename T> pretty_print::bucket_print_wrapper<T>
bucket_print(const T & m, typename T::size_type n)
{
    return pretty_print::bucket_print_wrapper<T>(m, n);
}


// Main magic entry point: An overload snuck into namespace std.
// Can we do better?

namespace std
{
    // Prints a container to the stream using default delimiters

    template<typename T, typename TChar, typename TCharTraits>
    inline typename enable_if< ::pretty_print::is_container<T>::value,
                              basic_ostream<TChar, TCharTraits> &>::type
    operator<<(basic_ostream<TChar, TCharTraits> & stream, const T & container)
    {
        return stream << ::pretty_print::print_container_helper<T, TChar, TCharTraits>(container);
    }
}



#endif  // H_PRETTY_PRINT

#ifdef LOCAL
#define dbg(args...)                       \
  {                                          \
    string _s = #args;                       \
    replace(_s.begin(), _s.end(), ',', ' '); \
    stringstream _ss(_s);                    \
    istream_iterator<string> _it(_ss);       \
    err(_it, args);                          \
  }
void err(std::istream_iterator<string> it) {}
template <typename T, typename... Args>
void err(std::istream_iterator<string> it, T a, Args... args) {
  cerr << *it << " = " << a << endl;
  err(++it, args...);
}
#else
#define dbg(args...) 42
#endif

#endif




void solve(int testId, istream &in, ostream &out) {
  int n;
  in >> n;
  int p, b;
  in >> p >> b;

  vector<int> a(n + 1);
  for (int i = 0; i < n; i++) {
    in >> a[i];
  }
  a[n] = p;

  dbg(a);

  vector<int> coe(n + 1);
  int g = modular::Extgcd(a, coe, p);

  if (g == 0) {
    if(b != 0){
      out << "NO";
      return;
    }
    out << "YES" << endl;
    for(int i = 0; i < n; i++){
      out << 0 << ' ';
    }
    return;
  }

  if (b % g != 0) {
    out << "NO";
    return;
  }

  out << "YES" << endl;
  int prod = b / g;
  for (int i = 0; i < n; i++) {
    out << modular::Modmul(prod, coe[i], p) << ' ';
  }
}

RUN_ONCE