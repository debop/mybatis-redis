package org.mybatis.caches.rediscala

import org.apache.ibatis.cache.Cache
import redis.RedisClient

/**
 * RedisCacheFunSuite
 * @author debop created at 2014. 5. 19.
 */
class RedisCacheFunSuite extends AbstractRedisCacheFunSuite {

    implicit val actorSystem = akka.actor.ActorSystem()

    override def newCache: Cache = new RedisCache(DEFAULT_ID, RedisClient())
}
