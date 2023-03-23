package com.michaeltchuang.ride

import com.michaeltchuang.ride.data.DataProvider
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTests {
    @Test
    fun VideoHubNotEmpty() {
        Assert.assertNotNull(DataProvider.videos.size)
    }
}
