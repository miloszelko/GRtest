package milos.zelko.grtest.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_list.*
import milos.zelko.grtest.R
import milos.zelko.grtest.paging.RequestFailure
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

        rvUserList.layoutManager = LinearLayoutManager(this)
        rvUserList.setHasFixedSize(true)

        val adapter = UserAdapter(this)
        rvUserList.adapter = adapter

        userListViewModel.userPagedList?.observe(this, Observer {
            refreshLayout.isRefreshing = false
            adapter.submitList(it)
        })

        userListViewModel.getState().observe(this, Observer {
            handleState(it)
        })
        userListViewModel.getRequestFailure().observe(this, Observer {
            handleRequestFailure(it)
        })

        refreshLayout.setOnRefreshListener {
            userListViewModel.invalidateUserList()
        }
    }

    override fun userClicked(user: User) {
        val userDetailIntent = Intent(this, UserDetailActivity::class.java)
        userDetailIntent.putExtra(UserDetailActivity.USER_ID, user.id)
        startActivity(userDetailIntent)
    }

    private fun handleState(state: EState) {
        when(state) {
            EState.DONE -> {
                progressUserList.visibility = View.GONE
                tvError.visibility = View.GONE
                btnRetry.visibility = View.GONE
                rvUserList.visibility = View.VISIBLE
            }
            EState.LOADING -> {
                tvError.visibility = View.GONE
                btnRetry.visibility = View.GONE
                progressUserList.visibility = View.VISIBLE
            }
            EState.ERROR -> {
                progressUserList.visibility = View.GONE
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
            addDisposable(Observable.fromCallable { requestFailure.retryable.retry() }
                .subscribeOn(Schedulers.computation())
                .subscribe()
            )
        }
    }
}
