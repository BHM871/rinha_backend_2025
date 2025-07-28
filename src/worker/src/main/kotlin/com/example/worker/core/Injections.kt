package com.example.worker.core

import org.slf4j.LoggerFactory
import java.lang.reflect.Method

class Injections(
    private val application: Worker
) {

    private val log = LoggerFactory.getLogger(Injections::class.java)
    private val injectors = mutableMapOf<String, Method>()

    fun loadInjectors() {
        val references = application.property("worker.injectors", List::class)
        if (references == null || references.isEmpty())
            return

        log.info("Loading injectors...")
        for (reference in references) {
            try {
                if (reference == null || reference !is String)
                    throw RuntimeException("Injector $reference has invalid format")

                log.info("Loading injector $reference")
                val clazzName = reference.substring(0, reference.indexOfLast { it == '.' }.let { if (it != -1) it else reference.length })
                val methodName =
                    if (clazzName.length != reference.length) reference.substring(clazzName.length + 1)
                    else ""

                val clazz = Class.forName(clazzName)
                if (clazz == null)
                    throw RuntimeException("Injector '$clazzName' can't be loaded")

                val method = clazz.methods.firstOrNull { it.name == methodName }
                if (method == null)
                    throw RuntimeException("Injector method '$methodName' can't be loaded")

                injectors.put(clazz.name, method)
                log.info("Injector $reference loaded")
            } catch (e: Exception) {
                log.error("Can't load $reference: {}", e.message)
            }
        }
    }

    fun inject() {
        log.info("Injecting dependencies...")
        injectors.forEach {
            try {
                log.info("Injecting ${it.key}.${it.value.name}")
                it.value.invoke(application, application)
                log.info("Injector ${it.key}.${it.value.name} injected")
            } catch (e: Exception) {
                log.error("Can't call injector ${it.key}.${it.value.name}", e)
            }
        }
    }
}