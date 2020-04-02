#ifndef BINARY_SEARCH_H
#define BINARY_SEARCH_H

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
#ifndef DECIMAL_H
#define DECIMAL_H

namespace decimal {

ll Merge(int a, int b) {
  static ll mask = (1ll << 32) - 1;
  return ((ll)a << 32) | ((ll)b & mask);
}

template <class T>
T CeilDiv(T a, T b) {
  if (b < 0) {
    a = -a;
    b = -b;
  }
  T div = a / b;
  if (b * div < a) {
    div++;
  }
  return div;
}

template <class T>
T FloorDiv(T a, T b) {
  if (b < 0) {
    a = -a;
    b = -b;
  }
  T div = a / b;
  if (b * div > a) {
    div--;
  }
  return div;
}

template <class T>
T RoundToInt(double x) {
  return x >= 0 ? x + 0.5 : x - 0.5;
}
template <class T>
int Sign(T x) {
  return x < 0 ? -1 : x > 0 ? 1 : 0;
}
template <class T>
bool IsPlusOverflow(T a, T b) {
  if (Sign(a) != Sign(b)) {
    return false;
  }
  if (a < 0) {
    return a + b > 0;
  } else {
    return a + b < 0;
  }
}
template <class T>
bool IsMultiplicationOverflow(T a, T b, T limit) {
  if (limit < 0) {
    limit = -limit;
  }
  if (a < 0) {
    a = -a;
  }
  if (b < 0) {
    b = -b;
  }
  if (a == 0 || b == 0) {
    return false;
  }
  // a * b > limit => a > limit / b
  return a > limit / b;
}
}  // namespace decimal

#endif

namespace binary_search {
template <class T>
T BinarySearch(T l, T r, const function<bool(T)> &func) {
  assert(l <= r);
  while (l < r) {
    T mid = (l + r) >> 1;
    if (func(mid)) {
      r = mid;
    } else {
      l = mid + 1;
    }
  }
  return l;
}

/**
 * Used to find the maximum value of a lower convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func, T absolute, T relative) {
  while (r - l > absolute) {
    if (r < 0 && (r - l) / -r <= relative || l > 0 && (r - l) / l <= relative) {
      break;
    }
    T dist = (r - l) / 3;
    T ml = l + dist;
    T mr = r - dist;
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  return (l + r) / 2;
}

/**
 * Used to find the maximum value of a lower convex.
 * Assume f(-inf)<...<f(ans)=f(ans+1)=...=f(ans+k)>...>f(inf)
 */
template <class T>
T TernarySearch(T l, T r, const function<T(T)> &func) {
  while (r - l > 2) {
    T ml = l + decimal::FloorDiv(r - l, 3);
    T mr = r - decimal::CeilDiv(r - l, 3);
    if (func(ml) < func(mr)) {
      l = ml;
    } else {
      r = mr;
    }
  }
  while (l < r) {
    if (func(l) >= func(r)) {
      r--;
    } else {
      l++;
    }
  }
  return l;
}
}  // namespace binary_search

#endif

#ifndef MAX_FLOW_H
#define MAX_FLOW_H



namespace max_flow {

template <class T>
struct FlowEdge {
  int rev;
  T flow;
  bool real;
  int to;

  FlowEdge(int rv, T f, bool r, int t) : rev(rv), flow(f), real(r), to(t) {}
};

template <class T>
ostream &operator<<(ostream &os, FlowEdge<T> &e) {
  os << "{ flow = " << e.flow << ", real = " << e.real << ", to = " << e.to
     << "}";
  return os;
}

template <class T>
ostream &operator<<(ostream &os, vector<vector<FlowEdge<T>>> &g) {
  for (int i = 0; i < g.size(); i++) {
    for (FlowEdge<T> &e : g[i]) {
      if (e.real) {
        os << i << "-" << e.flow << "(" << g[e.to][e.rev].flow << ")"
           << "->" << e.to << endl;
      }
    }
  }
  return os;
}

template <class T>
void Reset(vector<vector<FlowEdge<T>>> &g) {
  for (vector<FlowEdge<T>> &v : g) {
    for (FlowEdge<T> &t : v) {
      if (t.real) {
        t.rev->flow += t.flow;
        t.flow = 0;
      }
    }
  }
}

template <class T>
inline void Send(vector<vector<FlowEdge<T>>> &g, FlowEdge<T> &e, T flow) {
  e.flow += flow;
  g[e.to][e.rev].flow -= flow;
}

template <class T>
void AddEdge(vector<vector<FlowEdge<T>>> &g, int u, int v, T cap) {
  if (u != v) {
    g[u].emplace_back(g[v].size(), 0, true, v);
    g[v].emplace_back(g[u].size() - 1, cap, false, u);
  } else {
    g[u].emplace_back(g[u].size() + 1, 0, true, u);
    g[u].emplace_back(g[u].size() - 1, cap, false, u);
  }
}

template <class T>
class MaxFlow {
 public:
  virtual T send(vector<vector<FlowEdge<T>>> &g, int src, int dst, T inf) = 0;
};

template <class T>
void Bfs(vector<vector<FlowEdge<T>>> &g, int inf, vector<int> &_dist,
         int _dst) {
  deque<int> dq;
  fill(_dist.begin(), _dist.end(), inf);
  _dist[_dst] = 0;
  dq.push_back(_dst);

  while (!dq.empty()) {
    int head = dq.front();
    dq.pop_front();
    for (FlowEdge<T> &e : g[head]) {
      if (e.flow == 0 || _dist[e.to] <= _dist[head] + 1) {
        continue;
      }
      _dist[e.to] = _dist[head] + 1;
      dq.push_back(e.to);
    }
  }
}

template <class T>
class ISAP : public MaxFlow<T> {
 private:
  vector<int> _dist;
  vector<int> _distCnt;
  int _src;
  int _dst;

  T dfs(vector<vector<FlowEdge<T>>> &g, int root, T flow) {
    if (root == _dst) {
      return flow;
    }
    T snapshot = flow;
    for (FlowEdge<T> &e : g[root]) {
      if (_dist[e.to] != _dist[root] - 1 || g[e.to][e.rev].flow == 0) {
        continue;
      }
      T sent = dfs(g, e.to, min(flow, g[e.to][e.rev].flow));
      flow -= sent;
      Send(g, e, sent);
      if (flow == 0) {
        break;
      }
    }
    if (flow > 0) {
      _distCnt[_dist[root]]--;
      _dist[root]++;
      _distCnt[_dist[root]]++;
      if (_distCnt[_dist[root] - 1] == 0) {
        _distCnt[_dist[_src]]--;
        _dist[_src] = g.size();
        _distCnt[_dist[_src]]++;
      }
    }

    return snapshot - flow;
  }

 public:
  T send(vector<vector<FlowEdge<T>>> &g, int src, int dst, T inf) {
    int n = g.size();
    _dist.resize(n);
    _distCnt.resize(n + 2);
    fill(_distCnt.begin(), _distCnt.end(), 0);
    _src = src;
    _dst = dst;
    Bfs(g, n, _dist, _dst);
    for (int i = 0; i < n; i++) {
      _distCnt[_dist[i]]++;
    }

    T total = 0;
    while (total < inf && _dist[_src] < n) {
      total += dfs(g, _src, inf - total);
    }

    return total;
  }
};

template <class T>
class Dinic : public MaxFlow<T> {
private:
  int _s;
  int _t;
  vector<int> dists;
  vector<typename vector<FlowEdge<T>>::iterator> iterators;
  vector<typename vector<FlowEdge<T>>::iterator> ends;
  T send(vector<vector<FlowEdge<T>>> &g, int root, T flow) {
    if (root == _t) {
      return flow;
    }
    T snapshot = flow;
    while (iterators[root] != ends[root]) {
      FlowEdge<T> &e = *iterators[root];
      iterators[root]++;
      T remain;
      if (dists[e.to] + 1 != dists[root] || (remain = g[e.to][e.rev].flow) == 0) {
        continue;
      }
      T sent = send(g, e.to, min(flow, remain));
      flow -= sent;
      Send(g, e, sent);
      if (flow == 0) {
        iterators[root]--;
        break;
      }
    }
    return snapshot - flow;
  }

 public:
  Dinic(int vertexNum)
      : dists(vertexNum), iterators(vertexNum), ends(vertexNum) {}

  T send(vector<vector<FlowEdge<T>>> &g, int s, int t, T inf) {
    _s = s;
    _t = t;
    T flow = 0;
    int n = g.size();
    while (flow < inf) {
      Bfs(g, n, dists, t);
      if (dists[s] == n) {
        break;
      }
      for (int i = 0; i < n; i++) {
        iterators[i] = g[i].begin();
        ends[i] = g[i].end();
      }
      flow += send(g, s, inf - flow);
    }
    return flow;
  }
};

}  // namespace max_flow

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


using namespace max_flow;
typedef max_flow::FlowEdge<int> fe;

int n;
inline int Left(int i) { return i; }
inline int Right(int i) { return i + n; }
inline int Src() { return n + n; }
inline int Dst() { return Src() + 1; }

void solve(int testId, istream &in, ostream &out) {
  in >> n;
  vector<vector<int>> mat(n, vector<int>(n));
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      in >> mat[i][j];
    }
  }

  vector<vector<fe>> g(Dst() + 1);
  max_flow::ISAP<int> dinic;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      AddEdge(g, Left(i), Right(j), 1);
    }
  }
  for (int i = 0; i < n; i++) {
    AddEdge(g, Src(), Left(i), 1);
    AddEdge(g, Right(i), Dst(), 1);
  }

  function<bool(int)> checker = [&](int mid) {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        auto &e = g[i][j];
        if (mat[i][j] <= mid) {
          e.flow = 0;
          g[e.to][e.rev].flow = 1;
        } else {
          e.flow = 0;
          g[e.to][e.rev].flow = 0;
        }
      }
    }
    for(auto &e : g[Src()]){
      e.flow = 0;
      g[e.to][e.rev].flow = 1;
    }
    for(auto &e : g[Dst()]){
      e.flow = 1;
      g[e.to][e.rev].flow = 0;
    }
dbg(g);
    
    int flow = dinic.send(g, Src(), Dst(), n);
    return flow >= n;
  };

  int mid = binary_search::BinarySearch((int)-1e6, (int)1e6, checker);
  checker(mid);

  out << mid << endl;
  for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
      if (g[i][j].flow > 0) {
        out << i + 1 << ' ' << j + 1 << endl;
      }
    }
  }
}

RUN_ONCE