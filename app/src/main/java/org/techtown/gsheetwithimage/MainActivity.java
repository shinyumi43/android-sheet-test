package org.techtown.gsheetwithimage;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // 인증 버튼
    Button addUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addUser=(Button)findViewById(R.id.btn_add_user);

        // addUser 화면 전환
        addUser.setOnClickListener(view -> {

            Intent intent = new Intent(getApplicationContext(),AddUser.class);
            startActivity(intent);
        });
    }
}