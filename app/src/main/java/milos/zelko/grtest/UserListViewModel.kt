package milos.zelko.grtest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User

class UserListViewModel: ViewModel() {

    var userPagedList: LiveData<PagedList<User>>? = null
    val state: MutableLiveData<EState>
    val requestFailureLiveData: MutableLiveData<RequestFailure>

    init {
        val userDataSourceFactory = UserDataSourceFactory()
        state = userDataSourceFactory.getState()
        requestFailureLiveData = userDataSourceFactory.getRequstFailureLiveData()

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setPageSize(UserDataSource.PAGE_SIZE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }
}