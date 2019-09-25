package milos.zelko.grtest.paging

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User
import milos.zelko.grtest.network.RetrofitClient

class UserDataSource: PageKeyedDataSource<Int, User>()  {

    private val compositeDisposable = CompositeDisposable()
    val state: MutableLiveData<EState> = MutableLiveData()
    val requestFailure: MutableLiveData<RequestFailure> = MutableLiveData()

    companion object {
        const val PAGE_SIZE = 5
        const val FIRST_PAGE = 1
    }

    private fun updateState(state: EState) {
        this.state.postValue(state)
    }

    private fun handleError(retryable: Retryable, t: Throwable) {
        requestFailure.postValue(RequestFailure(retryable, t))
    }

    /**
     * Loads initial data
     */
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        updateState(EState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(
            FIRST_PAGE,
            PAGE_SIZE
        )
            .subscribeOn(Schedulers.computation())
            .subscribe (
                 {
                     updateState(EState.DONE)
                     callback.onResult(it.data, null, FIRST_PAGE + 1)
                 },  {
                     updateState(EState.ERROR)
                     Log.e("UserDataSource", "loadInitial ERROR", it)

                     val retryable = object : Retryable {
                         override fun retry() {
                             loadInitial(params, callback)
                         }
                     }
                     handleError(retryable, it)
                 }
             )
         )
    }

    /**
     * Loads the next page data
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        updateState(EState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(params.key,
            PAGE_SIZE
        )
            .subscribeOn(Schedulers.computation())
            .subscribe(
                {
                    updateState(EState.DONE)
                    callback.onResult(it.data, params.key + 1)
                },
                {
                    updateState(EState.ERROR)
                    Log.e("UserDataSource", "loadAfter ERROR", it)
                    val retryable = object : Retryable {
                        override fun retry() {
                            loadAfter(params, callback)
                        }
                    }
                    handleError(retryable, it)
                }
            )
        )
    }

    /**
     * Loads the previous page. This method is useful in cases where the data changes and we need
     * to fetch our list starting from the middle. We have no use for it as our data will remain unchanged.
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {

    }
}