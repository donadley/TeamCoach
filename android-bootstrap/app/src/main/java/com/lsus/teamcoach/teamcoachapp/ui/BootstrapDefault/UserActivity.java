package com.lsus.teamcoach.teamcoachapp.ui.BootstrapDefault;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Constants;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.core.User;
import com.lsus.teamcoach.teamcoachapp.ui.Framework.BootstrapActivity;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.InjectView;
import retrofit.RetrofitError;

import static com.lsus.teamcoach.teamcoachapp.core.Constants.Extra.USER;

public class UserActivity extends BootstrapActivity implements View.OnClickListener {

    @Inject
    BootstrapService bootstrapService;

    @InjectView(R.id.iv_avatar) protected ImageView avatar;
    @InjectView(R.id.tv_name) protected TextView name;
    @InjectView(R.id.tv_userEmail) protected TextView email;
    @InjectView(R.id.tv_phonenumber) protected TextView tv_phonenumber;
    @InjectView(R.id.tv_role) protected TextView role;
    @InjectView(R.id.button_Edit) protected Button button_Edit;
    @InjectView(R.id.button_Submit) protected Button button_Submit;
    @InjectView(R.id.et_name) protected EditText et_name;
    @InjectView(R.id.et_userEmail) protected  EditText et_email;
    @InjectView(R.id.et_phonenubmer) protected EditText et_phonenubmer;
    @InjectView(R.id.tv_roleTag) protected TextView tv_roleTag;

    private User user;
    private SafeAsyncTask<Boolean> authenticationTask;
    private String authToken;
    private Singleton singleton = Singleton.getInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_view);

        if (getIntent() != null && getIntent().getExtras() != null) {
            user = (User) getIntent().getExtras().getSerializable(USER);
        }

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(user.getAvatarUrl())
                .placeholder(R.drawable.gravatar_icon)
                .into(avatar);

        name.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));

        role.setText(String.format("%s", user.getRole()));

        email.setText(String.format("%s", user.getEmail()));

        tv_phonenumber.setText(String.format("%s", user.getPhoneNumber()));

        // Gets the logged in accounts user information
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(Constants.Auth.BOOTSTRAP_ACCOUNT_TYPE);
        Account myAccount =  accounts[0];

        //User Singlton to get user or session
        authToken = accountManager.peekAuthToken(myAccount,Constants.Auth.AUTHTOKEN_TYPE );
        user.setSessionToken(authToken);

        if(myAccount.name.equalsIgnoreCase(user.getUsername())){
            button_Edit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == button_Edit.getId())
        {
            //The Edit button has been clicked
            onEdit();
        }else if(view.getId() == button_Submit.getId()){
            //The Submit button has been clicked
            onSubmit();
        }
    }

    private void onEdit(){
        button_Edit.setVisibility(View.GONE);
        button_Submit.setVisibility(View.VISIBLE);

        //Resets all TextViews to be EditText fields
        name.setVisibility(View.GONE);
        et_name.setVisibility(View.VISIBLE);
        email.setVisibility(View.GONE);
        et_email.setVisibility(View.VISIBLE);
        tv_phonenumber.setVisibility(View.GONE);
        et_phonenubmer.setVisibility(View.VISIBLE);
        role.setVisibility(View.GONE);
        tv_roleTag.setVisibility(View.GONE);

        //Sets the text in the EditText fields
        et_name.setText(String.format("%s", name.getText().toString()));
        et_email.setText(String.format("%s", email.getText().toString()));
        et_phonenubmer.setText(String.format("%s", tv_phonenumber.getText().toString()));

    }

    private void onSubmit(){
        if(validateFields()){
            showProgress();
            button_Submit.setVisibility(View.GONE);
            button_Edit.setVisibility(View.VISIBLE);

            String[] names = et_name.getText().toString().split(" ");
            String firstName = getFirstName(names);
            String lastName = getLastName(names);

            //Resets all EditText fields to be TextViews
            name.setVisibility(View.VISIBLE);
            et_name.setVisibility(View.GONE);
            email.setVisibility(View.VISIBLE);
            et_email.setVisibility(View.GONE);
            tv_phonenumber.setVisibility(View.VISIBLE);
            et_phonenubmer.setVisibility(View.GONE);
            role.setVisibility(View.VISIBLE);
            tv_roleTag.setVisibility(View.VISIBLE);

            //Sets the text in the TextViews.
            // Needs to be changed to update Parse using Rest API!  ------------------------------
            user.setUsername(et_email.getText().toString());
            user.setEmail(et_email.getText().toString());
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPhoneNumber(et_phonenubmer.getText().toString());

            authenticationTask = new SafeAsyncTask<Boolean>() {
                public Boolean call() throws Exception {

                    bootstrapService.update(user);
                    return true;
                }

                @Override
                protected void onException(final Exception e) throws RuntimeException {
                    // Retrofit Errors are handled inside of the {
                    if(!(e instanceof RetrofitError)) {
                        final Throwable cause = e.getCause() != null ? e.getCause() : e;
                        if(cause != null) {
                            Toaster.showLong(UserActivity.this, cause.getMessage());
                        }
                    }
                }

                @Override
                public void onSuccess(final Boolean authSuccess) {
                    //Update user in singleton
                    singleton.setCurrentUser(user);

                    name.setText(String.format("%s", et_name.getText().toString()));
                    email.setText(String.format("%s", et_email.getText().toString()));
                    tv_phonenumber.setText(String.format("%s", et_phonenubmer.getText().toString()));
                }

                @Override
                protected void onFinally() throws RuntimeException {
                    hideProgress();
                    authenticationTask = null;
                }
            };
            authenticationTask.execute();


        }
    }

    private boolean validateFields(){
        //TODO Need to introduce checks here!!!!
        //Handles the setting of the first and last name.
        String[] names = et_name.getText().toString().split(" ");
        if (names.length == 1){
            Toaster.showLong(this, "Please enter full name.");
            return false;
        }

        String userEmail = et_email.getText().toString();
        if(!userEmail.contains("@")){
            Toaster.showLong(this, "Invalid Email");
            return false;
        }else{
            if(!userEmail.contains(".com") && !userEmail.contains(".org")){
                Toaster.showLong(this, "Invalid Email");
                return false;
            }
        }

        return true;
    }

    private String getFirstName(String[] names){
        return names[0];
    }

    private String getLastName(String[] names){
        String lastName = "";
        if(names.length < 3){
            lastName = names[1];
        }else{
            for(int i = 1; i < names.length - 1; i++){
                if(i == names.length){
                    lastName = names[i];
                }else{
                    lastName += names[i] + " ";
                }
            }
        }

        return lastName;
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        dismissDialog(0);
    }

    /**
     * Show progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void showProgress() {
        showDialog(0);
    }
}

