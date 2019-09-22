package milos.zelko.grtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import milos.zelko.grtest.model.User

class UserListViewModel: ViewModel() {

    var userPagedList: LiveData<PagedList<User>>? = null
    private var liveDataSource: LiveData<PageKeyedDataSource<Int, User>>? = null

    init {
        val userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getUserLiveDataSource()

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(UserDataSource.PAGE_SIZE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }
}