package org.mybatis.caches.rediscala

import org.apache.ibatis.cache.Cache
import org.scalatest.{BeforeAndAfter, Matchers, FunSuite}

/**
 * AbstractRedisFunSuite
 * @author debop created at 2014. 5. 19.
 */
abstract class AbstractRedisCacheFunSuite extends FunSuite with Matchers with BeforeAndAfter {

    val DEFAULT_ID = "mybatis"
    var cache: Cache = _

    def newCache: Cache

    before {
        cache = newCache
        cache.clear()
    }

    after {
        cache.clear()
        cache = null
    }

    test("all object saved") {
        val cacheSize = 10000

        (0 until cacheSize) foreach { i =>
            cache.putObject(i, i)
            cache.getObject(i) shouldEqual i
        }

        cache.getSize shouldEqual cacheSize
    }

    test("copy cache item") {
        val count = 1000
        (0 until count) foreach { i =>
            cache.putObject(i, i)
            cache.getObject(i) shouldEqual i
        }
    }

    test("remove item on demand") {
        cache.putObject(0, 0)
        cache.getObject(0) should not be null

        cache.removeObject(0)
        cache.getObject(0) shouldEqual null
    }

    test("flush all items") {
        (0 until 5) foreach { i =>
            cache.putObject(i, i)
        }

        cache.getObject(0) should not be null
        cache.getObject(4) should not be null

        cache.clear()

        cache.getObject(0) shouldEqual null
        cache.getObject(4) shouldEqual null

    }

}
