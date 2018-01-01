package com.codemybrainsout.imageviewer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import butterknife.BindView;

/**
 * Created by ahulr on 23-06-2017.
 */

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;

    private static final int FACEBOOK = 0;

    private static final int GOOGLE = 1;

    @BindView(R.id.facebook_login_button)
    LoginButton facebookLoginButton;

    private String googleToken = "";

    private int selectedLogin = -1;

    /* Should we automatically resolve ConnectionResults when possible? */
    private boolean googleShouldResolve = false;

    private CallbackManager facebookCallbackManager;

    private ProgressDialogHelper progressDialogHelper = new ProgressDialogHelper();

    /* Client used to interact with Google APIs. */
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Prepare facebook manager
        facebookCallbackManager = CallbackManager.Factory.create();

        // Prepare google api manager
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PLUS_LOGIN))
                .build();

        if (googleApiClient.isConnected()) {
            googleApiClient.clearDefaultAccountAndReconnect();
            Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient);
            googleApiClient.disconnect();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setupViews();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(googleApiClient);
            googleApiClient.disconnect();
        }
    }

    private void setupViews() {
        facebookLoginButton.setReadPermissions("public_profile", "email", "user_friends");
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookLoginResult(loginResult);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                ToastHelper.showError(LoginActivity.this);
            }
        });
    }

    void handleFacebookLoginResult(LoginResult loginResult) {
        progressDialogHelper.show(this);
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), (json, response) -> {
                    progressDialogHelper.dismiss();
                    FacebookMe me = FacebookMe.fromJson(json, FacebookMe.class);
                    if (Profile.getCurrentProfile() != null) {
                        Uri picture = Profile.getCurrentProfile().getProfilePictureUri(640, 640);
                        if (picture != null) {
                            me.setImageUrl(picture.toString());
                        }
                    }
                    authorize(loginResult.getAccessToken(), me);
                });
        Bundle params = new Bundle();
        params.putString("fields", "name,email");
        request.setParameters(params);
        request.executeAsync();
    }

    private void authorizeGoogle(GoogleMe me) {
        int providerId = ProviderManager.getCurrentProvider().getId();
        final AuthParams params = new AuthParams.Builder()
                .providerId(providerId)
                .identityProvider(IdentityProvider.GOOGLE)
                .idToken(googleToken)
                .build();

        progressDialogHelper.show(this);
        new AuthService().authorizations(params)
                .subscribe(pair -> {
                    progressDialogHelper.dismiss();

                    Response<AuthToken> authTokenResponse = pair.first;
                    Response<User> userResponse = pair.second;

                    if (authTokenResponse.statusCode() == Header.StatusCode.UNPROCESSABLE_ENTITY) {
                        openProviderLoginActivityWithGooleEmailAddress(me);
                    } else if (!authTokenResponse.isSuccessful()) {
                        ToastHelper.show(this, authTokenResponse.body());
                    } else {
                        //do nothing
                    }
                });
    }

    private void authorize(AccessToken accessToken, FacebookMe me) {

        int providerId = ProviderManager.getCurrentProvider().getId();
        final AuthParams params = new AuthParams.Builder()
                .providerId(providerId)
                .identityProvider(IdentityProvider.FACEBOOK)
                .token(accessToken.getToken())
                .build();

        progressDialogHelper.show(this);
        new AuthService().authorizations(params)
                .subscribe(pair -> {
                    progressDialogHelper.dismiss();

                    Response<AuthToken> authTokenResponse = pair.first;
                    Response<User> userResponse = pair.second;

                    if (authTokenResponse.statusCode() == Header.StatusCode.UNPROCESSABLE_ENTITY) {
                        //check state of activity
                        openProviderLoginActivityWithFacebookEmailAddress(me);

                    } else if (!authTokenResponse.isSuccessful()) {
                        ToastHelper.show(this, authTokenResponse.body());
                    } else {
                        //do nothing
                    }
                });
    }

    private void openProviderLoginActivityWithGooleEmailAddress(GoogleMe me) {
        ProviderLoginActivity.launch(this, me.getEmail(), IdentityProvider.GOOGLE);
    }

    private void openProviderLoginActivityWithFacebookEmailAddress(FacebookMe me) {
        ProviderLoginActivity.launch(this, me.getEmail(), IdentityProvider.FACEBOOK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (selectedLogin == FACEBOOK) {
            facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (selectedLogin == GOOGLE) {
            if (requestCode == RC_SIGN_IN) {
                // If the error resolution was not successful we should not resolve further.
                if (resultCode != RESULT_OK) {
                    googleShouldResolve = false;
                }
                googleApiClient.connect();
            }
        }
    }

    @OnClick(R.id.facebook_login_button)
    void onFacebookButtonClicked() {
        selectedLogin = FACEBOOK;
    }

    @OnClick(R.id.google_login_button)
    void onGooglePlusClicked() {
        selectedLogin = GOOGLE;
        googleShouldResolve = true;
        progressDialogHelper.show(this);
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // onConnected indicates that an account was selected on the device, that the selected
        // account has granted any requested permissions to our app and that we were able to
        // establish a service connection to Google Play services.
        googleShouldResolve = false;
        progressDialogHelper.show(this);
        if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
            String email = Plus.AccountApi.getAccountName(googleApiClient);
            new RetrieveTokenTask().execute(email);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Could not connect to Google Play Services.  The user needs to select an account,
        // grant permissions or resolve an error in order to sign in. Refer to the javadoc for
        // ConnectionResult to see possible error codes.
        progressDialogHelper.dismiss();
        if (googleShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    progressDialogHelper.show(this);
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    googleApiClient.connect();
                }
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.

                // List of error code: https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult
                GoogleApiAvailability.getInstance().getErrorDialog(this, connectionResult.getErrorCode(), 0).show();
            }
        } else {
            // Show the signed-out UI
            // in our case, do nothing~~
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost. The GoogleApiClient will automatically
        // attempt to re-connect. Any UI elements that depend on connection to Google APIs should
        // be hidden or disabled until onConnected is called again.
        progressDialogHelper.dismiss();

        // For code meaning: https://developers.google.com/android/reference/com/google/android/gms/common/api/GoogleApiClient.ConnectionCallbacks
        ToastHelper.show(this, "Connection suspended code: " + String.valueOf(i));
    }

    private class RetrieveTokenTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        //constructor


        @Override
        protected String doInBackground(String... params) {
            String accountName = params[0];
            String scopes = "audience:server:client_id:" + BuildConfig.GOOGLE_SERVER_ID;
            String token = "";
            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accountName, scopes);
            } catch (IOException | GoogleAuthException e) {
                this.exception = e;
            }
            progressDialogHelper.dismiss();
            return token;
        }

        @Override
        protected void onPostExecute(String token) {
            super.onPostExecute(token);

            if (exception != null) {
                ToastHelper.showError(getApplicationContext());
            } else {
                googleToken = token;

                Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);
                String name = currentPerson.getDisplayName();
                String email = Plus.AccountApi.getAccountName(googleApiClient);
                GoogleMe me = new GoogleMe(name, email);
                authorizeGoogle(me);
            }
        }
    }
}
