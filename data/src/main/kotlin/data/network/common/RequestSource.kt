package data.network.common

import com.nytimes.android.external.store3.base.impl.Store
import io.reactivex.Single

internal abstract class RequestSource<in RequestModel, ResponseModel>(
        private val store: Store<ResponseModel, RequestModel>)
    : Gettable<RequestModel, ResponseModel> {
    override fun fetch(parameters: RequestModel): Single<ResponseModel> {
        return operate(store.fetch(parameters))
    }

    override fun get(parameters: RequestModel): Single<ResponseModel> {
        return operate(store.get(parameters))
    }

    /**
     * Do whatever operations you want on the data at this level.
     * @param sourceStream The stream whose items are to be mapped.
     */
    open fun operate(sourceStream: Single<ResponseModel>) = sourceStream
}
