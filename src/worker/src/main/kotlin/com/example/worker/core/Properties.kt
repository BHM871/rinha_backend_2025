package com.example.worker.core

import com.example.worker.util.YamlReader
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.reflect.KClass

class Properties {

    companion object {
        private var properties: Map<String, Any?>? = null
        private var parent: MutableMap<String, Any?>? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> property(key: String, type: KClass<T>) : T? {
            if (parent == null)
                return null

            val pkeys = key.split(".")

            var tmp = parent
            for (i in 0..pkeys.lastIndex-1) {
                if (tmp!![pkeys[i]] == null)
                    return null

                tmp = tmp[pkeys[i]] as MutableMap<String, Any?>
            }

            val value = tmp!![pkeys[pkeys.lastIndex]].toString()

            return when(type) {
                Int::class -> value.toIntOrNull()
                Double::class -> value.toDoubleOrNull()
                Boolean::class -> value.toBooleanStrictOrNull()
                String::class -> value
                else -> null
            } as T?
        }
    }

    fun loadProperties() {
        val reader = getReader()
        if (reader == null) return

        properties = YamlReader(reader).readFile()
        if (properties != null) {
            parent = mutableMapOf(
                *properties!!.entries
                    .stream()
                    .filter { it?.value != null }
                    .map { Pair (it.key, it.value) }
                    .toList()
                    .toTypedArray()
            )
        }
    }

    private fun getReader() : InputStreamReader? {
        val stream = this.javaClass.classLoader.getResource("application.yaml")
        if (stream == null) return null

        return InputStreamReader(FileInputStream(stream.path.toString()))
    }
}

fun <T : Any> Application.property(key: String, type: KClass<T>) : T? = Properties.property<T>(key, type)
