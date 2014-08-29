package is.gussi.bukkit.plugin.gatekeeper;

public class Data {
	public enum Type {
		WHITELIST, BLACKLIST;
	}
	public enum ContentType {
		CIDR, PLAYER, UUID
	}
	private ContentType contentType;
	private Type type;
	private String content;
	private String source;
	
	public Data(Type type, ContentType content_type, String content, String source) {
		this.setType(type);
		this.setContentType(content_type);
		this.setContent(content);
		this.setSource(source);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType content_type) {
		this.contentType = content_type;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}