package com.example.retrofit_networking;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText name,email,about;
    String date_time="";
    Button btn;
    Uri mUri;
    String url="";
    CircleImageView dp;
    TextView status;
    int x=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        listView=findViewById(R.id.listView);
        dp=findViewById(R.id.imageView);
        name=findViewById(R.id.Name);
        email=findViewById(R.id.Email);
        about=findViewById(R.id.About);
        btn=findViewById(R.id.button);
        status=findViewById(R.id.status);

        Intent intent=getIntent();
        if(intent.getStringExtra("name")!=null){
            btn.setText("Update Profile");
            status.setText("Update Profile");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                LocalDateTime dateTime = LocalDateTime.now();
                date_time=dateTime.toString();
            }
            String name1=intent.getStringExtra("name");
            String email1=intent.getStringExtra("email");
            String about1=intent.getStringExtra("about");
            String _id1=intent.getStringExtra("_id");
            x=intent.getIntExtra("pointer",-1);
            url=intent.getStringExtra("img_url");
           if(!url.equals("noImage")){
               Picasso.get().load(url).fit().into(dp);
           }


            name.setText(name1);
            email.setText(email1);
            about.setText(about1);
            btn.setOnClickListener(view -> updateData(_id1,url,x));
        }
        else{
            btn.setOnClickListener(view -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDateTime dateTime = LocalDateTime.now();
                    date_time=dateTime.toString();
                }
                uploadData();
            });
        }


        dp.setOnClickListener(view -> uploadDp());

    }

    private void updateData(String id1, String url,int x) {
        ProgressDialog pb=new ProgressDialog(this);
        pb.setMessage("Updating Data..");
        pb.setCancelable(false);
        pb.show();
        ApiSet apiSet=ApiController.getInstance().updateData();
        Post_model pmm=new Post_model(name.getText().toString(),email.getText().toString(),about.getText().toString(),url,
                date_time,id1);
        Call<Post_model> call=apiSet.updateData(pmm);
        call.enqueue(new Callback<Post_model>() {
            @Override
            public void onResponse(Call<Post_model> call, Response<Post_model> response) {
                if(response.body().getMessage_code()==201){
                    Toast.makeText(MainActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                    WaytoHome(x);
                }

            }
            @Override
            public void onFailure(Call<Post_model> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadData() {
        ProgressDialog pb=new ProgressDialog(this);
        pb.setMessage("Posting Data..");
        pb.setCancelable(false);
        pb.show();
        ApiSet apiSet=ApiController.getInstance().postData();
        Post_model post_model;
        if(mUri!=null){
            post_model=new Post_model(name.getText().toString(),email.getText().toString(),about.getText().toString(),
                    url,date_time);
        }
        else {
            post_model=new Post_model(name.getText().toString(),email.getText().toString(),about.getText().toString(),
                    "noImage",date_time);
        }
        Call<Post_model> call=apiSet.postData(post_model);
        call.enqueue(new Callback<Post_model>() {

            @Override
            public void onResponse(Call<Post_model> call, Response<Post_model> response) {
                pb.dismiss();
                Log.i("message recived", response.toString());
                Log.i("code", "onResponse: "+response.body().getMessage_code());
                if(response.body().getMessage_code()== 200){
                    Toast.makeText(MainActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    WaytoHome(x);
                }
                else if(response.body().getMessage_code()== 100){
                    Toast.makeText(MainActivity.this, "unable to upload", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post_model> call, Throwable t) {
                pb.dismiss();
                Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                Log.i("failed", "onFailure: "+t.toString());
            }
        });

    }

    private void WaytoHome(int x) {

        Intent intent=new Intent(MainActivity.this,HomeScreen.class);
        intent.putExtra("pointer",x);
        startActivity(intent);
        finish();

    }

    private void uploadDp() {
//        Dexter.withActivity(MainActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
//            @Override
//            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//            }
//
//            @Override
//            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//                Toast.makeText(MainActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                permissionToken.continuePermissionRequest();
//            }
//        });
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode==101 && data!=null){
                mUri=data.getData();
                dp.setImageURI(mUri);
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Image uploading");
        progressDialog.show();
        FirebaseStorage.getInstance().getReference().child(mUri.toString()).putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
                FirebaseStorage.getInstance().getReference().child(mUri.toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                       url=uri.toString();
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                float percentage=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                progressDialog.setMessage("uploaded "+(int) percentage+" %");

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(MainActivity.this, "Firebase Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });



    }
}