package com.rustan.domain

import com.rustan.data.Repository
import com.rustan.model.ImageItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

interface GetDataUseCase {
    operator fun invoke(category: String): Flow<NetworkResult<List<ImageItem>>>
}
class GetDataUseCaseImpl(  private val repository: Repository,
                           private val coroutineDispatcherIO: CoroutineDispatcher):GetDataUseCase{
    override  fun invoke(category: String): Flow<NetworkResult<List<ImageItem>>> {
            return flow {
                runCatching {
                    repository.getImage(category)
                }.onSuccess {
                    emit(NetworkResult.Success(it))
                }.onFailure {
                    if (it is HttpException) {
                        emit(NetworkResult.Error.ApiError(it.code(), it.message()))
                    } else {
                        emit(NetworkResult.Error.Exception(it.message))
                    }
                }
            }.flowOn(coroutineDispatcherIO)
    }
}