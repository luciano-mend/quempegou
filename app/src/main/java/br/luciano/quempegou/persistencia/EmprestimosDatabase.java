package br.luciano.quempegou.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import br.luciano.quempegou.models.Amigo;
import br.luciano.quempegou.models.Emprestimo;

@Database(entities = { Emprestimo.class, Amigo.class }, version = 3, exportSchema = false)
public abstract class EmprestimosDatabase extends RoomDatabase {

    public abstract EmprestimoDao getEmprestimoDao();
    public abstract AmigoDao getAmigoDao();

    private static volatile EmprestimosDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            long currentTime = System.currentTimeMillis();
            database.execSQL("ALTER TABLE Emprestimo ADD COLUMN dataEmprestimo INTEGER NOT NULL DEFAULT " + currentTime);
            database.execSQL("ALTER TABLE Emprestimo ADD COLUMN dataDevolucao INTEGER");
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `Amigo` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `nome` TEXT NOT NULL, `observacao` TEXT, `dataInclusao` INTEGER NOT NULL, `ativo` INTEGER NOT NULL)");
            database.execSQL("CREATE INDEX IF NOT EXISTS `index_Amigo_nome` ON `Amigo` (`nome`) ");
        }
    };

    public static EmprestimosDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (EmprestimosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            EmprestimosDatabase.class,
                            "emprestimos.db")
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
