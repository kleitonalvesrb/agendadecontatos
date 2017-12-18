package kleitonbatista.com.agendadecontatos;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import kleitonbatista.com.agendadecontatos.DAO.ContatoDAO;
import kleitonbatista.com.agendadecontatos.helper.ContatoAdapter;
import kleitonbatista.com.agendadecontatos.helper.RecyclerItemClickListener;

public class MainActivity extends AppCompatActivity {

    private ContatoDAO helper;
    private List<ContatoInfo> contatos;

    private RecyclerView contatosRecycle;
    private ContatoAdapter adapter;


    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,EditActivity.class);

                i.putExtra("contato", new ContatoInfo());
//                startActivity(i);
                startActivityForResult(i,REQUEST_NEW);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        helper = new ContatoDAO(this);
        contatos = helper.getList("ASC");
        contatosRecycle = findViewById(R.id.contatosRecycle);
        contatosRecycle.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contatosRecycle.setLayoutManager(llm);
        adapter = new ContatoAdapter(contatos);
        contatosRecycle.setAdapter(adapter);


        contatosRecycle.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                abrirOpcoes(contatos.get(position));
            }
        }));
    }

    private void abrirOpcoes(final ContatoInfo contatoInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contatoInfo.getNome());
        builder.setItems(new CharSequence[]{"Ligar", "Editar", "Excluir"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                //ligar
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + contatoInfo.getTelefone()));
                                startActivity(intent);
                                break;
                            case 1:
                                //editar
                                Intent intentEditar = new Intent(MainActivity.this,EditActivity.class);
                                intentEditar.putExtra("contato", contatoInfo);
                                startActivityForResult(intentEditar,REQUEST_ALTER);
                                break;
                            case 2:
                                //excluir
                                contatos.remove(contatoInfo);
                                helper.apagarContato(contatoInfo);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(this,"AKI",Toast.LENGTH_SHORT).show();

        if(requestCode == REQUEST_NEW && resultCode == RESULT_OK){
            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.inserirContato(contatoInfo);
            contatos = helper.getList("ASC");
            adapter = new ContatoAdapter(contatos);
            contatosRecycle.setAdapter(adapter);
        }else if (requestCode == REQUEST_ALTER){
            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.alteraContato(contatoInfo);
            contatos = helper.getList("ASC");
            adapter = new ContatoAdapter(contatos);
            contatosRecycle.setAdapter(adapter);

            //alterar contato
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        String order = "ASC";
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.order_az) {
            order = "ASC";

        }else if (id == R.id.order_za){
            order = "DESC";
        }
        contatos = helper.getList(order);
        for(ContatoInfo c : contatos){
            Log.d("Aki",c.getNome());

        }
        helper = new ContatoDAO(this);

        contatos = helper.getList(order);
        adapter = new ContatoAdapter(contatos);
        contatosRecycle.setAdapter(adapter);



        adapter.notifyDataSetChanged();
        contatosRecycle.setAdapter(adapter);


        return true;
    }
}
