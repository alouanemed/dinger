package data

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri
import data.account.AccountComponentHolder
import data.account.AccountModule
import data.account.AppAccountAuthenticator
import data.account.DaggerAccountComponent
import data.alarm.AppAlarmManagerImpl
import data.autoswipe.AutoSwipeComponentHolder
import data.autoswipe.AutoSwipeLauncherFactoryImpl
import data.autoswipe.DaggerAutoSwipeComponent
import data.tinder.like.LikeRecommendationProviderImpl
import data.tinder.login.LoginProviderImpl
import data.tinder.recommendation.GetRecommendationProviderImpl
import domain.alarm.AlarmHolder
import domain.autoswipe.AutoSwipeHolder
import domain.like.LikeRecommendationHolder
import domain.loggedincheck.LoggedInCheckHolder
import domain.login.LoginHolder
import domain.recommendation.GetRecommendationHolder
import javax.inject.Inject

/**
 * @see <a href="https://firebase.googleblog.com/2016/12/how-does-firebase-initialize-on-android.html">
 *     The Firebase Blog: How does Firebase initialize on Android</a>
 */
internal class InitializationContentProvider : ContentProvider() {
    @Inject
    lateinit var loginProviderImpl: LoginProviderImpl
    @Inject
    lateinit var getRecommendationProviderImpl: GetRecommendationProviderImpl
    @Inject
    lateinit var likeRecommendationProviderImpl: LikeRecommendationProviderImpl
    @Inject
    lateinit var accountManagerImpl: AppAccountAuthenticator
    @Inject
    lateinit var alarmManagerImpl: AppAlarmManagerImpl
    @Inject
    lateinit var autoSwipeIntentFactoryImpl: AutoSwipeLauncherFactoryImpl

    override fun onCreate(): Boolean {
        val rootModule = RootModule(context)
        val accountModule = AccountModule()
        AccountComponentHolder.accountComponent = DaggerAccountComponent.builder()
                .rootModule(rootModule)
                .accountModule(accountModule)
                .build()
        AutoSwipeComponentHolder.autoSwipeComponent = DaggerAutoSwipeComponent.builder()
                .rootModule(rootModule)
                .build()
        DaggerInitializationComponent.builder()
                .rootModule(rootModule)
                .accountModule(accountModule)
                .build()
                .inject(this)
        LoginHolder.loginProvider(loginProviderImpl)
        LoginHolder.addAccountProvider(accountManagerImpl)
        LoggedInCheckHolder.loggedInCheckProvider(accountManagerImpl)
        GetRecommendationHolder.getRecommendationProvider(getRecommendationProviderImpl)
        LikeRecommendationHolder.likeRecommendationProvider(likeRecommendationProviderImpl)
        AlarmHolder.alarmManager(alarmManagerImpl)
        AutoSwipeHolder.autoSwipeIntentFactory(autoSwipeIntentFactoryImpl)
        return true
    }

    override fun insert(uri: Uri?, values: ContentValues?) = null
    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?) = null
    override fun update(uri: Uri?, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?) = 0
    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?) = 0
    override fun getType(uri: Uri?) = "vnd.android.cursor.item.none"
}
