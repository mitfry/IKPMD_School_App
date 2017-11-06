package s1080488.ikpmd_app.Models;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mitchell on 24-10-2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "https://www.dinodeluxe.eu/DinoDeluxe_App/register.php";
    private Map<String, String> params;

    public RegisterRequest(String username, String email, String firstname, String lastname, String ign, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("email", email);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("ign", ign);
        params.put("u_password", password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
