package milos.zelko.grtest.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import milos.zelko.grtest.model.User

class UserDataSourceFactory: DataSource.Factory<Int, User>() {

    val userDataSourceLiveData = MutableLiveData<UserDataSource>()

    override fun create(): DataSource<Int, User> {
        val userDataSource = UserDataSource()
        userDataSourceLiveData.postValue(userDataSource)
        return userDataSource
    }

}