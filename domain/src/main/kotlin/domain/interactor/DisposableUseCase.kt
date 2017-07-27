package domain.interactor

import io.reactivex.disposables.Disposable

abstract class DisposableUseCase internal constructor() {
    internal lateinit var assembledSubscriber: Disposable

    /**
     * Tears down the use case.
     */
      fun dispose() {
          if (!assembledSubscriber.isDisposed) {
            assembledSubscriber.dispose()
          }
      }
}
