package br.com.santos.valdomiro.anotacoeschopp.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.santos.valdomiro.anotacoeschopp.model.DBTabela;
import br.com.santos.valdomiro.anotacoeschopp.model.Nota;
import br.com.santos.valdomiro.anotacoeschopp.util.AppUtil;

public class BandoDeDados extends SQLiteOpenHelper {

    // Criação do banco de dados
    SQLiteDatabase database;

    private static final String DB_NAME = "anotacoes_chopp.sqlite";
    private static final int DB_VERSION = 1;

    public BandoDeDados(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        Log.d(AppUtil.TAG, "BandoDeDados: Banco Criado");

        // Abrir o banco de dados para leitura e escrita
        // Para que seja possível criar as tabelas nele
        database =getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela
        db.execSQL(DBTabela.criaTabela());

        Log.d(AppUtil.TAG, "onCreate: Criando tabela");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insert(String tabela, ContentValues values) {
        database = getWritableDatabase();
        boolean retorno = false;

        try {
            retorno = database.insert(tabela, null, values) > 0;
        } catch (SQLException e) {
            Log.d(AppUtil.TAG, "insert: Erro ao inserir Dados");
        }

        return retorno;
    }

    @SuppressLint({"Range", "Recycle"})
    public List<Nota> getAllNotes(String tabela) {
        database = getWritableDatabase();

        List<Nota> notas = new ArrayList<>();
        Nota nota;
        Cursor cursor;

        database.execSQL(DBTabela.criaTabela());

        String sqlSelect = "SELECT * FROM " + tabela + " ORDER BY id DESC";

        cursor = database.rawQuery(sqlSelect, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    nota = new Nota();

                    nota.setId(cursor.getInt(cursor.getColumnIndex(DBTabela.ID)));
                    nota.setCodigo(cursor.getString(cursor.getColumnIndex(DBTabela.CODIGO)));
                    nota.setHora(cursor.getString(cursor.getColumnIndex(DBTabela.HORA)));

                    notas.add(nota);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("erro", "getAllNotes: "+ e);
        }


        return notas;
    }

    @SuppressLint({"Range", "Recycle"})
    public List<Nota> listarCrescente(String tabela) {
        database = getWritableDatabase();

        List<Nota> notas = new ArrayList<>();
        Nota nota;
        Cursor cursor;
        String sqlSelect = "SELECT * FROM " + tabela + " ORDER BY id";

        cursor = database.rawQuery(sqlSelect, null);

        if (cursor.moveToFirst()) {
            do {
                nota = new Nota();

                nota.setId(cursor.getInt(cursor.getColumnIndex(DBTabela.ID)));
                nota.setCodigo(cursor.getString(cursor.getColumnIndex(DBTabela.CODIGO)));
                nota.setHora(cursor.getString(cursor.getColumnIndex(DBTabela.HORA)));

                notas.add(nota);
            } while (cursor.moveToNext());
        }

        return notas;
    }

    public boolean deletar(String tabela, int id) {
        database = getWritableDatabase();

        boolean retorno = false;

        try {
            retorno = database.delete(tabela, "id = ?", new String[] {String.valueOf(id)}) > 0;
        } catch (Exception e) {
            Log.d("apagar", "deletar: " + e.getMessage());
        }

        return retorno;
    }

    public void deletarTudo(String tabela) {
        database = getWritableDatabase();

//        String sql = "DELETE FROM " + tabela;
        String sql = "DROP TABLE " + tabela;

        try {
            database.execSQL(sql);
        } catch (Exception e) {
            Log.d("apagar", "deletar: " + e.getMessage());
        }

    }

    public boolean update(String tabela, ContentValues contentValues) {
        database = getWritableDatabase();

        boolean retorno = false;

        try {
            retorno = database.update(tabela, contentValues, "id=?", new String[]{String.valueOf(contentValues.get("id"))}) > 0;

        } catch (Exception e) {
            Log.d(AppUtil.TAG, "update: " + e.getMessage());
        }

        return retorno;
    }


}
