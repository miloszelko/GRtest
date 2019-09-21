package milos.zelko.grtest.activity

import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import milos.zelko.grtest.R
import milos.zelko.grtest.network.RetrofitClient

/**
 * Activity responsible for displaying of user list
 */
class UserListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        addDisposable(RetrofitClient.api.getUsers(2,3)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {response ->
                    val data = response.data
                },
                {e ->
                    Log.d("UserListActivity", e.message)
                }
        ))
    }
}
