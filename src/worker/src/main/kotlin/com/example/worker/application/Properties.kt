package com.example.worker.application

import java.io.FileInputStream
import java.io.InputStreamReader

class Properties {

    fun loadProperties() {
        val reader = getReader()
        if (reader == null) return

        readFile(reader)
    }

    private fun getReader() : InputStreamReader? {
        val stream = this.javaClass.classLoader.getResource("application.yaml")
        if (stream == null) return null

        return InputStreamReader(FileInputStream(stream.path.toString()))
    }

    private fun readFile(reader: InputStreamReader) {
        val lines = reader.readLines()

        if (lines.isEmpty()) return

        val indent = getIndent(lines)
        val phash = readLine(lines, 0, mutableMapOf(), indent)

        saveHash(phash, 0)
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
        for (t in 0..line.length) {
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

        if (index > 0 && phash[index-1]?.second !is MutableMap<*, *>) {
            return phash
        }

        if (phash[index+1] != null) {
            phash = saveHash(phash, index+1)
        }

        if (index == 0) {
            properties[phash[0]!!.first] = phash[0]?.second
            return phash
        }

        @Suppress("UNCHECKED_CAST")
        (phash[index-1]!!.second as MutableMap<String, Any?>)[phash[index]!!.first] = phash[index]?.second

        return phash
    }

    companion object {
        private val properties: MutableMap<String, Any?> = mutableMapOf()

        fun read(key: String) : Any? {
            if (key == "*")
                return properties

            val pkeys = key.split("\\.")

            var parent = properties
            for (i in 0..pkeys.lastIndex) {
                if (parent[pkeys[i]] == null)
                    return null

                parent = parent[pkeys[i]] as MutableMap<String, Any?>
            }

            return parent[pkeys[pkeys.lastIndex]]
        }

        fun readText(key: String) : String? {
            val value = read(key)
            return value?.toString()
        }

        fun readInt(key: String) : Int? {
            val value = readText(key)
            return value?.toIntOrNull()
        }

        fun readDouble(key: String) : Double? {
            val value = readText(key)
            return value?.toDoubleOrNull()
        }

        fun readBool(key: String) : Boolean? {
            val value = readText(key)
            return value?.toBooleanStrictOrNull()
        }
    }
}