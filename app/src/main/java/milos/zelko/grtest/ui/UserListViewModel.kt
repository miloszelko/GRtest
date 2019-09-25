package milos.zelko.grtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User
import milos.zelko.grtest.paging.RequestFailure
import milos.zelko.grtest.paging.UserDataSource
import milos.zelko.grtest.paging.UserDataSourceFactory

class UserListViewModel: ViewModel() {

    var userPagedList: LiveData<PagedList<User>>? = null

    private val userDataSourceFactory: UserDataSourceFactory =
        UserDataSourceFactory()

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(UserDataSource.PAGE_SIZE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun getState(): LiveData<EState> {
        return Transformations.switchMap<UserDataSource,
                EState>(userDataSourceFactory.userDataSourceLiveData) { it.state }
    }

    fun getRequestFailure(): LiveData<RequestFailure> {
        return Transformations.switchMap<UserDataSource,
                RequestFailure>(userDataSourceFactory.userDataSourceLiveData) { it.requestFailure }
    }

    fun invalidateUserList() {
        userPagedList?.value?.dataSource?.invalidate()
    }
}