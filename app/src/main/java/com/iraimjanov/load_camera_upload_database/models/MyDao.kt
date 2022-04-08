package com.iraimjanov.load_camera_upload_database.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Flowable

@Dao
interface MyDao {
    @Insert
    fun addImage(myImage: MyImage)

    @Query("select * from MyImage")
    fun getAllTypes(): Flowable<List<MyImage>>

}