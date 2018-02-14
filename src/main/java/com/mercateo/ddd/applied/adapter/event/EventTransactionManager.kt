package com.mercateo.ddd.applied.adapter.event

import com.mercateo.ddd.applied.domain.EventHandler
import mu.KLogging
import org.springframework.stereotype.Component
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.ResourceTransactionManager

class EventTransactionStatus : TransactionStatus {

    companion object : KLogging()

    override fun isRollbackOnly(): Boolean {
        return false
    }

    override fun isNewTransaction(): Boolean {
        return true;
    }

    override fun setRollbackOnly() {
    }

    override fun hasSavepoint(): Boolean {
        return false
    }

    override fun isCompleted(): Boolean {
        return false
    }

    override fun rollbackToSavepoint(savepoint: Any) {
        logger.info("rollbackToSavepoint()")
    }

    override fun flush() {
        logger.info("flush()")
    }

    override fun createSavepoint(): Any {
        logger.info("createSavepoint()")
        return "YES"
    }

    override fun releaseSavepoint(savepoint: Any) {
        logger.info("releaseSavepoint()")
    }
}

@Component
class EventTransactionManager(private val eventHandler : EventHandler) : ResourceTransactionManager {
    override fun getResourceFactory(): Any {
        logger.info("getResourceFactory()")
        return eventHandler
    }

    companion object : KLogging()

    override fun getTransaction(definition: TransactionDefinition?): TransactionStatus {
        logger.info("getTransaction({})", definition)
        return EventTransactionStatus()
    }

    override fun commit(status: TransactionStatus) {
        logger.info("commit({})", status);
    }

    override fun rollback(status: TransactionStatus) {
        logger.info("rollback({})", status);
    }
}
