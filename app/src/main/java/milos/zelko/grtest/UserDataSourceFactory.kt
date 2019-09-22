package milos.zelko.grtest

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import milos.zelko.grtest.model.User

class UserDataSourceFactory: DataSource.Factory<Int, User>() {

    private val userLiveDataSource = MutableLiveData<PageKeyedDataSource<Int, User>>()

    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource()
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }

    fun getUserLiveDataSource(): MutableLiveData<PageKeyedDataSource<Int, User>> {
        return userLiveDataSource
    }
}