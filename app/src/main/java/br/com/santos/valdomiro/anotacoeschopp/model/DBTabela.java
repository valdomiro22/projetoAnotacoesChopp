package br.com.santos.valdomiro.anotacoeschopp.model;

public class DBTabela {

    public static final String TABLE_NAME = "nota";

    public static final String ID = "id";
    public static final String CODIGO = "codigo";
    public static final String HORA = "hora";

    private static String sqlCriaTabela = "";

    public static String criaTabela() {
//        sqlCriaTabela = "CREATE TABLE "+ TABLE_NAME +" (";
        sqlCriaTabela = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" (";
        sqlCriaTabela += ID +" INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sqlCriaTabela += CODIGO +" TEXT, ";
        sqlCriaTabela += HORA +" TEXT";
        sqlCriaTabela += ")";

        return sqlCriaTabela;
    }
}
