package org.mybatis.caches.rediscala

import redis.RedisClient

/**
 * RedisCache
 * @author debop created at 2014. 5. 19.
 */
class RedisCache(private val _id: String, redis: RedisClient) extends AbstractRedisCache(_id, redis) {

}
