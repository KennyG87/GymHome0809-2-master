package com.example.kennykao;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.kennykao.gymhome_2.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static com.example.kennykao.Common.networkConnected;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_SIGNUP = 1;
    //    private AsyncTask SignupTask;
    private ProgressDialog progressDialog;
    private RadioGroup rgStatus;
    private RadioButton rbStudents;
    private RadioButton rbCoaches;
    private Integer userGender = 1, userStatus = 0;
    private Button btSignup;
    private EditText etUser;
    private TextView tvMessage;
    private StudentsVO stus;
    private CoachesVO coas;
    private MembersVO mems;
    private NewMembers newMembers;
    private TextView linktoLogin;
    private EditText etName;
    private EditText etNickname;
    private EditText etPassword;
    private EditText etConfirmPW;
    private RadioGroup rgSex;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private EditText etID;
    private EditText etEmail;
    private EditText etIntro;
    private Button btTakeNow;
    private Button btUpload;
    private Boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "AAAAAAAAAAAAAAAAAAAAAAAAAAA: ");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signup);
        setResult(RESULT_CANCELED);

        rgStatus = (RadioGroup) findViewById(R.id.rgStatus);
        rbCoaches = (RadioButton) findViewById(R.id.rbCoaches);
        rbStudents = (RadioButton) findViewById(R.id.rbStudents);
        btSignup = (Button) findViewById(R.id.btSignup);
        etUser = (EditText) findViewById(R.id.etUser);
        linktoLogin = (TextView) findViewById(R.id.linktoLogin);
        etName = (EditText) findViewById(R.id.etName);
        etNickname = (EditText) findViewById(R.id.etNickname);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etConfirmPW = (EditText) findViewById(R.id.etConfirmPW);
        rgSex = (RadioGroup) findViewById(R.id.rgSex);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        etID = (EditText) findViewById(R.id.etID);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etIntro = (EditText) findViewById(R.id.etIntro);

        rgStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                rbStudents = (RadioButton) group.findViewById(checkedID);
                Common.showToast(getApplicationContext(), (String) rbStudents.getText());
                userStatus = Integer.valueOf((String) rbStudents.getHint());
            }
        });

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedID) {
                rbMale = (RadioButton) group.findViewById(checkedID);
                Common.showToast(getApplicationContext(), (String) rbMale.getText());
                userGender = Integer.valueOf((String) rbMale.getHint());
            }
        });


        linktoLogin.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString().trim();
                String nickname = etNickname.getText().toString().trim();
                String username = etUser.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                Integer sex = rgSex.getCheckedRadioButtonId();
                String id = etID.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String intro = etIntro.getText().toString().trim();
//                Authentication();
                if (userStatus.toString().equals("0")) {
                    stus = new StudentsVO(username, "", password, 1, name, userGender, id, email, intro, null, 0);
                    coas = null;
                    mems = new MembersVO(0, username, userStatus.toString(), nickname, 0);
                } else if (userStatus.toString().equals("1")) {
                    stus = null;
                    coas = new CoachesVO(username, "", password, 1, name, userGender, id, email, intro, null, 0);
                    mems = new MembersVO(0, username, userStatus.toString(), nickname, 0);
                } else {
                    Log.d("LogIn", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB: ");
                    return;
                }
                //Log.e("1233", stus.getStu_acc().toString());
//                Log.e("1233", coas.getCoa_name().toString());


                newMembers = new NewMembers(stus, coas, mems);
               // Log.e("1233", newMembers.toString());
               // Log.e("1233", newMembers.getStudentsVO().getStu_name());
                if (networkConnected(SignupActivity.this)) {
                    new SignupTask().execute(Common.URL + "StudentsServlet",newMembers);
                    Log.d(TAG, "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC: ");


                }
            }
        });



    }

    class SignupTask extends AsyncTask<Object, Object, Object> {

        @Override
        protected Object doInBackground(Object... params) {
            Gson gson = new Gson();
            String url = params[0].toString();
            String jsonIn;
//            NewMembers newm=(NewMembers)params[1];
//            Log.d("13wqeqwe", "++++++++++"+newm.getStudentsVO().getStu_name() );
            String str="";
            str = gson.toJson(newMembers);
            Log.e("1233333333", str);
//            if(role.equals("健身者")){
//                role = "0";
//            }else {
//                role="1";
//            }
            try {
                jsonIn = getRemoteData(url, str);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }

//            Type listType = new TypeToken<NewMembers>() {
//            }.getType();
//            Object obj = gson.fromJson(jsonIn,Object.class);
            return "";

        }

    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder jsonIn = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();
        jsonIn = new StringBuilder();
        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn: " + jsonIn);
        return jsonIn.toString();
    }





    public void studentAutofill(View view) {
        etName.setText("艾健身");
        etNickname.setText("健身狂");
        etUser.setText("Crazyfit");
        etPassword.setText("123");
        etConfirmPW.setText("123");
        etID.setText("A123456789");
        etEmail.setText("crazyfit@gmail.com");
        etIntro.setText("人如其名愛健身94狂");
    }

    public void coachAutofill(View view) {
        etName.setText("艾教你");
        etNickname.setText("IJN");
        etUser.setText("94IJN");
        etPassword.setText("789");
        etConfirmPW.setText("789");
        etID.setText("A234567890");
        etEmail.setText("94ijn@gmail.com");
        etIntro.setText("94愛教你");
    }

            }

