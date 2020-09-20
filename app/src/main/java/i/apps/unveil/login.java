package i.apps.unveil;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.sql.Array;
import java.util.Arrays;
import java.util.List;

public class login extends AppCompatActivity {

    ImageView logo;
    ImageView fbBtn;
    ImageView googleBtn;
    ImageView phoneBtn;

    private CallbackManager mCallBackManager;
    private FirebaseAuth mFireBaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;


    TextView tnc;
    View div;

    private GoogleSignInClient mGoogleSignInCLient;
    private String TAG = "login";
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;

    private static final int RC_SIGN_IN1 = 101;//any random number
    private static final String NO_USER_LOGGED_IN = "NO USER LOGGED IN";
    private static final String USER_LOGGED_IN_AS = "USER LOGGED IN AS\n";
    private static final String SIGN_IN = "SIGN IN" ;
    private static final String SIGN_OUT = "SIGN OUT" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mFireBaseAuth = FirebaseAuth.getInstance();
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser()!= null) {


            Intent i = new Intent(getApplicationContext(), Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        if (mFireBaseAuth.getCurrentUser()!= null) {


            Intent i = new Intent(getApplicationContext(), Home.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


        setContentView(R.layout.activity_login);





        logo = findViewById(R.id.logo);
        fbBtn = (ImageView)findViewById(R.id.fbBtn);
        googleBtn =  findViewById(R.id.googleBtn);
        phoneBtn = findViewById(R.id.phoneBtn);
        tnc = findViewById(R.id.tnC);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInCLient = GoogleSignIn.getClient(this,gso);

        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallBackManager = CallbackManager.Factory.create();


        fbBtn.setVisibility(View.INVISIBLE);
        googleBtn.setVisibility(View.INVISIBLE);
        phoneBtn.setVisibility(View.INVISIBLE);
        tnc.setVisibility(View.INVISIBLE);


        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);
                logo.startAnimation(aniSlide);

                Animation btnSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slideup_btn);

                tnc.setVisibility(View.VISIBLE);


                tnc.startAnimation(btnSlide);

                btnSlide.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        fbBtn.setVisibility(View.VISIBLE);
                        googleBtn.setVisibility(View.VISIBLE);
                        phoneBtn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        }, 3000);
        
        
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        LoginManager.getInstance().registerCallback(mCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("FacebookAuthentication","onSuccess"+loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FacebookAuthentication","onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FacebookAuthentication","onError"+error);

            }
        });

        phoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogin();
            }
        });

        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(login.this, Arrays.asList("public_profile", "user_friends"));

            }
        });
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    updateUI(user);
                }else{
                    updateUI(null);
                }
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    mFireBaseAuth.signOut();
                }
            }
        };





    }



        private void showLogin() {
            //Show Login Screen here
            // Choose authentication providers
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.PhoneBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN1);
        }



    private void handleFacebookToken(AccessToken accessToken) {

        Log.d("FacebookAuthentication","handleFacebookToken"+accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFireBaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    Log.d("FacebookAuthentication", "sign in with credential: successful");
                    FirebaseUser user = mFireBaseAuth.getCurrentUser();
                    updateUI(user);
                    startActivity(new Intent(getApplicationContext(),Home.class));

                }else{
                    Log.d("FacebookAuthentication","sign in with credential: failure", task.getException());
                    Toast.makeText(login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInCLient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mCallBackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }

        if (requestCode == RC_SIGN_IN1) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "Sing In Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),Home.class));
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                Toast.makeText(this, "Sign in Failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try{
            GoogleSignInAccount acc = task.getResult(ApiException.class);
            Toast.makeText(this, "Signed In Successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),Home.class));
            FirebaseGoogleAuth(acc);
        }
        catch(ApiException e){
            Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acc) {

        AuthCredential authCredential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    }else{
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
            }
        });
    }

    private void updateUI(FirebaseUser user) {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if(account != null){
            String personName = account.getDisplayName();
            String id = account.getEmail();

            Toast.makeText(this, "Welcome "+personName, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFireBaseAuth.removeAuthStateListener(authStateListener);

    }
}
