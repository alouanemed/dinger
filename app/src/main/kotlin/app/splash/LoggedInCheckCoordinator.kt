package app.splash

import com.google.firebase.crash.FirebaseCrash
import domain.auth.LoggedInUserCheckUseCase
import domain.exec.PostExecutionSchedulerProvider
import io.reactivex.observers.DisposableSingleObserver

internal class LoggedInCheckCoordinator(
        private val postExecutionSchedulerProvider: PostExecutionSchedulerProvider,
        private val callback: ResultCallback) {
    private var useCase: LoggedInUserCheckUseCase? = null

    fun actionDoCheck() {
        useCase = LoggedInUserCheckUseCase(postExecutionSchedulerProvider)
        useCase?.execute(object : DisposableSingleObserver<Boolean>() {
            override fun onSuccess(t: Boolean) {
                when (t) {
                    true -> callback.onLoggedInUserFound()
                    false -> callback.onLoggedInUserNotFound()
                }
            }

            override fun onError(throwable: Throwable?) {
                throwable?.let { FirebaseCrash.report(it) }
                callback.onLoggedInUserNotFound()
            }
        })
    }

    fun actionCancelCheck() {
        useCase?.dispose()
    }

    interface ResultCallback {
        fun onLoggedInUserFound()

        fun onLoggedInUserNotFound()
    }
}