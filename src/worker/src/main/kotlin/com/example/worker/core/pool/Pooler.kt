package com.example.worker.core.pool

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Pooler(
    private val scope: CoroutineScope
) {

    private var processor: Processor? = null
    private var size: Int = 1

    fun of(processor: Processor) : Pooler {
        this.processor = processor
        return this
    }

    fun size(size: Int) : Pooler {
        this.size = size
        return this
    }

    fun start() {
        if (processor == null)
            throw RuntimeException("Pooler processor not defined")
        if (size <= 0)
            throw RuntimeException("Pooler size must be greater than 0")

        repeat(size) {
            scope.launch {
                while (true) {
                    processor!!.process()
                }
            }
        }
    }
}