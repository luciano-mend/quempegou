package br.luciano.quempegou.persistencia;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.luciano.quempegou.models.Emprestimo;

@Database(entities = { Emprestimo.class }, version = 1, exportSchema = false)
public abstract class EmprestimosDatabase extends RoomDatabase {

    public abstract EmprestimoDao getEmprestimoDao();

    // singleton + Double-checked Locking (bloqueio de verificação dupla)
    // volative garante que a escrita em INSTANCE só aconteça após o objeto estar 100% criado
    private static volatile EmprestimosDatabase INSTANCE;

    // allowMainThreadQueries permite que a aplicação execute queries na main thread
    public static EmprestimosDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (EmprestimosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                            EmprestimosDatabase.class,
                            "emprestimos.db").allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }
}
