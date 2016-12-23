package org.linagora.dao.openpaas;

import java.util.List;

import com.google.gson.Gson;

public class OpenPaasNotification {

	private String title;
	private ActionNotification action;
	private ObjectNotification object;
	private String link;
	private LevelNotification level;
	private String author;
	
	private List<TargetNotification> target;
	
	public OpenPaasNotification(String title, ActionNotification action, ObjectNotification object, String link,
			LevelNotification level, String author, List<TargetNotification> target) {
		super();
		this.title = title;
		this.action = action;
		this.object = object;
		this.link = link;
		this.level = level;
		this.author = author;
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ActionNotification getAction() {
		return action;
	}

	public void setAction(ActionNotification action) {
		this.action = action;
	}

	public ObjectNotification getObject() {
		return object;
	}

	public void setObject(ObjectNotification object) {
		this.object = object;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public LevelNotification getLevel() {
		return level;
	}

	public void setLevel(LevelNotification level) {
		this.level = level;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<TargetNotification> getTarget() {
		return target;
	}

	public void setTarget(List<TargetNotification> target) {
		this.target = target;
	}
	
	public String generateJson(){
		Gson gson = new Gson();
		String json = gson.toJson(this);
		json = json.toLowerCase();
		json = json.replace("objecttype", "objectType");
		return json;
	}
	
	
}
