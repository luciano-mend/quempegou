package br.luciano.quempegou.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.luciano.quempegou.models.Amigo;

@Dao
public interface AmigoDao {

    @Insert
    long insert(Amigo amigo);

    @Delete
    int delete(Amigo amigo);

    @Update
    int update(Amigo amigo);

    @Query("SELECT * FROM Amigo WHERE id = :id")
    Amigo getById(long id);

    @Query("SELECT * FROM Amigo ORDER BY nome ASC")
    List<Amigo> getAllAscending();

    @Query("SELECT * FROM Amigo WHERE ativo = 1 ORDER BY nome ASC")
    List<Amigo> getAllAtivos();

    @Query("SELECT COUNT(*) FROM Emprestimo WHERE amigo = :idAmigo")
    int countEmprestimosByAmigo(long idAmigo);
}
