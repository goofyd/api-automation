package core;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;

import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HeaderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.LogConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public abstract class Executor {

	protected RequestSpecification request;
	protected String requestBody;
	protected String responseBody;
	protected Method methodType;
	protected String url;

	protected Executor() {
		this.request = getDefault();
	}

	protected RequestSpecification getDefault() {
		return RestAssured.given().config(getDefaultConfig()).contentType(ContentType.JSON).accept(ContentType.JSON);
	}

	protected RestAssuredConfig getDefaultConfig() {
		return RestAssured.config()
				.httpClient(HttpClientConfig.httpClientConfig().setParam("http.connection.timeout", 40000)
						.setParam("http.socket.timeout", 60000))
				.connectionConfig(ConnectionConfig.connectionConfig().closeIdleConnectionsAfterEachResponseAfter(60,
						TimeUnit.SECONDS))
				.headerConfig(HeaderConfig.headerConfig().overwriteHeadersWithName("Authorization", "Content-Type"))
				.logConfig(LogConfig.logConfig().enablePrettyPrinting(true));
	}

	protected Response callAPI(String path, Method methodType, String reqBody) {
		return callAPI(path, methodType, reqBody, null);
	}

	protected Response callAPI(String path, Method methodType, String reqBody, String expectedString) {
		this.url = path;
		this.methodType = methodType;
		this.requestBody = reqBody;
		Response res = callAPIRequest(expectedString);
		res.then().log().all();
		return res;
	}

	private Response callAPIRequest(String expectedString) {
		if (expectedString != null && !expectedString.isEmpty()) {
			Awaitility.with().pollInterval(5, TimeUnit.SECONDS).await().atMost(60, TimeUnit.SECONDS).until(() -> {
				switch (this.methodType.toString()) {
				case "POST":
					System.out.println("in post await block");
					return this.request.body(this.requestBody).headers(new HashMap<>()).post(this.url).asString()
							.contains(expectedString);
				case "PUT":
					return this.request.body(this.requestBody).headers(new HashMap<>()).put(this.url).asString()
							.contains(expectedString);
				case "DELETE":
					return this.request.body(this.requestBody).headers(new HashMap<>()).delete(this.url).asString()
							.contains(expectedString);
				default:
					return this.request.headers(new HashMap<>()).get(this.url).asString().contains(expectedString);
				}

			});
		}
		switch (this.methodType.toString()) {
		case "POST":
			return this.request.body(this.requestBody).headers(new HashMap<>()).log().all().post(this.url);
		case "PUT":
			return this.request.body(this.requestBody).headers(new HashMap<>()).log().all().put(this.url);
		case "DELETE":
			return this.request.body(this.requestBody).headers(new HashMap<>()).log().all().delete(this.url);
		default:
			return this.request.headers(new HashMap<>()).log().all().get(this.url);
		}
	}

}
