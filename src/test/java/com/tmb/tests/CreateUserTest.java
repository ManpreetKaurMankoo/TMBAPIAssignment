package com.tmb.tests;

import static com.tmb.assertions.ResponseAssert.assertThat;
import static com.tmb.assertions.UserAssert.assertThat;
import static com.tmb.constants.StringEndpoints.withUserEndpoint;
import static com.tmb.utils.RequestCreatorUtility.hitPOSTAPI;
import static com.tmb.utils.RequestTestDataBuilder.withUserPayload;
import static com.tmb.utils.ResponseParserUtility.parseResponse;
import static io.github.sskorol.data.TestDataReader.use;

import org.testng.annotations.Test;

import com.tmb.pojos.User;

import io.github.sskorol.core.DataSupplier;
import io.github.sskorol.data.JsonReader;
import io.restassured.response.Response;
import one.util.streamex.StreamEx;

public final class CreateUserTest {

	@Test(dataProvider = "getData")
	public void createUserTest(User data) {

		//		System.out.println("Inside test");

		Response response = hitPOSTAPI(withUserPayload(data), withUserEndpoint());
		System.out.println("response: " + response);
		User userParsedResponse = parseResponse(response, User.class);
		System.out.println("Parsed res: " + userParsedResponse);



		assertThat(response)
		.gives201SuccessfulPostResponse()
		.hasExpectedResponseJsonSchema()
		.hasResponseTimeWithinTwoSecs()
		.containsHeaderApplicationJson();

		assertThat(userParsedResponse)
		.hasName(data.getName())
		.hasJob(data.getJob());

		//		response.then().spec(com.tmb.utils.RequestResponseSpecCreatorUtils.responseSpecification()).assertThat().log().all().body(
		//				matchesJsonSchema(new File("user.json")));

		//response code, response schema, response time within 2 secs, response header, response can be deserialized to POJO
	}

	@DataSupplier
	public StreamEx<User> getData() {
		return use(JsonReader.class)
				.withTarget(User.class)
				.withSource("testdata/testdata.json")
				.read();
	}

	//	@DataSupplier
	//	public Object[][] getData() {
	//		return new Object[][] {{"Name1", "Job1"}, {"Name2", "Job2"}};
	//	}

}
