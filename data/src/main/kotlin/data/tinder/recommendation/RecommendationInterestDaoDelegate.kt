package data.tinder.recommendation

import data.CollectibleDaoDelegate

internal class RecommendationInterestDaoDelegate(
        private val interestDao: RecommendationInterestDao,
        private val userInterestDao: RecommendationUser_InterestDao)
    : CollectibleDaoDelegate<String, ResolvedRecommendationInterest>() {
    override fun selectByPrimaryKey(primaryKey: String) =
            interestDao.selectInterestById(primaryKey).firstOrNull()?.let {
                return@let ResolvedRecommendationInterest(
                        id = it.id,
                        name = it.name)
            } ?: ResolvedRecommendationInterest.NONE

    override fun insertResolved(source: ResolvedRecommendationInterest) =
            interestDao.insertInterest(RecommendationInterestEntity(
                    id = source.id,
                    name = source.name))

    fun insertResolvedForUserId(
            userId: String, interests: Iterable<ResolvedRecommendationInterest>) {
        interests.forEach {
            insertResolved(it)
            userInterestDao.insertUser_Interest(RecommendationUserEntity_RecommendationInterestEntity(
                    recommendationUserEntityId = userId,
                    recommendationInterestEntityId = it.id))
        }
    }
}