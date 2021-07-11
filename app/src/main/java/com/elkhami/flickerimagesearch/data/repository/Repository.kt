package com.elkhami.flickerimagesearch.data.repository

import androidx.lifecycle.LiveData
import com.elkhami.flickerimagesearch.data.local.SavedPhoto

/**
 * Created by A.Elkhami on 11,July,2021
 */
interface Repository {

    suspend fun insertPhotoToDB(photo: SavedPhoto)

    suspend fun deleteSavedPhoto(photo: SavedPhoto)

    fun getSavedPhoto(imageId : Int): LiveData<SavedPhoto>

    fun getAllSavedPhotos(): LiveData<List<SavedPhoto>>

    suspend fun searchFlickerWithKeyword(searchWord :String)


}