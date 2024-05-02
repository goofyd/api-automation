package core.apis;

import java.util.Map;

import core.Configuration;
import core.Executor;
import io.restassured.http.Method;
import io.restassured.response.Response;
import utility.Data;

public class ReqResAPI extends Executor {
	
	private final String URL;
	
	public ReqResAPI() {
		URL = "https://reqres.in";
	}

	public Response createNewUser(Map<String, String> data) {
		return callAPI(URL.concat("/api/users"), Method.POST, data.get(Data.REQUEST_PAYLOAD), data.get(Data.EXPECTED_STRING));
	}
	
	public Response fetchUser(Map<String, String> data) {
		return callAPI(URL.concat("/api/users/").concat(""), Method.GET, null, data.get(Data.EXPECTED_STRING));
	}

}
