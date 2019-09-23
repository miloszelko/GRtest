package milos.zelko.grtest

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User

class UserDataSourceFactory: DataSource.Factory<Int, User>() {

    private val userLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, User>>()
    private val userDataSource = UserDataSource()
    private var state: MutableLiveData<EState>
    private val requestFailureLiveData: MutableLiveData<RequestFailure>

    init {
        state = userDataSource.getState()
        requestFailureLiveData = userDataSource.getRequestFailureLiveData()
    }

    override fun create(): DataSource<Int, User> {
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }

    fun getState() = state

    fun getRequstFailureLiveData() = requestFailureLiveData

    fun getUserLiveDataSource(): MutableLiveData<PageKeyedDataSource<Int, User>> {
        return userLiveDataSource
    }
}