package milos.zelko.grtest.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import milos.zelko.grtest.enum.ERequestState
import milos.zelko.grtest.model.User
import milos.zelko.grtest.network.RetrofitClient

/**
 * Gradually provides data for user list
 */
class UserDataSource: PageKeyedDataSource<Int, User>()  {

    private val compositeDisposable = CompositeDisposable()
    val state: MutableLiveData<ERequestState> = MutableLiveData()
    val requestFailure: MutableLiveData<RequestFailure> = MutableLiveData()

    companion object {
        const val PAGE_SIZE = 5
        const val FIRST_PAGE = 1
    }

    /**
     * Loads initial data
     */
    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, User>) {
        this.state.postValue(ERequestState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(FIRST_PAGE, PAGE_SIZE)
            .subscribeOn(Schedulers.computation())
            .subscribe (
                 {
                     this.state.postValue(ERequestState.DONE)
                     callback.onResult(it.data, null, FIRST_PAGE + 1)
                 },  {
                    this.state.postValue(ERequestState.ERROR)
                    val retryable = object : Retryable {
                         override fun retry() {
                             loadInitial(params, callback)
                         }
                     }
                    requestFailure.postValue(RequestFailure(retryable, it))
                 }
             )
         )
    }

    /**
     * Loads the next page data
     */
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {
        this.state.postValue(ERequestState.LOADING)
        compositeDisposable.add(RetrofitClient.api.getUsers(params.key, PAGE_SIZE)
            .subscribeOn(Schedulers.computation())
            .subscribe(
                {
                    this.state.postValue(ERequestState.DONE)
                    callback.onResult(it.data, params.key + 1)
                },
                {
                    this.state.postValue(ERequestState.ERROR)
                    val retryable = object : Retryable {
                        override fun retry() {
                            loadAfter(params, callback)
                        }
                    }
                    requestFailure.postValue(RequestFailure(retryable, it))
                }
            )
        )
    }

    /**
     * Loads the previous page. This method is useful in cases where the data changes and we need
     * to fetch our list starting from the middle
     */
    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, User>) {

    }
}