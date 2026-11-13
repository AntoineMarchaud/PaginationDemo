package com.amarchaud.ui.screen.mainList

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.amarchaud.domain.models.NameModel
import com.amarchaud.domain.models.UserModel
import com.amarchaud.domain.usecase.GetRandomUsersUseCase
import com.amarchaud.ui.fakes.FakePaginationDemoRepository
import com.amarchaud.ui.screen.mappers.toGenericUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainListViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val domainUsers = List(2) {
        UserModel(localId = it.toLong(), email = "user$it@test.fr", name = NameModel("Mr", "Jo", "Doe$it"))
    }
    private val repository = FakePaginationDemoRepository(pagingData = PagingData.from(domainUsers))

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = MainListViewModel(getRandomUsersUseCase = GetRandomUsersUseCase(repository))

    // asSnapshot() never completes on a static PagingData emitted by a never-ending flow:
    // take the current PagingData and snapshot it from a completing flow.
    private suspend fun MainListViewModel.usersSnapshot() = flowOf(users.first()).asSnapshot()

    @Test
    fun usersAreMappedToUiModels() = runTest(dispatcher) {
        val snapshot = viewModel().usersSnapshot()

        assertEquals(domainUsers.map { it.toGenericUiModel() }, snapshot)
    }

    @Test
    fun userClickEmitsNavigateToDetail() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onAction(MainListAction.UserClicked(42))

        assertEquals(MainListEvent.NavigateToDetail(42), viewModel.events.first())
    }

    @Test
    fun eventSentBeforeCollectionIsDelivered() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onAction(MainListAction.UserClicked(7))
        runCurrent()

        assertEquals(MainListEvent.NavigateToDetail(7), viewModel.events.first())
    }

    @Test
    fun refreshAsksTheListToRefresh() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onAction(MainListAction.Refresh)

        assertEquals(MainListEvent.RefreshList, viewModel.events.first())
    }

    @Test
    fun refreshDoesNotRebuildThePager() = runTest(dispatcher) {
        // Paging's own refresh() keeps the current list and merges duplicate refreshes:
        // the ViewModel must not create a new Pager (that made the list flash).
        val viewModel = viewModel()
        backgroundScope.launch { viewModel.users.collect {} }
        runCurrent()

        viewModel.onAction(MainListAction.Refresh)
        viewModel.onAction(MainListAction.Refresh)
        runCurrent()

        assertEquals(1, repository.getRandomUsersCalls)
    }
}
