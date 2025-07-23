package com.example.worker.util

import java.io.InputStreamReader

class YamlReader(
    val reader: InputStreamReader
) {
    fun readFile() : Map<String, Any?>? {
        val lines = reader.readLines()

        if (lines.isEmpty()) return null

        val indent = getIndent(lines)

        var hash = mutableMapOf<Int, Pair<String, Any?>>()
        hash[0] = Pair("", mutableMapOf<String, Any?>())

        hash = readLine(lines, 0, hash, indent)
        hash = saveHash(hash, 1)

        return hash[0]?.second as Map<String, Any?>?
    }

    private fun getIndent(lines: List<String>) : String {
        lines.forEach { line ->
            if (line[0].isWhitespace()) {
                val i = line.indexOfFirst { !it.isWhitespace() }
                if (i != -1) {
                    return line.substring(0, i)
                }
            }
        }
        return ""
    }

    private fun readLine(lines: List<String>, i: Int, hash: MutableMap<Int, Pair<String, Any?>>, indent: String) : MutableMap<Int, Pair<String, Any?>> {
        if (lines.lastIndex < i) return hash

        var line = lines[i]
            .replace("[ \t]*$".toRegex(), "")
            .replace("\"".toRegex(), "")
            .replace(" *: *".toRegex(), ":")
        var level = 0
        var phash = hash
        for (t in 1..line.length) {
            level = t
            if (indent.isNotEmpty() && line.startsWith(indent)) {
                line = line.substring(indent.length)
                continue
            }

            if (line[0].isWhitespace()) {
                return readLine(lines, i+1, phash, indent)
            }

            break
        }

        if (phash[level] != null) {
            phash = saveHash(phash, level)
        }

        val tuple = line.split(":")
        phash[level] =
            if (tuple[1].isEmpty()) Pair(tuple[0], mutableMapOf<String, Object>())
            else {
                if (tuple[1].toIntOrNull() != null)  Pair(tuple[0], tuple[1].toInt())
                else if (tuple[1].toDoubleOrNull() != null)  Pair(tuple[0], tuple[1].toDouble())
                else if (tuple[1].toBooleanStrictOrNull() != null)  Pair(tuple[0], tuple[1].toBooleanStrict())
                else Pair(tuple[0], tuple[1])
            }

        return readLine(lines, i+1, phash, indent)
    }

    private fun saveHash(hash: MutableMap<Int, Pair<String, Any?>>, index: Int) : MutableMap<Int, Pair<String, Any?>> {
        var phash = hash

        if (phash[index-1]?.second !is MutableMap<*, *>) {
            return phash
        }

        if (phash[index+1] != null) {
            phash = saveHash(phash, index+1)
        }

        @Suppress("UNCHECKED_CAST")
        (phash[index-1]!!.second as MutableMap<String, Any?>)[phash[index]!!.first] = phash[index]?.second

        return phash
    }
}