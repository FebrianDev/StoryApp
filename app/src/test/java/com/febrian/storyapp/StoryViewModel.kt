package com.febrian.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.febrian.storyapp.data.repository.StoryRepository
import com.febrian.storyapp.data.response.Story
import com.febrian.storyapp.ui.story.adapter.StoryAdapter
import com.febrian.storyapp.ui.story.vm.StoryViewModel
import com.febrian.storyapp.utils.CoroutinesTestRule
import com.febrian.storyapp.utils.DataDummy
import com.febrian.storyapp.utils.PagedTestDataSource
import com.febrian.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyRepository: StoryRepository


    @Before
    fun setup() {

    }

    @Test
    fun `Get stories with pager - successfully`() = runTest {
        val dummyStories = DataDummy.generateDummyListStory()
        val data = PagedTestDataSource.snapshot(dummyStories)

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = data

        `when`(storyRepository.getAllStories()).thenReturn(flowOf(data))

        val storyViewModel = StoryViewModel(storyRepository)
        val actualStories: PagingData<Story> = storyViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )
        differ.submitData(actualStories)

        // test data not null
        Assert.assertNotNull(differ.snapshot())

        //test data size
        Assert.assertEquals(dummyStories.size, differ.snapshot().size)

        //test first data is the same
        Assert.assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `Get stories with pager - when nothing story`() = runTest {
        val dummyNothing = arrayListOf<Story>()
        val data = PagedTestDataSource.snapshot(arrayListOf())

        val stories = MutableLiveData<PagingData<Story>>()
        stories.value = data

        `when`(storyRepository.getAllStories()).thenReturn(flowOf(data))
        val storyViewModel = StoryViewModel(storyRepository)
        val actualResult = storyViewModel.stories.getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = coroutinesTestRule.testDispatcher,
            workerDispatcher = coroutinesTestRule.testDispatcher
        )

        differ.submitData(actualResult)

        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(
            dummyNothing.isEmpty(),
            differ.snapshot().isEmpty()
        )

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}