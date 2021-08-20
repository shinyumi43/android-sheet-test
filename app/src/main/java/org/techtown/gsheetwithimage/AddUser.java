package org.techtown.gsheetwithimage;

import static org.techtown.gsheetwithimage.Configuration.ADD_USER_URL;
import static org.techtown.gsheetwithimage.Configuration.KEY_ACTION;
import static org.techtown.gsheetwithimage.Configuration.KEY_ID;
import static org.techtown.gsheetwithimage.Configuration.KEY_IMAGE;
import static org.techtown.gsheetwithimage.Configuration.KEY_NAME;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddUser extends AppCompatActivity implements View.OnClickListener {

    // 학번, 이름, 이미지, 이미지 추가 및 제출 버튼
    private EditText editTextUserName;
    private EditText editTextUserId;
    private ImageView imageViewUserImage;
    private Button buttonAddUser,buttonAddImage;
    String userImage;

    private final int PICK_IMAGE_REQUEST = 1;

    Bitmap rbitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        editTextUserId = (EditText) findViewById(R.id.et_uid);
        editTextUserName = (EditText) findViewById(R.id.et_uname);
        imageViewUserImage=(ImageView)findViewById(R.id.iv_uphoto);


        buttonAddUser = (Button) findViewById(R.id.btn_add_user);
        buttonAddImage = (Button) findViewById(R.id.btn_image);

        buttonAddImage.setOnClickListener(this);
        buttonAddUser.setOnClickListener(this);
    }


    // 이미지 조절
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);

    }


    // 이미지를 string 형태로 get
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }

    // google spread sheet 전송
    private void addUser(){
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        final String userId = editTextUserId.getText().toString().trim();
        final String userName = editTextUserName.getText().toString().trim();

        Log.e("null","values"+userImage);

        // post 방식으로 전송
        StringRequest stringRequest = new StringRequest(Request.Method.POST,ADD_USER_URL,
                response -> {
                    loading.dismiss();
                    Toast.makeText(AddUser.this,response,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(AddUser.this,error.toString(),Toast.LENGTH_LONG).show()){
            @Override
            protected Map<String,String> getParams(){
                // 전송할 값
                Map<String,String> params = new HashMap<>();
                params.put(KEY_ACTION,"insert");
                params.put(KEY_ID,userId);
                params.put(KEY_NAME,userName);
                params.put(KEY_IMAGE,userImage);

                return params;
            }

        };

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);
    }

    // 선택된 이미지를 띄운 상태로 화면 전환
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                // 갤러리 및 드라이브로부터 bitmap get
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                rbitmap = getResizedBitmap(bitmap,250); // bitmap 형식의 이미지를 imageview 표시하기 위해 크기 조절
                userImage = getStringImage(rbitmap);
                imageViewUserImage.setImageBitmap(rbitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 제출 및 이미지 추가 버튼에 따른 동작 수행
    @Override
    public void onClick(View v) {
        if(v == buttonAddUser){
            addUser();
        }
        if(v == buttonAddImage){
            showFileChooser();
        }

    }
}