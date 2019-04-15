package com.qxj.commondata.paging

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.*
import android.content.Context
import java.util.concurrent.Executors

@Entity
data class Student(@PrimaryKey(autoGenerate = true) val id: Int, val name: String)

@Dao
interface StudentDao {

    @Query("SELECT * FROM student ORDER BY name COLLATE NOCASE ASC")
    fun getAllStudents(): DataSource.Factory<Int, Student>

    @Insert
    fun insert(vararg students: Student)

    @Insert
    fun insert(student: List<Student>)
}

@Database(entities = [Student::class], version = 1)
abstract class StudentDatabase : RoomDatabase() {

    abstract fun studentDao(): StudentDao

    companion object {
        private var instance: StudentDatabase? = null
        @Synchronized
        fun getInstance(context: Context): StudentDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, StudentDatabase::class.java, "StudentDatabase.db")
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
//                                super.onCreate(db)
                                ioThread {
                                    getInstance(context).studentDao().insert(
                                            CHEESE_DATA.map { Student(id = 0, name = it) }
//                                            *CHEESE_DATA.map { Student(id = 0, name = it) }.toTypedArray()
                                    )
                                }

                            }
                        })
                        .build()
            }
            return instance!!
        }
    }
}

private val CHEESE_DATA = arrayListOf(
        "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
        "Aisy Cendre", "Allgauer Emmentaler", "Alverca", "Ambert", "American Cheese",
        "Ami du Chambertin", "Anejo Enchilado", "Anneau du Vic-Bilh", "Anthoriro", "Appenzell",
        "Aragon", "Ardi Gasna", "Ardrahan", "Armenian String", "Aromes au Gene de Marc",
        "Asadero", "Asiago", "Aubisque Pyrenees", "Autun", "Avaxtskyr", "Baby Swiss",
        "Babybel", "Baguette Laonnaise", "Bakers", "Baladi", "Balaton", "Bandal", "Banon",
        "Barry's Bay Cheddar", "Basing", "Basket Cheese", "Bath Cheese", "Bavarian Bergkase",
        "Baylough", "Beaufort", "Beauvoorde", "Beenleigh Blue", "Beer Cheese", "Bel Paese",
        "Bergader", "Bergere Bleue", "Berkswell", "Beyaz Peynir", "Bierkase", "Bishop Kennedy",
        "Blarney", "Bleu d'Auvergne", "Bleu de Gex", "Bleu de Laqueuille",
        "Bleu de Septmoncel", "Bleu Des Causses", "Blue", "Blue Castello", "Blue Rathgore",
        "Blue Vein (Australian)", "Blue Vein Cheeses", "Bocconcini", "Bocconcini (Australian)"
)

private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}


class MainViewModel(app: Application) : AndroidViewModel(app) {
    val dao = StudentDatabase.getInstance(app).studentDao()
    val allStudents = LivePagedListBuilder(dao.getAllStudents(),
            PagedList.Config.Builder()
                    .setPageSize(PAGE_SIZE)
                    .setEnablePlaceholders(ENABLE_PLACEHOLDERS)
                    .setInitialLoadSizeHint(PAGE_SIZE)
                    .build()
    ).build()

    companion object {
        private const val PAGE_SIZE = 15
        private const val ENABLE_PLACEHOLDERS = false
    }
}