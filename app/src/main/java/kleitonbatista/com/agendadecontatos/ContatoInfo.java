package kleitonbatista.com.agendadecontatos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by kleitonbatista on 12/12/2017.
 */

public class ContatoInfo implements Parcelable{
    private String nome = "";
    private String ref = "";
    private String email = "";
    private String telefone = "";
    private String endereco = "";
    private String foto = "";
    private Long id = -1L;


    public ContatoInfo(Parcel in){
        String[] data = new String[7];
        in.readStringArray(data);
        setNome(data[0]);
        setFoto(data[1]);
        setEmail(data[2]);
        setEndereco(data[3]);
        setTelefone(data[4]);
        setRef(data[5]);
        setId(Long.parseLong(data[6]));

    }
    public ContatoInfo(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{
                getNome(),getFoto(),getEmail(),getEndereco(),getTelefone(),getRef(),String.valueOf(getId())
        });
    }
    public static final Parcelable.Creator<ContatoInfo> CREATOR = new Parcelable.Creator<ContatoInfo>(){

        @Override
        public ContatoInfo createFromParcel(Parcel parcel) {
            return new ContatoInfo(parcel);
        }

        @Override
        public ContatoInfo[] newArray(int i) {
            return new ContatoInfo[i];
        }
    };
}
