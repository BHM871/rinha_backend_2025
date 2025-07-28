package com.example.worker.core

import com.example.worker.core.pool.Pooler
import com.example.worker.core.pool.Processor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.slf4j.LoggerFactory

class Processors(
    private val application: Worker,
    private val context: CoroutineDispatcher
) {

    private val log = LoggerFactory.getLogger(Processors::class.java)

    private val poolers = mutableMapOf<String, Pooler>()

    fun loadProcessors() {
        val processors = application.property("worker.processors", Map::class)
        if (processors == null)
            return

        log.info("Loading processors...")
        processors.mapNotNull {
            try {
                if (it.value !is Map<*,*>)
                    throw RuntimeException("Processor $it has a invalid format")

                log.info("Loading processor '${it.key}'")
                val values = it.value as Map<*, *>
                val name = values["name"] as String?
                val size = values["amount"] as Int?
                val module = values["module"] as String?

                if (module == null)
                    throw RuntimeException("Processor '${it.key}' module not defined")

                val processor = createProcessor(module)

                poolers.put(
                    it.key as String,
                    Pooler(CoroutineScope(context))
                        .name(name)
                        .size(size)
                        .of(processor)
                )
                log.info("Processor '${it.key}' loaded")
            } catch (e: Exception) {
                log.error("Can't load processor '${it.key}': {}", e.message)
            }
        }
    }

    fun startAll() {
        log.info("Starting all processors...")
        poolers.forEach {
            try {
                log.info("Starting processor '${it.key}'...")
                it.value.start()
                log.info("Processor '${it.key}' started")
            } catch (_: Exception) {
                log.error("Can't start pooler ${it.key}")
            }
        }
    }

    private fun createProcessor(module: String) : Processor {
        try {
            val clazz = Class.forName(module)
            if (clazz == null)
                throw RuntimeException("Processor module can't be loaded")

            if (!clazz.interfaces.any { it == Processor::class.java })
                throw RuntimeException("Processor module must implements ${Processor::class}")

            val processor = application.dependencies.resolve(clazz.kotlin) as Processor?
            if (processor == null)
                throw RuntimeException("Processor module wasn't added in ${application.dependencies.javaClass}")

            return processor
        } catch (_: ClassNotFoundException) {
            throw RuntimeException("Processor module not found")
        }
    }
}
