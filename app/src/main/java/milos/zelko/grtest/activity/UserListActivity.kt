package milos.zelko.grtest.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_list.*
import milos.zelko.grtest.R
import milos.zelko.grtest.UserAdapter
import milos.zelko.grtest.UserListViewModel
import milos.zelko.grtest.network.RetrofitClient

/**
 * Activity responsible for displaying of user list
 */
class UserListActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val userListViewModel = ViewModelProviders.of(this).get(UserListViewModel::class.java)

        rvUserList.layoutManager = LinearLayoutManager(this)
        rvUserList.setHasFixedSize(true)

        val adapter = UserAdapter()

        userListViewModel.userPagedList?.observe(this, Observer { adapter.submitList(it) })
        rvUserList.adapter = adapter
    }
}
