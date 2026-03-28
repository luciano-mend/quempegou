package br.luciano.quempegou.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.luciano.quempegou.models.Emprestimo;

@Dao
public interface EmprestimoDao {

    @Insert
    long insert(Emprestimo emprestimo);

    @Delete
    int delete(Emprestimo emprestimo);

    @Update
    int update(Emprestimo emprestimo);

    @Query("SELECT * FROM Emprestimo WHERE id = :id")
    Emprestimo getById(long id);

    @Query("SELECT * FROM Emprestimo ORDER BY nomeItemEmprestado ASC")
    List<Emprestimo> getAllAscending();

    @Query("SELECT * FROM Emprestimo ORDER BY nomeItemEmprestado DESC")
    List<Emprestimo> getAllDescending();

    @Query("SELECT * FROM Emprestimo ORDER BY devolvido, nomeItemEmprestado ASC")
    List<Emprestimo> getAllAscendingNaoDevolvidos();

}
