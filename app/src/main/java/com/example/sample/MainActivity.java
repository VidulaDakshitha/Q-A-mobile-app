package com.example.sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

   // ImageButton btn;
    TextView modelTextview;
    TextView modelTextview2;

    LoginButton loginButton;
    CircleImageView circleImage;
     TextView txtname,txtEmail;

     CallbackManager callback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // btn=findViewById(R.id.btnlogin);
        modelTextview = findViewById(R.id.forgot);
        modelTextview2 = findViewById(R.id.signup);

        loginButton=findViewById(R.id.login_button);
        txtname=findViewById(R.id.logname);
        txtEmail=findViewById(R.id.logemail);
        circleImage=findViewById(R.id.circleImageView);

        callback=CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("email","public_profile"));


        loginButton.registerCallback(callback, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

                Intent i=new Intent(MainActivity.this,register.class);

                startActivity(i);

            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        callback.onActivityResult(requestCode,resultCode,data);

        super.onActivityResult(requestCode, resultCode, data);

    }


    AccessTokenTracker tokentracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(currentAccessToken==null)
            {
                txtname.setText("");
                txtEmail.setText("");
                circleImage.setImageResource(0);
                Toast.makeText(MainActivity.this,"user logged out",Toast.LENGTH_LONG).show();
            }

            else
            {
                loadUserProfile(currentAccessToken);
            }

        }
    };

    private void loadUserProfile(AccessToken newAccessToken)
    {
        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {


                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");

                    String image_url="https://graph.facebook.com/"+id+"/picture?type=normal";

                    txtEmail.setText(email);
                    txtname.setText(first_name+" "+last_name);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();

                    Glide.with(MainActivity.this).load(image_url).into(circleImage) ;

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();


    }

    @Override
    protected void onResume() {
        super.onResume();

        //btn.setOnClickListener(new View.OnClickListener() {
          //  @Override
            //public void onClick(View view) {

               // Intent i=new Intent(MainActivity.this,act2.class);

                //startActivity(i);
        //    }
        //});

        modelTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2=new Intent(MainActivity.this,forgotPassword.class);
                startActivity(i2);


            }
        });

        modelTextview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2=new Intent(MainActivity.this,register.class);
                startActivity(i2);


            }
        });


    }


}
