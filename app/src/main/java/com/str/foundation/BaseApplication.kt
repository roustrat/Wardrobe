package com.str.foundation

import com.str.foundation.model.BaseRepository

interface BaseApplication {

    val repositories: List<BaseRepository>

}