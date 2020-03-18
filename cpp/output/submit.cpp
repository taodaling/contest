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
using std::multiset;
using std::multimap;

typedef unsigned int ui;
typedef long long ll;
typedef long double ld;
typedef unsigned long long ull;
typedef unsigned char byte;
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
#define dbg(args...)                         \
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
#define dbg2(x) cerr << #x << "=" << (x) << endl
#else
#define dbg(args...) 42
#define dbg2(x) 42
#endif

#endif

#ifndef GEO2_H
#define GEO2_H


#ifndef UTIL_H
#define UTIL_H



namespace util {
vector<int> Range(int l, int r) {
  vector<int> ans;
  ans.reserve(r - l + 1);
  for (int i = l; i <= r; i++) {
    ans.push_back(i);
  }
  return ans;
}
}  // namespace util

#endif

namespace geo2 {

const double PREC = 1e-10;

template <class T>
bool IsZero(T x) {
  return -PREC <= x && x <= PREC;
}
template <>
bool IsZero<int>(int x) {
  return x == 0;
}
template <>
bool IsZero<ll>(ll x) {
  return x == 0;
}

template <class T>
struct Point {
  T x, y;
  Point(T a, T b) : x(a), y(b) {}
  Point() { Point(0, 0); }
  Point<T> conj() const { return {x, -y}; }
  T square() const { return x * x + y * y; }
  double abs() const { return sqrt(square()); }
  bool half() const { return y > 0 || y == 0 && x < 0; }
};

template <class T>
class SortByOrder {
 public:
  bool operator()(const Point<T> &a, const Point<T> &b) {
    return a.x < b.x || a.x == b.x && a.y < b.y;
  }
};

template <class T>
class SortByPolarAngle {
 public:
  bool operator()(const Point<T> &a, const Point<T> &b) {
    if (a.half() != b.half()) {
      return a.half() < b.half();
    }
    return Orient(a, b) > 0;
  }
};

template <class T>
class SortByPolarAngleWithOrigin {
 private:
  Point<T> _origin;
  SortByPolarAngle<T> _sa;

 public:
  SortByPolarAngleWithOrigin(const Point<T> origin) : _origin(origin) {}

  bool operator()(const Point<T> &a, const Point<T> &b) {
    return _sa(a - _origin, b - _origin);
  }
};

template <class T>
Point<T> operator+(const Point<T> &a, const Point<T> &b) {
  return {a.x + b.x, a.y + b.y};
}

template <class T>
Point<T> operator-(const Point<T> &a, const Point<T> &b) {
  return {a.x - b.x, a.y - b.y};
}

template <class T>
Point<T> operator*(const Point<T> &a, T b) {
  return {a.x * b, a.y * b};
}

template <class T>
Point<T> operator*(T b, const Point<T> &a) {
  return {a.x * b, a.y * b};
}

template <class T>
Point<T> operator*(const Point<T> &a, const Point<T> &b) {
  return {a.x * b.x - a.y * b.y, a.x * b.y + a.y * b.x};
}

template <class T>
Point<T> operator/(const Point<T> &a, T b) {
  return {a.x / b, a.y / b};
}

template <class T>
Point<T> operator/(const Point<T> &a, const Point<T> &b) {
  return a * (b.conj() / b.square());
}

template <class T>
bool operator==(const Point<T> &a, const Point<T> &b) {
  return a.x == b.x && a.y == b.y;
}
template <class T>
bool operator!=(const Point<T> &a, const Point<T> &b) {
  return !(a == b);
}
template <class T>
ostream &operator<<(ostream &os, const Point<T> &pt) {
  os << "(" << pt.x << "," << pt.y << ")";
  return os;
}
template <class T>
istream &operator>>(istream &is, Point<T> &pt) {
  is >> pt.x >> pt.y;
  return is;
}

template <class T>
int Sign(T x) {
  return IsZero(x) ? 0 : x > 0 ? 1 : -1;
}

template <class T>
Point<T> Translate(const Point<T> &v, const Point<T> &p) {
  return p + v;
}

template <class T>
Point<T> Scale(const Point<T> &c, T factor, const Point<T> &p) {
  return c + (p - c) * factor;
}

template <class T>
Point<T> Rotate(const Point<T> &pt, double a) {
  double c = std::cos(a);
  double s = std::sin(a);
  return {pt.x * c - pt.y * s, pt.x * s + pt.y * c};
}

template <class T>
Point<T> Rotate(const Point<T> &origin, const Point<T> &pt, double a) {
  return Rotate(pt - origin, a) + origin;
}

template <class T>
Point<T> Perp(const Point<T> &pt) {
  return {-pt.y, pt.x};
}

template <class T>
Point<T> LinearTransformTo(const Point<T> &p, const Point<T> &fp,
                           const Point<T> &q, const Point<T> &fq,
                           const Point<T> &req) {
  return fp + (req - p) * (fq - fp) / (q - p);
}

template <class T>
T Dot(const Point<T> &a, const Point<T> &b) {
  return a.x * b.x + a.y * b.y;
}

template <class T>
T Dot(T x1, T y1, T x2, T y2) {
  return x1 * x2 + y1 * y2;
}

template <class T>
bool IsPerp(const Point<T> &a, const Point<T> &b) {
  return Dot(a, b) == 0;
}

template <class T>
double Angle(const Point<T> &a, const Point<T> &b) {
  return std::acos(clamp(Dot(a, b) / a.abs() / b.abs()), -1.0, 1.0);
}

template <class T>
T Cross(const Point<T> &a, const Point<T> &b) {
  return a.x * b.y - a.y * b.x;
}

template <class T>
T Cross(const Point<T> &origin, const Point<T> &a, const Point<T> &b) {
  return Cross(a - origin, b - origin);
}

template <class T>
int Orient(const Point<T> &b, const Point<T> &c) {
  return Sign(Cross(b, c));
}

template <class T>
int Orient(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Orient(b - a, c - a);
}

template <class T>
bool InAngle(const Point<T> &a, const Point<T> &b, const Point<T> &c,
             const Point<T> &p) {
  assert(Orient(a, b, c) != 0);
  if (Orient(a, b, c) < 0) {
    swap(b, c);
  }
  return Orient(a, b, p) >= 0 && Orient(a, c, p) <= 0;
}

template <class T>
double OrientedAngle(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  if (Orient(a, b, c) >= 0) {
    return Angle(b - a, c - a);
  } else {
    return 2 * PI - Angle(b - a, c - a);
  }
}

template <class T>
bool IsConvex(const vector<Point<T>> &p) {
  bool hasPos = false, hasNeg = false;
  for (int i = 0, n = p.size(); i < n; i++) {
    int o = Orient(p[i], p[(i + 1) % n], p[(i + 2) % n]);
    if (o > 0) hasPos = true;
    if (o < 0) hasNeg = true;
  }
  return !(hasPos & hasNeg);
}

/**
 * 判断c是否落在以a与b为直径两端的圆中（包含边界）
 */
template <class T>
bool InDisk(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Sign(Dot(a - c, b - c)) <= 0;
}

template <class T>
bool OnSegment(const Point<T> &a, const Point<T> &b, const Point<T> &c) {
  return Orient(a, b, c) == 0 && InDisk(a, b, c);
}

template <class T>
int Above(const Point<T> &a, const Point<T> &b) {
  return b.y >= a.y ? 1 : 0;
}

template <class T>
bool CrossRay(const Point<T> &a, const Point<T> &p, const Point<T> &q) {
  return (Above(a, q) - Above(a, p)) * Orient(a, p, q) > 0;
}

/**
 * 判断某个顶点是否落在矩形内，1表示矩形内，2表示矩形边缘�?0表示矩形�?
 */
template <class T>
int InPolygon(const vector<Point<T>> &polygon, const Point<T> &pt) {
  int cross = 0;
  for (int i = 0, n = polygon.size(); i < n; i++) {
    Point<T> cur = polygon[i];
    Point<T> next = polygon[(i + 1) % n];
    if (OnSegment(cur, next, pt)) {
      return 2;
    }
    if (CrossRay(pt, cur, next)) {
      cross++;
    }
  }
  return cross % 2;
}

template <class T>
int InPolygon(const vector<pair<Point<T>, Point<T>>> &polygonBorder,
              const Point<T> &pt) {
  int cross = 0;
  for (auto &seg : polygonBorder) {
    const Point<T> &cur = seg.first;
    const Point<T> &next = seg.second;
    if (OnSegment(cur, next, pt)) {
      return 2;
    }
    if (CrossRay(pt, cur, next)) {
      cross++;
    }
  }
  return cross % 2;
}

/**
 * 生成逆时针凸�?
 */
template <class T>
void ConvexHull(vector<Point<T>> &pts) {
  assert(pts.size() >= 2);
  int index = 0;
  int n = pts.size();
  SortByOrder<T> sbo;
  for (int i = 0; i < n; i++) {
    if (sbo(pts[i], pts[index])) {
      index = i;
    }
  }
  swap(pts[0], pts[index]);
  SortByPolarAngleWithOrigin<T> sba(pts[0]);
  sort(pts.begin() + 1, pts.end(), sba);
  int tail = 1;
  for (int i = 2; i < n; i++) {
    if (sba(pts[tail], pts[i])) {
      pts[++tail] = pts[i];
    } else if ((pts[tail] - pts[0]).square() < (pts[i] - pts[0]).square()) {
      pts[tail] = pts[i];
    }
  }
  pts.erase(pts.begin() + tail + 1, pts.end());
  deque<Point<T>> stk;
  stk.push_back(pts[0]);
  for (int i = 1; i < pts.size(); i++) {
    while (stk.size() >= 2) {
      Point<T> last = stk.back();
      stk.pop_back();
      Point<T> &second = stk.back();
      if (Orient(second, last, pts[i]) > 0) {
        stk.push_back(last);
        break;
      }
    }
    stk.push_back(pts[i]);
  }

  pts.assign(stk.begin(), stk.end());
}

/**
 * 获取线段a->b与线段c->d的交�?
 */
template <class T>
bool ProperIntersect(Point<T> &a, Point<T> &b, Point<T> &c, Point<T> &d,
                     Point<T> &ans) {
  T oa = Cross(c, d, a);
  T ob = Cross(c, d, b);
  T oc = Cross(a, b, c);
  T od = Cross(a, b, d);

  if (oa * ob < 0 && oc * od < 0) {
    ans = (a * ob - b * oa) / (ob - oa);
    return true;
  }
  return false;
}

/**
 * 判断线段交点
 */
template <class T>
bool Intersect(Point<T> a, Point<T> b, Point<T> c, Point<T> d, Point<T> &ans) {
  bool find = ProperIntersect(a, b, c, d, ans);
  if (!find && OnSegment(a, b, c)) {
    ans = c;
    find = true;
  }
  if (!find && OnSegment(a, b, d)) {
    ans = d;
    find = true;
  }
  if (!find && OnSegment(c, d, a)) {
    ans = a;
    find = true;
  }
  if (!find && OnSegment(c, d, b)) {
    ans = b;
    find = true;
  }
  return find;
}
}  // namespace geo2

#endif

#define doouble long double
#define EPS 1e-8
using pt = geo2::Point<double>;


double Round(double x) {
  if (x < 0) {
    return -Round(-x);
  }
  if ((ll)(x + EPS) != (ll)(x)) {
    return ceil(x);
  }
  if ((ll)(x - EPS) != (ll)x) {
    return floor(x);
  }
  return x;
}

void solve(int testId, istream &in, ostream &out) {
  int x1, y1, x2, y2;
  int n;
  in >> x1 >> y1 >> x2 >> y2 >> n;
  if (x1 == x2 || y1 == y2) {
    out << "no solution";
    return;
  }
  pt src(x1, y1);
  pt dst(x2, y2);

  int step = x1 < x2 ? 1 : -1;
  for (int i = x1 + step; i != x2 + step; i += step) {
    pt l = geo2::Scale(src, (double)(i - x1 - step) / (double)(x2 - x1), dst);
    pt r = geo2::Scale(src, (double)(i - x1) / (double)(x2 - x1), dst);
    l.y = Round(l.y);
    r.y = Round(r.y);

    pair<int, int> p(floor(min(l.y, r.y)), ceil(max(l.y, r.y)));
    int cnt = p.second - p.first;

    if (cnt > 1) {
      dbg(l, r, i, p);
    }
    dbg(cnt);
    if (n > cnt) {
      n -= cnt;
    } else {
      if (y1 < y2) {
        out << min(i - step, i) << ' ' << p.first + n - 1 << endl;
      } else {
        out << min(i - step, i) << ' ' << p.second - n << endl;
      }
      return;
    }
  }

  out << "no solution";
}

RUN_ONCE