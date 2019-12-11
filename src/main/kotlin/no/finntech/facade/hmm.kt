package no.finntech.facade

import java.io.File
import java.nio.ByteBuffer
import java.util.ArrayDeque
import kotlin.math.*
import java.security.SecureRandom

private val input by lazy {
    val classLoader: ClassLoader = Day1::class.java.classLoader
    File(classLoader.getResource("day10.txt").file).readText()
}

fun main() {
    val A = mutableListOf<Vec2>()
    input.lineSequence().forEachIndexed { y, ln ->
        ln.forEachIndexed { x, c ->
            if(c == '#') A.add(Vec2(x, y))
        }
    }

    val (best, ans1) = A.map { a ->
        val B = HashSet<Vec2>()
        for(b in A) {
            if(a != b) B.add((b-a).normalize())
        }
        a to B.size
    }.maxBy { it.second }!!

    println("Part 1: $ans1")

    val vmap = HashMap<Vec2, MutableList<Vec2>>().memoize { mutableListOf() }

    for(a in A) {
        if(a == best) continue
        vmap[(a - best).normalize()].add(a)
    }

    for(l in vmap.values) {
        l.sortBy { best.manDist(it) }
    }

//    val V0 = vmap.entries.sortedBy {
//        val (x, y) = it.key
//        -atan2(x.toDouble(), y.toDouble())
//    }

    val V = vmap.entries.sortedWith(Comparator { a, b -> compareAngle(a.key, b.key) })

//    test(V)
//    require(V0 == V)

    val seq = sequence {
        val Vi = ArrayDeque<ListIterator<Vec2>>()
        for((_, l) in V) {
            Vi.add(l.listIterator())
        }

        while(Vi.isNotEmpty()) {
            val li = Vi.remove()
            yield(li.next())
            if(li.hasNext()) Vi.add(li)
        }
    }

    val ans2 = seq.elementAt(199).let { (x, y) -> x * 100 + y }
    println("Part 2: $ans2")
}

fun Vec2.normalize() = gcd(x, y).let { Vec2(x/it, y/it) }

fun compareAngle(a: Vec2, b: Vec2): Int {
    (a.x < 0).compareTo(b.x < 0).let { if(it != 0) return it }
    (b cross a).sign.let { if(it != 0) return it }
    return a.y.sign.compareTo(b.y.sign)
}

private fun test(V: List<Map.Entry<Vec2, List<Vec2>>>) {
    for(i in 0 until V.lastIndex) {
        require(compareAngle(V[i].key, V[i].key) == 0)
        for(j in i+1..V.lastIndex) {
            require(compareAngle(V[i].key, V[j].key) == -1)
            require(compareAngle(V[j].key, V[i].key) == 1)
        }
    }

    println("Test passed.")
}
@Suppress("EqualsOrHashCode")
data class Vec2(val x: Int, val y: Int) {
    companion object {
        val ORIGIN = Vec2(0, 0)
    }

    // Manhattan distance
    fun manDist(other: Vec2) = abs(x - other.x) + abs(y - other.y)
    fun manDist() = abs(x) + abs(y)

    operator fun plus(dir: Dir2) = when(dir) {
        Dir2.Right -> Vec2(x+1, y)
        Dir2.Down -> Vec2(x, y+1)
        Dir2.Left -> Vec2(x-1, y)
        Dir2.Up -> Vec2(x, y-1)
    }

    operator fun plus(other: Vec2) = Vec2(x + other.x, y + other.y)
    operator fun minus(other: Vec2) = Vec2(x - other.x, y - other.y)
    operator fun times(scale: Int) = Vec2(x * scale, y * scale)

    fun opposite() = Vec2(-x, -y)
    inline operator fun unaryMinus() = opposite()

    // cross product
    infix fun cross(b: Vec2) = x.toLong() * b.y - y.toLong() * b.x

    override fun hashCode(): Int = x.bitConcat(y).hash().toInt()
}

enum class Dir2 { Right, Down, Left, Up;
    companion object {
        inline val East get() = Right
        inline val South get() = Down
        inline val West get() = Left
        inline val North get() = Up

        val values = values().asList()

        fun fromChar(char: Char) = when(char) {
            in "RrEe" -> Right
            in "DdSs" -> Down
            in "LlWw" -> Left
            in "UuNn" -> Up
            else -> error("Unrecognized direction")
        }
    }

    fun right() = values[(ordinal + 1) % 4]
    inline operator fun inc() = right()

    fun left() = values[(ordinal + 3) % 4]
    inline operator fun dec() = left()

    fun opposite() = values[(ordinal + 2) % 4]
    inline operator fun unaryMinus() = opposite()
}

interface NonNullMap<K, V: Any>: Map<K, V> {
    override fun get(key: K): V
}

interface NonNullMutableMap<K, V: Any>: MutableMap<K, V>, NonNullMap<K, V> {
    operator fun set(key: K, value: V) { put(key, value) }
}

fun <K, V : Any> Map<K, V>.default(defaultValue: (K) -> V): NonNullMap<K, V> {
    val map = this
    return object : NonNullMap<K, V>, Map<K, V> by map {
        override fun get(key: K): V = map[key] ?: defaultValue(key)
    }
}

fun <K, V : Any> Map<K, V>.default(defaultValue: V): NonNullMap<K, V> {
    val map = this
    return object : NonNullMap<K, V>, Map<K, V> by map {
        override fun get(key: K): V = map[key] ?: defaultValue
    }
}

fun <K, V : Any> Map<K, V>.nonNull(): NonNullMap<K, V> {
    val map = this
    return object : NonNullMap<K, V>, Map<K, V> by map {
        override fun get(key: K): V = map.getValue(key)
    }
}

@JvmName("nonNullMutable")
fun <K, V : Any> MutableMap<K, V>.nonNull(): NonNullMutableMap<K, V> {
    val map = this
    return object : NonNullMutableMap<K, V>, MutableMap<K, V> by map {
        override fun get(key: K): V = map.getValue(key)
    }
}

@JvmName("defaultMutable")
fun <K, V: Any> MutableMap<K, V>.default(defaultValue: (K) -> V): NonNullMutableMap<K, V> {
    val map = this
    return object : NonNullMutableMap<K, V>, MutableMap<K, V> by map {
        override fun get(key: K): V = map[key] ?: defaultValue(key)
    }
}

@JvmName("defaultMutable")
fun <K, V: Any> MutableMap<K, V>.default(defaultValue: V): NonNullMutableMap<K, V> {
    val map = this
    return object : NonNullMutableMap<K, V>, MutableMap<K, V> by map {
        override fun get(key: K): V = map[key] ?: defaultValue
        override fun put(key: K, value: V): V? = if(value == defaultValue) remove(key) else map.put(key, value)
    }
}

fun <K, V: Any> MutableMap<K, V>.memoize(defaultValue: (K) -> V): NonNullMap<K, V> {
    val map = this
    return object : NonNullMap<K, V>, Map<K, V> by map {
        override fun get(key: K): V = map.getOrPut(key) { defaultValue(key) }
    }
}
fun Int.bitConcat(other: Int) = toLong().shl(32) or other.toLong().and(0xffff_ffff)
tailrec fun gcd(a: Int, b: Int): Int = if(a == 0) abs(b) else gcd(b % a, a)


fun splitmix64(seed: Long): Long {
    var x = seed - 7046029254386353131
    x = (x xor (x ushr 30)) * -4658895280553007687
    x = (x xor (x ushr 27)) * -7723592293110705685
    return (x xor (x ushr 31))
}
val secureRandom = SecureRandom()
val _seed1 = ByteBuffer.wrap(secureRandom.generateSeed(8)).long
fun Long.hash() = splitmix64(_seed1 xor this)

private inline infix fun Int.rol(dist: Int) = shl(dist) or ushr(-dist)
val sipHasher by lazy { HalfSipHash() }
class HalfSipHash(val k0: Int = _seed1.toInt(), val k1: Int = _seed1.shr(32).toInt()) {
    private var v0 = 0
    private var v1 = 0
    private var v2 = 0
    private var v3 = 0

    fun init() {
        v0 = k0; v1 = k1; v2 = 0x6c796765 xor k0; v3 = 0x74656462 xor k1
    }

    private fun round() {
        v0 += v1; v1 = v1 rol 5; v1 = v1 xor v0; v0 = v0 rol 16; v2 += v3; v3 = v3 rol 8; v3 = v3 xor v2
        v0 += v3; v3 = v3 rol 7; v3 = v3 xor v0; v2 += v1; v1 = v1 rol 13; v1 = v1 xor v2; v2 = v2 rol 16
    }

    fun acc(m: Int) {
        v3 = v3 xor m
        round()
        v0 = v0 xor m
    }

    fun acc(m: Long) {
        acc(m.toInt())
        acc(m.shr(32).toInt())
    }

    private inline fun ByteArray.getOrFF(i: Int) = if(i < size) get(i).toInt() and 0xff else 0xff

    fun acc(input: String) {
        val bytes = input.toByteArray()
        val len = bytes.size
        for (i in 0 until len step 4) {
            val int = (bytes[i].toInt() shl 24
                    or (bytes.getOrFF(i+1) shl 16)
                    or (bytes.getOrFF(i+2) shl 8)
                    or bytes.getOrFF(i+3))
            acc(int)
        }
        comma()
    }

    fun comma() {
        v1 = v1 xor 0xff
        round()
    }

    fun finish(): Int {
        v2 = v2 xor 0xee
        round(); round(); round()
        return v1 xor v3
    }

    fun finishLong(): Long {
        v2 = v2 xor 0xee
        round(); round(); round()
        val h = v1 xor v3
        v1 = v1 xor 0xdd
        round(); round(); round()
        return h.toLong().shl(32) or (v1 xor v3).toLong().and(0xffff_ffff)
    }

    inline fun doHash(block: HalfSipHash.() -> Unit): Int {
        init()
        block()
        return finish()
    }

    fun hash(input: IntArray): Int {
        init()
        for (m in input) acc(m)
        comma()
        return finish()
    }

    fun hash(input: LongArray): Int {
        init()
        for (m in input) acc(m)
        comma()
        return finish()
    }

    fun hash(input: String): Int {
        init()
        acc(input)
        return finish()
    }
}

abstract class WrappedKeyMap<K, W, V>(val _del: MutableMap<W, V> = HashMap()): AbstractMutableMap<K, V>() {
    abstract fun wrap(key: K): W
    abstract fun unwrap(key: W): K

    override val entries: MutableSet<MutableMap.MutableEntry<K, V>>
            by lazy {
                object: AbstractMutableSet<MutableMap.MutableEntry<K, V>>() {
                    override val size
                        get() = _del.size

                    override fun add(element: MutableMap.MutableEntry<K, V>): Boolean = _del.put(wrap(element.key), element.value) != element.value

                    override fun iterator() = object: MutableIterator<MutableMap.MutableEntry<K, V>> {
                        private val delIterator = _del.iterator()

                        override fun hasNext(): Boolean = delIterator.hasNext()

                        override fun next() = object: MutableMap.MutableEntry<K, V> {
                            private val delEntry = delIterator.next()
                            override val key: K
                                get() = unwrap(delEntry.key)
                            override val value: V
                                get() = delEntry.value

                            override fun setValue(newValue: V): V = delEntry.setValue(newValue)
                        }

                        override fun remove() {
                            delIterator.remove()
                        }
                    }

                    override fun clear() { _del.clear() }
                    override fun contains(element: MutableMap.MutableEntry<K, V>) =
                            wrap(element.key).let { _del.containsKey(it) && _del[it] == element.value }
                }
            }


    override val size get() = _del.size
    override val values get() = _del.values
    override fun clear() = _del.clear()
    override fun containsKey(key: K) = _del.containsKey(wrap(key))
    override fun containsValue(value: V) = _del.containsValue(value)
    override fun get(key: K) = _del[wrap(key)]
    override fun put(key: K, value: V): V? = _del.put(wrap(key), value)
    override fun remove(key: K) = _del.remove(wrap(key))
}

open class StringHashMap<V>(_del: HashMap<Hash, V> = HashMap()) : WrappedKeyMap<String, StringHashMap.Hash, V>(_del) {
    override fun wrap(key: String): Hash = Hash(key)
    override fun unwrap(key: Hash): String = key.data

    class Hash(val data: String) {
        override fun hashCode(): Int = sipHasher.hash(data)
        override fun equals(other: Any?) =
                other is Hash && data == other.data
    }
}

open class LongHashMap<V>(_del: HashMap<Hash, V> = HashMap()) : WrappedKeyMap<Long, LongHashMap.Hash, V>(_del) {
    override fun wrap(key: Long): Hash = Hash(key)
    override fun unwrap(key: Hash): Long = key.data

    class Hash(val data: Long) {
        override fun hashCode(): Int = data.hash().toInt()
        override fun equals(other: Any?) =
                other is Hash && data == other.data
    }
}

abstract class MapBackedSet<T>(val _map: MutableMap<T, Unit>): AbstractMutableSet<T>() {
    override val size: Int get() = _map.size
    override fun add(element: T): Boolean = _map.put(element, Unit) == null
    override fun remove(element: T): Boolean = _map.remove(element) == Unit
    override fun clear() { _map.clear() }
    override fun contains(element: T) = _map.containsKey(element)
    override fun iterator() = object: MutableIterator<T> {
        val mapIterator = _map.iterator()
        override fun hasNext(): Boolean = mapIterator.hasNext()
        override fun next(): T = mapIterator.next().key
        override fun remove() = mapIterator.remove()
    }
}