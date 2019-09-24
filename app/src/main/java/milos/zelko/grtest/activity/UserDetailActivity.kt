package milos.zelko.grtest.activity

import android.os.Bundle
import android.view.View
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_user_detail.*
import milos.zelko.grtest.R
import milos.zelko.grtest.network.RetrofitClient

/**
 * Activity responsible for displaying details about specific user
 */
class UserDetailActivity : BaseActivity() {

    companion object {
        const val USER_ID  = "user_id"
    }

    private var userId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.title = resources.getString(R.string.user_detail)

        intent.extras?.let {
            userId = it.getLong(USER_ID)
        }

        btnUserDetailRetry.setOnClickListener {
            hideError()
            fetchUserData(userId)
        }
        showUserInfo(false)


        fetchUserData(userId)
    }

    private fun fetchUserData(userId: Long){
        if (userId == -1L) return

        addDisposable(
            RetrofitClient.api.getUser(userId)
                .subscribeOn(Schedulers.computation())
                .doOnSubscribe { showProgressBar(true) }
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { showProgressBar(false)  }
                .subscribe(
                    { userResponse ->
                        val user = userResponse.data
                        tvFirstName.text = user.firstName
                        tvLastName.text = user.lastName
                        tvUserDetailEmail.text = user.email
                        Picasso.get().load(user.avatar).into(ivUserDetailAvatar)
                        showUserInfo(true)
                    },
                    { error ->
                        showError(error)
                    }
                )
        )
    }

    private fun showProgressBar(boolean: Boolean) {
        val visibility = if (boolean) View.VISIBLE else View.GONE
        progressUserDetail.visibility = visibility
    }

    private fun showUserInfo(boolean: Boolean) {
        val visibility = if (boolean) View.VISIBLE else View.GONE
        tvFirstName.visibility = visibility
        tvLastName.visibility = visibility
        ivUserDetailAvatar.visibility = visibility
        tvUserDetailEmail.visibility = visibility
        tvFirstNameLabel.visibility = visibility
        tvLastNameLabel.visibility = visibility
        tvEmailLabel.visibility = visibility
    }

    private fun showError(error: Throwable) {
        var textMessage = resources.getString(R.string.error_data_load)
        error.message?.let {
            textMessage += ": \n $it"
        }
        tvUserDetailError.text = textMessage
        tvUserDetailError.visibility = View.VISIBLE
        btnUserDetailRetry.visibility = View.VISIBLE
    }

    private fun hideError() {
        tvUserDetailError.visibility = View.GONE
        btnUserDetailRetry.visibility = View.GONE
    }

}
