import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.AbstractQueue;
import java.util.function.UnaryOperator;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.Future;
import java.util.Map;
import java.time.Duration;
import java.io.PrintWriter;
import java.util.concurrent.CancellationException;
import java.util.Set;
import java.security.PrivilegedExceptionAction;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.concurrent.locks.LockSupport;
import java.security.AccessController;

import sun.misc.Unsafe;

import java.util.concurrent.Callable;
import java.util.function.Supplier;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.RandomAccess;
import java.lang.ref.SoftReference;
import java.util.function.IntFunction;
import java.security.PrivilegedActionException;
import java.util.concurrent.Executor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;
import java.lang.reflect.Array;
import java.util.concurrent.ScheduledFuture;
import java.util.ListIterator;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.IntConsumer;
import java.util.concurrent.TimeoutException;
import java.util.AbstractCollection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Locale;
import java.util.IllegalFormatException;
import java.util.concurrent.Delayed;
import java.util.function.Predicate;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.Collection;
import java.util.logging.Logger;
import java.lang.ref.Reference;
import java.util.Objects;
import java.util.List;
import java.io.Writer;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.IntStream;
import java.util.AbstractSet;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;
import java.util.concurrent.RejectedExecutionException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.util.NoSuchElementException;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ExecutorService;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.AbstractMap;
import java.util.Spliterator.OfInt;
import java.io.Closeable;
import java.util.Comparator;
import java.util.Collections;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 *
 * @author daltao
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(null, new TaskAdapter(), "daltao", 1 << 27);
        thread.start();
        thread.join();
    }

    static class TaskAdapter implements Runnable {
        @Override
        public void run() {
            InputStream inputStream = System.in;
            OutputStream outputStream = System.out;
            FastInput in = new FastInput(inputStream);
            FastOutput out = new FastOutput(outputStream);
            Task solver = new Task();
            solver.solve(1, in, out);
            out.close();
        }
    }

    static class Task {
        public void solve(int testNumber, FastInput in, FastOutput out) {
            CacheBuilder.newBuilder().build().getIfPresent(null);
        }

    }

    static class LocalCache<K, V> extends AbstractMap<K, V> implements ConcurrentMap<K, V> {
        static final int MAXIMUM_CAPACITY = 1 << 30;
        static final int MAX_SEGMENTS = 1 << 16;
        static final int CONTAINS_VALUE_RETRIES = 3;
        static final int DRAIN_THRESHOLD = 0x3F;
        static final int DRAIN_MAX = 16;
        static final Logger logger = Logger.getLogger(LocalCache.class.getName());
        final int segmentMask;
        final int segmentShift;
        final LocalCache.Segment<K, V>[] segments;
        final int concurrencyLevel;
        final Equivalence<Object> keyEquivalence;
        final Equivalence<Object> valueEquivalence;
        final LocalCache.Strength keyStrength;
        final LocalCache.Strength valueStrength;
        final long maxWeight;
        final Weigher<K, V> weigher;
        final long expireAfterAccessNanos;
        final long expireAfterWriteNanos;
        final long refreshNanos;
        final Queue<RemovalNotification<K, V>> removalNotificationQueue;
        final RemovalListener<K, V> removalListener;
        final Ticker ticker;
        final LocalCache.EntryFactory entryFactory;
        final StatsCounter globalStatsCounter;
        final CacheLoader<? super K, V> defaultLoader;
        static final LocalCache.ValueReference<Object, Object> UNSET = new LocalCache.ValueReference<Object, Object>() {

            public Object get() {
                return null;
            }


            public int getWeight() {
                return 0;
            }


            public ReferenceEntry<Object, Object> getEntry() {
                return null;
            }


            public LocalCache.ValueReference<Object, Object> copyFor(
                    ReferenceQueue<Object> queue,
                    Object value,
                    ReferenceEntry<Object, Object> entry) {
                return this;
            }


            public boolean isLoading() {
                return false;
            }


            public boolean isActive() {
                return false;
            }


            public Object waitForValue() {
                return null;
            }


            public void notifyNewValue(Object newValue) {
            }
        };
        static final Queue<?> DISCARDING_QUEUE = new AbstractQueue<Object>() {

            public boolean offer(Object o) {
                return true;
            }


            public Object peek() {
                return null;
            }


            public Object poll() {
                return null;
            }


            public int size() {
                return 0;
            }


            public Iterator<Object> iterator() {
                return ImmutableSet.of().iterator();
            }
        };
        Set<K> keySet;
        Collection<V> values;
        Set<Entry<K, V>> entrySet;

        LocalCache(
                CacheBuilder<? super K, ? super V> builder, CacheLoader<? super K, V> loader) {
            concurrencyLevel = Math.min(builder.getConcurrencyLevel(), MAX_SEGMENTS);

            keyStrength = builder.getKeyStrength();
            valueStrength = builder.getValueStrength();

            keyEquivalence = builder.getKeyEquivalence();
            valueEquivalence = builder.getValueEquivalence();

            maxWeight = builder.getMaximumWeight();
            weigher = builder.getWeigher();
            expireAfterAccessNanos = builder.getExpireAfterAccessNanos();
            expireAfterWriteNanos = builder.getExpireAfterWriteNanos();
            refreshNanos = builder.getRefreshNanos();

            removalListener = builder.getRemovalListener();
            removalNotificationQueue =
                    (removalListener == CacheBuilder.NullListener.INSTANCE)
                            ? LocalCache.<RemovalNotification<K, V>>discardingQueue()
                            : new ConcurrentLinkedQueue<RemovalNotification<K, V>>();

            ticker = builder.getTicker(recordsTime());
            entryFactory = LocalCache.EntryFactory.getFactory(keyStrength, usesAccessEntries(), usesWriteEntries());
            globalStatsCounter = builder.getStatsCounterSupplier().get();
            defaultLoader = loader;

            int initialCapacity = Math.min(builder.getInitialCapacity(), MAXIMUM_CAPACITY);
            if (evictsBySize() && !customWeigher()) {
                initialCapacity = (int) Math.min(initialCapacity, maxWeight);
            }

            // Find the lowest power-of-two segmentCount that exceeds concurrencyLevel, unless
            // maximumSize/Weight is specified in which case ensure that each segment gets at least 10
            // entries. The special casing for size-based eviction is only necessary because that eviction
            // happens per segment instead of globally, so too many segments compared to the maximum size
            // will result in random eviction behavior.
            int segmentShift = 0;
            int segmentCount = 1;
            while (segmentCount < concurrencyLevel && (!evictsBySize() || segmentCount * 20 <= maxWeight)) {
                ++segmentShift;
                segmentCount <<= 1;
            }
            this.segmentShift = 32 - segmentShift;
            segmentMask = segmentCount - 1;

            this.segments = newSegmentArray(segmentCount);

            int segmentCapacity = initialCapacity / segmentCount;
            if (segmentCapacity * segmentCount < initialCapacity) {
                ++segmentCapacity;
            }

            int segmentSize = 1;
            while (segmentSize < segmentCapacity) {
                segmentSize <<= 1;
            }

            if (evictsBySize()) {
                // Ensure sum of segment max weights = overall max weights
                long maxSegmentWeight = maxWeight / segmentCount + 1;
                long remainder = maxWeight % segmentCount;
                for (int i = 0; i < this.segments.length; ++i) {
                    if (i == remainder) {
                        maxSegmentWeight--;
                    }
                    this.segments[i] =
                            createSegment(segmentSize, maxSegmentWeight, builder.getStatsCounterSupplier().get());
                }
            } else {
                for (int i = 0; i < this.segments.length; ++i) {
                    this.segments[i] =
                            createSegment(segmentSize, CacheBuilder.UNSET_INT, builder.getStatsCounterSupplier().get());
                }
            }
        }

        boolean evictsBySize() {
            return maxWeight >= 0;
        }

        boolean customWeigher() {
            return weigher != CacheBuilder.OneWeigher.INSTANCE;
        }

        boolean expiresAfterWrite() {
            return expireAfterWriteNanos > 0;
        }

        boolean expiresAfterAccess() {
            return expireAfterAccessNanos > 0;
        }

        boolean refreshes() {
            return refreshNanos > 0;
        }

        boolean usesAccessQueue() {
            return expiresAfterAccess() || evictsBySize();
        }

        boolean usesWriteQueue() {
            return expiresAfterWrite();
        }

        boolean recordsWrite() {
            return expiresAfterWrite() || refreshes();
        }

        boolean recordsAccess() {
            return expiresAfterAccess();
        }

        boolean recordsTime() {
            return recordsWrite() || recordsAccess();
        }

        boolean usesWriteEntries() {
            return usesWriteQueue() || recordsWrite();
        }

        boolean usesAccessEntries() {
            return usesAccessQueue() || recordsAccess();
        }

        boolean usesKeyReferences() {
            return keyStrength != LocalCache.Strength.STRONG;
        }

        boolean usesValueReferences() {
            return valueStrength != LocalCache.Strength.STRONG;
        }

        // impl never uses a parameter or returns any non-null value
        static <K, V> LocalCache.ValueReference<K, V> unset() {
            return (LocalCache.ValueReference<K, V>) UNSET;
        }

        // impl never uses a parameter or returns any non-null value
        static <K, V> ReferenceEntry<K, V> nullEntry() {
            return (ReferenceEntry<K, V>) LocalCache.NullEntry.INSTANCE;
        }

        // impl never uses a parameter or returns any non-null value
        static <E> Queue<E> discardingQueue() {
            return (Queue) DISCARDING_QUEUE;
        }

        static int rehash(int h) {
            // Spread bits to regularize both segment and index locations,
            // using variant of single-word Wang/Jenkins hash.
            // TODO(kevinb): use Hashing/move this to Hashing?
            h += (h << 15) ^ 0xffffcd7d;
            h ^= (h >>> 10);
            h += (h << 3);
            h ^= (h >>> 6);
            h += (h << 2) + (h << 14);
            return h ^ (h >>> 16);
        }

        int hash(Object key) {
            int h = keyEquivalence.hash(key);
            return rehash(h);
        }

        void reclaimValue(LocalCache.ValueReference<K, V> valueReference) {
            ReferenceEntry<K, V> entry = valueReference.getEntry();
            int hash = entry.getHash();
            segmentFor(hash).reclaimValue(entry.getKey(), hash, valueReference);
        }

        void reclaimKey(ReferenceEntry<K, V> entry) {
            int hash = entry.getHash();
            segmentFor(hash).reclaimKey(entry, hash);
        }

        LocalCache.Segment<K, V> segmentFor(int hash) {
            // TODO(fry): Lazily create segments?
            return segments[(hash >>> segmentShift) & segmentMask];
        }

        LocalCache.Segment<K, V> createSegment(
                int initialCapacity, long maxSegmentWeight, StatsCounter statsCounter) {
            return new LocalCache.Segment<>(this, initialCapacity, maxSegmentWeight, statsCounter);
        }

        V getLiveValue(ReferenceEntry<K, V> entry, long now) {
            if (entry.getKey() == null) {
                return null;
            }
            V value = entry.getValueReference().get();
            if (value == null) {
                return null;
            }

            if (isExpired(entry, now)) {
                return null;
            }
            return value;
        }

        boolean isExpired(ReferenceEntry<K, V> entry, long now) {
            Preconditions.checkNotNull(entry);
            if (expiresAfterAccess() && (now - entry.getAccessTime() >= expireAfterAccessNanos)) {
                return true;
            }
            if (expiresAfterWrite() && (now - entry.getWriteTime() >= expireAfterWriteNanos)) {
                return true;
            }
            return false;
        }

        static <K, V> void connectAccessOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
            previous.setNextInAccessQueue(next);
            next.setPreviousInAccessQueue(previous);
        }

        static <K, V> void nullifyAccessOrder(ReferenceEntry<K, V> nulled) {
            ReferenceEntry<K, V> nullEntry = nullEntry();
            nulled.setNextInAccessQueue(nullEntry);
            nulled.setPreviousInAccessQueue(nullEntry);
        }

        static <K, V> void connectWriteOrder(ReferenceEntry<K, V> previous, ReferenceEntry<K, V> next) {
            previous.setNextInWriteQueue(next);
            next.setPreviousInWriteQueue(previous);
        }

        static <K, V> void nullifyWriteOrder(ReferenceEntry<K, V> nulled) {
            ReferenceEntry<K, V> nullEntry = nullEntry();
            nulled.setNextInWriteQueue(nullEntry);
            nulled.setPreviousInWriteQueue(nullEntry);
        }

        void processPendingNotifications() {
            RemovalNotification<K, V> notification;
            while ((notification = removalNotificationQueue.poll()) != null) {
                try {
                    removalListener.onRemoval(notification);
                } catch (Throwable e) {
                    logger.log(Level.WARNING, "Exception thrown by removal listener", e);
                }
            }
        }

        final LocalCache.Segment<K, V>[] newSegmentArray(int ssize) {
            return new LocalCache.Segment[ssize];
        }

        public boolean isEmpty() {
            /*
             * Sum per-segment modCounts to avoid mis-reporting when elements are concurrently added and
             * removed in one segment while checking another, in which case the table was never actually
             * empty at any point. (The sum ensures accuracy up through at least 1<<31 per-segment
             * modifications before recheck.) Method containsValue() uses similar constructions for
             * stability checks.
             */
            long sum = 0L;
            LocalCache.Segment<K, V>[] segments = this.segments;
            for (int i = 0; i < segments.length; ++i) {
                if (segments[i].count != 0) {
                    return false;
                }
                sum += segments[i].modCount;
            }

            if (sum != 0L) { // recheck unless no modifications
                for (int i = 0; i < segments.length; ++i) {
                    if (segments[i].count != 0) {
                        return false;
                    }
                    sum -= segments[i].modCount;
                }
                if (sum != 0L) {
                    return false;
                }
            }
            return true;
        }

        long longSize() {
            LocalCache.Segment<K, V>[] segments = this.segments;
            long sum = 0;
            for (int i = 0; i < segments.length; ++i) {
                sum += Math.max(0, segments[i].count); // see https://github.com/google/guava/issues/2108
            }
            return sum;
        }

        public int size() {
            return Ints.saturatedCast(longSize());
        }

        public V get(Object key) {
            if (key == null) {
                return null;
            }
            int hash = hash(key);
            return segmentFor(hash).get(key, hash);
        }

        public V getIfPresent(Object key) {
            int hash = hash(Preconditions.checkNotNull(key));
            V value = segmentFor(hash).get(key, hash);
            if (value == null) {
                globalStatsCounter.recordMisses(1);
            } else {
                globalStatsCounter.recordHits(1);
            }
            return value;
        }

        public V getOrDefault(Object key, V defaultValue) {
            V result = get(key);
            return (result != null) ? result : defaultValue;
        }

        public boolean containsKey(Object key) {
            // does not impact recency ordering
            if (key == null) {
                return false;
            }
            int hash = hash(key);
            return segmentFor(hash).containsKey(key, hash);
        }

        public boolean containsValue(Object value) {
            // does not impact recency ordering
            if (value == null) {
                return false;
            }

            // This implementation is patterned after ConcurrentHashMap, but without the locking. The only
            // way for it to return a false negative would be for the target value to jump around in the map
            // such that none of the subsequent iterations observed it, despite the fact that at every point
            // in time it was present somewhere int the map. This becomes increasingly unlikely as
            // CONTAINS_VALUE_RETRIES increases, though without locking it is theoretically possible.
            long now = ticker.read();
            final LocalCache.Segment<K, V>[] segments = this.segments;
            long last = -1L;
            for (int i = 0; i < CONTAINS_VALUE_RETRIES; i++) {
                long sum = 0L;
                for (LocalCache.Segment<K, V> segment : segments) {
                    // ensure visibility of most recent completed write
                    int unused = segment.count; // read-volatile

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = segment.table;
                    for (int j = 0; j < table.length(); j++) {
                        for (ReferenceEntry<K, V> e = table.get(j); e != null; e = e.getNext()) {
                            V v = segment.getLiveValue(e, now);
                            if (v != null && valueEquivalence.equivalent(value, v)) {
                                return true;
                            }
                        }
                    }
                    sum += segment.modCount;
                }
                if (sum == last) {
                    break;
                }
                last = sum;
            }
            return false;
        }

        public V put(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            int hash = hash(key);
            return segmentFor(hash).put(key, hash, value, false);
        }

        public V putIfAbsent(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            int hash = hash(key);
            return segmentFor(hash).put(key, hash, value, true);
        }

        public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(function);
            int hash = hash(key);
            return segmentFor(hash).compute(key, hash, function);
        }

        public V computeIfAbsent(K key, Function<? super K, ? extends V> function) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(function);
            return compute(key, (k, oldValue) -> (oldValue == null) ? function.apply(key) : oldValue);
        }

        public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> function) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(function);
            return compute(key, (k, oldValue) -> (oldValue == null) ? null : function.apply(k, oldValue));
        }

        public V merge(K key, V newValue, BiFunction<? super V, ? super V, ? extends V> function) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            Preconditions.checkNotNull(function);
            return compute(
                    key, (k, oldValue) -> (oldValue == null) ? newValue : function.apply(oldValue, newValue));
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            for (Entry<? extends K, ? extends V> e : m.entrySet()) {
                put(e.getKey(), e.getValue());
            }
        }

        public V remove(Object key) {
            if (key == null) {
                return null;
            }
            int hash = hash(key);
            return segmentFor(hash).remove(key, hash);
        }

        public boolean remove(Object key, Object value) {
            if (key == null || value == null) {
                return false;
            }
            int hash = hash(key);
            return segmentFor(hash).remove(key, hash, value);
        }

        public boolean replace(K key, V oldValue, V newValue) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(newValue);
            if (oldValue == null) {
                return false;
            }
            int hash = hash(key);
            return segmentFor(hash).replace(key, hash, oldValue, newValue);
        }

        public V replace(K key, V value) {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(value);
            int hash = hash(key);
            return segmentFor(hash).replace(key, hash, value);
        }

        public void clear() {
            for (LocalCache.Segment<K, V> segment : segments) {
                segment.clear();
            }
        }

        public Set<K> keySet() {
            // does not impact recency ordering
            Set<K> ks = keySet;
            return (ks != null) ? ks : (keySet = new KeySet(this));
        }

        public Collection<V> values() {
            // does not impact recency ordering
            Collection<V> vs = values;
            return (vs != null) ? vs : (values = new Values(this));
        }

        // Not supported.
        public Set<Entry<K, V>> entrySet() {
            // does not impact recency ordering
            Set<Entry<K, V>> es = entrySet;
            return (es != null) ? es : (entrySet = new EntrySet(this));
        }

        private static <E> ArrayList<E> toArrayList(Collection<E> c) {
            // Avoid calling ArrayList(Collection), which may call back into toArray.
            ArrayList<E> result = new ArrayList<E>(c.size());
            Iterators.addAll(result, c.iterator());
            return result;
        }

        boolean removeIf(BiPredicate<? super K, ? super V> filter) {
            Preconditions.checkNotNull(filter);
            boolean changed = false;
            for (K key : keySet()) {
                while (true) {
                    V value = get(key);
                    if (value == null || !filter.test(key, value)) {
                        break;
                    } else if (LocalCache.this.remove(key, value)) {
                        changed = true;
                        break;
                    }
                }
            }
            return changed;
        }

        enum Strength {
            STRONG {
                <K, V> LocalCache.ValueReference<K, V> referenceValue(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight) {
                    return (weight == 1)
                            ? new LocalCache.StrongValueReference<K, V>(value)
                            : new LocalCache.WeightedStrongValueReference<K, V>(value, weight);
                }


                Equivalence<Object> defaultEquivalence() {
                    return Equivalence.equals();
                }
            },
            SOFT {
                <K, V> LocalCache.ValueReference<K, V> referenceValue(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight) {
                    return (weight == 1)
                            ? new LocalCache.SoftValueReference<K, V>(segment.valueReferenceQueue, value, entry)
                            : new LocalCache.WeightedSoftValueReference<K, V>(
                            segment.valueReferenceQueue, value, entry, weight);
                }


                Equivalence<Object> defaultEquivalence() {
                    return Equivalence.identity();
                }
            },
            WEAK {
                <K, V> LocalCache.ValueReference<K, V> referenceValue(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight) {
                    return (weight == 1)
                            ? new LocalCache.WeakValueReference<K, V>(segment.valueReferenceQueue, value, entry)
                            : new LocalCache.WeightedWeakValueReference<K, V>(
                            segment.valueReferenceQueue, value, entry, weight);
                }


                Equivalence<Object> defaultEquivalence() {
                    return Equivalence.identity();
                }
            },
            ;

            abstract <K, V> LocalCache.ValueReference<K, V> referenceValue(
                    LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> entry, V value, int weight);

            abstract Equivalence<Object> defaultEquivalence();

        }

        enum EntryFactory {
            STRONG {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.StrongEntry<>(key, hash, next);
                }
            },
            STRONG_ACCESS {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.StrongAccessEntry<>(key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyAccessEntry(original, newEntry);
                    return newEntry;
                }
            },
            STRONG_WRITE {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.StrongWriteEntry<>(key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyWriteEntry(original, newEntry);
                    return newEntry;
                }
            },
            STRONG_ACCESS_WRITE {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.StrongAccessWriteEntry<>(key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyAccessEntry(original, newEntry);
                    copyWriteEntry(original, newEntry);
                    return newEntry;
                }
            },
            WEAK {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.WeakEntry<>(segment.keyReferenceQueue, key, hash, next);
                }
            },
            WEAK_ACCESS {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.WeakAccessEntry<>(segment.keyReferenceQueue, key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyAccessEntry(original, newEntry);
                    return newEntry;
                }
            },
            WEAK_WRITE {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.WeakWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyWriteEntry(original, newEntry);
                    return newEntry;
                }
            },
            WEAK_ACCESS_WRITE {
                <K, V> ReferenceEntry<K, V> newEntry(
                        LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next) {
                    return new LocalCache.WeakAccessWriteEntry<>(segment.keyReferenceQueue, key, hash, next);
                }


                <K, V> ReferenceEntry<K, V> copyEntry(
                        LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                    ReferenceEntry<K, V> newEntry = super.copyEntry(segment, original, newNext);
                    copyAccessEntry(original, newEntry);
                    copyWriteEntry(original, newEntry);
                    return newEntry;
                }
            },
            ;
            static final int ACCESS_MASK = 1;
            static final int WRITE_MASK = 2;
            static final int WEAK_MASK = 4;
            static final LocalCache.EntryFactory[] factories = {
                    STRONG,
                    STRONG_ACCESS,
                    STRONG_WRITE,
                    STRONG_ACCESS_WRITE,
                    WEAK,
                    WEAK_ACCESS,
                    WEAK_WRITE,
                    WEAK_ACCESS_WRITE,
            };

            static LocalCache.EntryFactory getFactory(
                    LocalCache.Strength keyStrength, boolean usesAccessQueue, boolean usesWriteQueue) {
                int flags =
                        ((keyStrength == LocalCache.Strength.WEAK) ? WEAK_MASK : 0)
                                | (usesAccessQueue ? ACCESS_MASK : 0)
                                | (usesWriteQueue ? WRITE_MASK : 0);
                return factories[flags];
            }

            abstract <K, V> ReferenceEntry<K, V> newEntry(
                    LocalCache.Segment<K, V> segment, K key, int hash, ReferenceEntry<K, V> next);

            <K, V> ReferenceEntry<K, V> copyEntry(
                    LocalCache.Segment<K, V> segment, ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                return newEntry(segment, original.getKey(), original.getHash(), newNext);
            }

            <K, V> void copyAccessEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
                // TODO(fry): when we link values instead of entries this method can go
                // away, as can connectAccessOrder, nullifyAccessOrder.
                newEntry.setAccessTime(original.getAccessTime());

                connectAccessOrder(original.getPreviousInAccessQueue(), newEntry);
                connectAccessOrder(newEntry, original.getNextInAccessQueue());

                nullifyAccessOrder(original);
            }

            <K, V> void copyWriteEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newEntry) {
                // TODO(fry): when we link values instead of entries this method can go
                // away, as can connectWriteOrder, nullifyWriteOrder.
                newEntry.setWriteTime(original.getWriteTime());

                connectWriteOrder(original.getPreviousInWriteQueue(), newEntry);
                connectWriteOrder(newEntry, original.getNextInWriteQueue());

                nullifyWriteOrder(original);
            }

        }

        interface ValueReference<K, V> {
            V get();

            V waitForValue() throws ExecutionException;

            int getWeight();

            ReferenceEntry<K, V> getEntry();

            LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry);

            void notifyNewValue(V newValue);

            boolean isLoading();

            boolean isActive();

        }

        private enum NullEntry implements ReferenceEntry<Object, Object> {
            INSTANCE,
            ;

            public LocalCache.ValueReference<Object, Object> getValueReference() {
                return null;
            }

            public void setValueReference(LocalCache.ValueReference<Object, Object> valueReference) {
            }

            public ReferenceEntry<Object, Object> getNext() {
                return null;
            }

            public int getHash() {
                return 0;
            }

            public Object getKey() {
                return null;
            }

            public long getAccessTime() {
                return 0;
            }

            public void setAccessTime(long time) {
            }

            public ReferenceEntry<Object, Object> getNextInAccessQueue() {
                return this;
            }

            public void setNextInAccessQueue(ReferenceEntry<Object, Object> next) {
            }

            public ReferenceEntry<Object, Object> getPreviousInAccessQueue() {
                return this;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<Object, Object> previous) {
            }

            public long getWriteTime() {
                return 0;
            }

            public void setWriteTime(long time) {
            }

            public ReferenceEntry<Object, Object> getNextInWriteQueue() {
                return this;
            }

            public void setNextInWriteQueue(ReferenceEntry<Object, Object> next) {
            }

            public ReferenceEntry<Object, Object> getPreviousInWriteQueue() {
                return this;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<Object, Object> previous) {
            }

        }

        abstract static class AbstractReferenceEntry<K, V> implements ReferenceEntry<K, V> {
            public LocalCache.ValueReference<K, V> getValueReference() {
                throw new UnsupportedOperationException();
            }

            public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getNext() {
                throw new UnsupportedOperationException();
            }

            public int getHash() {
                throw new UnsupportedOperationException();
            }

            public K getKey() {
                throw new UnsupportedOperationException();
            }

            public long getAccessTime() {
                throw new UnsupportedOperationException();
            }

            public void setAccessTime(long time) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                throw new UnsupportedOperationException();
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                throw new UnsupportedOperationException();
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                throw new UnsupportedOperationException();
            }

            public long getWriteTime() {
                throw new UnsupportedOperationException();
            }

            public void setWriteTime(long time) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                throw new UnsupportedOperationException();
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                throw new UnsupportedOperationException();
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                throw new UnsupportedOperationException();
            }

        }

        static class StrongEntry<K, V> extends LocalCache.AbstractReferenceEntry<K, V> {
            final K key;
            final int hash;
            final ReferenceEntry<K, V> next;
            volatile LocalCache.ValueReference<K, V> valueReference = unset();

            StrongEntry(K key, int hash, ReferenceEntry<K, V> next) {
                this.key = key;
                this.hash = hash;
                this.next = next;
            }

            public K getKey() {
                return this.key;
            }

            public LocalCache.ValueReference<K, V> getValueReference() {
                return valueReference;
            }

            public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
                this.valueReference = valueReference;
            }

            public int getHash() {
                return hash;
            }

            public ReferenceEntry<K, V> getNext() {
                return next;
            }

        }

        static final class StrongAccessEntry<K, V> extends LocalCache.StrongEntry<K, V> {
            volatile long accessTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextAccess = nullEntry();
            ReferenceEntry<K, V> previousAccess = nullEntry();

            StrongAccessEntry(K key, int hash, ReferenceEntry<K, V> next) {
                super(key, hash, next);
            }

            public long getAccessTime() {
                return accessTime;
            }

            public void setAccessTime(long time) {
                this.accessTime = time;
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }

        }

        static final class StrongWriteEntry<K, V> extends LocalCache.StrongEntry<K, V> {
            volatile long writeTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextWrite = nullEntry();
            ReferenceEntry<K, V> previousWrite = nullEntry();

            StrongWriteEntry(K key, int hash, ReferenceEntry<K, V> next) {
                super(key, hash, next);
            }

            public long getWriteTime() {
                return writeTime;
            }

            public void setWriteTime(long time) {
                this.writeTime = time;
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }

        }

        static final class StrongAccessWriteEntry<K, V> extends LocalCache.StrongEntry<K, V> {
            volatile long accessTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextAccess = nullEntry();
            ReferenceEntry<K, V> previousAccess = nullEntry();
            volatile long writeTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextWrite = nullEntry();
            ReferenceEntry<K, V> previousWrite = nullEntry();

            StrongAccessWriteEntry(K key, int hash, ReferenceEntry<K, V> next) {
                super(key, hash, next);
            }

            public long getAccessTime() {
                return accessTime;
            }

            public void setAccessTime(long time) {
                this.accessTime = time;
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }

            public long getWriteTime() {
                return writeTime;
            }

            public void setWriteTime(long time) {
                this.writeTime = time;
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }

        }

        static class WeakEntry<K, V> extends WeakReference<K> implements ReferenceEntry<K, V> {
            final int hash;
            final ReferenceEntry<K, V> next;
            volatile LocalCache.ValueReference<K, V> valueReference = unset();

            WeakEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) {
                super(key, queue);
                this.hash = hash;
                this.next = next;
            }

            public K getKey() {
                return get();
            }

            public long getAccessTime() {
                throw new UnsupportedOperationException();
            }

            public void setAccessTime(long time) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                throw new UnsupportedOperationException();
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                throw new UnsupportedOperationException();
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                throw new UnsupportedOperationException();
            }

            public long getWriteTime() {
                throw new UnsupportedOperationException();
            }

            public void setWriteTime(long time) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                throw new UnsupportedOperationException();
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                throw new UnsupportedOperationException();
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                throw new UnsupportedOperationException();
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                throw new UnsupportedOperationException();
            }

            public LocalCache.ValueReference<K, V> getValueReference() {
                return valueReference;
            }

            public void setValueReference(LocalCache.ValueReference<K, V> valueReference) {
                this.valueReference = valueReference;
            }

            public int getHash() {
                return hash;
            }

            public ReferenceEntry<K, V> getNext() {
                return next;
            }

        }

        static final class WeakAccessEntry<K, V> extends LocalCache.WeakEntry<K, V> {
            volatile long accessTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextAccess = nullEntry();
            ReferenceEntry<K, V> previousAccess = nullEntry();

            WeakAccessEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) {
                super(queue, key, hash, next);
            }

            public long getAccessTime() {
                return accessTime;
            }

            public void setAccessTime(long time) {
                this.accessTime = time;
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }

        }

        static final class WeakWriteEntry<K, V> extends LocalCache.WeakEntry<K, V> {
            volatile long writeTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextWrite = nullEntry();
            ReferenceEntry<K, V> previousWrite = nullEntry();

            WeakWriteEntry(ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) {
                super(queue, key, hash, next);
            }

            public long getWriteTime() {
                return writeTime;
            }

            public void setWriteTime(long time) {
                this.writeTime = time;
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }

        }

        static final class WeakAccessWriteEntry<K, V> extends LocalCache.WeakEntry<K, V> {
            volatile long accessTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextAccess = nullEntry();
            ReferenceEntry<K, V> previousAccess = nullEntry();
            volatile long writeTime = Long.MAX_VALUE;
            ReferenceEntry<K, V> nextWrite = nullEntry();
            ReferenceEntry<K, V> previousWrite = nullEntry();

            WeakAccessWriteEntry(
                    ReferenceQueue<K> queue, K key, int hash, ReferenceEntry<K, V> next) {
                super(queue, key, hash, next);
            }

            public long getAccessTime() {
                return accessTime;
            }

            public void setAccessTime(long time) {
                this.accessTime = time;
            }

            public ReferenceEntry<K, V> getNextInAccessQueue() {
                return nextAccess;
            }

            public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                this.nextAccess = next;
            }

            public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                return previousAccess;
            }

            public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                this.previousAccess = previous;
            }

            public long getWriteTime() {
                return writeTime;
            }

            public void setWriteTime(long time) {
                this.writeTime = time;
            }

            public ReferenceEntry<K, V> getNextInWriteQueue() {
                return nextWrite;
            }

            public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                this.nextWrite = next;
            }

            public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                return previousWrite;
            }

            public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                this.previousWrite = previous;
            }

        }

        static class WeakValueReference<K, V> extends WeakReference<V> implements LocalCache.ValueReference<K, V> {
            final ReferenceEntry<K, V> entry;

            WeakValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
                super(referent, queue);
                this.entry = entry;
            }

            public int getWeight() {
                return 1;
            }

            public ReferenceEntry<K, V> getEntry() {
                return entry;
            }

            public void notifyNewValue(V newValue) {
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return new LocalCache.WeakValueReference<>(queue, value, entry);
            }

            public boolean isLoading() {
                return false;
            }

            public boolean isActive() {
                return true;
            }

            public V waitForValue() {
                return get();
            }

        }

        static class SoftValueReference<K, V> extends SoftReference<V> implements LocalCache.ValueReference<K, V> {
            final ReferenceEntry<K, V> entry;

            SoftValueReference(ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry) {
                super(referent, queue);
                this.entry = entry;
            }

            public int getWeight() {
                return 1;
            }

            public ReferenceEntry<K, V> getEntry() {
                return entry;
            }

            public void notifyNewValue(V newValue) {
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return new LocalCache.SoftValueReference<>(queue, value, entry);
            }

            public boolean isLoading() {
                return false;
            }

            public boolean isActive() {
                return true;
            }

            public V waitForValue() {
                return get();
            }

        }

        static class StrongValueReference<K, V> implements LocalCache.ValueReference<K, V> {
            final V referent;

            StrongValueReference(V referent) {
                this.referent = referent;
            }

            public V get() {
                return referent;
            }

            public int getWeight() {
                return 1;
            }

            public ReferenceEntry<K, V> getEntry() {
                return null;
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return this;
            }

            public boolean isLoading() {
                return false;
            }

            public boolean isActive() {
                return true;
            }

            public V waitForValue() {
                return get();
            }

            public void notifyNewValue(V newValue) {
            }

        }

        static final class WeightedWeakValueReference<K, V> extends LocalCache.WeakValueReference<K, V> {
            final int weight;

            WeightedWeakValueReference(
                    ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
                super(queue, referent, entry);
                this.weight = weight;
            }

            public int getWeight() {
                return weight;
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return new LocalCache.WeightedWeakValueReference<>(queue, value, entry, weight);
            }

        }

        static final class WeightedSoftValueReference<K, V> extends LocalCache.SoftValueReference<K, V> {
            final int weight;

            WeightedSoftValueReference(
                    ReferenceQueue<V> queue, V referent, ReferenceEntry<K, V> entry, int weight) {
                super(queue, referent, entry);
                this.weight = weight;
            }

            public int getWeight() {
                return weight;
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return new LocalCache.WeightedSoftValueReference<>(queue, value, entry, weight);
            }

        }

        static final class WeightedStrongValueReference<K, V> extends LocalCache.StrongValueReference<K, V> {
            final int weight;

            WeightedStrongValueReference(V referent, int weight) {
                super(referent);
                this.weight = weight;
            }

            public int getWeight() {
                return weight;
            }

        }

        // This class is never serialized.
        static class Segment<K, V> extends ReentrantLock {
            final LocalCache<K, V> map;
            volatile int count;
            long totalWeight;
            int modCount;
            int threshold;
            volatile AtomicReferenceArray<ReferenceEntry<K, V>> table;
            final long maxSegmentWeight;
            final ReferenceQueue<K> keyReferenceQueue;
            final ReferenceQueue<V> valueReferenceQueue;
            final Queue<ReferenceEntry<K, V>> recencyQueue;
            final AtomicInteger readCount = new AtomicInteger();
            final Queue<ReferenceEntry<K, V>> writeQueue;
            final Queue<ReferenceEntry<K, V>> accessQueue;
            final StatsCounter statsCounter;

            Segment(
                    LocalCache<K, V> map,
                    int initialCapacity,
                    long maxSegmentWeight,
                    StatsCounter statsCounter) {
                this.map = map;
                this.maxSegmentWeight = maxSegmentWeight;
                this.statsCounter = Preconditions.checkNotNull(statsCounter);
                initTable(newEntryArray(initialCapacity));

                keyReferenceQueue = map.usesKeyReferences() ? new ReferenceQueue<K>() : null;

                valueReferenceQueue = map.usesValueReferences() ? new ReferenceQueue<V>() : null;

                recencyQueue =
                        map.usesAccessQueue()
                                ? new ConcurrentLinkedQueue<ReferenceEntry<K, V>>()
                                : LocalCache.<ReferenceEntry<K, V>>discardingQueue();

                writeQueue =
                        map.usesWriteQueue()
                                ? new LocalCache.WriteQueue<K, V>()
                                : LocalCache.<ReferenceEntry<K, V>>discardingQueue();

                accessQueue =
                        map.usesAccessQueue()
                                ? new LocalCache.AccessQueue<K, V>()
                                : LocalCache.<ReferenceEntry<K, V>>discardingQueue();
            }

            AtomicReferenceArray<ReferenceEntry<K, V>> newEntryArray(int size) {
                return new AtomicReferenceArray<>(size);
            }

            void initTable(AtomicReferenceArray<ReferenceEntry<K, V>> newTable) {
                this.threshold = newTable.length() * 3 / 4; // 0.75
                if (!map.customWeigher() && this.threshold == maxSegmentWeight) {
                    // prevent spurious expansion before eviction
                    this.threshold++;
                }
                this.table = newTable;
            }

            ReferenceEntry<K, V> newEntry(K key, int hash, ReferenceEntry<K, V> next) {
                return map.entryFactory.newEntry(this, Preconditions.checkNotNull(key), hash, next);
            }

            ReferenceEntry<K, V> copyEntry(ReferenceEntry<K, V> original, ReferenceEntry<K, V> newNext) {
                if (original.getKey() == null) {
                    // key collected
                    return null;
                }

                LocalCache.ValueReference<K, V> valueReference = original.getValueReference();
                V value = valueReference.get();
                if ((value == null) && valueReference.isActive()) {
                    // value collected
                    return null;
                }

                ReferenceEntry<K, V> newEntry = map.entryFactory.copyEntry(this, original, newNext);
                newEntry.setValueReference(valueReference.copyFor(this.valueReferenceQueue, value, newEntry));
                return newEntry;
            }

            void setValue(ReferenceEntry<K, V> entry, K key, V value, long now) {
                LocalCache.ValueReference<K, V> previous = entry.getValueReference();
                int weight = map.weigher.weigh(key, value);
                Preconditions.checkState(weight >= 0, "Weights must be non-negative");

                LocalCache.ValueReference<K, V> valueReference =
                        map.valueStrength.referenceValue(this, entry, value, weight);
                entry.setValueReference(valueReference);
                recordWrite(entry, weight, now);
                previous.notifyNewValue(value);
            }

            V get(Object key, int hash) {
                try {
                    if (count != 0) { // read-volatile
                        long now = map.ticker.read();
                        ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
                        if (e == null) {
                            return null;
                        }

                        V value = e.getValueReference().get();
                        if (value != null) {
                            recordRead(e, now);
                            return scheduleRefresh(e, e.getKey(), hash, value, now, map.defaultLoader);
                        }
                        tryDrainReferenceQueues();
                    }
                    return null;
                } finally {
                    postReadCleanup();
                }
            }

            V compute(K key, int hash, BiFunction<? super K, ? super V, ? extends V> function) {
                ReferenceEntry<K, V> e;
                LocalCache.ValueReference<K, V> valueReference = null;
                LocalCache.LoadingValueReference<K, V> loadingValueReference = null;
                boolean createNewEntry = true;
                V newValue;

                lock();
                try {
                    // re-read ticker once inside the lock
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            valueReference = e.getValueReference();
                            if (map.isExpired(e, now)) {
                                // This is a duplicate check, as preWriteCleanup already purged expired
                                // entries, but let's accomodate an incorrect expiration queue.
                                enqueueNotification(
                                        entryKey,
                                        hash,
                                        valueReference.get(),
                                        valueReference.getWeight(),
                                        RemovalCause.EXPIRED);
                            }

                            // immediately reuse invalid entries
                            writeQueue.remove(e);
                            accessQueue.remove(e);
                            createNewEntry = false;
                            break;
                        }
                    }

                    // note valueReference can be an existing value or even itself another loading value if
                    // the value for the key is already being computed.
                    loadingValueReference = new LocalCache.LoadingValueReference<>(valueReference);

                    if (e == null) {
                        createNewEntry = true;
                        e = newEntry(key, hash, first);
                        e.setValueReference(loadingValueReference);
                        table.set(index, e);
                    } else {
                        e.setValueReference(loadingValueReference);
                    }

                    newValue = loadingValueReference.compute(key, function);
                    if (newValue != null) {
                        if (valueReference != null && newValue == valueReference.get()) {
                            loadingValueReference.set(newValue);
                            e.setValueReference(valueReference);
                            recordWrite(e, 0, now); // no change in weight
                            return newValue;
                        }
                        try {
                            return getAndRecordStats(
                                    key, hash, loadingValueReference, Futures.immediateFuture(newValue));
                        } catch (ExecutionException exception) {
                            throw new AssertionError("impossible; Futures.immediateFuture can't throw");
                        }
                    } else if (createNewEntry) {
                        removeLoadingValue(key, hash, loadingValueReference);
                        return null;
                    } else {
                        removeEntry(e, hash, RemovalCause.EXPLICIT);
                        return null;
                    }
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            ListenableFuture<V> loadAsync(
                    final K key,
                    final int hash,
                    final LocalCache.LoadingValueReference<K, V> loadingValueReference,
                    CacheLoader<? super K, V> loader) {
                final ListenableFuture<V> loadingFuture = loadingValueReference.loadFuture(key, loader);
                loadingFuture.addListener(
                        new Runnable() {

                            public void run() {
                                try {
                                    getAndRecordStats(key, hash, loadingValueReference, loadingFuture);
                                } catch (Throwable t) {
                                    logger.log(Level.WARNING, "Exception thrown during refresh", t);
                                    loadingValueReference.setException(t);
                                }
                            }
                        },
                        MoreExecutors.directExecutor());
                return loadingFuture;
            }

            V getAndRecordStats(
                    K key,
                    int hash,
                    LocalCache.LoadingValueReference<K, V> loadingValueReference,
                    ListenableFuture<V> newValue) throws ExecutionException {
                V value = null;
                try {
                    value = Uninterruptibles.getUninterruptibly(newValue);
                    if (value == null) {
                        throw new InvalidCacheLoadException("CacheLoader returned null for key " + key + ".");
                    }
                    statsCounter.recordLoadSuccess(loadingValueReference.elapsedNanos());
                    storeLoadedValue(key, hash, loadingValueReference, value);
                    return value;
                } finally {
                    if (value == null) {
                        statsCounter.recordLoadException(loadingValueReference.elapsedNanos());
                        removeLoadingValue(key, hash, loadingValueReference);
                    }
                }
            }

            V scheduleRefresh(
                    ReferenceEntry<K, V> entry,
                    K key,
                    int hash,
                    V oldValue,
                    long now,
                    CacheLoader<? super K, V> loader) {
                if (map.refreshes()
                        && (now - entry.getWriteTime() > map.refreshNanos)
                        && !entry.getValueReference().isLoading()) {
                    V newValue = refresh(key, hash, loader, true);
                    if (newValue != null) {
                        return newValue;
                    }
                }
                return oldValue;
            }

            V refresh(K key, int hash, CacheLoader<? super K, V> loader, boolean checkTime) {
                final LocalCache.LoadingValueReference<K, V> loadingValueReference =
                        insertLoadingValueReference(key, hash, checkTime);
                if (loadingValueReference == null) {
                    return null;
                }

                ListenableFuture<V> result = loadAsync(key, hash, loadingValueReference, loader);
                if (result.isDone()) {
                    try {
                        return Uninterruptibles.getUninterruptibly(result);
                    } catch (Throwable t) {
                        // don't let refresh exceptions propagate; error was already logged
                    }
                }
                return null;
            }

            LocalCache.LoadingValueReference<K, V> insertLoadingValueReference(
                    final K key, final int hash, boolean checkTime) {
                ReferenceEntry<K, V> e = null;
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    // Look for an existing entry.
                    for (e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            // We found an existing entry.

                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            if (valueReference.isLoading()
                                    || (checkTime && (now - e.getWriteTime() < map.refreshNanos))) {
                                // refresh is a no-op if loading is pending
                                // if checkTime, we want to check *after* acquiring the lock if refresh still needs
                                // to be scheduled
                                return null;
                            }

                            // continue returning old value while loading
                            ++modCount;
                            LocalCache.LoadingValueReference<K, V> loadingValueReference =
                                    new LocalCache.LoadingValueReference<>(valueReference);
                            e.setValueReference(loadingValueReference);
                            return loadingValueReference;
                        }
                    }

                    ++modCount;
                    LocalCache.LoadingValueReference<K, V> loadingValueReference = new LocalCache.LoadingValueReference<>();
                    e = newEntry(key, hash, first);
                    e.setValueReference(loadingValueReference);
                    table.set(index, e);
                    return loadingValueReference;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            void tryDrainReferenceQueues() {
                if (tryLock()) {
                    try {
                        drainReferenceQueues();
                    } finally {
                        unlock();
                    }
                }
            }

            void drainReferenceQueues() {
                if (map.usesKeyReferences()) {
                    drainKeyReferenceQueue();
                }
                if (map.usesValueReferences()) {
                    drainValueReferenceQueue();
                }
            }

            void drainKeyReferenceQueue() {
                Reference<? extends K> ref;
                int i = 0;
                while ((ref = keyReferenceQueue.poll()) != null) {

                    ReferenceEntry<K, V> entry = (ReferenceEntry<K, V>) ref;
                    map.reclaimKey(entry);
                    if (++i == DRAIN_MAX) {
                        break;
                    }
                }
            }

            void drainValueReferenceQueue() {
                Reference<? extends V> ref;
                int i = 0;
                while ((ref = valueReferenceQueue.poll()) != null) {

                    LocalCache.ValueReference<K, V> valueReference = (LocalCache.ValueReference<K, V>) ref;
                    map.reclaimValue(valueReference);
                    if (++i == DRAIN_MAX) {
                        break;
                    }
                }
            }

            void clearReferenceQueues() {
                if (map.usesKeyReferences()) {
                    clearKeyReferenceQueue();
                }
                if (map.usesValueReferences()) {
                    clearValueReferenceQueue();
                }
            }

            void clearKeyReferenceQueue() {
                while (keyReferenceQueue.poll() != null) {
                }
            }

            void clearValueReferenceQueue() {
                while (valueReferenceQueue.poll() != null) {
                }
            }

            void recordRead(ReferenceEntry<K, V> entry, long now) {
                if (map.recordsAccess()) {
                    entry.setAccessTime(now);
                }
                recencyQueue.add(entry);
            }

            void recordLockedRead(ReferenceEntry<K, V> entry, long now) {
                if (map.recordsAccess()) {
                    entry.setAccessTime(now);
                }
                accessQueue.add(entry);
            }

            void recordWrite(ReferenceEntry<K, V> entry, int weight, long now) {
                // we are already under lock, so drain the recency queue immediately
                drainRecencyQueue();
                totalWeight += weight;

                if (map.recordsAccess()) {
                    entry.setAccessTime(now);
                }
                if (map.recordsWrite()) {
                    entry.setWriteTime(now);
                }
                accessQueue.add(entry);
                writeQueue.add(entry);
            }

            void drainRecencyQueue() {
                ReferenceEntry<K, V> e;
                while ((e = recencyQueue.poll()) != null) {
                    // An entry may be in the recency queue despite it being removed from
                    // the map . This can occur when the entry was concurrently read while a
                    // writer is removing it from the segment or after a clear has removed
                    // all of the segment's entries.
                    if (accessQueue.contains(e)) {
                        accessQueue.add(e);
                    }
                }
            }

            void tryExpireEntries(long now) {
                if (tryLock()) {
                    try {
                        expireEntries(now);
                    } finally {
                        unlock();
                        // don't call postWriteCleanup as we're in a read
                    }
                }
            }

            void expireEntries(long now) {
                drainRecencyQueue();

                ReferenceEntry<K, V> e;
                while ((e = writeQueue.peek()) != null && map.isExpired(e, now)) {
                    if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
                        throw new AssertionError();
                    }
                }
                while ((e = accessQueue.peek()) != null && map.isExpired(e, now)) {
                    if (!removeEntry(e, e.getHash(), RemovalCause.EXPIRED)) {
                        throw new AssertionError();
                    }
                }
            }

            void enqueueNotification(
                    K key, int hash, V value, int weight, RemovalCause cause) {
                totalWeight -= weight;
                if (cause.wasEvicted()) {
                    statsCounter.recordEviction();
                }
                if (map.removalNotificationQueue != DISCARDING_QUEUE) {
                    RemovalNotification<K, V> notification = RemovalNotification.create(key, value, cause);
                    map.removalNotificationQueue.offer(notification);
                }
            }

            void evictEntries(ReferenceEntry<K, V> newest) {
                if (!map.evictsBySize()) {
                    return;
                }

                drainRecencyQueue();

                // If the newest entry by itself is too heavy for the segment, don't bother evicting
                // anything else, just that
                if (newest.getValueReference().getWeight() > maxSegmentWeight) {
                    if (!removeEntry(newest, newest.getHash(), RemovalCause.SIZE)) {
                        throw new AssertionError();
                    }
                }

                while (totalWeight > maxSegmentWeight) {
                    ReferenceEntry<K, V> e = getNextEvictable();
                    if (!removeEntry(e, e.getHash(), RemovalCause.SIZE)) {
                        throw new AssertionError();
                    }
                }
            }

            ReferenceEntry<K, V> getNextEvictable() {
                for (ReferenceEntry<K, V> e : accessQueue) {
                    int weight = e.getValueReference().getWeight();
                    if (weight > 0) {
                        return e;
                    }
                }
                throw new AssertionError();
            }

            ReferenceEntry<K, V> getFirst(int hash) {
                // read this volatile field only once
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                return table.get(hash & (table.length() - 1));
            }

            ReferenceEntry<K, V> getEntry(Object key, int hash) {
                for (ReferenceEntry<K, V> e = getFirst(hash); e != null; e = e.getNext()) {
                    if (e.getHash() != hash) {
                        continue;
                    }

                    K entryKey = e.getKey();
                    if (entryKey == null) {
                        tryDrainReferenceQueues();
                        continue;
                    }

                    if (map.keyEquivalence.equivalent(key, entryKey)) {
                        return e;
                    }
                }

                return null;
            }

            ReferenceEntry<K, V> getLiveEntry(Object key, int hash, long now) {
                ReferenceEntry<K, V> e = getEntry(key, hash);
                if (e == null) {
                    return null;
                } else if (map.isExpired(e, now)) {
                    tryExpireEntries(now);
                    return null;
                }
                return e;
            }

            V getLiveValue(ReferenceEntry<K, V> entry, long now) {
                if (entry.getKey() == null) {
                    tryDrainReferenceQueues();
                    return null;
                }
                V value = entry.getValueReference().get();
                if (value == null) {
                    tryDrainReferenceQueues();
                    return null;
                }

                if (map.isExpired(entry, now)) {
                    tryExpireEntries(now);
                    return null;
                }
                return value;
            }

            boolean containsKey(Object key, int hash) {
                try {
                    if (count != 0) { // read-volatile
                        long now = map.ticker.read();
                        ReferenceEntry<K, V> e = getLiveEntry(key, hash, now);
                        if (e == null) {
                            return false;
                        }
                        return e.getValueReference().get() != null;
                    }

                    return false;
                } finally {
                    postReadCleanup();
                }
            }

            V put(K key, int hash, V value, boolean onlyIfAbsent) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    int newCount = this.count + 1;
                    if (newCount > this.threshold) { // ensure capacity
                        expand();
                        newCount = this.count + 1;
                    }

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    // Look for an existing entry.
                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            // We found an existing entry.

                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();

                            if (entryValue == null) {
                                ++modCount;
                                if (valueReference.isActive()) {
                                    enqueueNotification(
                                            key, hash, entryValue, valueReference.getWeight(), RemovalCause.COLLECTED);
                                    setValue(e, key, value, now);
                                    newCount = this.count; // count remains unchanged
                                } else {
                                    setValue(e, key, value, now);
                                    newCount = this.count + 1;
                                }
                                this.count = newCount; // write-volatile
                                evictEntries(e);
                                return null;
                            } else if (onlyIfAbsent) {
                                // Mimic
                                // "if (!map.containsKey(key)) ...
                                // else return map.get(key);
                                recordLockedRead(e, now);
                                return entryValue;
                            } else {
                                // clobber existing entry, count remains unchanged
                                ++modCount;
                                enqueueNotification(
                                        key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                                setValue(e, key, value, now);
                                evictEntries(e);
                                return entryValue;
                            }
                        }
                    }

                    // Create a new entry.
                    ++modCount;
                    ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                    setValue(newEntry, key, value, now);
                    table.set(index, newEntry);
                    newCount = this.count + 1;
                    this.count = newCount; // write-volatile
                    evictEntries(newEntry);
                    return null;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            void expand() {
                AtomicReferenceArray<ReferenceEntry<K, V>> oldTable = table;
                int oldCapacity = oldTable.length();
                if (oldCapacity >= MAXIMUM_CAPACITY) {
                    return;
                }

                /*
                 * Reclassify nodes in each list to new Map. Because we are using power-of-two expansion, the
                 * elements from each bin must either stay at same index, or move with a power of two offset.
                 * We eliminate unnecessary node creation by catching cases where old nodes can be reused
                 * because their next fields won't change. Statistically, at the default threshold, only about
                 * one-sixth of them need cloning when a table doubles. The nodes they replace will be garbage
                 * collectable as soon as they are no longer referenced by any reader thread that may be in
                 * the midst of traversing table right now.
                 */

                int newCount = count;
                AtomicReferenceArray<ReferenceEntry<K, V>> newTable = newEntryArray(oldCapacity << 1);
                threshold = newTable.length() * 3 / 4;
                int newMask = newTable.length() - 1;
                for (int oldIndex = 0; oldIndex < oldCapacity; ++oldIndex) {
                    // We need to guarantee that any existing reads of old Map can
                    // proceed. So we cannot yet null out each bin.
                    ReferenceEntry<K, V> head = oldTable.get(oldIndex);

                    if (head != null) {
                        ReferenceEntry<K, V> next = head.getNext();
                        int headIndex = head.getHash() & newMask;

                        // Single node on list
                        if (next == null) {
                            newTable.set(headIndex, head);
                        } else {
                            // Reuse the consecutive sequence of nodes with the same target
                            // index from the end of the list. tail points to the first
                            // entry in the reusable list.
                            ReferenceEntry<K, V> tail = head;
                            int tailIndex = headIndex;
                            for (ReferenceEntry<K, V> e = next; e != null; e = e.getNext()) {
                                int newIndex = e.getHash() & newMask;
                                if (newIndex != tailIndex) {
                                    // The index changed. We'll need to copy the previous entry.
                                    tailIndex = newIndex;
                                    tail = e;
                                }
                            }
                            newTable.set(tailIndex, tail);

                            // Clone nodes leading up to the tail.
                            for (ReferenceEntry<K, V> e = head; e != tail; e = e.getNext()) {
                                int newIndex = e.getHash() & newMask;
                                ReferenceEntry<K, V> newNext = newTable.get(newIndex);
                                ReferenceEntry<K, V> newFirst = copyEntry(e, newNext);
                                if (newFirst != null) {
                                    newTable.set(newIndex, newFirst);
                                } else {
                                    removeCollectedEntry(e);
                                    newCount--;
                                }
                            }
                        }
                    }
                }
                table = newTable;
                this.count = newCount;
            }

            boolean replace(K key, int hash, V oldValue, V newValue) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();
                            if (entryValue == null) {
                                if (valueReference.isActive()) {
                                    // If the value disappeared, this entry is partially collected.
                                    int newCount = this.count - 1;
                                    ++modCount;
                                    ReferenceEntry<K, V> newFirst =
                                            removeValueFromChain(
                                                    first,
                                                    e,
                                                    entryKey,
                                                    hash,
                                                    entryValue,
                                                    valueReference,
                                                    RemovalCause.COLLECTED);
                                    newCount = this.count - 1;
                                    table.set(index, newFirst);
                                    this.count = newCount; // write-volatile
                                }
                                return false;
                            }

                            if (map.valueEquivalence.equivalent(oldValue, entryValue)) {
                                ++modCount;
                                enqueueNotification(
                                        key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                                setValue(e, key, newValue, now);
                                evictEntries(e);
                                return true;
                            } else {
                                // Mimic
                                // "if (map.containsKey(key) && map.get(key).equals(oldValue))..."
                                recordLockedRead(e, now);
                                return false;
                            }
                        }
                    }

                    return false;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            V replace(K key, int hash, V newValue) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();
                            if (entryValue == null) {
                                if (valueReference.isActive()) {
                                    // If the value disappeared, this entry is partially collected.
                                    int newCount = this.count - 1;
                                    ++modCount;
                                    ReferenceEntry<K, V> newFirst =
                                            removeValueFromChain(
                                                    first,
                                                    e,
                                                    entryKey,
                                                    hash,
                                                    entryValue,
                                                    valueReference,
                                                    RemovalCause.COLLECTED);
                                    newCount = this.count - 1;
                                    table.set(index, newFirst);
                                    this.count = newCount; // write-volatile
                                }
                                return null;
                            }

                            ++modCount;
                            enqueueNotification(
                                    key, hash, entryValue, valueReference.getWeight(), RemovalCause.REPLACED);
                            setValue(e, key, newValue, now);
                            evictEntries(e);
                            return entryValue;
                        }
                    }

                    return null;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            V remove(Object key, int hash) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    int newCount = this.count - 1;
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();

                            RemovalCause cause;
                            if (entryValue != null) {
                                cause = RemovalCause.EXPLICIT;
                            } else if (valueReference.isActive()) {
                                cause = RemovalCause.COLLECTED;
                            } else {
                                // currently loading
                                return null;
                            }

                            ++modCount;
                            ReferenceEntry<K, V> newFirst =
                                    removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount; // write-volatile
                            return entryValue;
                        }
                    }

                    return null;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            boolean remove(Object key, int hash, Object value) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    int newCount = this.count - 1;
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();

                            RemovalCause cause;
                            if (map.valueEquivalence.equivalent(value, entryValue)) {
                                cause = RemovalCause.EXPLICIT;
                            } else if (entryValue == null && valueReference.isActive()) {
                                cause = RemovalCause.COLLECTED;
                            } else {
                                // currently loading
                                return false;
                            }

                            ++modCount;
                            ReferenceEntry<K, V> newFirst =
                                    removeValueFromChain(first, e, entryKey, hash, entryValue, valueReference, cause);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount; // write-volatile
                            return (cause == RemovalCause.EXPLICIT);
                        }
                    }

                    return false;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            boolean storeLoadedValue(
                    K key, int hash, LocalCache.LoadingValueReference<K, V> oldValueReference, V newValue) {
                lock();
                try {
                    long now = map.ticker.read();
                    preWriteCleanup(now);

                    int newCount = this.count + 1;
                    if (newCount > this.threshold) { // ensure capacity
                        expand();
                        newCount = this.count + 1;
                    }

                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> valueReference = e.getValueReference();
                            V entryValue = valueReference.get();
                            // replace the old LoadingValueReference if it's live, otherwise
                            // perform a putIfAbsent
                            if (oldValueReference == valueReference
                                    || (entryValue == null && valueReference != UNSET)) {
                                ++modCount;
                                if (oldValueReference.isActive()) {
                                    RemovalCause cause =
                                            (entryValue == null) ? RemovalCause.COLLECTED : RemovalCause.REPLACED;
                                    enqueueNotification(key, hash, entryValue, oldValueReference.getWeight(), cause);
                                    newCount--;
                                }
                                setValue(e, key, newValue, now);
                                this.count = newCount; // write-volatile
                                evictEntries(e);
                                return true;
                            }

                            // the loaded value was already clobbered
                            enqueueNotification(key, hash, newValue, 0, RemovalCause.REPLACED);
                            return false;
                        }
                    }

                    ++modCount;
                    ReferenceEntry<K, V> newEntry = newEntry(key, hash, first);
                    setValue(newEntry, key, newValue, now);
                    table.set(index, newEntry);
                    this.count = newCount; // write-volatile
                    evictEntries(newEntry);
                    return true;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            void clear() {
                if (count != 0) { // read-volatile
                    lock();
                    try {
                        long now = map.ticker.read();
                        preWriteCleanup(now);

                        AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                        for (int i = 0; i < table.length(); ++i) {
                            for (ReferenceEntry<K, V> e = table.get(i); e != null; e = e.getNext()) {
                                // Loading references aren't actually in the map yet.
                                if (e.getValueReference().isActive()) {
                                    K key = e.getKey();
                                    V value = e.getValueReference().get();
                                    RemovalCause cause =
                                            (key == null || value == null) ? RemovalCause.COLLECTED : RemovalCause.EXPLICIT;
                                    enqueueNotification(
                                            key, e.getHash(), value, e.getValueReference().getWeight(), cause);
                                }
                            }
                        }
                        for (int i = 0; i < table.length(); ++i) {
                            table.set(i, null);
                        }
                        clearReferenceQueues();
                        writeQueue.clear();
                        accessQueue.clear();
                        readCount.set(0);

                        ++modCount;
                        count = 0; // write-volatile
                    } finally {
                        unlock();
                        postWriteCleanup();
                    }
                }
            }

            ReferenceEntry<K, V> removeValueFromChain(
                    ReferenceEntry<K, V> first,
                    ReferenceEntry<K, V> entry,
                    K key,
                    int hash,
                    V value,
                    LocalCache.ValueReference<K, V> valueReference,
                    RemovalCause cause) {
                enqueueNotification(key, hash, value, valueReference.getWeight(), cause);
                writeQueue.remove(entry);
                accessQueue.remove(entry);

                if (valueReference.isLoading()) {
                    valueReference.notifyNewValue(null);
                    return first;
                } else {
                    return removeEntryFromChain(first, entry);
                }
            }

            ReferenceEntry<K, V> removeEntryFromChain(
                    ReferenceEntry<K, V> first, ReferenceEntry<K, V> entry) {
                int newCount = count;
                ReferenceEntry<K, V> newFirst = entry.getNext();
                for (ReferenceEntry<K, V> e = first; e != entry; e = e.getNext()) {
                    ReferenceEntry<K, V> next = copyEntry(e, newFirst);
                    if (next != null) {
                        newFirst = next;
                    } else {
                        removeCollectedEntry(e);
                        newCount--;
                    }
                }
                this.count = newCount;
                return newFirst;
            }

            void removeCollectedEntry(ReferenceEntry<K, V> entry) {
                enqueueNotification(
                        entry.getKey(),
                        entry.getHash(),
                        entry.getValueReference().get(),
                        entry.getValueReference().getWeight(),
                        RemovalCause.COLLECTED);
                writeQueue.remove(entry);
                accessQueue.remove(entry);
            }

            boolean reclaimKey(ReferenceEntry<K, V> entry, int hash) {
                lock();
                try {
                    int newCount = count - 1;
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        if (e == entry) {
                            ++modCount;
                            ReferenceEntry<K, V> newFirst =
                                    removeValueFromChain(
                                            first,
                                            e,
                                            e.getKey(),
                                            hash,
                                            e.getValueReference().get(),
                                            e.getValueReference(),
                                            RemovalCause.COLLECTED);
                            newCount = this.count - 1;
                            table.set(index, newFirst);
                            this.count = newCount; // write-volatile
                            return true;
                        }
                    }

                    return false;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            boolean reclaimValue(K key, int hash, LocalCache.ValueReference<K, V> valueReference) {
                lock();
                try {
                    int newCount = this.count - 1;
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> v = e.getValueReference();
                            if (v == valueReference) {
                                ++modCount;
                                ReferenceEntry<K, V> newFirst =
                                        removeValueFromChain(
                                                first,
                                                e,
                                                entryKey,
                                                hash,
                                                valueReference.get(),
                                                valueReference,
                                                RemovalCause.COLLECTED);
                                newCount = this.count - 1;
                                table.set(index, newFirst);
                                this.count = newCount; // write-volatile
                                return true;
                            }
                            return false;
                        }
                    }

                    return false;
                } finally {
                    unlock();
                    if (!isHeldByCurrentThread()) { // don't cleanup inside of put
                        postWriteCleanup();
                    }
                }
            }

            boolean removeLoadingValue(K key, int hash, LocalCache.LoadingValueReference<K, V> valueReference) {
                lock();
                try {
                    AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                    int index = hash & (table.length() - 1);
                    ReferenceEntry<K, V> first = table.get(index);

                    for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                        K entryKey = e.getKey();
                        if (e.getHash() == hash
                                && entryKey != null
                                && map.keyEquivalence.equivalent(key, entryKey)) {
                            LocalCache.ValueReference<K, V> v = e.getValueReference();
                            if (v == valueReference) {
                                if (valueReference.isActive()) {
                                    e.setValueReference(valueReference.getOldValue());
                                } else {
                                    ReferenceEntry<K, V> newFirst = removeEntryFromChain(first, e);
                                    table.set(index, newFirst);
                                }
                                return true;
                            }
                            return false;
                        }
                    }

                    return false;
                } finally {
                    unlock();
                    postWriteCleanup();
                }
            }

            boolean removeEntry(ReferenceEntry<K, V> entry, int hash, RemovalCause cause) {
                int newCount = this.count - 1;
                AtomicReferenceArray<ReferenceEntry<K, V>> table = this.table;
                int index = hash & (table.length() - 1);
                ReferenceEntry<K, V> first = table.get(index);

                for (ReferenceEntry<K, V> e = first; e != null; e = e.getNext()) {
                    if (e == entry) {
                        ++modCount;
                        ReferenceEntry<K, V> newFirst =
                                removeValueFromChain(
                                        first,
                                        e,
                                        e.getKey(),
                                        hash,
                                        e.getValueReference().get(),
                                        e.getValueReference(),
                                        cause);
                        newCount = this.count - 1;
                        table.set(index, newFirst);
                        this.count = newCount; // write-volatile
                        return true;
                    }
                }

                return false;
            }

            void postReadCleanup() {
                if ((readCount.incrementAndGet() & DRAIN_THRESHOLD) == 0) {
                    cleanUp();
                }
            }

            void preWriteCleanup(long now) {
                runLockedCleanup(now);
            }

            void postWriteCleanup() {
                runUnlockedCleanup();
            }

            void cleanUp() {
                long now = map.ticker.read();
                runLockedCleanup(now);
                runUnlockedCleanup();
            }

            void runLockedCleanup(long now) {
                if (tryLock()) {
                    try {
                        drainReferenceQueues();
                        expireEntries(now); // calls drainRecencyQueue
                        readCount.set(0);
                    } finally {
                        unlock();
                    }
                }
            }

            void runUnlockedCleanup() {
                // locked cleanup may generate notifications we can send unlocked
                if (!isHeldByCurrentThread()) {
                    map.processPendingNotifications();
                }
            }

        }

        static class LoadingValueReference<K, V> implements LocalCache.ValueReference<K, V> {
            volatile LocalCache.ValueReference<K, V> oldValue;
            final SettableFuture<V> futureValue = SettableFuture.create();
            final Stopwatch stopwatch = Stopwatch.createUnstarted();

            public LoadingValueReference() {
                this(null);
            }

            public LoadingValueReference(LocalCache.ValueReference<K, V> oldValue) {
                this.oldValue = (oldValue == null) ? LocalCache.<K, V>unset() : oldValue;
            }

            public boolean isLoading() {
                return true;
            }

            public boolean isActive() {
                return oldValue.isActive();
            }

            public int getWeight() {
                return oldValue.getWeight();
            }

            public boolean set(V newValue) {
                return futureValue.set(newValue);
            }

            public boolean setException(Throwable t) {
                return futureValue.setException(t);
            }

            private ListenableFuture<V> fullyFailedFuture(Throwable t) {
                return Futures.immediateFailedFuture(t);
            }

            public void notifyNewValue(V newValue) {
                if (newValue != null) {
                    // The pending load was clobbered by a manual write.
                    // Unblock all pending gets, and have them return the new value.
                    set(newValue);
                } else {
                    // The pending load was removed. Delay notifications until loading completes.
                    oldValue = unset();
                }

                // TODO(fry): could also cancel loading if we had a handle on its future
            }

            public ListenableFuture<V> loadFuture(K key, CacheLoader<? super K, V> loader) {
                try {
                    stopwatch.start();
                    V previousValue = oldValue.get();
                    if (previousValue == null) {
                        V newValue = loader.load(key);
                        return set(newValue) ? futureValue : Futures.immediateFuture(newValue);
                    }
                    ListenableFuture<V> newValue = loader.reload(key, previousValue);
                    if (newValue == null) {
                        return Futures.immediateFuture(null);
                    }
                    // To avoid a race, make sure the refreshed value is set into loadingValueReference
                    // *before* returning newValue from the cache query.
                    return Futures.transform(
                            newValue,
                            new com.google.common.base.Function<V, V>() {

                                public V apply(V newValue) {
                                    LocalCache.LoadingValueReference.this.set(newValue);
                                    return newValue;
                                }
                            },
                            MoreExecutors.directExecutor());
                } catch (Throwable t) {
                    ListenableFuture<V> result = setException(t) ? futureValue : fullyFailedFuture(t);
                    if (t instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                    }
                    return result;
                }
            }

            public V compute(K key, BiFunction<? super K, ? super V, ? extends V> function) {
                stopwatch.start();
                V previousValue;
                try {
                    previousValue = oldValue.waitForValue();
                } catch (ExecutionException e) {
                    previousValue = null;
                }
                V newValue;
                try {
                    newValue = function.apply(key, previousValue);
                } catch (Throwable th) {
                    this.setException(th);
                    throw th;
                }
                this.set(newValue);
                return newValue;
            }

            public long elapsedNanos() {
                return stopwatch.elapsed(TimeUnit.NANOSECONDS);
            }

            public V waitForValue() throws ExecutionException {
                return Uninterruptibles.getUninterruptibly(futureValue);
            }

            public V get() {
                return oldValue.get();
            }

            public LocalCache.ValueReference<K, V> getOldValue() {
                return oldValue;
            }

            public ReferenceEntry<K, V> getEntry() {
                return null;
            }

            public LocalCache.ValueReference<K, V> copyFor(
                    ReferenceQueue<V> queue, V value, ReferenceEntry<K, V> entry) {
                return this;
            }

        }

        static final class WriteQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
            final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>() {


                public long getWriteTime() {
                    return Long.MAX_VALUE;
                }


                public void setWriteTime(long time) {
                }

                ReferenceEntry<K, V> nextWrite = this;


                public ReferenceEntry<K, V> getNextInWriteQueue() {
                    return nextWrite;
                }


                public void setNextInWriteQueue(ReferenceEntry<K, V> next) {
                    this.nextWrite = next;
                }

                ReferenceEntry<K, V> previousWrite = this;


                public ReferenceEntry<K, V> getPreviousInWriteQueue() {
                    return previousWrite;
                }


                public void setPreviousInWriteQueue(ReferenceEntry<K, V> previous) {
                    this.previousWrite = previous;
                }
            };

            public boolean offer(ReferenceEntry<K, V> entry) {
                // unlink
                connectWriteOrder(entry.getPreviousInWriteQueue(), entry.getNextInWriteQueue());

                // add to tail
                connectWriteOrder(head.getPreviousInWriteQueue(), entry);
                connectWriteOrder(entry, head);

                return true;
            }

            public ReferenceEntry<K, V> peek() {
                ReferenceEntry<K, V> next = head.getNextInWriteQueue();
                return (next == head) ? null : next;
            }

            public ReferenceEntry<K, V> poll() {
                ReferenceEntry<K, V> next = head.getNextInWriteQueue();
                if (next == head) {
                    return null;
                }

                remove(next);
                return next;
            }

            public boolean remove(Object o) {
                ReferenceEntry<K, V> e = (ReferenceEntry) o;
                ReferenceEntry<K, V> previous = e.getPreviousInWriteQueue();
                ReferenceEntry<K, V> next = e.getNextInWriteQueue();
                connectWriteOrder(previous, next);
                nullifyWriteOrder(e);

                return next != LocalCache.NullEntry.INSTANCE;
            }

            public boolean contains(Object o) {
                ReferenceEntry<K, V> e = (ReferenceEntry) o;
                return e.getNextInWriteQueue() != LocalCache.NullEntry.INSTANCE;
            }

            public boolean isEmpty() {
                return head.getNextInWriteQueue() == head;
            }

            public int size() {
                int size = 0;
                for (ReferenceEntry<K, V> e = head.getNextInWriteQueue();
                     e != head;
                     e = e.getNextInWriteQueue()) {
                    size++;
                }
                return size;
            }

            public void clear() {
                ReferenceEntry<K, V> e = head.getNextInWriteQueue();
                while (e != head) {
                    ReferenceEntry<K, V> next = e.getNextInWriteQueue();
                    nullifyWriteOrder(e);
                    e = next;
                }

                head.setNextInWriteQueue(head);
                head.setPreviousInWriteQueue(head);
            }

            public Iterator<ReferenceEntry<K, V>> iterator() {
                return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {

                    protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                        ReferenceEntry<K, V> next = previous.getNextInWriteQueue();
                        return (next == head) ? null : next;
                    }
                };
            }

        }

        static final class AccessQueue<K, V> extends AbstractQueue<ReferenceEntry<K, V>> {
            final ReferenceEntry<K, V> head = new LocalCache.AbstractReferenceEntry<K, V>() {


                public long getAccessTime() {
                    return Long.MAX_VALUE;
                }


                public void setAccessTime(long time) {
                }

                ReferenceEntry<K, V> nextAccess = this;


                public ReferenceEntry<K, V> getNextInAccessQueue() {
                    return nextAccess;
                }


                public void setNextInAccessQueue(ReferenceEntry<K, V> next) {
                    this.nextAccess = next;
                }

                ReferenceEntry<K, V> previousAccess = this;


                public ReferenceEntry<K, V> getPreviousInAccessQueue() {
                    return previousAccess;
                }


                public void setPreviousInAccessQueue(ReferenceEntry<K, V> previous) {
                    this.previousAccess = previous;
                }
            };

            public boolean offer(ReferenceEntry<K, V> entry) {
                // unlink
                connectAccessOrder(entry.getPreviousInAccessQueue(), entry.getNextInAccessQueue());

                // add to tail
                connectAccessOrder(head.getPreviousInAccessQueue(), entry);
                connectAccessOrder(entry, head);

                return true;
            }

            public ReferenceEntry<K, V> peek() {
                ReferenceEntry<K, V> next = head.getNextInAccessQueue();
                return (next == head) ? null : next;
            }

            public ReferenceEntry<K, V> poll() {
                ReferenceEntry<K, V> next = head.getNextInAccessQueue();
                if (next == head) {
                    return null;
                }

                remove(next);
                return next;
            }

            public boolean remove(Object o) {
                ReferenceEntry<K, V> e = (ReferenceEntry) o;
                ReferenceEntry<K, V> previous = e.getPreviousInAccessQueue();
                ReferenceEntry<K, V> next = e.getNextInAccessQueue();
                connectAccessOrder(previous, next);
                nullifyAccessOrder(e);

                return next != LocalCache.NullEntry.INSTANCE;
            }

            public boolean contains(Object o) {
                ReferenceEntry<K, V> e = (ReferenceEntry) o;
                return e.getNextInAccessQueue() != LocalCache.NullEntry.INSTANCE;
            }

            public boolean isEmpty() {
                return head.getNextInAccessQueue() == head;
            }

            public int size() {
                int size = 0;
                for (ReferenceEntry<K, V> e = head.getNextInAccessQueue();
                     e != head;
                     e = e.getNextInAccessQueue()) {
                    size++;
                }
                return size;
            }

            public void clear() {
                ReferenceEntry<K, V> e = head.getNextInAccessQueue();
                while (e != head) {
                    ReferenceEntry<K, V> next = e.getNextInAccessQueue();
                    nullifyAccessOrder(e);
                    e = next;
                }

                head.setNextInAccessQueue(head);
                head.setPreviousInAccessQueue(head);
            }

            public Iterator<ReferenceEntry<K, V>> iterator() {
                return new AbstractSequentialIterator<ReferenceEntry<K, V>>(peek()) {

                    protected ReferenceEntry<K, V> computeNext(ReferenceEntry<K, V> previous) {
                        ReferenceEntry<K, V> next = previous.getNextInAccessQueue();
                        return (next == head) ? null : next;
                    }
                };
            }

        }

        abstract class HashIterator<T> implements Iterator<T> {
            int nextSegmentIndex;
            int nextTableIndex;
            LocalCache.Segment<K, V> currentSegment;
            AtomicReferenceArray<ReferenceEntry<K, V>> currentTable;
            ReferenceEntry<K, V> nextEntry;
            WriteThroughEntry nextExternal;
            WriteThroughEntry lastReturned;

            HashIterator() {
                nextSegmentIndex = segments.length - 1;
                nextTableIndex = -1;
                advance();
            }

            public abstract T next();

            final void advance() {
                nextExternal = null;

                if (nextInChain()) {
                    return;
                }

                if (nextInTable()) {
                    return;
                }

                while (nextSegmentIndex >= 0) {
                    currentSegment = segments[nextSegmentIndex--];
                    if (currentSegment.count != 0) {
                        currentTable = currentSegment.table;
                        nextTableIndex = currentTable.length() - 1;
                        if (nextInTable()) {
                            return;
                        }
                    }
                }
            }

            boolean nextInChain() {
                if (nextEntry != null) {
                    for (nextEntry = nextEntry.getNext(); nextEntry != null; nextEntry = nextEntry.getNext()) {
                        if (advanceTo(nextEntry)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            boolean nextInTable() {
                while (nextTableIndex >= 0) {
                    if ((nextEntry = currentTable.get(nextTableIndex--)) != null) {
                        if (advanceTo(nextEntry) || nextInChain()) {
                            return true;
                        }
                    }
                }
                return false;
            }

            boolean advanceTo(ReferenceEntry<K, V> entry) {
                try {
                    long now = ticker.read();
                    K key = entry.getKey();
                    V value = getLiveValue(entry, now);
                    if (value != null) {
                        nextExternal = new WriteThroughEntry(key, value);
                        return true;
                    } else {
                        // Skip stale entry.
                        return false;
                    }
                } finally {
                    currentSegment.postReadCleanup();
                }
            }

            public boolean hasNext() {
                return nextExternal != null;
            }

            WriteThroughEntry nextEntry() {
                if (nextExternal == null) {
                    throw new NoSuchElementException();
                }
                lastReturned = nextExternal;
                advance();
                return lastReturned;
            }

            public void remove() {
                Preconditions.checkState(lastReturned != null);
                LocalCache.this.remove(lastReturned.getKey());
                lastReturned = null;
            }

        }

        final class KeyIterator extends HashIterator<K> {
            public K next() {
                return nextEntry().getKey();
            }

        }

        final class ValueIterator extends HashIterator<V> {
            public V next() {
                return nextEntry().getValue();
            }

        }

        final class WriteThroughEntry implements Entry<K, V> {
            final K key;
            V value;

            WriteThroughEntry(K key, V value) {
                this.key = key;
                this.value = value;
            }

            public K getKey() {
                return key;
            }

            public V getValue() {
                return value;
            }

            public boolean equals(Object object) {
                // Cannot use key and value equivalence
                if (object instanceof Entry) {
                    Entry<?, ?> that = (Entry<?, ?>) object;
                    return key.equals(that.getKey()) && value.equals(that.getValue());
                }
                return false;
            }

            public int hashCode() {
                // Cannot use key and value equivalence
                return key.hashCode() ^ value.hashCode();
            }

            public V setValue(V newValue) {
                V oldValue = put(key, newValue);
                value = newValue; // only if put succeeds
                return oldValue;
            }

            public String toString() {
                return getKey() + "=" + getValue();
            }

        }

        final class EntryIterator extends HashIterator<Entry<K, V>> {
            public Entry<K, V> next() {
                return nextEntry();
            }

        }

        abstract class AbstractCacheSet<T> extends AbstractSet<T> {
            final ConcurrentMap<?, ?> map;

            AbstractCacheSet(ConcurrentMap<?, ?> map) {
                this.map = map;
            }

            public int size() {
                return map.size();
            }

            public boolean isEmpty() {
                return map.isEmpty();
            }

            public void clear() {
                map.clear();
            }

            public Object[] toArray() {
                return toArrayList(this).toArray();
            }

            public <E> E[] toArray(E[] a) {
                return toArrayList(this).toArray(a);
            }

        }

        final class KeySet extends AbstractCacheSet<K> {
            KeySet(ConcurrentMap<?, ?> map) {
                super(map);
            }

            public Iterator<K> iterator() {
                return new KeyIterator();
            }

            public boolean contains(Object o) {
                return map.containsKey(o);
            }

            public boolean remove(Object o) {
                return map.remove(o) != null;
            }

        }

        final class Values extends AbstractCollection<V> {
            private final ConcurrentMap<?, ?> map;

            Values(ConcurrentMap<?, ?> map) {
                this.map = map;
            }

            public int size() {
                return map.size();
            }

            public boolean isEmpty() {
                return map.isEmpty();
            }

            public void clear() {
                map.clear();
            }

            public Iterator<V> iterator() {
                return new ValueIterator();
            }

            public boolean removeIf(Predicate<? super V> filter) {
                Preconditions.checkNotNull(filter);
                return LocalCache.this.removeIf((k, v) -> filter.test(v));
            }

            public boolean contains(Object o) {
                return map.containsValue(o);
            }

            public Object[] toArray() {
                return toArrayList(this).toArray();
            }

            public <E> E[] toArray(E[] a) {
                return toArrayList(this).toArray(a);
            }

        }

        final class EntrySet extends AbstractCacheSet<Entry<K, V>> {
            EntrySet(ConcurrentMap<?, ?> map) {
                super(map);
            }

            public Iterator<Entry<K, V>> iterator() {
                return new EntryIterator();
            }

            public boolean removeIf(Predicate<? super Entry<K, V>> filter) {
                Preconditions.checkNotNull(filter);
                return LocalCache.this.removeIf((k, v) -> filter.test(Maps.immutableEntry(k, v)));
            }

            public boolean contains(Object o) {
                if (!(o instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> e = (Entry<?, ?>) o;
                Object key = e.getKey();
                if (key == null) {
                    return false;
                }
                V v = LocalCache.this.get(key);

                return v != null && valueEquivalence.equivalent(e.getValue(), v);
            }

            public boolean remove(Object o) {
                if (!(o instanceof Entry)) {
                    return false;
                }
                Entry<?, ?> e = (Entry<?, ?>) o;
                Object key = e.getKey();
                return key != null && LocalCache.this.remove(key, e.getValue());
            }

        }

        static class LocalManualCache<K, V> implements Cache<K, V>, Serializable {
            final LocalCache<K, V> localCache;

            LocalManualCache(CacheBuilder<? super K, ? super V> builder) {
                this(new LocalCache<K, V>(builder, null));
            }

            private LocalManualCache(LocalCache<K, V> localCache) {
                this.localCache = localCache;
            }

            public V getIfPresent(Object key) {
                return localCache.getIfPresent(key);
            }

        }

    }

    static final class Suppliers {
        private Suppliers() {
        }

        public static <T> Supplier<T> ofInstance(T instance) {
            return new Suppliers.SupplierOfInstance<T>(instance);
        }

        private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
            final T instance;

            SupplierOfInstance(T instance) {
                this.instance = instance;
            }

            public T get() {
                return instance;
            }

            public boolean equals(Object obj) {
                if (obj instanceof Suppliers.SupplierOfInstance) {
                    Suppliers.SupplierOfInstance<?> that = (Suppliers.SupplierOfInstance<?>) obj;
                    return com_google_common_base_Objects.equal(instance, that.instance);
                }
                return false;
            }

            public int hashCode() {
                return com_google_common_base_Objects.hashCode(instance);
            }

            public String toString() {
                return "Suppliers.ofInstance(" + instance + ")";
            }

        }

    }

    static final class InternalFutures {
        public static Throwable tryInternalFastPathGetFailure(InternalFutureFailureAccess future) {
            return future.tryInternalFastPathGetFailure();
        }

        private InternalFutures() {
        }

    }

    static interface GuardedBy {
    }

    static final class Hashing {
        private static final long C1 = 0xcc9e2d51;
        private static final long C2 = 0x1b873593;

        private Hashing() {
        }

        static int smear(int hashCode) {
            return (int) (C2 * Integer.rotateLeft((int) (hashCode * C1), 15));
        }

        static int smearedHash(Object o) {
            return smear((o == null) ? 0 : o.hashCode());
        }

    }

    static final class Ints {
        private Ints() {
        }

        public static int saturatedCast(long value) {
            if (value > Integer.MAX_VALUE) {
                return Integer.MAX_VALUE;
            }
            if (value < Integer.MIN_VALUE) {
                return Integer.MIN_VALUE;
            }
            return (int) value;
        }

    }

    static final class ObjectArrays {
        private ObjectArrays() {
        }

        public static <T> T[] newArray(T[] reference, int length) {
            return com_google_common_collect_Platform.newArray(reference, length);
        }

    }

    static // uses writeReplace(), not default serialization
    final class SingletonImmutableList<E> extends ImmutableList<E> {
        final transient E element;

        SingletonImmutableList(E element) {
            this.element = Preconditions.checkNotNull(element);
        }

        public E get(int index) {
            Preconditions.checkElementIndex(index, 1);
            return element;
        }

        public UnmodifiableIterator<E> iterator() {
            return Iterators.singletonIterator(element);
        }

        public Spliterator<E> spliterator() {
            return Collections.singleton(element).spliterator();
        }

        public int size() {
            return 1;
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, 1);
            return (fromIndex == toIndex) ? ImmutableList.<E>of() : this;
        }

        public String toString() {
            return '[' + element.toString() + ']';
        }

    }

    static final class Sets {
        private Sets() {
        }

        static int hashCodeImpl(Set<?> s) {
            int hashCode = 0;
            for (Object o : s) {
                hashCode += o != null ? o.hashCode() : 0;

                hashCode = ~~hashCode;
                // Needed to deal with unusual integer overflow in GWT.
            }
            return hashCode;
        }

        static boolean equalsImpl(Set<?> s, Object object) {
            if (s == object) {
                return true;
            }
            if (object instanceof Set) {
                Set<?> o = (Set<?>) object;

                try {
                    return s.size() == o.size() && s.containsAll(o);
                } catch (NullPointerException | ClassCastException ignored) {
                    return false;
                }
            }
            return false;
        }

    }

    static class UncheckedExecutionException extends RuntimeException {
        protected UncheckedExecutionException() {
        }

        protected UncheckedExecutionException(String message) {
            super(message);
        }

        public UncheckedExecutionException(String message, Throwable cause) {
            super(message, cause);
        }

        public UncheckedExecutionException(Throwable cause) {
            super(cause);
        }

    }

    static abstract class GwtFuturesCatchingSpecialization {
    }

    static interface ListenableFuture<V> extends Future<V> {
        void addListener(Runnable listener, Executor executor);

    }

    static abstract class FluentFuture<V> extends GwtFluentFutureCatchingSpecialization<V> {
        FluentFuture() {
        }

        abstract static class TrustedFuture<V> extends FluentFuture<V> implements AbstractFuture.Trusted<V> {
            public final V get() throws InterruptedException, ExecutionException {
                return super.get();
            }

            public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return super.get(timeout, unit);
            }

            public final boolean isDone() {
                return super.isDone();
            }

            public final boolean isCancelled() {
                return super.isCancelled();
            }

            public final void addListener(Runnable listener, Executor executor) {
                super.addListener(listener, executor);
            }

            public final boolean cancel(boolean mayInterruptIfRunning) {
                return super.cancel(mayInterruptIfRunning);
            }

        }

    }

    static final class CacheStats {
        private final long hitCount;
        private final long missCount;
        private final long loadSuccessCount;
        private final long loadExceptionCount;
        private final long totalLoadTime;
        private final long evictionCount;

        public CacheStats(
                long hitCount,
                long missCount,
                long loadSuccessCount,
                long loadExceptionCount,
                long totalLoadTime,
                long evictionCount) {
            Preconditions.checkArgument(hitCount >= 0);
            Preconditions.checkArgument(missCount >= 0);
            Preconditions.checkArgument(loadSuccessCount >= 0);
            Preconditions.checkArgument(loadExceptionCount >= 0);
            Preconditions.checkArgument(totalLoadTime >= 0);
            Preconditions.checkArgument(evictionCount >= 0);

            this.hitCount = hitCount;
            this.missCount = missCount;
            this.loadSuccessCount = loadSuccessCount;
            this.loadExceptionCount = loadExceptionCount;
            this.totalLoadTime = totalLoadTime;
            this.evictionCount = evictionCount;
        }

        public int hashCode() {
            return com_google_common_base_Objects.hashCode(
                    hitCount, missCount, loadSuccessCount, loadExceptionCount, totalLoadTime, evictionCount);
        }

        public boolean equals(Object object) {
            if (object instanceof CacheStats) {
                CacheStats other = (CacheStats) object;
                return hitCount == other.hitCount
                        && missCount == other.missCount
                        && loadSuccessCount == other.loadSuccessCount
                        && loadExceptionCount == other.loadExceptionCount
                        && totalLoadTime == other.totalLoadTime
                        && evictionCount == other.evictionCount;
            }
            return false;
        }

        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("hitCount", hitCount)
                    .add("missCount", missCount)
                    .add("loadSuccessCount", loadSuccessCount)
                    .add("loadExceptionCount", loadExceptionCount)
                    .add("totalLoadTime", totalLoadTime)
                    .add("evictionCount", evictionCount)
                    .toString();
        }

    }

    static abstract class ExtraObjectsMethodsForWeb {
    }

    static final class Maps {
        private Maps() {
        }

        public static <K, V> Entry<K, V> immutableEntry(K key, V value) {
            return new ImmutableEntry<>(key, value);
        }

    }

    static // uses writeReplace(), not default serialization
    final class RegularImmutableSet<E> extends ImmutableSet<E> {
        static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet<>(new Object[0], 0, null, 0);
        private final transient Object[] elements;
        final transient Object[] table;
        private final transient int mask;
        private final transient int hashCode;

        RegularImmutableSet(Object[] elements, int hashCode, Object[] table, int mask) {
            this.elements = elements;
            this.table = table;
            this.mask = mask;
            this.hashCode = hashCode;
        }

        public boolean contains(Object target) {
            Object[] table = this.table;
            if (target == null || table == null) {
                return false;
            }
            for (int i = Hashing.smearedHash(target); ; i++) {
                i &= mask;
                Object candidate = table[i];
                if (candidate == null) {
                    return false;
                } else if (candidate.equals(target)) {
                    return true;
                }
            }
        }

        public int size() {
            return elements.length;
        }

        public UnmodifiableIterator<E> iterator() {
            return (UnmodifiableIterator<E>) Iterators.forArray(elements);
        }

        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(elements, ImmutableSet.SPLITERATOR_CHARACTERISTICS);
        }

        Object[] internalArray() {
            return elements;
        }

        int internalArrayStart() {
            return 0;
        }

        int internalArrayEnd() {
            return elements.length;
        }

        int copyIntoArray(Object[] dst, int offset) {
            System.arraycopy(elements, 0, dst, offset, elements.length);
            return offset + elements.length;
        }

        public int hashCode() {
            return hashCode;
        }

        boolean isHashCodeFast() {
            return true;
        }

    }

    static // we're overriding default serialization
    abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
        static final int SPLITERATOR_CHARACTERISTICS = ImmutableCollection.SPLITERATOR_CHARACTERISTICS | Spliterator.DISTINCT;

        // fully variant implementation (never actually produces any Es)
        public static <E> ImmutableSet<E> of() {
            return (ImmutableSet<E>) RegularImmutableSet.EMPTY;
        }

        ImmutableSet() {
        }

        boolean isHashCodeFast() {
            return false;
        }

        public boolean equals(Object object) {
            if (object == this) {
                return true;
            } else if (object instanceof ImmutableSet
                    && isHashCodeFast()
                    && ((ImmutableSet<?>) object).isHashCodeFast()
                    && hashCode() != object.hashCode()) {
                return false;
            }
            return Sets.equalsImpl(this, object);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }

        public abstract UnmodifiableIterator<E> iterator();

    }

    static final class Lists {
        private Lists() {
        }

        static boolean equalsImpl(List<?> thisList, Object other) {
            if (other == Preconditions.checkNotNull(thisList)) {
                return true;
            }
            if (!(other instanceof List)) {
                return false;
            }
            List<?> otherList = (List<?>) other;
            int size = thisList.size();
            if (size != otherList.size()) {
                return false;
            }
            if (thisList instanceof RandomAccess && otherList instanceof RandomAccess) {
                // avoid allocation and use the faster loop
                for (int i = 0; i < size; i++) {
                    if (!com_google_common_base_Objects.equal(thisList.get(i), otherList.get(i))) {
                        return false;
                    }
                }
                return true;
            } else {
                return Iterators.elementsEqual(thisList.iterator(), otherList.iterator());
            }
        }

        static int indexOfImpl(List<?> list, Object element) {
            if (list instanceof RandomAccess) {
                return indexOfRandomAccess(list, element);
            } else {
                ListIterator<?> listIterator = list.listIterator();
                while (listIterator.hasNext()) {
                    if (com_google_common_base_Objects.equal(element, listIterator.next())) {
                        return listIterator.previousIndex();
                    }
                }
                return -1;
            }
        }

        private static int indexOfRandomAccess(List<?> list, Object element) {
            int size = list.size();
            if (element == null) {
                for (int i = 0; i < size; i++) {
                    if (list.get(i) == null) {
                        return i;
                    }
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (element.equals(list.get(i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        static int lastIndexOfImpl(List<?> list, Object element) {
            if (list instanceof RandomAccess) {
                return lastIndexOfRandomAccess(list, element);
            } else {
                ListIterator<?> listIterator = list.listIterator(list.size());
                while (listIterator.hasPrevious()) {
                    if (com_google_common_base_Objects.equal(element, listIterator.previous())) {
                        return listIterator.nextIndex();
                    }
                }
                return -1;
            }
        }

        private static int lastIndexOfRandomAccess(List<?> list, Object element) {
            if (element == null) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i) == null) {
                        return i;
                    }
                }
            } else {
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (element.equals(list.get(i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

    }

    static abstract class AbstractMapEntry<K, V> implements Entry<K, V> {
        public abstract K getKey();

        public abstract V getValue();

        public V setValue(V value) {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object object) {
            if (object instanceof Entry) {
                Entry<?, ?> that = (Entry<?, ?>) object;
                return com_google_common_base_Objects.equal(this.getKey(), that.getKey())
                        && com_google_common_base_Objects.equal(this.getValue(), that.getValue());
            }
            return false;
        }

        public int hashCode() {
            K k = getKey();
            V v = getValue();
            return ((k == null) ? 0 : k.hashCode()) ^ ((v == null) ? 0 : v.hashCode());
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }

    }

    static interface com_google_common_base_Function<F, T> extends java.util.function.Function<F, T> {
// TODO(kevinb): remove this T apply( F input);

        boolean equals(Object object);

    }

    static abstract class Equivalence<T> implements BiPredicate<T, T> {
        protected Equivalence() {
        }

        public final boolean equivalent(T a, T b) {
            if (a == b) {
                return true;
            }
            if (a == null || b == null) {
                return false;
            }
            return doEquivalent(a, b);
        }

        public final boolean test(T t, T u) {
            return equivalent(t, u);
        }

        protected abstract boolean doEquivalent(T a, T b);

        public final int hash(T t) {
            if (t == null) {
                return 0;
            }
            return doHash(t);
        }

        protected abstract int doHash(T t);

        public static Equivalence<Object> equals() {
            return Equivalence.Equals.INSTANCE;
        }

        public static Equivalence<Object> identity() {
            return Equivalence.Identity.INSTANCE;
        }

        static final class Equals extends Equivalence<Object> implements Serializable {
            static final Equals INSTANCE = new Equals();

            protected boolean doEquivalent(Object a, Object b) {
                return a.equals(b);
            }

            protected int doHash(Object o) {
                return o.hashCode();
            }

        }

        static final class Identity extends Equivalence<Object> implements Serializable {
            static final Identity INSTANCE = new Identity();

            protected boolean doEquivalent(Object a, Object b) {
                return false;
            }

            protected int doHash(Object o) {
                return System.identityHashCode(o);
            }

        }

    }

    static final class Preconditions {
        private Preconditions() {
        }

        public static void checkArgument(boolean expression) {
            if (!expression) {
                throw new IllegalArgumentException();
            }
        }

        public static void checkState(boolean expression) {
            if (!expression) {
                throw new IllegalStateException();
            }
        }

        public static void checkState(boolean expression, Object errorMessage) {
            if (!expression) {
                throw new IllegalStateException(String.valueOf(errorMessage));
            }
        }

        public static void checkState(
                boolean b, String errorMessageTemplate, Object p1) {
            if (!b) {
                throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, p1));
            }
        }

        public static <T> T checkNotNull(T reference) {
            if (reference == null) {
                throw new NullPointerException();
            }
            return reference;
        }

        public static <T> T checkNotNull(T reference, Object errorMessage) {
            if (reference == null) {
                throw new NullPointerException(String.valueOf(errorMessage));
            }
            return reference;
        }

        public static int checkElementIndex(int index, int size) {
            return checkElementIndex(index, size, "index");
        }

        public static int checkElementIndex(int index, int size, String desc) {
            // Carefully optimized for execution by hotspot (explanatory comment above)
            if (index < 0 || index >= size) {
                throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
            }
            return index;
        }

        private static String badElementIndex(int index, int size, String desc) {
            if (index < 0) {
                return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
            } else if (size < 0) {
                throw new IllegalArgumentException("negative size: " + size);
            } else { // index >= size
                return Strings.lenientFormat("%s (%s) must be less than size (%s)", desc, index, size);
            }
        }

        public static int checkPositionIndex(int index, int size) {
            return checkPositionIndex(index, size, "index");
        }

        public static int checkPositionIndex(int index, int size, String desc) {
            // Carefully optimized for execution by hotspot (explanatory comment above)
            if (index < 0 || index > size) {
                throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
            }
            return index;
        }

        private static String badPositionIndex(int index, int size, String desc) {
            if (index < 0) {
                return Strings.lenientFormat("%s (%s) must not be negative", desc, index);
            } else if (size < 0) {
                throw new IllegalArgumentException("negative size: " + size);
            } else { // index > size
                return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", desc, index, size);
            }
        }

        public static void checkPositionIndexes(int start, int end, int size) {
            // Carefully optimized for execution by hotspot (explanatory comment above)
            if (start < 0 || end < start || end > size) {
                throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
            }
        }

        private static String badPositionIndexes(int start, int end, int size) {
            if (start < 0 || start > size) {
                return badPositionIndex(start, size, "start index");
            }
            if (end < 0 || end > size) {
                return badPositionIndex(end, size, "end index");
            }
            // end < start
            return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", end, start);
        }

    }

    static final class Uninterruptibles {
        public static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
            boolean interrupted = false;
            try {
                while (true) {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private Uninterruptibles() {
        }

    }

    static abstract class Optional<T> implements Serializable {
        Optional() {
        }

        public abstract boolean equals(Object object);

        public abstract int hashCode();

        public abstract String toString();

    }

    static abstract class InternalFutureFailureAccess {
        protected InternalFutureFailureAccess() {
        }

        protected abstract Throwable tryInternalFastPathGetFailure();

    }

    static final class Verify {
        private Verify() {
        }

    }

    static interface RemovalListener<K, V> {
        void onRemoval(RemovalNotification<K, V> notification);

    }

    static abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E> implements ListIterator<E> {
        protected UnmodifiableListIterator() {
        }

        public final void add(E e) {
            throw new UnsupportedOperationException();
        }

        public final void set(E e) {
            throw new UnsupportedOperationException();
        }

    }

    static abstract class UnmodifiableIterator<E> implements Iterator<E> {
        protected UnmodifiableIterator() {
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }

    }

    static final class CollectSpliterators {
        private CollectSpliterators() {
        }

        static <T> Spliterator<T> indexed(int size, int extraCharacteristics, IntFunction<T> function) {
            return indexed(size, extraCharacteristics, function, null);
        }

        static <T> Spliterator<T> indexed(
                int size,
                int extraCharacteristics,
                IntFunction<T> function,
                Comparator<? super T> comparator) {
            if (comparator != null) {
                Preconditions.checkArgument((extraCharacteristics & Spliterator.SORTED) != 0);
            }
            class WithCharacteristics implements Spliterator<T> {
                private final Spliterator.OfInt delegate;

                WithCharacteristics(Spliterator.OfInt delegate) {
                    this.delegate = delegate;
                }


                public boolean tryAdvance(Consumer<? super T> action) {
                    return delegate.tryAdvance((IntConsumer) i -> action.accept(function.apply(i)));
                }


                public void forEachRemaining(Consumer<? super T> action) {
                    delegate.forEachRemaining((IntConsumer) i -> action.accept(function.apply(i)));
                }


                public Spliterator<T> trySplit() {
                    Spliterator.OfInt split = delegate.trySplit();
                    return (split == null) ? null : new WithCharacteristics(split);
                }


                public long estimateSize() {
                    return delegate.estimateSize();
                }


                public int characteristics() {
                    return Spliterator.ORDERED
                            | Spliterator.SIZED
                            | Spliterator.SUBSIZED
                            | extraCharacteristics;
                }


                public Comparator<? super T> getComparator() {
                    if (hasCharacteristics(Spliterator.SORTED)) {
                        return comparator;
                    } else {
                        throw new IllegalStateException();
                    }
                }
            }
            return new WithCharacteristics(IntStream.range(0, size).spliterator());
        }

    }

    static enum RemovalCause {
        /**
         * The entry was manually removed by the user. This can result from the user invoking {@link
         * Cache#invalidate}, {@link Cache#invalidateAll(Iterable)}, {@link Cache#invalidateAll()}, {@link
         * Map#remove}, {@link ConcurrentMap#remove}, or {@link Iterator#remove}.
         */
        EXPLICIT {
            boolean wasEvicted() {
                return false;
            }
        },
        /**
         * The entry itself was not actually removed, but its value was replaced by the user. This can
         * result from the user invoking {@link Cache#put}, {@link LoadingCache#refresh}, {@link Map#put},
         * {@link Map#putAll}, {@link ConcurrentMap#replace(Object, Object)}, or {@link
         * ConcurrentMap#replace(Object, Object, Object)}.
         */
        REPLACED {
            boolean wasEvicted() {
                return false;
            }
        },
        /**
         * The entry was removed automatically because its key or value was garbage-collected. This can
         * occur when using {@link CacheBuilder#weakKeys}, {@link CacheBuilder#weakValues}, or {@link
         * CacheBuilder#softValues}.
         */
        COLLECTED {
            boolean wasEvicted() {
                return true;
            }
        },
        /**
         * The entry's expiration timestamp has passed. This can occur when using {@link
         * CacheBuilder#expireAfterWrite} or {@link CacheBuilder#expireAfterAccess}.
         */
        EXPIRED {
            boolean wasEvicted() {
                return true;
            }
        },
        /**
         * The entry was evicted due to size constraints. This can occur when using {@link
         * CacheBuilder#maximumSize} or {@link CacheBuilder#maximumWeight}.
         */
        SIZE {
            boolean wasEvicted() {
                return true;
            }
        },
        ;

        abstract boolean wasEvicted();

    }

    static final class SettableFuture<V> extends AbstractFuture.TrustedFuture<V> {
        public static <V> SettableFuture<V> create() {
            return new SettableFuture<V>();
        }

        public boolean set(V value) {
            return super.set(value);
        }

        public boolean setException(Throwable throwable) {
            return super.setException(throwable);
        }

        public boolean setFuture(ListenableFuture<? extends V> future) {
            return super.setFuture(future);
        }

        private SettableFuture() {
        }

    }

    static abstract class AbstractIndexedListIterator<E> extends UnmodifiableListIterator<E> {
        private final int size;
        private int position;

        protected abstract E get(int index);

        protected AbstractIndexedListIterator(int size) {
            this(size, 0);
        }

        protected AbstractIndexedListIterator(int size, int position) {
            Preconditions.checkPositionIndex(position, size);
            this.size = size;
            this.position = position;
        }

        public final boolean hasNext() {
            return position < size;
        }

        public final E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return get(position++);
        }

        public final int nextIndex() {
            return position;
        }

        public final boolean hasPrevious() {
            return position > 0;
        }

        public final E previous() {
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }
            return get(--position);
        }

        public final int previousIndex() {
            return position - 1;
        }

    }

    static interface Cache<K, V> {
        V getIfPresent(Object key);

    }

    static class FastInput {
        private final InputStream is;

        public FastInput(InputStream is) {
            this.is = is;
        }

    }

    static interface Beta {
    }

    static interface Weak {
    }

    static final class Futures extends GwtFuturesCatchingSpecialization {
        private Futures() {
        }

        public static <V> ListenableFuture<V> immediateFuture(V value) {
            if (value == null) {
                // This cast is safe because null is assignable to V for all V (i.e. it is covariant)

                ListenableFuture<V> typedNull = (ListenableFuture) ImmediateFuture.ImmediateSuccessfulFuture.NULL;
                return typedNull;
            }
            return new ImmediateSuccessfulFuture<V>(value);
        }

        public static <V> ListenableFuture<V> immediateFailedFuture(Throwable throwable) {
            Preconditions.checkNotNull(throwable);
            return new ImmediateFailedFuture<V>(throwable);
        }

        public static <I, O> ListenableFuture<O> transform(
                ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
            return AbstractTransformFuture.create(input, function, executor);
        }

        // TODO(cpovirk): Consider calling getDone() in our own code.
        public static <V> V getDone(Future<V> future) throws ExecutionException {
            /*
             * We throw IllegalStateException, since the call could succeed later. Perhaps we "should" throw
             * IllegalArgumentException, since the call could succeed with a different argument. Those
             * exceptions' docs suggest that either is acceptable. Google's Java Practices page recommends
             * IllegalArgumentException here, in part to keep its recommendation simple: Static methods
             * should throw IllegalStateException only when they use static state.
             *
             *
             * Why do we deviate here? The answer: We want for fluentFuture.getDone() to throw the same
             * exception as Futures.getDone(fluentFuture).
             */
            Preconditions.checkState(future.isDone(), "Future was expected to be done: %s", future);
            return Uninterruptibles.getUninterruptibly(future);
        }

// TODO(cpovirk): Consider removing, especially if we provide run(Runnable)

        public static final class FutureCombiner<V> {
            private final boolean allMustSucceed;
            private final ImmutableList<ListenableFuture<? extends V>> futures;

            private FutureCombiner(
                    boolean allMustSucceed, ImmutableList<ListenableFuture<? extends V>> futures) {
                this.allMustSucceed = allMustSucceed;
                this.futures = futures;
            }

        }

    }

    static final class MoreExecutors {
        private MoreExecutors() {
        }

        public static Executor directExecutor() {
            return DirectExecutor.INSTANCE;
        }

        static Executor rejectionPropagatingExecutor(
                final Executor delegate, final AbstractFuture<?> future) {
            Preconditions.checkNotNull(delegate);
            Preconditions.checkNotNull(future);
            if (delegate == directExecutor()) {
                // directExecutor() cannot throw RejectedExecutionException
                return delegate;
            }
            return new Executor() {
                boolean thrownFromDelegate = true;


                public void execute(final Runnable command) {
                    try {
                        delegate.execute(
                                new Runnable() {

                                    public void run() {
                                        thrownFromDelegate = false;
                                        command.run();
                                    }
                                });
                    } catch (RejectedExecutionException e) {
                        if (thrownFromDelegate) {
                            // wrap exception?
                            future.setException(e);
                        }
                        // otherwise it must have been thrown from a transitive call and the delegate runnable
                        // should have handled it.
                    }
                }
            };
        }

    }

    static // we use non-short circuiting comparisons intentionally


    abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
        private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(
                System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
        private static final long SPIN_THRESHOLD_NANOS = 1000L;
        private static final AbstractFuture.AtomicHelper ATOMIC_HELPER;
        private static final Object NULL = new Object();
        private volatile Object value;
        private volatile AbstractFuture.Listener listeners;
        private volatile AbstractFuture.Waiter waiters;

        static {
            AbstractFuture.AtomicHelper helper;
            Throwable thrownUnsafeFailure = null;
            Throwable thrownAtomicReferenceFieldUpdaterFailure = null;

            try {
                helper = new AbstractFuture.UnsafeAtomicHelper();
            } catch (Throwable unsafeFailure) {
                thrownUnsafeFailure = unsafeFailure;
                // catch absolutely everything and fall through to our 'SafeAtomicHelper'
                // The access control checks that ARFU does means the caller class has to be AbstractFuture
                // instead of SafeAtomicHelper, so we annoyingly define these here
                try {
                    helper =
                            new AbstractFuture.SafeAtomicHelper(
                                    AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, Thread.class, "thread"),
                                    AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.Waiter.class, AbstractFuture.Waiter.class, "next"),
                                    AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Waiter.class, "waiters"),
                                    AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, AbstractFuture.Listener.class, "listeners"),
                                    AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
                } catch (Throwable atomicReferenceFieldUpdaterFailure) {
                    // Some Android 5.0.x Samsung devices have bugs in JDK reflection APIs that cause
                    // getDeclaredField to throw a NoSuchFieldException when the field is definitely there.
                    // For these users fallback to a suboptimal implementation, based on synchronized. This will
                    // be a definite performance hit to those users.
                    thrownAtomicReferenceFieldUpdaterFailure = atomicReferenceFieldUpdaterFailure;
                    helper = new AbstractFuture.SynchronizedHelper();
                }
            }
            ATOMIC_HELPER = helper;

            // Prevent rare disastrous classloading in first call to LockSupport.park.
            // See: https://bugs.openjdk.java.net/browse/JDK-8074773

            Class<?> ensureLoaded = LockSupport.class;

            // Log after all static init is finished; if an installed logger uses any Futures methods, it
            // shouldn't break in cases where reflection is missing/broken.
            if (thrownAtomicReferenceFieldUpdaterFailure != null) {
                log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", thrownUnsafeFailure);
                log.log(
                        Level.SEVERE, "SafeAtomicHelper is broken!", thrownAtomicReferenceFieldUpdaterFailure);
            }
        }

        private void removeWaiter(AbstractFuture.Waiter node) {
            node.thread = null; // mark as 'deleted'
            restart:
            while (true) {
                AbstractFuture.Waiter pred = null;
                AbstractFuture.Waiter curr = waiters;
                if (curr == AbstractFuture.Waiter.TOMBSTONE) {
                    return; // give up if someone is calling complete
                }
                AbstractFuture.Waiter succ;
                while (curr != null) {
                    succ = curr.next;
                    if (curr.thread != null) { // we aren't unlinking this node, update pred.
                        pred = curr;
                    } else if (pred != null) { // We are unlinking this node and it has a predecessor.
                        pred.next = succ;
                        if (pred.thread == null) { // We raced with another node that unlinked pred. Restart.
                            continue restart;
                        }
                    } else if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) { // We are unlinking head
                        continue restart; // We raced with an add or complete
                    }
                    curr = succ;
                }
                break;
            }
        }

        protected AbstractFuture() {
        }

        public V get(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException, ExecutionException {
            // NOTE: if timeout < 0, remainingNanos will be < 0 and we will fall into the while(true) loop
            // at the bottom and throw a timeoutexception.
            final long timeoutNanos = unit.toNanos(timeout); // we rely on the implicit null check on unit.
            long remainingNanos = timeoutNanos;
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Object localValue = value;
            if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                return getDoneValue(localValue);
            }
            // we delay calling nanoTime until we know we will need to either park or spin
            final long endNanos = remainingNanos > 0 ? System.nanoTime() + remainingNanos : 0;
            long_wait_loop:
            if (remainingNanos >= SPIN_THRESHOLD_NANOS) {
                AbstractFuture.Waiter oldHead = waiters;
                if (oldHead != AbstractFuture.Waiter.TOMBSTONE) {
                    AbstractFuture.Waiter node = new AbstractFuture.Waiter();
                    do {
                        node.setNext(oldHead);
                        if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                            while (true) {
                                LockSupport.parkNanos(this, remainingNanos);
                                // Check interruption first, if we woke up due to interruption we need to honor that.
                                if (Thread.interrupted()) {
                                    removeWaiter(node);
                                    throw new InterruptedException();
                                }

                                // Otherwise re-read and check doneness. If we loop then it must have been a spurious
                                // wakeup
                                localValue = value;
                                if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                                    return getDoneValue(localValue);
                                }

                                // timed out?
                                remainingNanos = endNanos - System.nanoTime();
                                if (remainingNanos < SPIN_THRESHOLD_NANOS) {
                                    // Remove the waiter, one way or another we are done parking this thread.
                                    removeWaiter(node);
                                    break long_wait_loop; // jump down to the busy wait loop
                                }
                            }
                        }
                        oldHead = waiters; // re-read and loop.
                    } while (oldHead != AbstractFuture.Waiter.TOMBSTONE);
                }
                // re-read value, if we get here then we must have observed a TOMBSTONE while trying to add a
                // waiter.
                return getDoneValue(value);
            }
            // If we get here then we have remainingNanos < SPIN_THRESHOLD_NANOS and there is no node on the
            // waiters list
            while (remainingNanos > 0) {
                localValue = value;
                if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                    return getDoneValue(localValue);
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                remainingNanos = endNanos - System.nanoTime();
            }

            String futureToString = toString();
            final String unitString = unit.toString().toLowerCase(Locale.ROOT);
            String message = "Waited " + timeout + " " + unit.toString().toLowerCase(Locale.ROOT);
            // Only report scheduling delay if larger than our spin threshold - otherwise it's just noise
            if (remainingNanos + SPIN_THRESHOLD_NANOS < 0) {
                // We over-waited for our timeout.
                message += " (plus ";
                long overWaitNanos = -remainingNanos;
                long overWaitUnits = unit.convert(overWaitNanos, TimeUnit.NANOSECONDS);
                long overWaitLeftoverNanos = overWaitNanos - unit.toNanos(overWaitUnits);
                boolean shouldShowExtraNanos =
                        overWaitUnits == 0 || overWaitLeftoverNanos > SPIN_THRESHOLD_NANOS;
                if (overWaitUnits > 0) {
                    message += overWaitUnits + " " + unitString;
                    if (shouldShowExtraNanos) {
                        message += ",";
                    }
                    message += " ";
                }
                if (shouldShowExtraNanos) {
                    message += overWaitLeftoverNanos + " nanoseconds ";
                }

                message += "delay)";
            }
            // It's confusing to see a completed future in a timeout message; if isDone() returns false,
            // then we know it must have given a pending toString value earlier. If not, then the future
            // completed after the timeout expired, and the message might be success.
            if (isDone()) {
                throw new TimeoutException(message + " but future completed as timeout expired");
            }
            throw new TimeoutException(message + " for " + futureToString);
        }

        public V get() throws InterruptedException, ExecutionException {
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            Object localValue = value;
            if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                return getDoneValue(localValue);
            }
            AbstractFuture.Waiter oldHead = waiters;
            if (oldHead != AbstractFuture.Waiter.TOMBSTONE) {
                AbstractFuture.Waiter node = new AbstractFuture.Waiter();
                do {
                    node.setNext(oldHead);
                    if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
                        // we are on the stack, now wait for completion.
                        while (true) {
                            LockSupport.park(this);
                            // Check interruption first, if we woke up due to interruption we need to honor that.
                            if (Thread.interrupted()) {
                                removeWaiter(node);
                                throw new InterruptedException();
                            }
                            // Otherwise re-read and check doneness. If we loop then it must have been a spurious
                            // wakeup
                            localValue = value;
                            if (localValue != null & !(localValue instanceof AbstractFuture.SetFuture)) {
                                return getDoneValue(localValue);
                            }
                        }
                    }
                    oldHead = waiters; // re-read and loop.
                } while (oldHead != AbstractFuture.Waiter.TOMBSTONE);
            }
            // re-read value, if we get here then we must have observed a TOMBSTONE while trying to add a
            // waiter.
            return getDoneValue(value);
        }

        private V getDoneValue(Object obj) throws ExecutionException {
            // While this seems like it might be too branch-y, simple benchmarking proves it to be
            // unmeasurable (comparing done AbstractFutures with immediateFuture)
            if (obj instanceof AbstractFuture.Cancellation) {
                throw cancellationExceptionWithCause("Task was cancelled.", ((AbstractFuture.Cancellation) obj).cause);
            } else if (obj instanceof AbstractFuture.Failure) {
                throw new ExecutionException(((AbstractFuture.Failure) obj).exception);
            } else if (obj == NULL) {
                return null;
            } else {
                // this is the only other option
                V asV = (V) obj;
                return asV;
            }
        }

        public boolean isDone() {
            final Object localValue = value;
            return localValue != null & !(localValue instanceof AbstractFuture.SetFuture);
        }

        public boolean isCancelled() {
            final Object localValue = value;
            return localValue instanceof AbstractFuture.Cancellation;
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            Object localValue = value;
            boolean rValue = false;
            if (localValue == null | localValue instanceof AbstractFuture.SetFuture) {
                // Try to delay allocating the exception. At this point we may still lose the CAS, but it is
                // certainly less likely.
                Object valueToSet =
                        GENERATE_CANCELLATION_CAUSES
                                ? new AbstractFuture.Cancellation(
                                mayInterruptIfRunning, new CancellationException("Future.cancel() was called."))
                                : (mayInterruptIfRunning
                                ? AbstractFuture.Cancellation.CAUSELESS_INTERRUPTED
                                : AbstractFuture.Cancellation.CAUSELESS_CANCELLED);
                AbstractFuture<?> abstractFuture = this;
                while (true) {
                    if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
                        rValue = true;
                        // We call interuptTask before calling complete(), which is consistent with
                        // FutureTask
                        if (mayInterruptIfRunning) {
                            abstractFuture.interruptTask();
                        }
                        complete(abstractFuture);
                        if (localValue instanceof AbstractFuture.SetFuture) {
                            // propagate cancellation to the future set in setfuture, this is racy, and we don't
                            // care if we are successful or not.
                            ListenableFuture<?> futureToPropagateTo = ((AbstractFuture.SetFuture) localValue).future;
                            if (futureToPropagateTo instanceof AbstractFuture.Trusted) {
                                // If the future is a TrustedFuture then we specifically avoid calling cancel()
                                // this has 2 benefits
                                // 1. for long chains of futures strung together with setFuture we consume less stack
                                // 2. we avoid allocating Cancellation objects at every level of the cancellation
                                //    chain
                                // We can only do this for TrustedFuture, because TrustedFuture.cancel is final and
                                // does nothing but delegate to this method.
                                AbstractFuture<?> trusted = (AbstractFuture<?>) futureToPropagateTo;
                                localValue = trusted.value;
                                if (localValue == null | localValue instanceof AbstractFuture.SetFuture) {
                                    abstractFuture = trusted;
                                    continue; // loop back up and try to complete the new future
                                }
                            } else {
                                // not a TrustedFuture, call cancel directly.
                                futureToPropagateTo.cancel(mayInterruptIfRunning);
                            }
                        }
                        break;
                    }
                    // obj changed, reread
                    localValue = abstractFuture.value;
                    if (!(localValue instanceof AbstractFuture.SetFuture)) {
                        // obj cannot be null at this point, because value can only change from null to non-null.
                        // So if value changed (and it did since we lost the CAS), then it cannot be null and
                        // since it isn't a SetFuture, then the future must be done and we should exit the loop
                        break;
                    }
                }
            }
            return rValue;
        }

        protected void interruptTask() {
        }

        protected final boolean wasInterrupted() {
            final Object localValue = value;
            return (localValue instanceof AbstractFuture.Cancellation) && ((AbstractFuture.Cancellation) localValue).wasInterrupted;
        }

        public void addListener(Runnable listener, Executor executor) {
            Preconditions.checkNotNull(listener, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            // Checking isDone and listeners != TOMBSTONE may seem redundant, but our contract for
            // addListener says that listeners execute 'immediate' if the future isDone(). However, our
            // protocol for completing a future is to assign the value field (which sets isDone to true) and
            // then to release waiters, followed by executing afterDone(), followed by releasing listeners.
            // That means that it is possible to observe that the future isDone and that your listeners
            // don't execute 'immediately'.  By checking isDone here we avoid that.
            // A corollary to all that is that we don't need to check isDone inside the loop because if we
            // get into the loop we know that we weren't done when we entered and therefore we aren't under
            // an obligation to execute 'immediately'.
            if (!isDone()) {
                AbstractFuture.Listener oldHead = listeners;
                if (oldHead != AbstractFuture.Listener.TOMBSTONE) {
                    AbstractFuture.Listener newNode = new AbstractFuture.Listener(listener, executor);
                    do {
                        newNode.next = oldHead;
                        if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
                            return;
                        }
                        oldHead = listeners; // re-read
                    } while (oldHead != AbstractFuture.Listener.TOMBSTONE);
                }
            }
            // If we get here then the Listener TOMBSTONE was set, which means the future is done, call
            // the listener.
            executeListener(listener, executor);
        }

        protected boolean set(V value) {
            Object valueToSet = value == null ? NULL : value;
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                complete(this);
                return true;
            }
            return false;
        }

        protected boolean setException(Throwable throwable) {
            Object valueToSet = new AbstractFuture.Failure(Preconditions.checkNotNull(throwable));
            if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                complete(this);
                return true;
            }
            return false;
        }

        protected boolean setFuture(ListenableFuture<? extends V> future) {
            Preconditions.checkNotNull(future);
            Object localValue = value;
            if (localValue == null) {
                if (future.isDone()) {
                    Object value = getFutureValue(future);
                    if (ATOMIC_HELPER.casValue(this, null, value)) {
                        complete(this);
                        return true;
                    }
                    return false;
                }
                AbstractFuture.SetFuture valueToSet = new AbstractFuture.SetFuture<V>(this, future);
                if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
                    // the listener is responsible for calling completeWithFuture, directExecutor is appropriate
                    // since all we are doing is unpacking a completed future which should be fast.
                    try {
                        future.addListener(valueToSet, DirectExecutor.INSTANCE);
                    } catch (Throwable t) {
                        // addListener has thrown an exception! SetFuture.run can't throw any exceptions so this
                        // must have been caused by addListener itself. The most likely explanation is a
                        // misconfigured mock. Try to switch to Failure.
                        AbstractFuture.Failure failure;
                        try {
                            failure = new AbstractFuture.Failure(t);
                        } catch (Throwable oomMostLikely) {
                            failure = AbstractFuture.Failure.FALLBACK_INSTANCE;
                        }
                        // Note: The only way this CAS could fail is if cancel() has raced with us. That is ok.
                        boolean unused = ATOMIC_HELPER.casValue(this, valueToSet, failure);
                    }
                    return true;
                }
                localValue = value; // we lost the cas, fall through and maybe cancel
            }
            // The future has already been set to something. If it is cancellation we should cancel the
            // incoming future.
            if (localValue instanceof AbstractFuture.Cancellation) {
                // we don't care if it fails, this is best-effort.
                future.cancel(((AbstractFuture.Cancellation) localValue).wasInterrupted);
            }
            return false;
        }

        private static Object getFutureValue(ListenableFuture<?> future) {
            if (future instanceof AbstractFuture.Trusted) {
                // Break encapsulation for TrustedFuture instances since we know that subclasses cannot
                // override .get() (since it is final) and therefore this is equivalent to calling .get()
                // and unpacking the exceptions like we do below (just much faster because it is a single
                // field read instead of a read, several branches and possibly creating exceptions).
                Object v = ((AbstractFuture<?>) future).value;
                if (v instanceof AbstractFuture.Cancellation) {
                    // If the other future was interrupted, clear the interrupted bit while preserving the cause
                    // this will make it consistent with how non-trustedfutures work which cannot propagate the
                    // wasInterrupted bit
                    AbstractFuture.Cancellation c = (AbstractFuture.Cancellation) v;
                    if (c.wasInterrupted) {
                        v =
                                c.cause != null
                                        ? new AbstractFuture.Cancellation(/* wasInterrupted= */ false, c.cause)
                                        : AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
                    }
                }
                return v;
            }
            if (future instanceof InternalFutureFailureAccess) {
                Throwable throwable =
                        InternalFutures.tryInternalFastPathGetFailure((InternalFutureFailureAccess) future);
                if (throwable != null) {
                    return new AbstractFuture.Failure(throwable);
                }
            }
            boolean wasCancelled = future.isCancelled();
            // Don't allocate a CancellationException if it's not necessary
            if (!GENERATE_CANCELLATION_CAUSES & wasCancelled) {
                return AbstractFuture.Cancellation.CAUSELESS_CANCELLED;
            }
            // Otherwise calculate the value by calling .get()
            try {
                Object v = getUninterruptibly(future);
                if (wasCancelled) {
                    return new AbstractFuture.Cancellation(
                            false,
                            new IllegalArgumentException(
                                    "get() did not throw CancellationException, despite reporting "
                                            + "isCancelled() == true: "
                                            + future));
                }
                return v == null ? NULL : v;
            } catch (ExecutionException exception) {
                if (wasCancelled) {
                    return new AbstractFuture.Cancellation(
                            false,
                            new IllegalArgumentException(
                                    "get() did not throw CancellationException, despite reporting "
                                            + "isCancelled() == true: "
                                            + future,
                                    exception));
                }
                return new AbstractFuture.Failure(exception.getCause());
            } catch (CancellationException cancellation) {
                if (!wasCancelled) {
                    return new AbstractFuture.Failure(
                            new IllegalArgumentException(
                                    "get() threw CancellationException, despite reporting isCancelled() == false: "
                                            + future,
                                    cancellation));
                }
                return new AbstractFuture.Cancellation(false, cancellation);
            } catch (Throwable t) {
                return new AbstractFuture.Failure(t);
            }
        }

        private static <V> V getUninterruptibly(Future<V> future) throws ExecutionException {
            boolean interrupted = false;
            try {
                while (true) {
                    try {
                        return future.get();
                    } catch (InterruptedException e) {
                        interrupted = true;
                    }
                }
            } finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        private static void complete(AbstractFuture<?> future) {
            AbstractFuture.Listener next = null;
            outer:
            while (true) {
                future.releaseWaiters();
                // We call this before the listeners in order to avoid needing to manage a separate stack data
                // structure for them.  Also, some implementations rely on this running prior to listeners
                // so that the cleanup work is visible to listeners.
                // afterDone() should be generally fast and only used for cleanup work... but in theory can
                // also be recursive and create StackOverflowErrors
                future.afterDone();
                // push the current set of listeners onto next
                next = future.clearListeners(next);
                future = null;
                while (next != null) {
                    AbstractFuture.Listener curr = next;
                    next = next.next;
                    Runnable task = curr.task;
                    if (task instanceof AbstractFuture.SetFuture) {
                        AbstractFuture.SetFuture<?> setFuture = (AbstractFuture.SetFuture<?>) task;
                        // We unwind setFuture specifically to avoid StackOverflowErrors in the case of long
                        // chains of SetFutures
                        // Handling this special case is important because there is no way to pass an executor to
                        // setFuture, so a user couldn't break the chain by doing this themselves.  It is also
                        // potentially common if someone writes a recursive Futures.transformAsync transformer.
                        future = setFuture.owner;
                        if (future.value == setFuture) {
                            Object valueToSet = getFutureValue(setFuture.future);
                            if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
                                continue outer;
                            }
                        }
                        // other wise the future we were trying to set is already done.
                    } else {
                        executeListener(task, curr.executor);
                    }
                }
                break;
            }
        }

        protected void afterDone() {
        }

        protected final Throwable tryInternalFastPathGetFailure() {
            if (this instanceof AbstractFuture.Trusted) {
                Object obj = value;
                if (obj instanceof AbstractFuture.Failure) {
                    return ((AbstractFuture.Failure) obj).exception;
                }
            }
            return null;
        }

        final void maybePropagateCancellationTo(Future<?> related) {
            if (related != null & isCancelled()) {
                related.cancel(wasInterrupted());
            }
        }

        private void releaseWaiters() {
            AbstractFuture.Waiter head;
            do {
                head = waiters;
            } while (!ATOMIC_HELPER.casWaiters(this, head, AbstractFuture.Waiter.TOMBSTONE));
            for (AbstractFuture.Waiter currentWaiter = head; currentWaiter != null; currentWaiter = currentWaiter.next) {
                currentWaiter.unpark();
            }
        }

        private AbstractFuture.Listener clearListeners(AbstractFuture.Listener onto) {
            // We need to
            // 1. atomically swap the listeners with TOMBSTONE, this is because addListener uses that to
            //    to synchronize with us
            // 2. reverse the linked list, because despite our rather clear contract, people depend on us
            //    executing listeners in the order they were added
            // 3. push all the items onto 'onto' and return the new head of the stack
            AbstractFuture.Listener head;
            do {
                head = listeners;
            } while (!ATOMIC_HELPER.casListeners(this, head, AbstractFuture.Listener.TOMBSTONE));
            AbstractFuture.Listener reversedList = onto;
            while (head != null) {
                AbstractFuture.Listener tmp = head;
                head = head.next;
                tmp.next = reversedList;
                reversedList = tmp;
            }
            return reversedList;
        }

        public String toString() {
            StringBuilder builder = new StringBuilder().append(super.toString()).append("[status=");
            if (isCancelled()) {
                builder.append("CANCELLED");
            } else if (isDone()) {
                addDoneString(builder);
            } else {
                String pendingDescription;
                try {
                    pendingDescription = pendingToString();
                } catch (RuntimeException e) {
                    // Don't call getMessage or toString() on the exception, in case the exception thrown by the
                    // subclass is implemented with bugs similar to the subclass.
                    pendingDescription = "Exception thrown from implementation: " + e.getClass();
                }
                // The future may complete during or before the call to getPendingToString, so we use null
                // as a signal that we should try checking if the future is done again.
                if (pendingDescription != null && !pendingDescription.isEmpty()) {
                    builder.append("PENDING, info=[").append(pendingDescription).append("]");
                } else if (isDone()) {
                    addDoneString(builder);
                } else {
                    builder.append("PENDING");
                }
            }
            return builder.append("]").toString();
        }

        protected String pendingToString() {
            Object localValue = value;
            if (localValue instanceof AbstractFuture.SetFuture) {
                return "setFuture=[" + userObjectToString(((AbstractFuture.SetFuture) localValue).future) + "]";
            } else if (this instanceof ScheduledFuture) {
                return "remaining delay=["
                        + ((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS)
                        + " ms]";
            }
            return null;
        }

        private void addDoneString(StringBuilder builder) {
            try {
                V value = getUninterruptibly(this);
                builder.append("SUCCESS, result=[").append(userObjectToString(value)).append("]");
            } catch (ExecutionException e) {
                builder.append("FAILURE, cause=[").append(e.getCause()).append("]");
            } catch (CancellationException e) {
                builder.append("CANCELLED"); // shouldn't be reachable
            } catch (RuntimeException e) {
                builder.append("UNKNOWN, cause=[").append(e.getClass()).append(" thrown from get()]");
            }
        }

        private String userObjectToString(Object o) {
            // This is some basic recursion detection for when people create cycles via set/setFuture
            // This is however only partial protection though since it only detects self loops.  We could
            // detect arbitrary cycles using a thread local or possibly by catching StackOverflowExceptions
            // but this should be a good enough solution (it is also what jdk collections do in these cases)
            if (o == this) {
                return "this future";
            }
            return String.valueOf(o);
        }

        private static void executeListener(Runnable runnable, Executor executor) {
            try {
                executor.execute(runnable);
            } catch (RuntimeException e) {
                // Log it and keep going -- bad runnable and/or executor. Don't punish the other runnables if
                // we're given a bad one. We only catch RuntimeException because we want Errors to propagate
                // up.
                log.log(
                        Level.SEVERE,
                        "RuntimeException while executing runnable " + runnable + " with executor " + executor,
                        e);
            }
        }

        private static CancellationException cancellationExceptionWithCause(
                String message, Throwable cause) {
            CancellationException exception = new CancellationException(message);
            exception.initCause(cause);
            return exception;
        }

        interface Trusted<V> extends ListenableFuture<V> {
        }

        abstract static class TrustedFuture<V> extends AbstractFuture<V> implements AbstractFuture.Trusted<V> {
            public final V get() throws InterruptedException, ExecutionException {
                return super.get();
            }

            public final V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return super.get(timeout, unit);
            }

            public final boolean isDone() {
                return super.isDone();
            }

            public final boolean isCancelled() {
                return super.isCancelled();
            }

            public final void addListener(Runnable listener, Executor executor) {
                super.addListener(listener, executor);
            }

            public final boolean cancel(boolean mayInterruptIfRunning) {
                return super.cancel(mayInterruptIfRunning);
            }

        }

        private static final class Waiter {
            static final AbstractFuture.Waiter TOMBSTONE = new AbstractFuture.Waiter(false /* ignored param */);
            volatile Thread thread;
            volatile AbstractFuture.Waiter next;

            Waiter(boolean unused) {
            }

            Waiter() {
                // avoid volatile write, write is made visible by subsequent CAS on waiters field
                ATOMIC_HELPER.putThread(this, Thread.currentThread());
            }

            void setNext(AbstractFuture.Waiter next) {
                ATOMIC_HELPER.putNext(this, next);
            }

            void unpark() {
                // This is racy with removeWaiter. The consequence of the race is that we may spuriously call
                // unpark even though the thread has already removed itself from the list. But even if we did
                // use a CAS, that race would still exist (it would just be ever so slightly smaller).
                Thread w = thread;
                if (w != null) {
                    thread = null;
                    LockSupport.unpark(w);
                }
            }

        }

        private static final class Listener {
            static final AbstractFuture.Listener TOMBSTONE = new AbstractFuture.Listener(null, null);
            final Runnable task;
            final Executor executor;
            AbstractFuture.Listener next;

            Listener(Runnable task, Executor executor) {
                this.task = task;
                this.executor = executor;
            }

        }

        private static final class Failure {
            static final AbstractFuture.Failure FALLBACK_INSTANCE = new AbstractFuture.Failure(
                    new Throwable("Failure occurred while trying to finish a future.") {

                        public synchronized Throwable fillInStackTrace() {
                            return this; // no stack trace
                        }
                    });
            final Throwable exception;

            Failure(Throwable exception) {
                this.exception = Preconditions.checkNotNull(exception);
            }

        }

        private static final class Cancellation {
            static final AbstractFuture.Cancellation CAUSELESS_INTERRUPTED;
            static final AbstractFuture.Cancellation CAUSELESS_CANCELLED;
            final boolean wasInterrupted;
            final Throwable cause;

            static {
                if (GENERATE_CANCELLATION_CAUSES) {
                    CAUSELESS_CANCELLED = null;
                    CAUSELESS_INTERRUPTED = null;
                } else {
                    CAUSELESS_CANCELLED = new AbstractFuture.Cancellation(false, null);
                    CAUSELESS_INTERRUPTED = new AbstractFuture.Cancellation(true, null);
                }
            }

            Cancellation(boolean wasInterrupted, Throwable cause) {
                this.wasInterrupted = wasInterrupted;
                this.cause = cause;
            }

        }

        private static final class SetFuture<V> implements Runnable {
            final AbstractFuture<V> owner;
            final ListenableFuture<? extends V> future;

            SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
                this.owner = owner;
                this.future = future;
            }

            public void run() {
                if (owner.value != this) {
                    // nothing to do, we must have been cancelled, don't bother inspecting the future.
                    return;
                }
                Object valueToSet = getFutureValue(future);
                if (ATOMIC_HELPER.casValue(owner, this, valueToSet)) {
                    complete(owner);
                }
            }

        }

        private abstract static class AtomicHelper {
            abstract void putThread(AbstractFuture.Waiter waiter, Thread newValue);

            abstract void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue);

            abstract boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update);

            abstract boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update);

            abstract boolean casValue(AbstractFuture<?> future, Object expect, Object update);

        }

        private static final class UnsafeAtomicHelper extends AbstractFuture.AtomicHelper {
            static final sun.misc.Unsafe UNSAFE;
            static final long LISTENERS_OFFSET;
            static final long WAITERS_OFFSET;
            static final long VALUE_OFFSET;
            static final long WAITER_THREAD_OFFSET;
            static final long WAITER_NEXT_OFFSET;

            static {
                sun.misc.Unsafe unsafe = null;
                try {
                    unsafe = Unsafe.sun.misc.Unsafe.getUnsafe();
                } catch (SecurityException tryReflectionInstead) {
                    try {
                        unsafe =
                                AccessController.doPrivileged(
                                        new PrivilegedExceptionAction<sun.misc.Unsafe>() {

                                            public sun.misc.Unsafe run() throws Exception {
                                                Class<sun.misc.Unsafe> k = sun.misc.Unsafe.class;
                                                for (java.lang.reflect.Field f : k.getDeclaredFields()) {
                                                    f.setAccessible(true);
                                                    Object x = f.get(null);
                                                    if (k.isInstance(x)) {
                                                        return k.cast(x);
                                                    }
                                                }
                                                throw new NoSuchFieldError("the Unsafe");
                                            }
                                        });
                    } catch (PrivilegedActionException e) {
                        throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                    }
                }
                try {
                    Class<?> abstractFuture = AbstractFuture.class;
                    WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
                    LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
                    VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
                    WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
                    WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
                    UNSAFE = unsafe;
                } catch (Exception e) {
                    Throwables.throwIfUnchecked(e);
                    throw new RuntimeException(e);
                }
            }

            void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
                UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
            }

            void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
                UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
            }

            boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
                return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
            }

            boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
                return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
            }

            boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
                return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
            }

        }

        private static final class SafeAtomicHelper extends AbstractFuture.AtomicHelper {
            final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
            final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
            final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
            final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
            final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;

            SafeAtomicHelper(
                    AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater,
                    AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater,
                    AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater,
                    AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater,
                    AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater) {
                this.waiterThreadUpdater = waiterThreadUpdater;
                this.waiterNextUpdater = waiterNextUpdater;
                this.waitersUpdater = waitersUpdater;
                this.listenersUpdater = listenersUpdater;
                this.valueUpdater = valueUpdater;
            }

            void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
                waiterThreadUpdater.lazySet(waiter, newValue);
            }

            void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
                waiterNextUpdater.lazySet(waiter, newValue);
            }

            boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
                return waitersUpdater.compareAndSet(future, expect, update);
            }

            boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
                return listenersUpdater.compareAndSet(future, expect, update);
            }

            boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
                return valueUpdater.compareAndSet(future, expect, update);
            }

        }

        private static final class SynchronizedHelper extends AbstractFuture.AtomicHelper {
            void putThread(AbstractFuture.Waiter waiter, Thread newValue) {
                waiter.thread = newValue;
            }

            void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue) {
                waiter.next = newValue;
            }

            boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update) {
                synchronized (future) {
                    if (future.waiters == expect) {
                        future.waiters = update;
                        return true;
                    }
                    return false;
                }
            }

            boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update) {
                synchronized (future) {
                    if (future.listeners == expect) {
                        future.listeners = update;
                        return true;
                    }
                    return false;
                }
            }

            boolean casValue(AbstractFuture<?> future, Object expect, Object update) {
                synchronized (future) {
                    if (future.value == expect) {
                        future.value = update;
                        return true;
                    }
                    return false;
                }
            }

        }

    }

    static // Parent source might not be available while compiling subclass

    interface ForOverride {
    }

    static final class Throwables {
        private Throwables() {
        }

        public static void throwIfUnchecked(Throwable throwable) {
            Preconditions.checkNotNull(throwable);
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            if (throwable instanceof Error) {
                throw (Error) throwable;
            }
        }

    }

    static interface LoadingCache<K, V> extends Cache<K, V>, Function<K, V> {
        V apply(K key);

    }

    static enum DirectExecutor implements Executor {
        INSTANCE,
        ;

        public void execute(Runnable command) {
            command.run();
        }

        public String toString() {
            return "MoreExecutors.directExecutor()";
        }

    }

    static abstract class AbstractCache<K, V> implements Cache<K, V> {
        protected AbstractCache() {
        }

        public interface StatsCounter {
            void recordHits(int count);

            void recordMisses(int count);

            void recordLoadSuccess(long loadTime);

            void recordLoadException(long loadTime);

            void recordEviction();

        }

    }

    static // we're overriding default serialization
    abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess {
        public static <E> ImmutableList<E> of() {
            return (ImmutableList<E>) RegularImmutableList.EMPTY;
        }

        public static <E> ImmutableList<E> of(E element) {
            return new SingletonImmutableList<E>(element);
        }

        ImmutableList() {
        }

        public UnmodifiableIterator<E> iterator() {
            return listIterator();
        }

        public UnmodifiableListIterator<E> listIterator() {
            return listIterator(0);
        }

        public UnmodifiableListIterator<E> listIterator(int index) {
            return new AbstractIndexedListIterator<E>(size(), index) {

                protected E get(int index) {
                    return ImmutableList.this.get(index);
                }
            };
        }

        public void forEach(Consumer<? super E> consumer) {
            Preconditions.checkNotNull(consumer);
            int n = size();
            for (int i = 0; i < n; i++) {
                consumer.accept(get(i));
            }
        }

        public int indexOf(Object object) {
            return (object == null) ? -1 : Lists.indexOfImpl(this, object);
        }

        public int lastIndexOf(Object object) {
            return (object == null) ? -1 : Lists.lastIndexOfImpl(this, object);
        }

        public boolean contains(Object object) {
            return indexOf(object) >= 0;
        }

        public ImmutableList<E> subList(int fromIndex, int toIndex) {
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size());
            int length = toIndex - fromIndex;
            if (length == size()) {
                return this;
            } else if (length == 0) {
                return of();
            } else if (length == 1) {
                return of(get(fromIndex));
            } else {
                return subListUnchecked(fromIndex, toIndex);
            }
        }

        ImmutableList<E> subListUnchecked(int fromIndex, int toIndex) {
            return new SubList(fromIndex, toIndex - fromIndex);
        }

        public final boolean addAll(int index, Collection<? extends E> newElements) {
            throw new UnsupportedOperationException();
        }

        public final E set(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public final void add(int index, E element) {
            throw new UnsupportedOperationException();
        }

        public final E remove(int index) {
            throw new UnsupportedOperationException();
        }

        public final void replaceAll(UnaryOperator<E> operator) {
            throw new UnsupportedOperationException();
        }

        public final void sort(Comparator<? super E> c) {
            throw new UnsupportedOperationException();
        }

        public Spliterator<E> spliterator() {
            return CollectSpliterators.indexed(size(), ImmutableCollection.SPLITERATOR_CHARACTERISTICS, this::get);
        }

        int copyIntoArray(Object[] dst, int offset) {
            // this loop is faster for RandomAccess instances, which ImmutableLists are
            int size = size();
            for (int i = 0; i < size; i++) {
                dst[offset + i] = get(i);
            }
            return offset + size;
        }

        public boolean equals(Object obj) {
            return Lists.equalsImpl(this, obj);
        }

        public int hashCode() {
            int hashCode = 1;
            int n = size();
            for (int i = 0; i < n; i++) {
                hashCode = 31 * hashCode + get(i).hashCode();

                hashCode = ~~hashCode;
                // needed to deal with GWT integer overflow
            }
            return hashCode;
        }

        class SubList extends ImmutableList<E> {
            final transient int offset;
            final transient int length;

            SubList(int offset, int length) {
                this.offset = offset;
                this.length = length;
            }

            public int size() {
                return length;
            }

            public E get(int index) {
                Preconditions.checkElementIndex(index, length);
                return ImmutableList.this.get(index + offset);
            }

            public ImmutableList<E> subList(int fromIndex, int toIndex) {
                Preconditions.checkPositionIndexes(fromIndex, toIndex, length);
                return ImmutableList.this.subList(fromIndex + offset, toIndex + offset);
            }

        }

    }

    static final class Ascii {
        private static final char CASE_MASK = 0x20;

        private Ascii() {
        }

        public static String toLowerCase(String string) {
            int length = string.length();
            for (int i = 0; i < length; i++) {
                if (isUpperCase(string.charAt(i))) {
                    char[] chars = string.toCharArray();
                    for (; i < length; i++) {
                        char c = chars[i];
                        if (isUpperCase(c)) {
                            chars[i] = (char) (c ^ CASE_MASK);
                        }
                    }
                    return String.valueOf(chars);
                }
            }
            return string;
        }

        public static boolean isUpperCase(char c) {
            return (c >= 'A') && (c <= 'Z');
        }

    }

    static class ExecutionError extends Error {
        protected ExecutionError() {
        }

        protected ExecutionError(String message) {
            super(message);
        }

        public ExecutionError(String message, Error cause) {
            super(message, cause);
        }

        public ExecutionError(Error cause) {
            super(cause);
        }

    }

    static class ImmutableEntry<K, V> extends AbstractMapEntry<K, V> implements Serializable {
        final K key;
        final V value;

        ImmutableEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

    }

    static interface ReferenceEntry<K, V> {
        ValueReference<K, V> getValueReference();

        void setValueReference(ValueReference<K, V> valueReference);

        ReferenceEntry<K, V> getNext();

        int getHash();

        K getKey();

        long getAccessTime();

        void setAccessTime(long time);

        ReferenceEntry<K, V> getNextInAccessQueue();

        void setNextInAccessQueue(ReferenceEntry<K, V> next);

        ReferenceEntry<K, V> getPreviousInAccessQueue();

        void setPreviousInAccessQueue(ReferenceEntry<K, V> previous);

        long getWriteTime();

        void setWriteTime(long time);

        ReferenceEntry<K, V> getNextInWriteQueue();

        void setNextInWriteQueue(ReferenceEntry<K, V> next);

        ReferenceEntry<K, V> getPreviousInWriteQueue();

        void setPreviousInWriteQueue(ReferenceEntry<K, V> previous);

    }

    static interface Nullable {
    }

    static abstract class Ticker {
        private static final Ticker SYSTEM_TICKER = new Ticker() {

            public long read() {
                return com_google_common_base_Platform.systemNanoTime();
            }
        };

        protected Ticker() {
        }

        // TODO(kak): Consider removing this
        public abstract long read();

        public static Ticker systemTicker() {
            return SYSTEM_TICKER;
        }

    }

    static interface GwtCompatible {
    }

    static interface CompatibleWith {
    }

    static abstract class GwtFluentFutureCatchingSpecialization<V> extends AbstractFuture<V> {
    }

    static final class Strings {
        private Strings() {
        }

        public static String lenientFormat(
                String template, Object... args) {
            template = String.valueOf(template); // null -> "null"

            if (args == null) {
                args = new Object[]{"(Object[])null"};
            } else {
                for (int i = 0; i < args.length; i++) {
                    args[i] = lenientToString(args[i]);
                }
            }

            // start substituting the arguments into the '%s' placeholders
            StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
            int templateStart = 0;
            int i = 0;
            while (i < args.length) {
                int placeholderStart = template.indexOf("%s", templateStart);
                if (placeholderStart == -1) {
                    break;
                }
                builder.append(template, templateStart, placeholderStart);
                builder.append(args[i++]);
                templateStart = placeholderStart + 2;
            }
            builder.append(template, templateStart, template.length());

            // if we run out of placeholders, append the extra args in square braces
            if (i < args.length) {
                builder.append(" [");
                builder.append(args[i++]);
                while (i < args.length) {
                    builder.append(", ");
                    builder.append(args[i++]);
                }
                builder.append(']');
            }

            return builder.toString();
        }

        private static String lenientToString(Object o) {
            try {
                return String.valueOf(o);
            } catch (Exception e) {
                // Default toString() behavior - see Object.toString()
                String objectToString =
                        o.getClass().getName() + '@' + Integer.toHexString(System.identityHashCode(o));
                // Logger is created inline with fixed name to avoid forcing Proguard to create another class.
                Logger.getLogger("com.google.common.base.Strings")
                        .log(Level.WARNING, "Exception during lenientFormat for " + objectToString, e);
                return "<" + objectToString + " threw " + e.getClass().getName() + ">";
            }
        }

    }

    static final class CacheBuilder<K, V> {
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
        private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
        private static final int DEFAULT_EXPIRATION_NANOS = 0;
        private static final int DEFAULT_REFRESH_NANOS = 0;
        static final Supplier<? extends StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(
                new StatsCounter() {

                    public void recordHits(int count) {
                    }


                    public void recordMisses(int count) {
                    }


                    public void recordLoadSuccess(long loadTime) {
                    }


                    public void recordLoadException(long loadTime) {
                    }


                    public void recordEviction() {
                    }


                    public CacheStats snapshot() {
                        return EMPTY_STATS;
                    }
                });
        static final CacheStats EMPTY_STATS = new CacheStats(0, 0, 0, 0, 0, 0);
        static final Ticker NULL_TICKER = new Ticker() {

            public long read() {
                return 0;
            }
        };
        private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
        static final int UNSET_INT = -1;
        boolean strictParsing = true;
        int initialCapacity = UNSET_INT;
        int concurrencyLevel = UNSET_INT;
        long maximumSize = UNSET_INT;
        long maximumWeight = UNSET_INT;
        Weigher<? super K, ? super V> weigher;
        Strength keyStrength;
        Strength valueStrength;
        long expireAfterWriteNanos = UNSET_INT;
        long expireAfterAccessNanos = UNSET_INT;
        long refreshNanos = UNSET_INT;
        Equivalence<Object> keyEquivalence;
        Equivalence<Object> valueEquivalence;
        RemovalListener<? super K, ? super V> removalListener;
        Ticker ticker;
        Supplier<? extends StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;

        private CacheBuilder() {
        }

        public static CacheBuilder<Object, Object> newBuilder() {
            return new CacheBuilder<>();
        }

        Equivalence<Object> getKeyEquivalence() {
            return MoreObjects.firstNonNull(keyEquivalence, getKeyStrength().defaultEquivalence());
        }

        Equivalence<Object> getValueEquivalence() {
            return MoreObjects.firstNonNull(valueEquivalence, getValueStrength().defaultEquivalence());
        }

        int getInitialCapacity() {
            return (initialCapacity == UNSET_INT) ? DEFAULT_INITIAL_CAPACITY : initialCapacity;
        }

        int getConcurrencyLevel() {
            return (concurrencyLevel == UNSET_INT) ? DEFAULT_CONCURRENCY_LEVEL : concurrencyLevel;
        }

        long getMaximumWeight() {
            if (expireAfterWriteNanos == 0 || expireAfterAccessNanos == 0) {
                return 0;
            }
            return (weigher == null) ? maximumSize : maximumWeight;
        }

        <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher() {
            return (Weigher<K1, V1>) MoreObjects.firstNonNull(weigher, CacheBuilder.OneWeigher.INSTANCE);
        }

        Strength getKeyStrength() {
            return MoreObjects.firstNonNull(keyStrength, LocalCache.Strength.STRONG);
        }

        Strength getValueStrength() {
            return MoreObjects.firstNonNull(valueStrength, LocalCache.Strength.STRONG);
        }

        long getExpireAfterWriteNanos() {
            return (expireAfterWriteNanos == UNSET_INT) ? DEFAULT_EXPIRATION_NANOS : expireAfterWriteNanos;
        }

        long getExpireAfterAccessNanos() {
            return (expireAfterAccessNanos == UNSET_INT)
                    ? DEFAULT_EXPIRATION_NANOS
                    : expireAfterAccessNanos;
        }

        long getRefreshNanos() {
            return (refreshNanos == UNSET_INT) ? DEFAULT_REFRESH_NANOS : refreshNanos;
        }

        Ticker getTicker(boolean recordsTime) {
            if (ticker != null) {
                return ticker;
            }
            return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
        }

        <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener() {
            return (RemovalListener<K1, V1>)
                    MoreObjects.firstNonNull(removalListener, CacheBuilder.NullListener.INSTANCE);
        }

        Supplier<? extends StatsCounter> getStatsCounterSupplier() {
            return statsCounterSupplier;
        }

        public <K1 extends K, V1 extends V> Cache<K1, V1> build() {
            checkWeightWithWeigher();
            checkNonLoadingCache();
            return new LocalCache.LocalManualCache<>(this);
        }

        private void checkNonLoadingCache() {
            Preconditions.checkState(refreshNanos == UNSET_INT, "refreshAfterWrite requires a LoadingCache");
        }

        private void checkWeightWithWeigher() {
            if (weigher == null) {
                Preconditions.checkState(maximumWeight == UNSET_INT, "maximumWeight requires weigher");
            } else {
                if (strictParsing) {
                    Preconditions.checkState(maximumWeight != UNSET_INT, "weigher requires maximumWeight");
                } else {
                    if (maximumWeight == UNSET_INT) {
                        logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
                    }
                }
            }
        }

        public String toString() {
            MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
            if (initialCapacity != UNSET_INT) {
                s.add("initialCapacity", initialCapacity);
            }
            if (concurrencyLevel != UNSET_INT) {
                s.add("concurrencyLevel", concurrencyLevel);
            }
            if (maximumSize != UNSET_INT) {
                s.add("maximumSize", maximumSize);
            }
            if (maximumWeight != UNSET_INT) {
                s.add("maximumWeight", maximumWeight);
            }
            if (expireAfterWriteNanos != UNSET_INT) {
                s.add("expireAfterWrite", expireAfterWriteNanos + "ns");
            }
            if (expireAfterAccessNanos != UNSET_INT) {
                s.add("expireAfterAccess", expireAfterAccessNanos + "ns");
            }
            if (keyStrength != null) {
                s.add("keyStrength", Ascii.toLowerCase(keyStrength.toString()));
            }
            if (valueStrength != null) {
                s.add("valueStrength", Ascii.toLowerCase(valueStrength.toString()));
            }
            if (keyEquivalence != null) {
                s.addValue("keyEquivalence");
            }
            if (valueEquivalence != null) {
                s.addValue("valueEquivalence");
            }
            if (removalListener != null) {
                s.addValue("removalListener");
            }
            return s.toString();
        }

        enum NullListener implements RemovalListener<Object, Object> {
            INSTANCE,
            ;

            public void onRemoval(RemovalNotification<Object, Object> notification) {
            }

        }

        enum OneWeigher implements Weigher<Object, Object> {
            INSTANCE,
            ;

            public int weigh(Object key, Object value) {
                return 1;
            }

        }

    }

    static final class MoreObjects {
        public static <T> T firstNonNull(T first, T second) {
            if (first != null) {
                return first;
            }
            if (second != null) {
                return second;
            }
            throw new NullPointerException("Both parameters are null");
        }

        public static MoreObjects.ToStringHelper toStringHelper(Object self) {
            return new MoreObjects.ToStringHelper(self.getClass().getSimpleName());
        }

        private MoreObjects() {
        }

        public static final class ToStringHelper {
            private final String className;
            private final MoreObjects.ToStringHelper.ValueHolder holderHead = new MoreObjects.ToStringHelper.ValueHolder();
            private MoreObjects.ToStringHelper.ValueHolder holderTail = holderHead;
            private boolean omitNullValues = false;

            private ToStringHelper(String className) {
                this.className = Preconditions.checkNotNull(className);
            }

            public MoreObjects.ToStringHelper add(String name, Object value) {
                return addHolder(name, value);
            }

            public MoreObjects.ToStringHelper add(String name, int value) {
                return addHolder(name, String.valueOf(value));
            }

            public MoreObjects.ToStringHelper add(String name, long value) {
                return addHolder(name, String.valueOf(value));
            }

            public MoreObjects.ToStringHelper addValue(Object value) {
                return addHolder(value);
            }

            public String toString() {
                // create a copy to keep it consistent in case value changes
                boolean omitNullValuesSnapshot = omitNullValues;
                String nextSeparator = "";
                StringBuilder builder = new StringBuilder(32).append(className).append('{');
                for (MoreObjects.ToStringHelper.ValueHolder valueHolder = holderHead.next;
                     valueHolder != null;
                     valueHolder = valueHolder.next) {
                    Object value = valueHolder.value;
                    if (!omitNullValuesSnapshot || value != null) {
                        builder.append(nextSeparator);
                        nextSeparator = ", ";

                        if (valueHolder.name != null) {
                            builder.append(valueHolder.name).append('=');
                        }
                        if (value != null && value.getClass().isArray()) {
                            Object[] objectArray = {value};
                            String arrayString = Arrays.deepToString(objectArray);
                            builder.append(arrayString, 1, arrayString.length() - 1);
                        } else {
                            builder.append(value);
                        }
                    }
                }
                return builder.append('}').toString();
            }

            private MoreObjects.ToStringHelper.ValueHolder addHolder() {
                MoreObjects.ToStringHelper.ValueHolder valueHolder = new MoreObjects.ToStringHelper.ValueHolder();
                holderTail = holderTail.next = valueHolder;
                return valueHolder;
            }

            private MoreObjects.ToStringHelper addHolder(Object value) {
                MoreObjects.ToStringHelper.ValueHolder valueHolder = addHolder();
                valueHolder.value = value;
                return this;
            }

            private MoreObjects.ToStringHelper addHolder(String name, Object value) {
                MoreObjects.ToStringHelper.ValueHolder valueHolder = addHolder();
                valueHolder.value = value;
                valueHolder.name = Preconditions.checkNotNull(name);
                return this;
            }

            private static final class ValueHolder {
                String name;
                Object value;
                MoreObjects.ToStringHelper.ValueHolder next;

            }

        }

    }

    static final class com_google_common_base_Objects extends ExtraObjectsMethodsForWeb {
        private com_google_common_base_Objects() {
        }

        public static boolean equal(Object a, Object b) {
            return a == b || (a != null && a.equals(b));
        }

        public static int hashCode(Object... objects) {
            return Arrays.hashCode(objects);
        }

    }

    static interface GwtIncompatible {
    }

    static // we're overriding default serialization
    // TODO(kevinb): I think we should push everything down to "BaseImmutableCollection" or something,
    // just to do everything we can to emphasize the "practically an interface" nature of this class.
    abstract class ImmutableCollection<E> extends AbstractCollection<E> implements Serializable {
        static final int SPLITERATOR_CHARACTERISTICS = Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED;
        private static final Object[] EMPTY_ARRAY = {};

        ImmutableCollection() {
        }

        public abstract UnmodifiableIterator<E> iterator();

        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(this, SPLITERATOR_CHARACTERISTICS);
        }

        public final Object[] toArray() {
            return toArray(EMPTY_ARRAY);
        }

        public final <T> T[] toArray(T[] other) {
            Preconditions.checkNotNull(other);
            int size = size();

            if (other.length < size) {
                Object[] internal = internalArray();
                if (internal != null) {
                    return com_google_common_collect_Platform.copy(internal, internalArrayStart(), internalArrayEnd(), other);
                }
                other = ObjectArrays.newArray(other, size);
            } else if (other.length > size) {
                other[size] = null;
            }
            copyIntoArray(other, 0);
            return other;
        }

        Object[] internalArray() {
            return null;
        }

        int internalArrayStart() {
            throw new UnsupportedOperationException();
        }

        int internalArrayEnd() {
            throw new UnsupportedOperationException();
        }

        public abstract boolean contains(Object object);

        public final boolean add(E e) {
            throw new UnsupportedOperationException();
        }

        public final boolean remove(Object object) {
            throw new UnsupportedOperationException();
        }

        public final boolean addAll(Collection<? extends E> newElements) {
            throw new UnsupportedOperationException();
        }

        public final boolean removeAll(Collection<?> oldElements) {
            throw new UnsupportedOperationException();
        }

        public final boolean removeIf(Predicate<? super E> filter) {
            throw new UnsupportedOperationException();
        }

        public final boolean retainAll(Collection<?> elementsToKeep) {
            throw new UnsupportedOperationException();
        }

        public final void clear() {
            throw new UnsupportedOperationException();
        }

        int copyIntoArray(Object[] dst, int offset) {
            for (E e : this) {
                dst[offset++] = e;
            }
            return offset;
        }

    }

    static interface Weigher<K, V> {
        int weigh(K key, V value);

    }

    static abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T> {
        private T nextOrNull;

        protected AbstractSequentialIterator(T firstOrNull) {
            this.nextOrNull = firstOrNull;
        }

        protected abstract T computeNext(T previous);

        public final boolean hasNext() {
            return nextOrNull != null;
        }

        public final T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                return nextOrNull;
            } finally {
                nextOrNull = computeNext(nextOrNull);
            }
        }

    }

    static final class com_google_common_collect_Platform {
        static <T> T[] newArray(T[] reference, int length) {
            Class<?> type = reference.getClass().getComponentType();

            // the cast is safe because
            // result.getClass() == reference.getClass().getComponentType()

            T[] result = (T[]) Array.newInstance(type, length);
            return result;
        }

        static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
            return Arrays.copyOfRange(source, from, to, (Class<? extends T[]>) arrayOfType.getClass());
        }

        private com_google_common_collect_Platform() {
        }

    }

    static final class Stopwatch {
        private final Ticker ticker;
        private boolean isRunning;
        private long elapsedNanos;
        private long startTick;

        public static Stopwatch createUnstarted() {
            return new Stopwatch();
        }

        Stopwatch() {
            this.ticker = Ticker.systemTicker();
        }

        Stopwatch(Ticker ticker) {
            this.ticker = Preconditions.checkNotNull(ticker, "ticker");
        }

        public Stopwatch start() {
            Preconditions.checkState(!isRunning, "This stopwatch is already running.");
            isRunning = true;
            startTick = ticker.read();
            return this;
        }

        private long elapsedNanos() {
            return isRunning ? ticker.read() - startTick + elapsedNanos : elapsedNanos;
        }

        public long elapsed(TimeUnit desiredUnit) {
            return desiredUnit.convert(elapsedNanos(), TimeUnit.NANOSECONDS);
        }

        public String toString() {
            long nanos = elapsedNanos();

            TimeUnit unit = chooseUnit(nanos);
            double value = (double) nanos / TimeUnit.NANOSECONDS.convert(1, unit);

            // Too bad this functionality is not exposed as a regular method call
            return com_google_common_base_Platform.formatCompact4Digits(value) + " " + abbreviate(unit);
        }

        private static TimeUnit chooseUnit(long nanos) {
            if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.DAYS;
            }
            if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.HOURS;
            }
            if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.MINUTES;
            }
            if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.SECONDS;
            }
            if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.MILLISECONDS;
            }
            if (TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0) {
                return TimeUnit.MICROSECONDS;
            }
            return TimeUnit.NANOSECONDS;
        }

        private static String abbreviate(TimeUnit unit) {
            switch (unit) {
                case NANOSECONDS:
                    return "ns";
                case MICROSECONDS:
                    return "\u03bcs"; // μs
                case MILLISECONDS:
                    return "ms";
                case SECONDS:
                    return "s";
                case MINUTES:
                    return "min";
                case HOURS:
                    return "h";
                case DAYS:
                    return "d";
                default:
                    throw new AssertionError();
            }
        }

    }

    static interface VisibleForTesting {
    }

    static abstract class CacheLoader<K, V> {
        protected CacheLoader() {
        }

        public abstract V load(K key) throws Exception;

        // Futures
        public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
            Preconditions.checkNotNull(key);
            Preconditions.checkNotNull(oldValue);
            return Futures.immediateFuture(load(key));
        }

        public static final class InvalidCacheLoadException extends RuntimeException {
            public InvalidCacheLoadException(String message) {
                super(message);
            }

        }

    }

    static class FastOutput implements AutoCloseable, Closeable {
        private StringBuilder cache = new StringBuilder(10 << 20);
        private final Writer os;

        public FastOutput(Writer os) {
            this.os = os;
        }

        public FastOutput(OutputStream os) {
            this(new OutputStreamWriter(os));
        }

        public FastOutput flush() {
            try {
                os.append(cache);
                os.flush();
                cache.setLength(0);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
            return this;
        }

        public void close() {
            flush();
            try {
                os.close();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        public String toString() {
            return cache.toString();
        }

    }

    static final class Iterators {
        private Iterators() {
        }

        static <T> UnmodifiableListIterator<T> emptyListIterator() {
            return (UnmodifiableListIterator<T>) Iterators.ArrayItr.EMPTY;
        }

        public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2) {
            while (iterator1.hasNext()) {
                if (!iterator2.hasNext()) {
                    return false;
                }
                Object o1 = iterator1.next();
                Object o2 = iterator2.next();
                if (!com_google_common_base_Objects.equal(o1, o2)) {
                    return false;
                }
            }
            return !iterator2.hasNext();
        }

        public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator) {
            Preconditions.checkNotNull(addTo);
            Preconditions.checkNotNull(iterator);
            boolean wasModified = false;
            while (iterator.hasNext()) {
                wasModified |= addTo.add(iterator.next());
            }
            return wasModified;
        }

        public static <T> UnmodifiableIterator<T> forArray(final T... array) {
            return forArray(array, 0, array.length, 0);
        }

        static <T> UnmodifiableListIterator<T> forArray(
                final T[] array, final int offset, int length, int index) {
            Preconditions.checkArgument(length >= 0);
            int end = offset + length;

            // Technically we should give a slightly more descriptive error on overflow
            Preconditions.checkPositionIndexes(offset, end, array.length);
            Preconditions.checkPositionIndex(index, length);
            if (length == 0) {
                return emptyListIterator();
            }
            return new Iterators.ArrayItr<T>(array, offset, length, index);
        }

        public static <T> UnmodifiableIterator<T> singletonIterator(final T value) {
            return new UnmodifiableIterator<T>() {
                boolean done;


                public boolean hasNext() {
                    return !done;
                }


                public T next() {
                    if (done) {
                        throw new NoSuchElementException();
                    }
                    done = true;
                    return value;
                }
            };
        }

        private static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
            static final UnmodifiableListIterator<Object> EMPTY = new Iterators.ArrayItr<>(new Object[0], 0, 0, 0);
            private final T[] array;
            private final int offset;

            ArrayItr(T[] array, int offset, int length, int index) {
                super(length, index);
                this.array = array;
                this.offset = offset;
            }

            protected T get(int index) {
                return array[offset + index];
            }

        }

    }

    static interface CanIgnoreReturnValue {
    }

    static interface com_google_common_base_Supplier<T> extends java.util.function.Supplier<T> {
        T get();

    }

    static final class com_google_common_base_Platform {
        private com_google_common_base_Platform() {
        }

        static long systemNanoTime() {
            return System.nanoTime();
        }

        static String formatCompact4Digits(double value) {
            return String.format(Locale.ROOT, "%.4g", value);
        }

    }

    static interface MonotonicNonNull {
    }

    static class RegularImmutableList<E> extends ImmutableList<E> {
        static final ImmutableList<Object> EMPTY = new RegularImmutableList<>(new Object[0]);
        final transient Object[] array;

        RegularImmutableList(Object[] array) {
            this.array = array;
        }

        public int size() {
            return array.length;
        }

        Object[] internalArray() {
            return array;
        }

        int internalArrayStart() {
            return 0;
        }

        int internalArrayEnd() {
            return array.length;
        }

        int copyIntoArray(Object[] dst, int dstOff) {
            System.arraycopy(array, 0, dst, dstOff, array.length);
            return dstOff + array.length;
        }

        public E get(int index) {
            return (E) array[index];
        }

        public UnmodifiableListIterator<E> listIterator(int index) {
            // for performance
            // The fake cast to E is safe because the creation methods only allow E's
            return (UnmodifiableListIterator<E>) Iterators.forArray(array, 0, array.length, index);
        }

        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(array, ImmutableCollection.SPLITERATOR_CHARACTERISTICS);
        }

    }

    static abstract class AbstractTransformFuture<I, O, F, T> extends FluentFuture.TrustedFuture<O> implements Runnable {
        ListenableFuture<? extends I> inputFuture;
        F function;

        static <I, O> ListenableFuture<O> create(
                ListenableFuture<I> input, Function<? super I, ? extends O> function, Executor executor) {
            Preconditions.checkNotNull(function);
            AbstractTransformFuture.TransformFuture<I, O> output = new AbstractTransformFuture.TransformFuture<>(input, function);
            input.addListener(output, MoreExecutors.rejectionPropagatingExecutor(executor, output));
            return output;
        }

        AbstractTransformFuture(ListenableFuture<? extends I> inputFuture, F function) {
            this.inputFuture = Preconditions.checkNotNull(inputFuture);
            this.function = Preconditions.checkNotNull(function);
        }

        public final void run() {
            ListenableFuture<? extends I> localInputFuture = inputFuture;
            F localFunction = function;
            if (isCancelled() | localInputFuture == null | localFunction == null) {
                return;
            }
            inputFuture = null;

            if (localInputFuture.isCancelled()) {

                boolean unused =
                        setFuture((ListenableFuture<O>) localInputFuture); // Respects cancellation cause setting
                return;
            }

            /*
             * Any of the setException() calls below can fail if the output Future is cancelled between now
             * and then. This means that we're silently swallowing an exception -- maybe even an Error. But
             * this is no worse than what FutureTask does in that situation. Additionally, because the
             * Future was cancelled, its listeners have been run, so its consumers will not hang.
             *
             * Contrast this to the situation we have if setResult() throws, a situation described below.
             */
            I sourceResult;
            try {
                sourceResult = Futures.getDone(localInputFuture);
            } catch (CancellationException e) {
                // TODO(user): verify future behavior - unify logic with getFutureValue in AbstractFuture. This
                // code should be unreachable with correctly implemented Futures.
                // Cancel this future and return.
                // At this point, inputFuture is cancelled and outputFuture doesn't exist, so the value of
                // mayInterruptIfRunning is irrelevant.
                cancel(false);
                return;
            } catch (ExecutionException e) {
                // Set the cause of the exception as this future's exception.
                setException(e.getCause());
                return;
            } catch (RuntimeException e) {
                // Bug in inputFuture.get(). Propagate to the output Future so that its consumers don't hang.
                setException(e);
                return;
            } catch (Error e) {
                /*
                 * StackOverflowError, OutOfMemoryError (e.g., from allocating ExecutionException), or
                 * something. Try to treat it like a RuntimeException. If we overflow the stack again, the
                 * resulting Error will propagate upward up to the root call to set().
                 */
                setException(e);
                return;
            }

            T transformResult;
            try {
                transformResult = doTransform(localFunction, sourceResult);
            } catch (Throwable t) {
                // This exception is irrelevant in this thread, but useful for the client.
                setException(t);
                return;
            } finally {
                function = null;
            }

            /*
             * If set()/setValue() throws an Error, we let it propagate. Why? The most likely Error is a
             * StackOverflowError (from deep transform(..., directExecutor()) nesting), and calling
             * setException(stackOverflowError) would fail:
             *
             * - If the stack overflowed before set()/setValue() could even store the result in the output
             * Future, then a call setException() would likely also overflow.
             *
             * - If the stack overflowed after set()/setValue() stored its result, then a call to
             * setException() will be a no-op because the Future is already done.
             *
             * Both scenarios are bad: The output Future might never complete, or, if it does complete, it
             * might not run some of its listeners. The likely result is that the app will hang. (And of
             * course stack overflows are bad news in general. For example, we may have overflowed in the
             * middle of defining a class. If so, that class will never be loadable in this process.) The
             * best we can do (since logging may overflow the stack) is to let the error propagate. Because
             * it is an Error, it won't be caught and logged by AbstractFuture.executeListener. Instead, it
             * can propagate through many layers of AbstractTransformFuture up to the root call to set().
             *
             * https://github.com/google/guava/issues/2254
             *
             * Other kinds of Errors are possible:
             *
             * - OutOfMemoryError from allocations in setFuture(): The calculus here is similar to
             * StackOverflowError: We can't reliably call setException(error).
             *
             * - Any kind of Error from a listener. Even if we could distinguish that case (by exposing some
             * extra state from AbstractFuture), our options are limited: A call to setException() would be
             * a no-op. We could log, but if that's what we really want, we should modify
             * AbstractFuture.executeListener to do so, since that method would have the ability to continue
             * to execute other listeners.
             *
             * What about RuntimeException? If there is a bug in set()/setValue() that produces one, it will
             * propagate, too, but only as far as AbstractFuture.executeListener, which will catch and log
             * it.
             */
            setResult(transformResult);
        }

        abstract T doTransform(F function, I result) throws Exception;

        abstract void setResult(T result);

        protected final void afterDone() {
            maybePropagateCancellationTo(inputFuture);
            this.inputFuture = null;
            this.function = null;
        }

        protected String pendingToString() {
            ListenableFuture<? extends I> localInputFuture = inputFuture;
            F localFunction = function;
            String superString = super.pendingToString();
            String resultString = "";
            if (localInputFuture != null) {
                resultString = "inputFuture=[" + localInputFuture + "], ";
            }
            if (localFunction != null) {
                return resultString + "function=[" + localFunction + "]";
            } else if (superString != null) {
                return resultString + superString;
            }
            return null;
        }

        private static final class TransformFuture<I, O> extends AbstractTransformFuture<I, O, Function<? super I, ? extends O>, O> {
            TransformFuture(
                    ListenableFuture<? extends I> inputFuture, Function<? super I, ? extends O> function) {
                super(inputFuture, function);
            }

            O doTransform(Function<? super I, ? extends O> function, I input) {
                return function.apply(input);
            }

            void setResult(O result) {
                set(result);
            }

        }

    }

    static abstract class ImmediateFuture<V> implements ListenableFuture<V> {
        private static final Logger log = Logger.getLogger(ImmediateFuture.class.getName());

        public void addListener(Runnable listener, Executor executor) {
            Preconditions.checkNotNull(listener, "Runnable was null.");
            Preconditions.checkNotNull(executor, "Executor was null.");
            try {
                executor.execute(listener);
            } catch (RuntimeException e) {
                // ListenableFuture's contract is that it will not throw unchecked exceptions, so log the bad
                // runnable and/or executor and swallow it.
                log.log(
                        Level.SEVERE,
                        "RuntimeException while executing runnable " + listener + " with executor " + executor,
                        e);
            }
        }

        public boolean cancel(boolean mayInterruptIfRunning) {
            return false;
        }

        public abstract V get() throws ExecutionException;

        public V get(long timeout, TimeUnit unit) throws ExecutionException {
            Preconditions.checkNotNull(unit);
            return get();
        }

        public boolean isCancelled() {
            return false;
        }

        public boolean isDone() {
            return true;
        }

        static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V> {
            static final ImmediateSuccessfulFuture<Object> NULL = new ImmediateSuccessfulFuture<>(null);
            private final V value;

            ImmediateSuccessfulFuture(V value) {
                this.value = value;
            }

            public V get() {
                return value;
            }

            public String toString() {
                // Behaviour analogous to AbstractFuture#toString().
                return super.toString() + "[status=SUCCESS, result=[" + value + "]]";
            }

        }

        static final class ImmediateFailedFuture<V> extends TrustedFuture<V> {
            ImmediateFailedFuture(Throwable thrown) {
                setException(thrown);
            }

        }

    }

    static final class RemovalNotification<K, V> extends SimpleImmutableEntry<K, V> {
        private final RemovalCause cause;

        public static <K, V> RemovalNotification<K, V> create(
                K key, V value, RemovalCause cause) {
            return new RemovalNotification(key, value, cause);
        }

        private RemovalNotification(K key, V value, RemovalCause cause) {
            super(key, value);
            this.cause = Preconditions.checkNotNull(cause);
        }

    }
}

