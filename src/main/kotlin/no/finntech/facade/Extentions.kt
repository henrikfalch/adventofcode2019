package no.finntech.facade

import java.io.File
import java.math.BigInteger


fun fileLinesAsList(path:String): List<String> {
    val classLoader: ClassLoader = Day1::class.java.classLoader
    val file = File(classLoader.getResource(path).file)
    return file.readLines()
}

fun fileAsIntList(path:String): List<Int> {
    val classLoader: ClassLoader = Day1::class.java.classLoader
    val file = File(classLoader.getResource(path).file)
    return file.readLines().flatMap { it.split(",") }.map { it.toInt() }
}

fun fileAsBigIntList(path:String): List<BigInteger> {
    val classLoader: ClassLoader = Day1::class.java.classLoader
    val file = File(classLoader.getResource(path).file)
    return file.readLines().flatMap { it.split(",") }.map { it.toBigInteger() }
}

fun fileAsShortIntList(path:String): List<Int> {
    val classLoader: ClassLoader = Day1::class.java.classLoader
    val file = File(classLoader.getResource(path).file)
    val readLines = file.readLines()
    return readLines.flatMap { it.split("") }.filter { it.isNotBlank() }.map { it.toInt() }
}

