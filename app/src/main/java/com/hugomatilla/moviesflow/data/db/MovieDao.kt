package com.hugomatilla.moviesflow.data.db

import androidx.room.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow

internal const val tableName = "MOVIE"

internal const val ID = "_id"
internal const val TITLE = "TITLE"
internal const val OVERVIEW = "OVERVIEW"
internal const val RATING = "RATING"
internal const val STARRED = "STARRED"
internal const val RELEASE_DATE = "RELEASE_DATE"
internal const val IMAGE = "IMAGE"
internal const val POSTER = "POSTER"
internal const val BUDGET = "BUDGET"

@Entity(tableName = tableName)
data class Movie(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var id: Long,
    @SerializedName("original_title") @ColumnInfo(name = TITLE) var title: String,
    @SerializedName("overview") @ColumnInfo(name = OVERVIEW) var overview: String,
    @SerializedName("vote_average") @ColumnInfo(name = RATING) var rating: Float,
    @SerializedName("poster_path") @ColumnInfo(name = POSTER) var poster: String,
    @SerializedName("backdrop_path") @ColumnInfo(name = IMAGE) var image: String,
    @SerializedName("release_date") @ColumnInfo(name = RELEASE_DATE) var release_date: String,// TODO  How to convert to Date in DB
    @SerializedName("budget") @ColumnInfo(name = BUDGET) var budget: Int?, // In Detail
    @ColumnInfo(name = STARRED) var starred: Boolean
)

@Dao
interface MovieDao :
    BaseDao<Movie> {

    @Query("SELECT * FROM $tableName WHERE $ID=:id")
    suspend fun getById(id: Long): List<Movie>

    @Query("SELECT * FROM $tableName ORDER BY $STARRED DESC")
    fun getAll(): Flow<List<Movie>>

    @Query("UPDATE $tableName SET $STARRED = :starred WHERE $ID=:id")
    fun starMovieById(id: Long, starred: Boolean)

}

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(item: List<T>)

    @Query("DELETE FROM $tableName")
    suspend fun deleteAll()
}
