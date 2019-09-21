package milos.zelko.grtest.activity

import android.support.v7.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseActivity: AppCompatActivity() {

    private val disposables = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }
}