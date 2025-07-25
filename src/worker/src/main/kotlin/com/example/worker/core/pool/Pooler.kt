package com.example.worker.core.pool

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class Pooler(
    private val scope: CoroutineScope
) {

    private val log = LoggerFactory.getLogger(Pooler::class.java)

    private var _name: String? = null
    private var size: Int = 1
    private var processor: Processor? = null

    val name: String
        get() = _name ?: "Pooler@$processor"

    fun name(name: String?) : Pooler {
        this._name = name
        return this
    }

    fun size(size: Int?) : Pooler {
        this.size = size ?: this.size
        return this
    }

    fun of(processor: Processor) : Pooler {
        this.processor = processor
        return this
    }

    fun start() {
        if (processor == null)
            throw RuntimeException("Pooler processor not defined")
        if (size <= 0)
            throw RuntimeException("Pooler size must be greater than 0")

        log.info("Starting processor: ${name ?: processor}...")
        repeat(size) {
            scope.launch {
                withContext(Dispatchers.IO) {
                    while (true) {
                        processor!!.process()
                    }
                }
            }
        }
    }
}