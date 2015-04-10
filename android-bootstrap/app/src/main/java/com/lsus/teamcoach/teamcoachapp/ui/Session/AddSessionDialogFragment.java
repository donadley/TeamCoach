package com.lsus.teamcoach.teamcoachapp.ui.Session;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.github.kevinsawicki.wishlist.Toaster;
import com.lsus.teamcoach.teamcoachapp.Injector;
import com.lsus.teamcoach.teamcoachapp.R;
import com.lsus.teamcoach.teamcoachapp.core.BootstrapService;
import com.lsus.teamcoach.teamcoachapp.core.Drill;
import com.lsus.teamcoach.teamcoachapp.core.Singleton;
import com.lsus.teamcoach.teamcoachapp.ui.Library.DrillListFragment;
import com.lsus.teamcoach.teamcoachapp.util.SafeAsyncTask;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Views;
import retrofit.RetrofitError;

/**
 * Created by TeamCoach on 4/10/2015.
 */
public class AddSessionDialogFragment extends DialogFragment implements View.OnClickListener {

    @InjectView(R.id.btnCancelAddSession) protected Button btnCancelAddSession;
    @InjectView(R.id.btnAddSession) protected Button btnAddSession;
    @InjectView(R.id.etAddSessionName) protected EditText etSessionName;
    @InjectView(R.id.sAddSessionAgeGroup) protected Spinner sAgeGroup;
    @InjectView(R.id.sAddSessionType) protected Spinner sSessionType;
    @InjectView(R.id.etAddSessionDescription) protected EditText etDescription;

    @Inject
    BootstrapService bootstrapService;

    private SafeAsyncTask<Boolean> authenticationTask;

    private boolean ageSelected;
    private boolean typeSelected;
    private String sessionName;
    private String age;
    private String type;
    private String description;
    private String creator;

    private DrillListFragment parentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Create Session");

        View view = inflater.inflate(R.layout.add_session_dialog_fragment, container, false);
        Injector.inject(this);

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Views.inject(this, view);

        btnCancelAddSession.setOnClickListener(this);
        btnAddSession.setOnClickListener(this);

        //Sets up the values for the Age Groups
        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.age_group_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sAgeGroup.setAdapter(ageAdapter);

        //Sets up the values for the Drill Types
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.drill_type_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sSessionType.setAdapter(typeAdapter);

        //TODO fix setting the age.
        if(ageSelected){
            sAgeGroup.setSelection(getIndex(sAgeGroup, age));
        }

        if(typeSelected){
            sSessionType.setSelection(getIndex(sSessionType, type));
        }
    }

    @Override
    public void onClick(View view) {
        if(btnAddSession.getId() == view.getId()) {
            if(validateFields()){

                //TODO Make a new Activity to create a session.
                authenticationTask = new SafeAsyncTask<Boolean>() {
                    public Boolean call() throws Exception {
                        //TODO change to session
                        //TODO Make sure session is up to date with parse.com
                        Drill drill = new Drill(sessionName, type, age, description, 0, 0, creator, 0);

                        bootstrapService.addDrill(drill);
                        return true;
                    }

                    @Override
                    protected void onException(final Exception e) throws RuntimeException {
                        // Retrofit Errors are handled inside of the {
                        if (!(e instanceof RetrofitError)) {
                            final Throwable cause = e.getCause() != null ? e.getCause() : e;
                            if (cause != null) {
                                Toaster.showLong(getActivity(), cause.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onSuccess(final Boolean authSuccess) {
                        if(typeSelected) parentFragment.refresh();
                        AddSessionDialogFragment.this.dismiss();
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
        if(btnCancelAddSession.getId() == view.getId()){
            dismiss();
        }
    }

    public void setAgeSelected(Boolean ageSelected){
        this.ageSelected = ageSelected;
    }

    public void setTypeSelected(Boolean typeSelected) { this.typeSelected = typeSelected; }

    public void setAge(String age){
        this.age = age;
    }

    public void setType(String type) { this.type = type; }

    private boolean validateFields(){
        if (!etSessionName.getText().toString().equalsIgnoreCase("")){
            sessionName = etSessionName.getText().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!sSessionType.getSelectedItem().toString().equalsIgnoreCase("")) {
            type = sSessionType.getSelectedItem().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!sAgeGroup.getSelectedItem().toString().equalsIgnoreCase("")){
            age = sAgeGroup.getSelectedItem().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        if(!etDescription.getText().toString().equalsIgnoreCase("")){
            description = etDescription.getText().toString();
        } else {
            Toaster.showShort(this.getActivity(), "Please fill out all fields.");
            return false;
        }

        Singleton singleton = Singleton.getInstance();
        creator = singleton.getCurrentUser().getEmail();

        return true;
    }

    private int getIndex(Spinner spinner, String item){
        int index = 0;

        for(int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(item)){
                index = i;
            }
        }
        return index;
    }

    /**
     * Hide progress dialog
     */
    @SuppressWarnings("deprecation")
    protected void hideProgress() {
        getActivity().dismissDialog(0);
    }

    public void setDrillListFragment(DrillListFragment drillListFragment){
        this.parentFragment = drillListFragment;
    }
}
