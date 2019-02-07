package endpoints;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.Category;
import model.Pet;
import model.Tag;

public class PetEndpoint extends BaseEndpoints {
	private final String PET_ENDPOINT_PATH = "pet/";
	private Pet defaultPet;

	public PetEndpoint() {
		super();
		defaultPet = new Pet();
	}

	public String getPath() {
		return this.PET_ENDPOINT_PATH;
	}

	public Pet getDefaultPet() {
		return this.defaultPet;
	}

	public Response addPetWithNoPostBody(RequestSpecification request) {
		request.body("{");
		return request.post(getBaseUrl() + this.getPath());
	}

	public Response addPet(RequestSpecification request) {
		return addPet(request, getDefaultPet());
	}

	public Response addPet(RequestSpecification request, Pet pet) {
		return addPet(request, pet.getId(), pet.getCategory(), pet.getName(), pet.getPhotoUrls(), pet.getTag(),
				pet.getStatus());
	}

	public Response addPet(RequestSpecification request, Integer id, Category category, String name, String[] photoUrls,
			Tag[] tags, String status) {
		
		JSONObject requestParams = createAddPetRequestJSONBody(id, category, name, photoUrls, tags, status);
		

		// Add the Json to the body of the request
		request.body(requestParams.toString());

		return request.post(getBaseUrl() + this.getPath());
	}

	public Response deletePet(RequestSpecification request) {
		return deletePet(request, getDefaultPet());
	}

	public Response deletePet(RequestSpecification request, Pet pet) {

		String id = pet.getId().toString();
		return request.delete(getBaseUrl() + this.getPath() + id);
	}

	public Response getPetById() {
		return getPetById(getDefaultPet().getId().toString());
	}

	public Response getPetById(String id) {
		return RestAssured.when().get(getBaseUrl() + this.getPath() + id);
	}

	private JSONObject createAddPetRequestJSONBody(Integer id, Category category, String name, String[] photoUrls,
			Tag[] tag, String status) {
		JSONObject petParams = new JSONObject();
		JSONObject categoryParams = new JSONObject();
		JSONObject tagParams;
		JSONArray tagsParams = new JSONArray();
		JSONArray photoUrlParams = new JSONArray();

		// Create category Params
		categoryParams.put("id", category.getId());
		categoryParams.put("name", category.getName());

		// Create tag Params
		for (int i = 0; i < tag.length; i++) {
			tagParams = new JSONObject();
			tagParams.put("id", tag[i].getId());
			tagParams.put("name", tag[i].getName());
			tagsParams.put(i, tagParams);
		}

		// setting photoURls
		for (int i = 0; i < photoUrls.length; i++) {
			photoUrlParams.put(photoUrls[i]);
		}

		// Do not add an id value if one has not been provided
		if(id != null) {
			petParams.put("id", id);
		}
		
		petParams.put("category", categoryParams);
		petParams.put("name", name);

		petParams.put("photoUrls", photoUrlParams);

		petParams.put("tags", tagsParams);
		petParams.put("status", status);

		return petParams;
	}

	public void verifyPetValuesAreAsExpected(Response response, Pet pet) {
		String expectedId = pet.getId().toString();
		String expectedCategoryId = pet.getCategory().getId().toString();
		String expectedCategoryName = pet.getCategory().getName();
		String expectedName = pet.getName();
		String expectedPhotoUrls[] = pet.getPhotoUrls();
		Tag expectedTags[] = pet.getTag();
		String expectedStatus = pet.getStatus();

		// get Tag expectedIds and names
		String[] expectedTagIds = new String[expectedTags.length];
		String[] expectedTagNamess = new String[expectedTags.length];
		for (int i = 0; i < expectedTags.length; i++) {
			expectedTagIds[i] = expectedTags[i].getId().toString();
			expectedTagNamess[i] = expectedTags[i].getName();
		}

		verifyResponseKeyValues("id", expectedId, response);
		verifyNestedResponseKeyValues("category", "id", expectedCategoryId, response);
		verifyNestedResponseKeyValues("category", "name", expectedCategoryName, response);
		verifyResponseKeyValues("name", expectedName, response);
		verifyNestedArrayValueResponseKeyValues("photoUrls", expectedPhotoUrls, response);
		verifyNestedArrayMapResponseKeyValues("tags", "id", expectedTagIds, response);
		verifyNestedArrayMapResponseKeyValues("tags", "name", expectedTagNamess, response);
		verifyResponseKeyValues("status", expectedStatus, response);
	}
	
	public void verifyPetHasAnId(Response response) {
		String idVal = response.jsonPath().getString("id");
		verifyTrue(idVal != null);
		verifyTrue(idVal.length() > 0);
	}

	public Pet createPet(Integer id, Category category, String name, String[] photoUrls, Tag[] tags, String status) {
		Pet pet = new Pet(id, category, name, photoUrls, tags, status);
		return pet;
	}

	public Pet createPet(Integer id, Pair<String, String> category, String name, String[] photoUrls,
			ArrayList<Pair<Integer, String>> tags, String status) {

		Category cat = new Category(Integer.parseInt(category.getKey()), category.getValue());

		Tag[] tagArray = new Tag[tags.size()];
		for (int i = 0; i < tags.size(); i++) {
			Pair<Integer, String> p = tags.get(i);
			tagArray[i] = new Tag(p.getKey(), p.getValue());
		}

		return createPet(id, cat, name, photoUrls, tagArray, status);

	}

	public Pet createPet(Integer id, String categoryString, String name, String photoUrlsString, String tagsString,
			String status) {
		String[] categoryStringArray = categoryString.split(":");

		Pair<String, String> category = Pair.of(categoryStringArray[0], categoryStringArray[1]);

		String[] photoUrlsArray = photoUrlsString.split(":");

		ArrayList<Pair<Integer, String>> tagsList = new ArrayList();

		if (!"".equalsIgnoreCase(tagsString)) {
			String[] tagStringArray = tagsString.split(",");
			for (int i = 0; i < tagStringArray.length; i++) {
				String[] tagArray = tagStringArray[i].split(":");
				tagsList.add(Pair.of(Integer.parseInt(tagArray[0]), tagArray[1]));
			}
		}

		return createPet(id, category, name, photoUrlsArray, tagsList, status);
	}

}
