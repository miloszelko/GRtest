package milos.zelko.grtest.activity

import android.os.Bundle
import android.util.Log
import milos.zelko.grtest.R

/**
 * Activity responsible for displaying details about specific user
 */
class UserDetailActivity : BaseActivity() {

    companion object {
        const val USER_ID  = "user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        supportActionBar?.title = resources.getString(R.string.user_detail)

        intent.extras?.let {
            val userId = it.getLong(USER_ID)
            Log.d("UserDetailActivity", "USER ID: $userId")
        }
    }

}
