package com.example.kennykao;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kennykao.gymhome_2.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private ImageView ivChoosePicture;
    private File file;
    private static final int REQUEST_TAKE_PICTURE = 0;
    private static final int REQUEST_UPLOAD_PICTURE = 1;
    private byte[] image;

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
        ivChoosePicture = (ImageView) findViewById(R.id.ivChoosePicture);
        btUpload = (Button) findViewById(R.id.btUpload);
        btTakeNow = (Button) findViewById(R.id.btTakeNow);

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
                    stus = new StudentsVO(username, "", password, 1, name, userGender, id, email, intro, image, 0);
                    coas = null;
                    mems = new MembersVO(0, username, userStatus.toString(), nickname, 0);
                } else if (userStatus.toString().equals("1")) {
                    stus = null;
                    coas = new CoachesVO(username, "", password, 1, name, userGender, id, email, intro, image, 0);
                    mems = new MembersVO(0, username, userStatus.toString(), nickname, 0);
                } else {
                    Log.d("Signup", "BBBBBBBBBBBBBBBBBBBBBBBBBBBBB: ");
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

    protected void onStart() {
        super.onStart();
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        Common.askPermissions(this, permissions, Common.PERMISSION_READ_EXTERNAL_STORAGE);
    }


    public void onTakeNowClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        Uri contentUri = FileProvider.getUriForFile(
                this,getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQUEST_TAKE_PICTURE);
        } else {
            Toast.makeText(this, R.string.NoCameraFound,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void onTakePictureClick(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        Uri contentUri = FileProvider.getUriForFile(
                this,getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQUEST_TAKE_PICTURE);
        } else {
            Toast.makeText(this, R.string.NoCameraFound,
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Common.PERMISSION_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    btTakeNow.setEnabled(true);
                    btUpload.setEnabled(true);
                } else {
                    btTakeNow.setEnabled(false);
                    btUpload.setEnabled(false);
                }
                break;
        }
    }

    public void onUploadClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_UPLOAD_PICTURE);
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            int newSize = 512;
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE:
                    Bitmap srcPicture = BitmapFactory.decodeFile(file.getPath());
                    Bitmap downsizedPicture = Common.downSize(srcPicture, newSize);
                    ivChoosePicture.setImageBitmap(downsizedPicture);
                    ByteArrayOutputStream out1 = new ByteArrayOutputStream();
                    downsizedPicture.compress(Bitmap.CompressFormat.JPEG, 100, out1);
                    image = out1.toByteArray();
                    break;

                case REQUEST_UPLOAD_PICTURE:
                    Uri uri = intent.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
                        Bitmap srcImage = BitmapFactory.decodeFile(imagePath);
                        Bitmap downsizedImage = Common.downSize(srcImage, newSize);
                        ivChoosePicture.setImageBitmap(downsizedImage);
                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
                        downsizedImage.compress(Bitmap.CompressFormat.JPEG, 100, out2);
                        image = out2.toByteArray();
                    }
                    break;
            }
        }
    }
            }

