package com.adox.matias.formadox.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.adox.matias.formadox.LoginActivity;


public class SecureActivity extends ActionBarActivity {

	protected String id;
	protected String hash;
	protected boolean startedSession;
	protected Class<?> classType;

	protected SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		prefs = getSharedPreferences(Util.SHAPREF,
				Context.MODE_PRIVATE);

		id = prefs.getString(Util.ID_SP, "");
		hash = prefs.getString(Util.HASH_SP, "");

		if ("".equals(id) || "".equals(hash)) {

			if (this.classType == null)
				this.classType = getClass();

			Intent loginIntent = new Intent(this, LoginActivity.class);
			loginIntent.putExtra("class", this.classType);
			finish();
			startActivity(loginIntent);

			startedSession = false;
		} else {
			startedSession = true;
		}

	}

	@Override
	public void onResume() {
		super.onResume();

		prefs = getSharedPreferences(Util.SHAPREF,
				Context.MODE_PRIVATE);

		id = prefs.getString(Util.ID_SP, "");
		hash = prefs.getString(Util.HASH_SP, "");

		startedSession = !"".equals(id) && !"".equals(hash);
	}

	public boolean isStartedSession() {
		return startedSession;
	}

	public void setStartedSession(boolean startedSession) {
		this.startedSession = startedSession;
	}

}
