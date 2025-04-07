import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 执行从版本 1 到版本 2 的数据库结构更改
        database.execSQL("ALTER TABLE your_table ADD COLUMN new_column TEXT")
    }
}