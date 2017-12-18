package kleitonbatista.com.agendadecontatos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import kleitonbatista.com.agendadecontatos.ContatoInfo;

/**
 * Created by kleitonbatista on 13/12/2017.
 */

public class ContatoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private static final String TABELA = "Contatos";
    private static final String DATABASE = "DadosAgenda";

    public ContatoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = "CREATE TABLE " + TABELA +
                " (id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nome TEXT NOT NULL, "+
                " referencia TEXT, " +
                " email TEXT, "+
                " endereco TEXT, " +
                " telefone TEXT, "+
                " foto TEXT);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public List<ContatoInfo> getList(String order){
        List<ContatoInfo> contatos = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+ TABELA + " ORDER BY nome "+ order + ";",null);
        while (cursor.moveToNext()){
            ContatoInfo c = new ContatoInfo();
            c.setId(cursor.getLong(cursor.getColumnIndex("id")));
            c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            c.setTelefone(cursor.getString(cursor.getColumnIndex("telefone")));
            c.setEndereco(cursor.getString(cursor.getColumnIndex("endereco")));
            c.setRef(cursor.getString(cursor.getColumnIndex("referencia")));
            c.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            c.setFoto(cursor.getString(cursor.getColumnIndex("foto")));

            contatos.add(c);
        }
        cursor.close();
        return contatos;
    }

    public void inserirContato (ContatoInfo c){
        ContentValues values = new ContentValues();
        values.put("nome", c.getNome());
        values.put("referencia",c.getRef());
        values.put("email", c.getEmail());
        values.put("endereco", c.getEmail());
        values.put("telefone", c.getTelefone());
        values.put("foto", c.getFoto());

        getWritableDatabase().insert(TABELA, null,values);
    }

    public void alteraContato(ContatoInfo c){

        ContentValues values = new ContentValues();

        values.put("id", c.getId());
        values.put("nome", c.getNome());
        values.put("referencia",c.getRef());
        values.put("email", c.getEmail());
        values.put("endereco", c.getEmail());
        values.put("telefone", c.getTelefone());
        values.put("foto", c.getFoto());

        String[] idParaSerAlterado = {c.getId().toString()};
        getWritableDatabase().update(TABELA,values,"id=?",idParaSerAlterado);
    }

    public void apagarContato(ContatoInfo c ){
        SQLiteDatabase db = getWritableDatabase();
        String []args = {c.getId().toString()};
        db.delete(TABELA, "id=?", args);
    }
}
