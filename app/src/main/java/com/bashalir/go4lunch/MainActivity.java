package com.bashalir.go4lunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bashalir.go4lunch.Api.UserHelper;
import com.bashalir.go4lunch.Controllers.Activities.PageActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    public static final int RC_SIGN_IN = 123;

    protected FirebaseUser getCurrentUser () {return FirebaseAuth.getInstance().getCurrentUser();}


    @BindView(R.id.logo_iv)
    ImageView logo;
    @BindView(R.id.slogan_tv)
    TextView slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        slogan.setText(R.string.slogan);

        this.startSignInActivity();
    }

    private void startSignInActivity() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.FacebookBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                this.createUserInFirestore(user);


                this.startPageActivity();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

    private void createUserInFirestore(FirebaseUser user) {


            if (user != null){

                String urlPicture = (user.getPhotoUrl() != null) ? user.getPhotoUrl().toString() : null;
                String username = user.getDisplayName();
                String uid = user.getUid();
                String idRestaurant=null;

                UserHelper.createUser(uid, username, urlPicture, idRestaurant).addOnFailureListener(this.onFailureListener());
            }

    }

    private void startPageActivity() {
        Intent intent = new Intent(this, PageActivity.class);
        startActivity(intent);
    }


    // --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener(){
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), getString(R.string.fui_error_unknown), Toast.LENGTH_LONG).show();
            }
        };
    }



}
