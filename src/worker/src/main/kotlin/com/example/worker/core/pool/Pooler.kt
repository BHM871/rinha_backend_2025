package com.example.worker.core.pool

class Pooler {

    private var processor: Processor? = null

    fun of(processor: Processor) : Pooler {
        this.processor = processor
        return this
    }

    suspend fun start() {
        if (processor == null)
            throw RuntimeException("Pooler processor not defined")

        while (true) {
            processor!!.process()
        }
    }
}