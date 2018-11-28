package com.example.madeleine.family_map_v2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.madeleine.family_map_v2.Model.*;

public class LoginFragment extends Fragment {
    private static EditText serverHostEditText;
    private static EditText serverPortEditText;
    private static EditText usernameEditText;
    private static EditText passwordEditText;
    private static EditText firstNameEditText;
    private static EditText lastNameEditText;
    private static EditText emailEditText;
    private static RadioGroup genderRadio;
    private static RadioButton selectedGenderButton;
    private CurrentSession session = CurrentSession.getInstance();
    private static Button loginButton;
    private static Button registerButton;
    boolean checkedGender = false;



    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_login, container, false);



        serverHostEditText = v.findViewById(R.id.host_name);
        serverPortEditText = v.findViewById(R.id.port_name);
        usernameEditText = v.findViewById(R.id.user_name);
        passwordEditText = v.findViewById(R.id.password);
        firstNameEditText = v.findViewById(R.id.first_name);
        lastNameEditText = v.findViewById(R.id.last_name);
        emailEditText = v.findViewById(R.id.email);
        genderRadio = v.findViewById(R.id.gender);
        loginButton = v.findViewById(R.id.login_button);
        registerButton = v.findViewById(R.id.register_button);
        loginButton.setEnabled(false);
        registerButton.setEnabled(false);

        //TEMPORARY LOGIN STUFF:
        serverPortEditText.setText("8083");
        serverHostEditText.setText("10.0.2.2");
        usernameEditText.setText("q");
        passwordEditText.setText("r");
        loginButton.setEnabled(true);

        serverHostEditText.addTextChangedListener(watcher);
        serverPortEditText.addTextChangedListener(watcher);
        usernameEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
        firstNameEditText.addTextChangedListener(watcher);
        lastNameEditText.addTextChangedListener(watcher);
        emailEditText.addTextChangedListener(watcher);

        genderRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
              {
                  public void onCheckedChanged(RadioGroup group, int checkedId)
                  {
                      // This will get the radiobutton that has changed in its check state
                      RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                      // This puts the value (true/false) into the variable
                      boolean isChecked = checkedRadioButton.isChecked();
                      // If the radiobutton that has changed in check state is now checked...
                      if (isChecked)
                      {
                          checkedGender = true;
                          if (serverHostEditText.getText().toString().length() == 0 ||
                                  serverPortEditText.getText().toString().length() == 0 ||
                                  usernameEditText.getText().toString().length() == 0 ||
                                  passwordEditText.getText().toString().length() == 0 ||
                                  firstNameEditText.getText().toString().length() == 0 ||
                                  lastNameEditText.getText().toString().length() == 0 ||
                                  emailEditText.getText().toString().length() == 0 || !checkedGender) {
                              registerButton.setEnabled(false);
                          }
                          else {
                              registerButton.setEnabled(true);
                          }

                      }
                  }
              });


        registerButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  final String hostName = serverHostEditText.getText().toString();
                  final String portName = serverPortEditText.getText().toString();
                  final String userName = usernameEditText.getText().toString();
                  final String password = passwordEditText.getText().toString();
                  final String firstName = firstNameEditText.getText().toString();
                  final String lastName = lastNameEditText.getText().toString();
                  final String email = emailEditText.getText().toString();
                  String gender;

                  selectedGenderButton = genderRadio.findViewById(genderRadio.getCheckedRadioButtonId());
                  if (selectedGenderButton.getId() == R.id.gender_female) gender = "f";
                  else gender = "m";

                  session.setPortHost(portName, hostName);

                  RegisterRequest request = new RegisterRequest();
                  request.userName = userName;
                  request.password = password;
                  request.firstName = firstName;
                  request.lastName = lastName;
                  request.email = email;
                  request.gender = gender;

                  new RegisterAsyncTask().execute(request);

              }
          }
//
        );
        loginButton.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  final String hostName = serverHostEditText.getText().toString();
                  final String portName = serverPortEditText.getText().toString();
                  final String userName = usernameEditText.getText().toString();
                  final String password = passwordEditText.getText().toString();
                  session.setPortHost(portName, hostName);

                  LoginRequest request = new LoginRequest();
                  request.userName = userName;
                  request.password = password;

                  new LoginAsyncTask().execute(request);
              }
          }
//
        );
        return v;
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {}
        @Override
        public void afterTextChanged(Editable s) {
            if (serverHostEditText.getText().toString().length() == 0 ||
                    serverPortEditText.getText().toString().length() == 0 ||
                    usernameEditText.getText().toString().length() == 0 ||
                    passwordEditText.getText().toString().length() == 0) {
                loginButton.setEnabled(false);
            }
            else {
                loginButton.setEnabled(true);
            }

            if (serverHostEditText.getText().toString().length() == 0 ||
                    serverPortEditText.getText().toString().length() == 0 ||
                    usernameEditText.getText().toString().length() == 0 ||
                    passwordEditText.getText().toString().length() == 0 ||
                    firstNameEditText.getText().toString().length() == 0 ||
                    lastNameEditText.getText().toString().length() == 0 ||
                    emailEditText.getText().toString().length() == 0 || !checkedGender) {
                registerButton.setEnabled(false);
            }
            else {
                registerButton.setEnabled(true);
            }

        }
    };

    private class LoginAsyncTask extends AsyncTask<LoginRequest, Void, LoginResponse> {
        protected LoginResponse doInBackground(LoginRequest...params) { //feeds array params
            return session.login(params[0]);
        }
        protected void onPostExecute(LoginResponse response) {
            if (response.success) {
                String firstName = session.personList.get(0).firstName;
                String lastName = session.personList.get(0).lastName;
                Toast.makeText(getActivity(), "Login success! Welcome, " + firstName + " " +
                lastName, Toast.LENGTH_SHORT).show();

                session.loggedIn = true;
                Intent intent = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                startActivity(intent);

            }
            else {
                Toast.makeText(getActivity(), "Login failure, " + response.message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class RegisterAsyncTask extends AsyncTask<RegisterRequest, Void, RegisterResponse> {
        protected RegisterResponse doInBackground(RegisterRequest...params) { //feeds array params
            return session.register(params[0]);
        }


        protected void onPostExecute(RegisterResponse response) {
            if (response.success) {
                String firstName = session.personList.get(0).firstName;
                String lastName = session.personList.get(0).lastName;
                Toast.makeText(getActivity(), "Register success! Welcome, " + firstName + " " +
                        lastName, Toast.LENGTH_SHORT).show();
                session.loggedIn = true;
                Intent intent = new Intent(LoginFragment.this.getActivity(), MainActivity.class);
                startActivity(intent);
            }

            else {
                Toast.makeText(getActivity(), "Register failure, " + response.message, Toast.LENGTH_LONG).show();
            }
        }
    }
}