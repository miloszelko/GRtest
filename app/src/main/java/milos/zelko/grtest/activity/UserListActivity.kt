package milos.zelko.grtest.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_list.*
import milos.zelko.grtest.R
import milos.zelko.grtest.RequestFailure
import milos.zelko.grtest.UserAdapter
import milos.zelko.grtest.UserListViewModel
import milos.zelko.grtest.enum.EState
import milos.zelko.grtest.model.User

/**
 * Activity responsible for displaying of user list
 */
class UserListActivity : BaseActivity(), UserAdapter.UserClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)
        supportActionBar?.title = resources.getString(R.string.users)

        val userListViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)

        userListViewModel.state.observe(this, Observer { handleState(it) })
        userListViewModel.requestFailureLiveData.observe(this, Observer { handleRequestFailure(it) })

        rvUserList.layoutManager = LinearLayoutManager(this)
        rvUserList.setHasFixedSize(true)

        val adapter = UserAdapter(this)

        userListViewModel.userPagedList?.observe(this, Observer { adapter.submitList(it) })
        rvUserList.adapter = adapter
    }

    override fun userClicked(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.USER_ID, user.id)
        startActivity(userDetailIntent)
    }

    private fun handleState(state: EState) {
        when(state) {
            EState.DONE -> {
                rvUserList.visibility = View.VISIBLE
                tvError.visibility = View.GONE
                btnRetry.visibility = View.GONE
            }
            EState.LOADING -> {
                Log.d("UserListActivity", "LOADING")
            }
            EState.ERROR -> {
                rvUserList.visibility = View.GONE
                tvError.visibility = View.VISIBLE
                btnRetry.visibility = View.VISIBLE
            }
        }
    }

    private fun handleRequestFailure(requestFailure: RequestFailure) {
        var textMessage = resources.getString(R.string.error_data_load)
        requestFailure.error.message?.let {
            textMessage += ": \n $it"
        }
        tvError.text = textMessage
        btnRetry.setOnClickListener {
            tvError.visibility = View.GONE
            btnRetry.visibility = View.GONE
            addDisposable(Observable.fromCallable { requestFailure.retryable.retry() }
                .subscribeOn(Schedulers.computation())
                .subscribe()
            )
        }
    }
}
