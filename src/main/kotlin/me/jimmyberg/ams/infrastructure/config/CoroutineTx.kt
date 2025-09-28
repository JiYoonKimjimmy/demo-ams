package me.jimmyberg.ams.infrastructure.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend inline fun <T> tx(crossinline block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

suspend inline fun <T> cpu(crossinline block: suspend () -> T): T =
    withContext(Dispatchers.Default) { block() }

