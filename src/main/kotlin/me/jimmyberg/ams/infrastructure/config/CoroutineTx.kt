package me.jimmyberg.ams.infrastructure.config

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend inline fun <T> tx(crossinline block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }


