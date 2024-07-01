package br.com.santos.valdomiro.anotacoeschopp.controller;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import java.util.List;

import br.com.santos.valdomiro.anotacoeschopp.database.BandoDeDados;
import br.com.santos.valdomiro.anotacoeschopp.model.DBTabela;
import br.com.santos.valdomiro.anotacoeschopp.model.Nota;
import br.com.santos.valdomiro.anotacoeschopp.util.AppUtil;

public class DBController extends BandoDeDados {

    ContentValues contentValues;

    public DBController(Context context) {
        super(context);

        Log.d(AppUtil.TAG, "DBController: Conectando ao banco de dados");
    }

    public boolean incluir(Nota nota) {
        contentValues = new ContentValues();

        contentValues.put(DBTabela.CODIGO, nota.getCodigo());
        contentValues.put(DBTabela.HORA, nota.getHora());

        return insert(DBTabela.TABLE_NAME, contentValues);
    }

    public List<Nota> listarController() {
        return getAllNotes(DBTabela.TABLE_NAME);
    }

    public List<Nota> listarOrdemCrescente() {
        return listarCrescente(DBTabela.TABLE_NAME);
    }

    public boolean deletarController(int id) {
        Log.d("apagar", "deletarController: Executao controller");
        return deletar(DBTabela.TABLE_NAME, id);
    }

    public boolean alterarController(Nota nota) {
        contentValues = new ContentValues();

        contentValues.put(DBTabela.ID, nota.getId());
        contentValues.put(DBTabela.CODIGO, nota.getCodigo());
        contentValues.put(DBTabela.HORA, nota.getHora());

        return update(DBTabela.TABLE_NAME, contentValues);
    }

    public void apagarTabela() {
        deletarTudo(DBTabela.TABLE_NAME);
    }
}
