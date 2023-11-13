package com.teamtripdraw.android.data.localDatabase.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.teamtripdraw.android.data.localDatabase.TripDrawDatabase
import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class AddressDaoTest {

    private lateinit var tripDrawDatabase: TripDrawDatabase
    private lateinit var sut: AddressDao

    @Before
    fun setUp() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        tripDrawDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            TripDrawDatabase::class.java,
        ).build()
        sut = tripDrawDatabase.addressDao()
    }

    @After
    fun tearDown() {
        tripDrawDatabase.close()
    }

    @Test
    fun `불러온_시도_목록은_이름순으로_정렬되어_있다`() = runBlocking {
        // given
        val addressEntities = listOf(
            AddressEntity("강원도", "강릉시", "강남동"),
            AddressEntity("대구광역시", "남구", "이천동"),
        )
        sut.insertAll(addressEntities)

        // when
        val result = sut.getSiDos()
        val expected: List<String> = listOf("강원도", "대구광역시")

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `불러온_시군구_목록은_이름순으로_정렬되어_있다`() = runBlocking {
        // given
        val addressEntities = listOf(
            AddressEntity("강원도", "동해시", "동호동"),
            AddressEntity("강원도", "강릉시", "강남동"),
        )
        sut.insertAll(addressEntities)

        // when
        val result = sut.getSiGunGus("강원도")
        val expected: List<String> = listOf("강릉시", "동해시")

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `불러온_읍면동_목록은_이름순으로_정렬되어_있다`() = runBlocking {
        // given
        val addressEntities = listOf(
            AddressEntity("강원도", "동해시", "망상동"),
            AddressEntity("강원도", "동해시", "동호동"),
        )
        sut.insertAll(addressEntities)

        // when
        val result = sut.getEupMyeonDongs("강원도", "동해시")
        val expected: List<String> = listOf("동호동", "망상동")

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `주소_목록_중_업데이트가_일어난지_가장_오래_된_날짜를_가져올_수_있다`() = runBlocking {
        // given
        val oldestDateTime: String =
            LocalDateTime.of(2023, 10, 31, 10, 0).format(AddressEntity.dateTimeFormatter)
        val latestDateTime: String =
            LocalDateTime.of(2023, 11, 1, 10, 0).format(AddressEntity.dateTimeFormatter)
        val addressEntities = listOf(
            AddressEntity("서울특별시", "송파구", "방이1동", oldestDateTime),
            AddressEntity("서울특별시", "송파구", "방이2동", latestDateTime),
        )
        sut.insertAll(addressEntities)

        // when
        val result = sut.getOldestUpdateDate()
        val expected: String = oldestDateTime

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `주소를_삽입할_수_있다`() = runBlocking {
        // given
        val addressEntity = AddressEntity("서울특별시", "송파구", "방이1동")

        // when & then
        sut.insert(addressEntity)
    }

    @Test
    fun `주소_목록을_삽입할_수_있다`() = runBlocking {
        // given
        val addressEntities = listOf(AddressEntity("서울특별시", "송파구", "방이1동"))

        // when & then
        sut.insertAll(addressEntities)
    }

    @Test
    fun `주소_목록_전체를_삭제할_수_있다`() = runBlocking {
        // given
        val addressEntities = listOf(AddressEntity("서울특별시", "송파구", "방이1동"))
        sut.insertAll(addressEntities)

        // when & then
        sut.deleteAll()
    }

    @Test
    fun `주소_목록의_수를_가져올_수_있다`() = runBlocking {
        // then
        assertEquals(sut.getCount(), 0)
    }
}
