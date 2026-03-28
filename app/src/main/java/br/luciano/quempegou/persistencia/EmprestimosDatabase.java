package br.luciano.quempegou.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import br.luciano.quempegou.models.Emprestimo;

@Database(entities = { Emprestimo.class }, version = 2, exportSchema = false)
public abstract class EmprestimosDatabase extends RoomDatabase {

    public abstract EmprestimoDao getEmprestimoDao();

    // singleton + Double-checked Locking (bloqueio de verificação dupla)
    // volative garante que a escrita em INSTANCE só aconteça após o objeto estar 100% criado
    private static volatile EmprestimosDatabase INSTANCE;

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            long currentTime = System.currentTimeMillis();
            database.execSQL("ALTER TABLE Emprestimo ADD COLUMN dataEmprestimo INTEGER NOT NULL DEFAULT " + currentTime);
            database.execSQL("ALTER TABLE Emprestimo ADD COLUMN dataDevolucao INTEGER");
        }
    };

    // allowMainThreadQueries permite que a aplicação execute queries na main thread
    public static EmprestimosDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (EmprestimosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            EmprestimosDatabase.class,
                            "emprestimos.db")
                            .addMigrations(MIGRATION_1_2)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
