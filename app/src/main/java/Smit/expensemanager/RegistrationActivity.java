package Smit.expensemanager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mpass;
    private Button btnReg;
    private TextView mSignin;

    private ProgressDialog mDialog;

    // Firebase Authentication

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        registration();
        mAuth=FirebaseAuth.getInstance();
        mDialog=new ProgressDialog(this);

    }


    private void registration(){

        mEmail=findViewById(R.id.email_reg);
        mpass=findViewById(R.id.password_reg);
        btnReg=findViewById(R.id.btn_reg);
        mSignin=findViewById(R.id.signin_here);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mEmail.getText().toString().trim();
                String pass=mpass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email Not Valid");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    mpass.setError("Password is not Valid");
                    return;
                }

                mDialog.setMessage("Processing");


                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            mDialog.dismiss();

                            Toast.makeText(getApplicationContext(),"Registration Completed!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));

                        }else {
                            mDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Registration Unsuccessful :(",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
}
