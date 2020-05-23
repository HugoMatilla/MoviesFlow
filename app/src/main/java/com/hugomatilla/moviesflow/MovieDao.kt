package com.hugomatilla.moviesflow

import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

internal const val tableName = "MOVIE"

internal const val ID = "_id"
internal const val TITLE = "TITLE"
internal const val DESC = "DESC"
internal const val RATING = "RATING"
internal const val STARRED = "STARRED"
internal const val RELEASE_DATE = "RELEASE_DATE"
internal const val FAV = "FAV"
internal const val IMAGE = "IMAGE"

@Entity(tableName = tableName)
data class Movie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var id: Long,
    @SerializedName("original_title") @ColumnInfo(name = TITLE) var title: String,
//  @ColumnInfo(name = DESC) var desc: String,
    @ColumnInfo(name = RATING) var rating: Int,
    @ColumnInfo(name = STARRED) var starred: Boolean,
//  @ColumnInfo(name = RELEASE_DATE) var release_date: Int,// How to convert to Date in DB
//  @ColumnInfo(name = FAV) var fav: Boolean,
    @SerializedName("poster_path") @ColumnInfo(name = IMAGE) var image: String
)

@Dao
interface MovieDao : GenericDao<Movie> {

    @Query("SELECT * FROM $tableName ORDER BY $STARRED DESC")
    fun getAll(): Flow<List<Movie>>

    @Query("UPDATE $tableName SET $STARRED = :starred WHERE $ID=:id")
    fun starMovieById(id: Long, starred: Boolean)

}

interface GenericDao<T> {

    @Query("SELECT * FROM $tableName WHERE $ID=:id")
    suspend fun getById(id: Long): List<T>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<T>)

    @Query("DELETE FROM $tableName")
    suspend fun deleteAll()
}
