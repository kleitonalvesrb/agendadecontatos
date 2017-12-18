package kleitonbatista.com.agendadecontatos;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    private View layout;
    private ContatoInfo contato;

    private ImageButton foto;
    private EditText nome;
    private EditText ref;
    private EditText telefone;
    private EditText email;
    private EditText endereco;

    private Button salvar;

    private final int CAMERA = 1 ;
    private final int GALERIA = 2;

    private final String IMAGE_DIR = "/FotosContatos";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        contato = (ContatoInfo) getIntent().getParcelableExtra("contato");
        layout = findViewById(R.id.mainLayout);

        foto = findViewById(R.id.foto);
        nome = findViewById(R.id.editNome);
        ref = findViewById(R.id.editReferencia);
        telefone = findViewById(R.id.editFone);
        email = findViewById(R.id.editEmail);
        endereco = findViewById(R.id.editEndereco);

        nome.setText(contato.getNome());
        ref.setText(contato.getRef());
        telefone.setText(contato.getTelefone());
        email.setText(contato.getEmail());
        endereco.setText(contato.getTelefone());
        File imgFile = new File(contato.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.btnSalvar);


        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertaImagem();
            }
        });



        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contato.setNome(nome.getText().toString());
                contato.setEmail(email.getText().toString());
                contato.setRef(ref.getText().toString());
                contato.setEndereco(endereco.getText().toString());
                contato.setTelefone(telefone.getText().toString());

                if (!contato.getNome().isEmpty()){
                    Intent i = new Intent();
                    i.putExtra("contato",contato);
                    setResult(RESULT_OK,i);
                    finish();
                }else{
                    Toast.makeText(EditActivity.this,"É necessário um nome para salvar!",Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    private void alertaImagem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a fonte da imagem");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clicaTirarFoto();
            }
        });

        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               clicaCarregarImagem();
            }
        });

        builder.create().show();
    }

    private void clicaTirarFoto(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }else{
            showCamera();
        }
    }

    /**
     * Pede permissão para acessar a camera
     */
    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
            Snackbar.make(layout, "É necessário permitir para utilizar a câmera!", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA);
                }
            }).show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE},CAMERA);
        }
    }
    private void showCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    /**
     * GELERIA
     */
    private void clicaCarregarImagem(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestGaleriaPermission();
        }else{
            showGaleria();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA :{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaTirarFoto();
                }
                break;
            }
            case GALERIA:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaCarregarImagem();
                }
                break;
            }
        }
    }

    /**
     * Pede permissão para acessar a galeria
     */
    private void requestGaleriaPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Snackbar.make(layout, "É necessário permitir para utilizar a galeria!", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ActivityCompat.requestPermissions(EditActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},GALERIA);
                }
            }).show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},GALERIA);
        }
    }
    private void showGaleria(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,GALERIA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED || data == null){
            return;
        }
        if(requestCode == GALERIA){
            Uri contentUri = data.getData();
            try {
                Bitmap bitmap;
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentUri);
                contato.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }else if(requestCode == CAMERA){
            Bitmap bitmap =(Bitmap) data.getExtras().get("data");
            contato.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);
        }
    }


    /**
     * Salva a imagem na galeria
     * @param bitmap
     * @return
     */
    private String saveImage(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytes);
        File diretorio = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);
        if(!diretorio.exists()){
            diretorio.mkdirs();
        }
        try {
            File f = new File(diretorio, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            /**
             * Permite que a imagem possa ser vista pela galeria.
             */
            MediaScannerConnection.scanFile(this,new String[]{f.getPath()},new String[]{"image/jpeg"},null);
            fo.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
