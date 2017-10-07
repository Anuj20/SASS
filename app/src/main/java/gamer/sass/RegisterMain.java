package gamer.sass;

import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gamer on 8/26/2017.
 */

public class RegisterMain extends AppCompatActivity {

    private FirebaseDatabase DbFirebase;
    private DatabaseReference Dbreference;
    private FirebaseStorage storageFirebase;
    private StorageReference storagereference;

    private EditText name, age, email, password, phone;
    private ImageView profilepic;
    private Button register;

    Bitmap myBitmap;
    Uri picUri;
    private Uri downloaduri;

    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE = 12;
    private static final int PICK_FROM_CAMERA = 1;
    private int PICK_FROM_FILE= 2;
    private DialogInterface.OnClickListener listener;
    private ProgressDialog progress;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;



    private FirebaseAuth Dbauth;

    String clientname, clientage, clientemail, clientpassword, clientphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_main);

        progress= new ProgressDialog(this);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        Dbauth = FirebaseAuth.getInstance();

        DbFirebase = FirebaseDatabase.getInstance();
        storageFirebase = FirebaseStorage.getInstance();
        Dbreference = DbFirebase.getReference().child("Profile_Detail");
        storagereference= storageFirebase.getReference().child("Profile_pics");

        name = (EditText) findViewById(R.id.reg_name);
        age = (EditText) findViewById(R.id.reg_age);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        phone = (EditText) findViewById(R.id.reg_contact);

        profilepic = (ImageView) findViewById(R.id.reg_profilepic);
        profilepic.setImageResource(R.drawable.sass);

        register = (Button) findViewById(R.id.reg_regbutton);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[2]) == PackageManager.PERMISSION_GRANTED)
                {
                    //do something
                    startActivityForResult(getPickImageChooserIntent(), 200);
                }
                else if(ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[1])
                            || ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[2])){
                        //Show Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this);
                        builder.setTitle("Need Multiple Permissions");
                        builder.setMessage("This app cannot function without Camera and Storage permissions.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ActivityCompat.requestPermissions(RegisterMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else if (permissionStatus.getBoolean(permissionsRequired[0],false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this);
                        builder.setTitle("Need Multiple Permissions");
                        builder.setMessage("This app needs Camera and storage permissions.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and storage", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                finish();
                            }
                        });
                        builder.show();
                    }  else {
                        //just request the permission
                        ActivityCompat.requestPermissions(RegisterMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }

                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(permissionsRequired[0],true);
                    editor.commit();
                } else {
                    //You already have the permission, just go ahead.
                    proceedAfterPermission();
                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clientname= name.getText().toString().trim();
                clientage= age.getText().toString().trim();
                clientemail= email.getText().toString().trim();
                clientpassword= password.getText().toString().trim();
                clientphone= phone.getText().toString();

                if(TextUtils.isEmpty(clientname) ||TextUtils.isEmpty(clientage) || TextUtils.isEmpty(clientemail)
                        || TextUtils.isEmpty(clientpassword) || TextUtils.isEmpty(clientphone)){
                    Toast.makeText(getApplicationContext(), "One or more fields empty", Toast.LENGTH_LONG).show();
                }
                else {
                    progress.show();
                    createuser();
                }

            }
        });
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);


        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]==PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                proceedAfterPermission();
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(RegisterMain.this,permissionsRequired[2])){
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterMain.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(RegisterMain.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap bitmap;
        if (resultCode == Activity.RESULT_OK) {

            if (getPickImageResultUri(data) != null) {
                picUri = getPickImageResultUri(data);

                try {
                    myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                    myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    myBitmap = getResizedBitmap(myBitmap, 500);

                    profilepic.setImageBitmap(myBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bitmap = (Bitmap) data.getExtras().get("data");
                myBitmap = bitmap;
                profilepic.setImageBitmap(myBitmap);

            }

        }
        else  if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }

    }

    private void proceedAfterPermission() {
        Toast.makeText(getBaseContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(RegisterMain.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }


    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree){
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.

     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("pic_uri", picUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        picUri = savedInstanceState.getParcelable("pic_uri");
    }

    private void createuser() {
        Dbauth.createUserWithEmailAndPassword(clientemail, clientpassword)
                .addOnCompleteListener(RegisterMain.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UploadDetials();
                            progress.dismiss();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            progress.hide();
                            Toast.makeText(RegisterMain.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void UploadDetials() {
        StorageReference picref = storagereference.child(picUri.getLastPathSegment());
        //upload file to firebase
        picref.putFile(picUri).addOnSuccessListener
                (new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size,
                        // content-type, and download URL.
                        downloaduri = taskSnapshot.getDownloadUrl();
                        Userdetails userdetails = new Userdetails(clientname, clientphone,
                                clientage, clientemail, clientpassword, downloaduri.toString());

                        Dbreference.push().setValue(userdetails);

                        name.setText("");
                        age.setText("");
                        email.setText("");
                        //prod_image.setImageBitmap(R.drawable.hardwork);
                        password.setText("");
                        phone.setText("");
                        //Toast.makeText(RegisterMain.this, "Registeration successful", Toast.LENGTH_SHORT).show();
                    }
                });
        final FirebaseUser user = Dbauth.getCurrentUser();
        final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(clientname).setPhotoUri(downloaduri).build();
        user.updateProfile(profileUpdates);
        user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterMain.this,
                            "Verification email sent to " + user.getEmail(),
                            Toast.LENGTH_SHORT).show();
                    updateUI(user);
                } else {
                    // Log.e(TAG, "sendEmailVerification", task.getException());
                    progress.hide();
                    Toast.makeText(RegisterMain.this,"Failed to send verification email.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(getApplicationContext(), "Registeration successful! Welcome", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Homepage.class));
            progress.hide();
        } else
            progress.hide();
        Toast.makeText(getApplicationContext(), "Register failed!!\n" + "It may have occured because:\n"+
                "1).Your phone does not have an active internet connection\n"+ "2).You have entered incorrect details\n"+
                "3) You are already registered with SASS", Toast.LENGTH_LONG).show();
    }
}
