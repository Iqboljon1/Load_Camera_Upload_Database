package com.iraimjanov.load_camera_upload_database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class MyImage {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
    var image: String? = null
    var name: String? = null

    constructor(image: String?, name: String?) {
        this.image = image
        this.name = name
    }

    constructor(id: Int?, image: String?, name: String?) {
        this.id = id
        this.image = image
        this.name = name
    }

    constructor()

}