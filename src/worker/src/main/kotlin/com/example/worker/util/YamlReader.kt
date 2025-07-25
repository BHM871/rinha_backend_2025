package com.example.worker.util

import java.io.InputStreamReader

class YamlReader(
    private val reader: InputStreamReader
) {
    fun readFile() : Map<String, Any?>? {
        val lines = reader.readLines()

        if (lines.isEmpty()) return null

        val indent = getIndent(lines)

        var hash = mutableMapOf<Int, Pair<String, Any?>>()
        hash[0] = Pair("", mutableMapOf<String, Any?>())

        hash = readLine(lines, 0, hash, indent)
        hash = saveHash(hash, 1)

        @Suppress("UNCHECKED_CAST")
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

        val level = getLevel(line, indent)
        if (level == null)
            return readLine(lines, i+1, hash, indent)

        line = line.substring(indent.length*(level-1))

        var phash = hash
        if (phash[level] != null) {
            phash = saveHash(phash, level)
        }

        val tmp = getValue(line)
        if (tmp == null)
            return readLine(lines, i+1, phash, indent)

        if (tmp.first == "-") {
            if (phash[level-1]?.second !is MutableList<*>) {
                phash[level-1] = Pair(phash[level-1]!!.first, mutableListOf<Any?>())
            }

            @Suppress("UNCHECKED_CAST")
            (phash[level-1]!!.second as MutableList<Any?>).add(tmp.second)
        } else {
            phash[level] = tmp
        }

        return readLine(lines, i+1, phash, indent)
    }

    private fun getLevel(line: String, indent: String) : Int? {
        var pline = line
        for (t in 1..pline.length) {
            if (indent.isNotEmpty() && pline.startsWith(indent)) {
                pline = pline.substring(indent.length)
                continue
            }

            if (pline[0].isWhitespace()) {
                return null
            }

            return t
        }
        return null
    }

    private fun getValue(line: String) : Pair<String, Any?>? {
        val tuple = line.split(":")

        // Simple value
        if (tuple.size >= 2 && tuple[1].isNotEmpty()) {
            // Try load value by ENV
            return if (tuple[1].startsWith("\${") && tuple[tuple.lastIndex].endsWith("}")) {
                val first = tuple[1].substring(2)
                val last = tuple[tuple.lastIndex].substring(0, tuple[tuple.lastIndex].length-1)
                var value = getEnv(first)

                if (value != null && value.isNotEmpty())
                    return Pair(tuple[0], getValueTyped(value))

                for (v in 2..tuple.lastIndex-1) {
                    value = tuple[v]
                    value = getEnv(value)

                    if (value != null && value.isNotEmpty())
                        return Pair(tuple[0], getValueTyped(value))
                }

                value = getEnv(last) ?: last

                Pair(tuple[0], getValueTyped(value))
            } else {
                // Only parse fist value
                Pair(tuple[0], getValueTyped(tuple[1]))
            }
        }

        // Object value
        if (tuple.size == 2 && tuple[1].isEmpty())
            return Pair(tuple[0], mutableMapOf<String, Any?>())

        // Array value
        if (line.startsWith('-')) {
            return Pair("-", getValueTyped(line.split(' ')[1]))
        }

        return null
    }

    private fun getValueTyped(value: String) : Any? {
        return if (value.toIntOrNull() != null) value.toInt()
        else if (value.toDoubleOrNull() != null) value.toDouble()
        else if (value.toBooleanStrictOrNull() != null) value.toBooleanStrict()
        else value
    }

    private fun getEnv(name: String) : String? {
        return System.getProperty(name) ?: System.getenv(name)
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