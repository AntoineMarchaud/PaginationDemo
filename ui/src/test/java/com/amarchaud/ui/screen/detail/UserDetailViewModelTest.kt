package com.amarchaud.ui.screen.detail

import com.amarchaud.domain.models.CoordinatesModel
import com.amarchaud.domain.models.LocationModel
import com.amarchaud.domain.models.NameModel
import com.amarchaud.domain.models.UserModel
import com.amarchaud.domain.usecase.GetOneUserUseCase
import com.amarchaud.ui.fakes.FakePaginationDemoRepository
import com.amarchaud.ui.screen.detail.mappers.toDetailUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    private val user = UserModel(localId = 42, email = "jo@doe.fr", name = NameModel("Mr", "Jo", "Doe"))

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        repository: FakePaginationDemoRepository = FakePaginationDemoRepository(users = mapOf(42L to user)),
        id: Long = 42,
    ) = UserDetailViewModel(
        getOneUserUseCase = GetOneUserUseCase(repository),
        localId = id,
    )

    @Test
    fun loadsUserIntoState() = runTest(dispatcher) {
        val viewModel = viewModel()

        advanceUntilIdle()

        assertEquals(UserDetailState(user = user.toDetailUiModel()), viewModel.state.value)
    }

    @Test
    fun unknownUserKeepsDefaultState() = runTest(dispatcher) {
        val viewModel = viewModel(repository = FakePaginationDemoRepository())

        advanceUntilIdle()

        assertEquals(UserDetailState(), viewModel.state.value)
    }

    @Test
    fun backClickEmitsNavigateBack() = runTest(dispatcher) {
        val viewModel = viewModel()

        viewModel.onAction(UserDetailAction.BackClicked)

        assertEquals(UserDetailEvent.NavigateBack, viewModel.events.first())
    }

    @Test
    fun emailClickOpensMailApp() = runTest(dispatcher) {
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onAction(UserDetailAction.EmailClicked)

        assertEquals(UserDetailEvent.OpenUri("mailto:jo@doe.fr"), viewModel.events.first())
    }

    @Test
    fun phoneClickOpensDialer() = runTest(dispatcher) {
        val withPhone = user.copy(phone = "(568)-263-5288")
        val viewModel = viewModel(repository = FakePaginationDemoRepository(users = mapOf(42L to withPhone)))
        advanceUntilIdle()

        viewModel.onAction(UserDetailAction.PhoneClicked)

        assertEquals(UserDetailEvent.OpenUri("tel:5682635288"), viewModel.events.first())
    }

    @Test
    fun addressClickOpensMaps() = runTest(dispatcher) {
        val located = user.copy(
            // random randomuser.me coordinates are ignored: the address is searched
            location = LocationModel(
                city = "Paris",
                country = "France",
                coordinates = CoordinatesModel(latitude = "-80.09", longitude = "66.91"),
            ),
        )
        val viewModel = viewModel(repository = FakePaginationDemoRepository(users = mapOf(42L to located)))
        advanceUntilIdle()

        viewModel.onAction(UserDetailAction.AddressClicked)

        assertEquals(
            UserDetailEvent.OpenUri("https://www.google.com/maps/search/?api=1&query=Paris%2C%20France"),
            viewModel.events.first(),
        )
    }

    @Test
    fun contactActionWithoutDataEmitsNothing() = runTest(dispatcher) {
        // the test user has no phone number
        val viewModel = viewModel()
        advanceUntilIdle()

        viewModel.onAction(UserDetailAction.PhoneClicked)

        assertNull(withTimeoutOrNull(1_000) { viewModel.events.first() })
    }
}
