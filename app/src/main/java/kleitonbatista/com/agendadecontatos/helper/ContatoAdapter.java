package kleitonbatista.com.agendadecontatos.helper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import kleitonbatista.com.agendadecontatos.ContatoInfo;
import kleitonbatista.com.agendadecontatos.R;

/**
 * Created by kleitonbatista on 15/12/2017.
 */

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContactViewHoleder> {

    private List<ContatoInfo> contatos;

    public ContatoAdapter(List<ContatoInfo> lista){
        this.contatos = lista;
    }

    @Override
    public ContactViewHoleder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_contato,parent,false);
        return new ContactViewHoleder(v);
    }

    @Override
    public void onBindViewHolder(ContactViewHoleder holder, int position) {
        ContatoInfo c = contatos.get(position);
        holder.nome.setText(c.getNome());
        holder.referencia.setText(c.getRef());
        holder.telefone.setText(c.getTelefone());

        File imgFile = new File(c.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    static class ContactViewHoleder extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nome;
        TextView referencia;
        TextView telefone;

        ContactViewHoleder(View v){
            super(v);
            telefone = v.findViewById(R.id.textoFone);
            nome = v.findViewById(R.id.textoNome);
            referencia = v.findViewById(R.id.textoRef);
            foto = v.findViewById(R.id.fotoContato);
        }
    }
}
