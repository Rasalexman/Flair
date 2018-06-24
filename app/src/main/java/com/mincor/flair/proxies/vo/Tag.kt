package com.mincor.flair.proxies.vo

/**
 * Created by a.minkin on 02.11.2017.
 */
data class Tag(var guid:String = "",            // главный ключ тега (УНИКАЛЬНЫЙ)
               var name:String = "",            // название тега
               var isRemoved:Boolean = false,   // был ли удален тег, если да то его не стоит выводить в поиске тега и тд...
               var isWatched:Boolean = false,    // был ли этот тег выбран для просмотра в фильтрации
               var next:String? = "",            // следующий в запросе (пагинация)
               var watchedCount:Long? = 0       // порядковый номер добавления тэга в просмотренные
               )
{
    override fun toString(): String {
        return this.name
    }
}
