package com.adox.matias.formadox.http;

import org.json.JSONObject;

import java.util.HashMap;

public interface HttpCallCallbackHandler {
	public void onResponse(String action, HashMap<String, String> response, HashMap<String, String> map);
	public void onResponse(String action, JSONObject response, HashMap<String, String> map);
}
