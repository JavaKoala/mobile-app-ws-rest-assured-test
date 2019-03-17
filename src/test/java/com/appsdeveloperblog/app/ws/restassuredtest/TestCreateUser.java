package com.appsdeveloperblog.app.ws.restassuredtest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

class TestCreateUser {

	private final String CONTEXT_PATH="/mobile-app-ws";

	@BeforeEach
	void setUp() throws Exception {
		RestAssured.baseURI="http://localhost";
		RestAssured.port=8080;
	}

	@Test
	void test() {

        List<Map<String, Object>> userAddresses = new ArrayList<>();

        Map<String, Object> shippingAddress = new HashMap<>();
        shippingAddress.put("city", "Washington");
        shippingAddress.put("country", "USA");
        shippingAddress.put("streetName", "1600 Pennsylvania Ave");
        shippingAddress.put("postalCode", "12345");
        shippingAddress.put("type", "shipping");

        Map<String, Object> billingAddress = new HashMap<>();
        billingAddress.put("city", "Test City");
        billingAddress.put("country", "USA");
        billingAddress.put("streetName", "123 Street name");
        billingAddress.put("postalCode", "123456");
        billingAddress.put("type", "billing");

        userAddresses.add(shippingAddress);
        userAddresses.add(billingAddress);

        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", "Test");
        userDetails.put("lastName", "User");
        userDetails.put("email", "test.user@test.com");
        userDetails.put("password", "123");
        userDetails.put("addresses", userAddresses);

		Response response = given()
		.contentType("application/json")
		.accept("application/json")
		.body(userDetails)
		.when()
		.post(CONTEXT_PATH + "/users")
		.then()
		.statusCode(200)
		.contentType("application/json")
		.extract()
		.response();

		String userId = response.jsonPath().getString("userId");
		assertNotNull(userId);

		String bodyString = response.body().asString();
		try {
			JSONObject responseBodyJson = new JSONObject(bodyString);
			JSONArray addresses = responseBodyJson.getJSONArray("addresses");

			assertNotNull(addresses);
			assertTrue( addresses.length() == 2);

			String addressId = addresses.getJSONObject(0).getString("addressId");
			assertNotNull(addressId);
			assertTrue(addressId.length() == 30);
		} catch (JSONException e) {
			fail(e.getMessage());
		}
	}

}
