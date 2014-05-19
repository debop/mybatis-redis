package org.mybatis.caches.rediscala

import java.util.concurrent.locks.{ReadWriteLock, ReentrantReadWriteLock}
import org.apache.ibatis.cache.Cache
import org.mybatis.caches.rediscala.concurrent.Asyncs
import org.slf4j.LoggerFactory
import redis.RedisClient
import scala.beans.BeanProperty
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * MyBatis cache for Redis
 * @author debop created at 2014. 5. 19.
 */
abstract class AbstractRedisCache(@BeanProperty val id: String, val redis: RedisClient) extends Cache {

    require(id != null && id.length > 0)

    lazy val log = LoggerFactory.getLogger(getClass)

    implicit val anyRefFormatter = new SnappyFstCacheEntryFormatter[AnyRef]()

    @BeanProperty val readWriteLock: ReadWriteLock = new ReentrantReadWriteLock()

    override def clear() {
        log.debug(s"clear all cache items. id=$id")
        redis.del(id)
    }

    override def getObject(key: Any): AnyRef = {
        log.trace(s"load cache item. id=$id, key=$key")

        val value = redis.hget[AnyRef](id, key.toString).map(x => x.getOrElse(null.asInstanceOf[AnyRef]))
        Asyncs.result(value)
    }

    override def getSize = Asyncs.result(redis.hlen(id)).toInt

    override def putObject(key: Any, value: AnyRef) {
        log.trace(s"put cache item. id=$id, key=$key")
        redis.hset(id, key.toString, value)
    }

    override def removeObject(key: Any): AnyRef = {
        log.trace(s"remove cache. id=$id, key=$key")

        val result = getObject(key)
        redis.hdel(id, Seq(key.toString): _*)
    }

    override def hashCode(): Int = id.hashCode

    override def equals(obj: Any): Boolean = obj match {
        case obj: Cache => hashCode() == obj.hashCode()
        case _ => false
    }

    override def toString: String = s"RedisCache {$id}"
}
