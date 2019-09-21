package milos.zelko.grtest.activity

import android.os.Bundle
import milos.zelko.grtest.R

/**
 * Activity responsible for displaying details about specific user
 */
class UserDetailActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
    }
}
