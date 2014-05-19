package org.mybatis.caches.rediscala

import akka.util.ByteString
import org.mybatis.caches.rediscala.serializer.{SnappyRedisSerializer, BinaryRedisSerializer, FstRedisSerializer, RedisSerializer}
import redis.ByteStringFormatter

/**
 * 캐시 정보를 Redis 서버에 저장할 때 사용할 Formatter 입니다.
 * @param serializer 캐시 정보를 직렬화/역직렬화 합니다
 * @tparam T  Redis Value 의 수형
 */
abstract class CacheEntryFormatter[T](val serializer: RedisSerializer[T]) extends ByteStringFormatter[T] {

    override def serialize(data: T): ByteString = {
        data match {
            case null => ByteString.empty
            case _ => ByteString(serializer.serialize(data))
        }

    }

    override def deserialize(bs: ByteString): T = {
        bs match {
            case null => null.asInstanceOf[T]
            case _ => serializer.deserialize(bs.toArray)
        }
    }
}

class BinaryCacheEntryFormatter[T]
    extends CacheEntryFormatter[T](new BinaryRedisSerializer[T]()) {}

class FstCacheEntryFormatter[T]
    extends CacheEntryFormatter[T](new FstRedisSerializer[T]()) {}


class SnappyFstCacheEntryFormatter[T]
    extends CacheEntryFormatter[T](SnappyRedisSerializer[T](new FstRedisSerializer[T]())) {}

class SnappyBinaryCacheEntryFormatter[T]
    extends CacheEntryFormatter[T](SnappyRedisSerializer[T](new BinaryRedisSerializer[T]())) {}