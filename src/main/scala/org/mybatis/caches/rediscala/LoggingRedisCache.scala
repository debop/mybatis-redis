package org.mybatis.caches.rediscala

import org.apache.ibatis.cache.decorators.LoggingCache
import redis.RedisClient

/**
 * LoggingRedisCache
 * @author debop created at 2014. 5. 19.
 */
class LoggingRedisCache(private val _id: String, redis: RedisClient)
    extends LoggingCache(new RedisCache(_id, redis)) {
}
