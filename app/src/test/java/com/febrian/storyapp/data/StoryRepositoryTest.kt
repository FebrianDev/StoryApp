package com.febrian.storyapp.data

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.febrian.storyapp.data.api.ApiService
import com.febrian.storyapp.data.db.StoryDatabase
import com.febrian.storyapp.data.repository.StoryRepository
import com.febrian.storyapp.ui.story.adapter.StoryAdapter
import com.febrian.storyapp.utils.CoroutinesTestRule
import com.febrian.storyapp.utils.DataDummy
import com.febrian.storyapp.utils.PagedTestDataSource
import com.febrian.storyapp.utils.UserPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var userPreference: UserPreference

    @Mock
    private lateinit var database: StoryDatabase

    @Mock
    private lateinit var storyRepositoryMock: StoryRepository

    private lateinit var storyRepository: StoryRepository

    private var dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setup() {
        storyRepository = StoryRepository(database, apiService, userPreference)
    }

    @Test
    fun `Get stories with pager - successfully`() = runTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val data = PagedTestDataSource.snapshot(dummyStories)

        val expectedResult = flowOf(data)

        `when`(storyRepositoryMock.getAllStories()).thenReturn(expectedResult)

        storyRepositoryMock.getAllStories().collect { actualResult ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actualResult)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                dummyStoriesResponse.listStory.size,
                differ.snapshot().size
            )
            Assert.assertEquals(dummyStoriesResponse.listStory[0], differ.snapshot()[0])
        }
    }

    @Test
    fun `Get stories with pager - when nothing story`() = runTest {
        dummyStoriesResponse.listStory = arrayListOf()
        val data = PagedTestDataSource.snapshot(arrayListOf())

        val expectedResult = flowOf(data)

        `when`(storyRepositoryMock.getAllStories()).thenReturn(expectedResult)

        storyRepositoryMock.getAllStories().collect { actualResult ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actualResult)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                dummyStoriesResponse.listStory.isEmpty(),
                differ.snapshot().isEmpty()
            )
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}