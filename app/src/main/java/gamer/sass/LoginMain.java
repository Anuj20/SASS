package gamer.sass;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginMain extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "userDetails";
    private FirebaseAuth Dbauth;

    GoogleApiClient googleApiClient;
    EditText Email, password;
    Button Login;
    TextView signuptext, resetpassword;
    private int RC_SIGN_IN = 112;
    SignInButton google;
    LoginButton loginButton;

    CallbackManager callbackManager;

    private ProgressBar progress_login;

    String userpassword, useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        progress_login= (ProgressBar) findViewById(R.id.progress_login);

        Dbauth = FirebaseAuth.getInstance();

        Email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.pass);
        google = (SignInButton) findViewById(R.id.googlesignin);
        loginButton = (LoginButton) findViewById(R.id.button_fbsignin);
        Login = (Button) findViewById(R.id.login_button);
        signuptext = (TextView) findViewById(R.id.Signup_text);
        resetpassword= (TextView) findViewById(R.id.forgotpassword);

        useremail= Email.getText().toString();
        userpassword = password.getText().toString();

        loginButton.setOnClickListener(this);
        google.setOnClickListener(this);
        Login.setOnClickListener(this);
        signuptext.setOnClickListener(this);
        resetpassword.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                // .requestScopes(new Scope(Scopes.PLUS_LOGIN)) // "https://www.googleapis.com/auth/plus.login"
                // .requestScopes(new Scope(Scopes.PLUS_ME)) // "https://www.googleapis.com/auth/plus.me"
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(LoginMain.this).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginMain.this, "connection failed!", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = Dbauth.getCurrentUser();
        if (user != null) {
            startActivity(new Intent(this, Homepage.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
        finish();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.googlesignin:
                GoogleSignin();
                break;

            case R.id.login_button:
                NativeSignin();
                break;

            case R.id.button_fbsignin:
                FbSignin();
                break;

            case R.id.Signup_text:
                startActivity(new Intent(this, RegisterMain.class));
                break;

            case R.id.forgotpassword:
                Passwordreset();
                break;
        }
    }

    private void Passwordreset() {
        useremail= Email.getText().toString();

        if(useremail.compareToIgnoreCase("")==0){
            Toast.makeText(this, "please enter the registered user email", Toast.LENGTH_SHORT).show();
        }else {
            progress_login.setVisibility(View.VISIBLE);
            Dbauth.sendPasswordResetEmail(useremail).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        progress_login.setVisibility(View.INVISIBLE);
                        Log.d(TAG, "Email sent.");
                        Toast.makeText(LoginMain.this, "password reset Email successfully sent to: " + useremail, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void NativeSignin() {
        useremail= Email.getText().toString();
        userpassword = password.getText().toString();

        if(useremail.compareToIgnoreCase("")== 0 || userpassword.compareToIgnoreCase("") == 0){
            Toast.makeText(this, "please enter the registered user email or password", Toast.LENGTH_SHORT).show();
        }else {
            progress_login.setVisibility(View.VISIBLE);
            Dbauth.signInWithEmailAndPassword(useremail, userpassword)
                    .addOnCompleteListener(LoginMain.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Log.d(TAG, "createUserWithEmail:success");
                                progress_login.setVisibility(View.GONE);
                                FirebaseUser user = Dbauth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    private void GoogleSignin() {
        progress_login.setVisibility(View.VISIBLE);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        progress_login.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        Dbauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = Dbauth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    private void FbSignin() {
        progress_login.setVisibility(View.VISIBLE);
        FacebookSdk.isInitialized();
        callbackManager = CallbackManager.Factory.create();
        //loginButton.setReadPermissions("email");
        //loginButton.setReadPermissions("user_friends");
        LoginManager.getInstance().logInWithReadPermissions(LoginMain.this, Arrays.asList("public_profile", "email", "user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
                //Profile prof= Profile.getCurrentProfile();
                // String email= prof.getProperty("email").toString();
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject me, GraphResponse response) {
                                if (response.getError() != null) {
                                    // handle error
                                    Toast.makeText(LoginMain.this, "error fetching email", Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                } else {
                                    try {
                                        String email = response.getJSONObject().get("email").toString();
                                        Log.e("Result", email);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //String id = me.optString("id");
                                    // send email and id to your web server
                                    // Log.e("Result1", response.getRawResponse());
                                    // Log.e("Result", me.toString());
                                }
                            }
                        }).executeAsync();
            }
            @Override
            public void onCancel() {
                progress_login.setVisibility(View.GONE);
                Toast.makeText(LoginMain.this, "Login Cancelled!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                progress_login.setVisibility(View.GONE);
                Toast.makeText(LoginMain.this, "Error Logging In!! Please check your internet connection",
                        Toast.LENGTH_LONG).show();

            }
        });
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Dbauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(LoginMain.this, Homepage.class));
                            finish();
                            Toast.makeText(LoginMain.this, "Login Success! Welcome", Toast.LENGTH_SHORT).show();
                            progress_login.setVisibility(View.GONE);

                        } else {
                            progress_login.setVisibility(View.GONE);
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginMain.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            progress_login.setVisibility(View.VISIBLE);
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }else
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(LoginMain.this, "Login Success! Welcome", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Homepage.class));
            finish();
            progress_login.setVisibility(View.GONE);
        } else if (user == null) {
            progress_login.setVisibility(View.GONE);
            Toast.makeText(LoginMain.this, "login failed!!\n" + "It may have occured because:\n" +
                    "1).Your phone does not have an active internet connection\n" + "2).Your are not registered with us\n"
                    + "3).You have entered incorrect login credentials", Toast.LENGTH_LONG).show();
        }
    }

}

