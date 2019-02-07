package model;

public class Pet {
	private Integer id;
	private Category category;
	private String name;
	private String[] photoUrls;
	private Tag[] tags;
	private String status;
	
	public Pet() {
		setId(29);
		setCategory(new Category(13, "Cainine"));
		setName("Wolf");
		setPhotoUrls(new String[] {"http://pathtoimage1"});
		setTag(new Tag[] {new Tag(17, "Furry")});
		setStatus("available");
	}	
	
	public Pet(Integer id, Category category, String name, String[] photoUrls, Tag[] tags, String status) {
		setId(id);
		setCategory(category);
		setName(name);
		setPhotoUrls(photoUrls);
		setTag(tags);
		setStatus(status);
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getId() {
		return this.id;
	}
	
	public void setCategory(Category category) {
		this.category = category;
	}
	
	public Category getCategory() {
		return this.category;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPhotoUrls(String[] photoUrls) {
		this.photoUrls = photoUrls;
	}
	
	public String[] getPhotoUrls() {
		return this.photoUrls;
	}
	
	public void setTag(Tag[] tags) {
		this.tags = tags;
	}
	
	public Tag[] getTag() {
		return this.tags;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return this.status;
	}
		

}
