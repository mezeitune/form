package com.adox.matias.formadox;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;


import com.adox.matias.formadox.http.HttpCall;
import com.adox.matias.formadox.http.HttpCallCallbackHandler;
import com.adox.matias.formadox.util.Util;

import org.json.JSONObject;

import java.util.HashMap;


public class LoginActivity extends ActionBarActivity implements HttpCallCallbackHandler {


    private EditText etUser, etPass;
    private TextView tV1;
    private Button btnEntrar;
    ProgressDialog progress;
    Class<?> classType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.classType = (Class<?>) getIntent().getExtras().get("class");

        // Inicialiso todos lso botones , textview , etc
        etUser = (EditText) findViewById(R.id.etUser);
        etPass = (EditText) findViewById(R.id.etPass);
        tV1 = (TextView) findViewById(R.id.tV1);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);

        SharedPreferences prefe = getSharedPreferences(Util.SHAPREF,
                Context.MODE_PRIVATE);

        if (!"".equals(prefe.getString(Util.USER_SP, ""))
                && !"".equals(prefe.getString(Util.PASS_SP, "")))

        {
            login(prefe.getString(Util.USER_SP, ""), prefe.getString(Util.PASS_SP, ""));
        }else{
            btnEntrar.setOnClickListener(
                    new View.OnClickListener() {
                        public void onClick(View view) {
                            login(etUser.getText().toString(), etPass.getText().toString());
                        }
                    });
            etPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    login(etUser.getText().toString(), etPass.getText().toString());
                    return false;
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        //moveTaskToBack(true);
    }


    public void login(String usr, String pass) {// para logearse

        progress = new ProgressDialog(this);
		progress.setTitle(getResources().getString(R.string.loading_tit));
		progress.setMessage(getResources().getString(R.string.loading_txt));
		progress.setCancelable(true);
		progress.show();
        if(Util.isNetworkAvailable(getApplicationContext())) {
            HttpCall call = new HttpCall(getResources().getString(
                    R.string.services_url), Util.ACC_LOGIN, this, HttpCall.CallType.JSON);

            call.addParam("user", usr);
            call.addParam("pass", pass);

            call.start();
        }else {
            Util.showToast("En este momento no tiene internet, luego se guardara la informacion requerida",
                    this);
            progress.dismiss();
        }

    }

    public void processLogin(JSONObject response,
                             HashMap<String, String> paramsMap) {

        try{

            if (response.has("state")) {

                if ("OK".equals(response.getString("state"))) {

                        SharedPreferences preferencias = getSharedPreferences(Util.SHAPREF,
                                Context.MODE_PRIVATE);
                        Editor editor = preferencias.edit();

                        editor.putString(Util.USER_SP, paramsMap.get("user"));
                        editor.putString(Util.PASS_SP, paramsMap.get("pass"));

                        editor.putString(Util.ID_SP , response.getString("id"));
                        editor.putString(Util.HASH_SP , response.getString("hash"));
                        editor.apply();

                        Intent alertIntent = new Intent(this, FormularioActivity.class);

                        finish();
                        alertIntent.putExtra("deDondeViene", "LoginActivity");
                        startActivity(alertIntent);

                    } else {
                        Util.showToast(getResources().getString(R.string.errorlogin),
                                this);
                    }
                } else {
                Util.showToast(getResources().getString(R.string.errorlogin_net),
                        this);
                }

        }catch (Exception  e){
            Util.showToast(getResources().getString(R.string.errorlogin_net),
                    this);
        }
    }

    //Estos onResponse se ponen por la interface del HttpCall
    //Que lo que hacen es ver que hacer cuando el servidor responde , y la app recibe una respuesta
    @Override
    public void onResponse(String action, HashMap<String, String> response,
                           HashMap<String, String> paramsMap) {
        // No implementado
    }

    @Override
    public void onResponse(String action, JSONObject response,
                           HashMap<String, String> paramsMap) {

        if (Util.ACC_LOGIN.equals(action)) {
            this.processLogin(response, paramsMap);
        }

        progress.dismiss();

    }
}
