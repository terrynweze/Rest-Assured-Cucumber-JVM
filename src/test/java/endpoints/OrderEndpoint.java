package endpoints;



import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Order;

public class OrderEndpoint extends BaseEndpoints{
	public static final int INVALID_ORDER_STATUS_CODE = 400;
	private final String ORDER_ENDPOINT_PATH = "store/order/";
	private Order defaultOrder;
	
	public OrderEndpoint() {
		super();
		defaultOrder = new Order();
	}
	
	public String getPath() {
		return this.ORDER_ENDPOINT_PATH;
	}
	
	public Response getOrderById(String id) {
		return RestAssured.when().get(getBaseUrl() + this.getPath() + id);
	}
	
	public Response placeOrder(RequestSpecification request) {
		return placeOrder(
				request,
				defaultOrder);
	}
	
	public Response placeOrder(RequestSpecification request,Order order) {
		return placeOrder(
				request,
				order.getId(),
				order.getPetId(),
				order.getQuantity(),
				order.getShipDate(),
				order.getStatus(),
				order.getComplete());
	}
	
	public Response placeOrder(RequestSpecification request, Integer orderId, int petID, int quantity, String shipDate, String status, boolean complete) {
		JSONObject requestParams = createOrderRequestJSONBody(orderId, petID, quantity, shipDate, status, complete);
				
		// Add the Json to the body of the request
		request.body(requestParams.toString());
		
		return request.post(getBaseUrl() + this.getPath());
	}
	
	private JSONObject createOrderRequestJSONBody(Integer orderId, int petID, int quantity, String shipDate, String status, boolean complete) {
		JSONObject requestParams = new JSONObject();
		requestParams.put("id", orderId); 
		requestParams.put("petId", petID); 
		requestParams.put("quantity", quantity);
		requestParams.put("shipDate", shipDate);
		requestParams.put("status",  status);
		requestParams.put("complete",  complete);
		
		return requestParams;
	}
	
	public Order getDefaultOrder() {
		return this.defaultOrder;
	}
	
	public void verifyOrderValuesAreAsExpected(Response response, Order order) {
		String id = order.getId().toString();
		String petId = Integer.toString(order.getPetId());
		String quantity = Integer.toString(order.getQuantity());
		String shipDate = prepareShipDate(order.getShipDate());
		String status = order.getStatus();
		String complete = Boolean.toString(order.getComplete());
		
		verifyResponseKeyValues("id", id, response);
		verifyResponseKeyValues("petId", petId, response);
		verifyResponseKeyValues("quantity", quantity, response);
		verifyResponseKeyValues("shipDate", shipDate, response);
		verifyResponseKeyValues("status", status, response);
		verifyResponseKeyValues("complete",complete, response);
		
	}
	
	private String prepareShipDate(String dateString) {
		return dateString.replace("Z", "+0000");
	}

}
