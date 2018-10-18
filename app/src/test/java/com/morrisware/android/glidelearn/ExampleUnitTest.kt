package com.morrisware.android.glidelearn

import androidx.core.util.Pools
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testPool() {
        val pool = Pools.SimplePool<String>(10)
        var data = pool.acquire()
        System.out.println(data)
        if (data == null) {
            data = "1"
        }
        val res = pool.release(data)
        System.out.println(res)
        data = pool.acquire()
        System.out.println(data)
    }
}
