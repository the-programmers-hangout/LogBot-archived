package me.moe.logbot.util.types

import java.util.ArrayDeque

class LimitedList<T>(private var limit: Int) : ArrayDeque<T>() {
    override fun add(element: T): Boolean {
        if(size == limit) {
            this.removeFirst()
        }
        return super.add(element)
    }

    fun changeLimit(limit: Int) {
        this.limit = limit
    }
}
