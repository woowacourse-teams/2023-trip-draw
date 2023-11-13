package com.teamtripdraw.android.data.dataSource.address

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.teamtripdraw.android.data.localDatabase.TripDrawDatabase
import com.teamtripdraw.android.data.localDatabase.dao.AddressDao
import com.teamtripdraw.android.data.localDatabase.entity.AddressEntity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class LocalAddressDataSourceImplTest {

    private lateinit var tripDrawDatabase: TripDrawDatabase
    private lateinit var addressDao: AddressDao
    private lateinit var sut: LocalAddressDataSourceImpl

    @Before
    fun setUp() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        tripDrawDatabase = Room.inMemoryDatabaseBuilder(
            appContext,
            TripDrawDatabase::class.java,
        ).build()
        addressDao = tripDrawDatabase.addressDao()
        sut = LocalAddressDataSourceImpl(addressDao)
    }

    @After
    fun tearDown() {
        tripDrawDatabase.close()
    }

    @Test
    fun `시도_목록을_조회할_수_있다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity("강원도", "강릉시", "강남동")
        sut.insert(addressEntity)

        // when
        val result: List<String> = sut.getSiDos()
        val expected: List<String> = arrayListOf("강원도")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `조회한_시도_목록에는_중복되는_값이_없다`() = runBlocking {
        // given
        val addressEntities: List<AddressEntity> = listOf(
            AddressEntity("강원도", "강릉시", "강남동"),
            AddressEntity("강원도", "강릉시", "강동면"),
        )
        sut.insertAll(addressEntities)

        // when
        val result: List<String> = sut.getSiDos()
        val expected: List<String> = arrayListOf("강원도")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `시군구_목록을_조회할_수_있다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity("강원도", "강릉시", "강남동")
        sut.insert(addressEntity)

        // when
        val result: List<String> = sut.getSiGunGus("강원도")
        val expected: List<String> = arrayListOf("강릉시")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `조회한_시군구_목록에는_중복되는_값이_없다`() = runBlocking {
        // given
        val addressEntities: List<AddressEntity> = listOf(
            AddressEntity("강원도", "강릉시", "강남동"),
            AddressEntity("강원도", "강릉시", "강동면"),
        )
        sut.insertAll(addressEntities)

        // when
        val result: List<String> = sut.getSiGunGus("강원도")
        val expected: List<String> = arrayListOf("강릉시")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `읍면동_목록을_조회할_수_있다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity("강원도", "강릉시", "강남동")
        sut.insert(addressEntity)

        // when
        val result: List<String> = sut.getEupMyeonDongs("강원도", "강릉시")
        val expected: List<String> = arrayListOf("강남동")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `읍면동_목록에는_중복되는_값이_없다`() = runBlocking {
        // given
        val addressEntities: List<AddressEntity> = listOf(
            AddressEntity("강원도", "강릉시", "강남동"),
            AddressEntity("강원도", "강릉시", "강남동"),
        )
        sut.insertAll(addressEntities)

        // when
        val result: List<String> = sut.getEupMyeonDongs("강원도", "강릉시")
        val expected: List<String> = arrayListOf("강남동")

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `주소_목록이_없다면_업데이트가_필요한_상황이다`() = runBlocking {
        // when
        val result: Boolean = sut.requiresUpdate()
        val expected: Boolean = true

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `업데이트된_지_가장_오래된_데이터가_현재와_31일_이상_차이가_난다면_업데이트가_필요한_상황이다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity(
            "강원도",
            "강릉시",
            "강남동",
            LocalDateTime.of(2023, 10, 1, 0, 0).format(AddressEntity.dateTimeFormatter),
        )
        sut.insert(addressEntity)

        // when
        val result: Boolean = sut.requiresUpdate(LocalDateTime.of(2023, 11, 1, 0, 0))
        val expected: Boolean = true

        // then
        assertEquals(result, expected)
    }

    @Test
    fun `주소를_추가할_수_있다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity("강원도", "강릉시", "강남동")

        // when
        sut.insert(addressEntity)

        // then
        assertEquals(addressDao.getCount(), 1)
    }

    @Test
    fun `주소_목록을_추가할_수_있다`() = runBlocking {
        // given
        val addressEntities: List<AddressEntity> = listOf(
            AddressEntity("강원도", "강릉시", "강남동"),
            AddressEntity("강원도", "강릉시", "강동면"),
        )

        // when
        sut.insertAll(addressEntities)

        // then
        assertEquals(addressDao.getCount(), 2)
    }

    @Test
    fun `주소_목록을_삭제할_수_있다`() = runBlocking {
        // given
        val addressEntity: AddressEntity = AddressEntity("강원도", "강릉시", "강남동")
        sut.insert(addressEntity)

        // when
        sut.deleteAll()

        // then
        assertEquals(addressDao.getCount(), 0)
    }
}
