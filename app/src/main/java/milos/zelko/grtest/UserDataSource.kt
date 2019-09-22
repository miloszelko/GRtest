package milos.zelko.grtest

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User
import milos.zelko.grtest.network.RetrofitClient



class UserDataSource: PageKeyedDataSource<Int, User>()  {

    private val compositeDisposable = CompositeDisposable()
    var state: MutableLiveData<EState> = MutableLiveData()

    companion object {
        const val PAGE_SIZE = 5
        const val FIRST_PAGE = 1
    }

    private fun updateState(state: EState) {
        this.state.postValue(state)
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        updateState(EState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(FIRST_PAGE, PAGE_SIZE)
             .subscribe (
                 {
                     Log.d("UserDataSource", "LoadInitial")
                     updateState(EState.DONE)
                     callback.onResult(it.data, null, FIRST_PAGE + 1)
                 },  {
                     updateState(EState.ERROR)
                     Log.d("UserDataSource", "LoadInitial ERROR: ${it.message}")
                 }
             )
         )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        updateState(EState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(params.key, PAGE_SIZE)
            .subscribe(
                {
                    Log.d("UserDataSource", "LoadAfter, PAGE: ${params.key}")
                    updateState(EState.DONE)
                    callback.onResult(it.data, params.key + 1)
                },
                {
                    updateState(EState.ERROR)
                    Log.d("UserDataSource", "LoadAfter ERROR: ${it.message}")
                }
            )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}