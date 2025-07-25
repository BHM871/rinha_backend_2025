package com.example.worker.core

import com.example.worker.util.YamlReader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.InputStreamReader
import kotlin.reflect.KClass

class Properties {

    private val log: Logger = LoggerFactory.getLogger(Properties::class.java)

    companion object {
        private var properties: Map<String, Any?>? = null
        private var parent: MutableMap<String, Any?>? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : Any> property(key: String, type: KClass<T>) : T? {
            if (parent == null)
                return null

            val pkeys = key.split('.')

            var tmp = parent
            for (i in 0..pkeys.lastIndex-1) {
                tmp = tmp!![pkeys[i]] as MutableMap<String, Any?>?

                if (tmp == null)
                    return null
            }

            val last = pkeys[pkeys.lastIndex]
            var value: Any?
            if (last.contains("\\[[0-9]*\\]".toRegex())) {
                val lkey = last.substring(0, last.indexOf('['))
                if (tmp!![lkey] == null)
                    return null

                val list = tmp[lkey] as List<*>

                val index = last.substring(lkey.length + 1, last.length-1).toIntOrNull()
                if (index == null || index < 0 || index > list.lastIndex)
                    return null

                value = list[index]
            } else {
                value = tmp!![last]
            }

            if (value == null)
                return null

            return when(type) {
                Int::class -> value.toString().toIntOrNull()
                Double::class -> value.toString().toDoubleOrNull()
                Boolean::class -> value.toString().toBooleanStrictOrNull()
                List::class ->
                    if (type.isInstance(value)) value
                    else null
                String::class -> value
                else -> null
            } as T?
        }
    }

    fun loadProperties() {
        log.info("Loading properties...")
        val reader = getReader()
        if (reader == null) return

        log.info("Parsing properties values...")
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

        log.info("Properties loaded.")
    }

    private fun getReader() : InputStreamReader? {
        val stream = this.javaClass.classLoader.getResource("application.yaml")
        if (stream == null) return null

        log.info("Reading 'application.yaml' file...")
        return InputStreamReader(FileInputStream(stream.path.toString()))
    }
}

fun <T : Any> Application.property(key: String, type: KClass<T>) : T? = Properties.property<T>(key, type)
