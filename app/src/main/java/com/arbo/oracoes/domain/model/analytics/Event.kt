package com.arbo.oracoes.domain.model.analytics

abstract class Event {

    abstract val name: String

    val params: MutableMap<String, String> = mutableMapOf()

    fun addParam(name: String, value: String?): Event{
        if (value != null){
            params.put(name, value)
        }
        return this
    }

    override fun toString(): String {
        return "Event(name='$name', params=$params)"
    }

}