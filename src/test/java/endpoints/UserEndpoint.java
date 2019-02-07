package endpoints;

import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.User;

public class UserEndpoint extends BaseEndpoints {
	private final String USER_ENDPOINT_PATH = "user/";
	private User defaultUser;
	
	public UserEndpoint() {
		super();
		defaultUser = new User();
	}
	
	public String getPath() {
		return this.USER_ENDPOINT_PATH;
	}
	
	public User getDefaultUser() {
		return this.defaultUser;
	}
	
	public Response submitLogin(RequestSpecification request, String validity) {
		
		if("valid".equalsIgnoreCase(validity)) {
			return submitLogin(request, getDefaultUser(), true);
		}
		
		return submitLogin(request, getDefaultUser(), false);
	}
	
	public Response submitLogin(RequestSpecification request, User user, boolean validUser) {
//		request.param("username", user.getUsername());
//		request.param("password", user.getPassword());
		
		
		//RestAssured.baseURI = getBaseUrl() + this.getPath();
		return RestAssured.given().urlEncodingEnabled(true)
            .param("username", user.getUsername())
            .param("password", user.getPassword())
            .header("Content-Type", "application/xml")
            .get(getBaseUrl() + this.getPath());
            
		//return request.get(getBaseUrl() + this.getPath());
	}
	
	public Response getUserByUsername() {
		return getUserByUsername(getDefaultUser().getUsername());
	}
	
	public Response getUserByUsername(String username) {
		return RestAssured.when().get(getBaseUrl() + this.getPath() + username);
	}
	
	
	
	public Response createUser(RequestSpecification request) {
		return createUser(
				request,
				defaultUser);
	}
	
	public Response createUser(RequestSpecification request,User user) {
		return placeOrder(
				request,
				user.getId(),
				user.getUsername(),
				user.getFirstname(),
				user.getLastnamename(),
				user.getEmail(),
				user.getPassword(),
				user.getPhone(),
				user.getUserStatus());
	}
	
	public Response placeOrder(RequestSpecification request, int id, String username, String firstname, String lastname, String email, String password, String phone, int userStatus) {
		JSONObject requestParams = createUserRequestJSONBody(id, username, firstname, lastname, email, password, phone, userStatus);
				
		// Add the Json to the body of the request
		request.body(requestParams.toString());
		
		return request.post(getBaseUrl() + this.getPath());
	}
	
	private JSONObject createUserRequestJSONBody(int id, String username, String firstname, String lastname, String email, String password, String phone, int userStatus) {
		JSONObject requestParams = new JSONObject();
		requestParams.put("id", id); 
		requestParams.put("username", username); 
		requestParams.put("firstName", firstname);
		requestParams.put("lastName", lastname);
		requestParams.put("email",  email);
		requestParams.put("password",  password);
		requestParams.put("phone",  phone);
		requestParams.put("userStatus",  userStatus);
		
		return requestParams;
	}
	

}
