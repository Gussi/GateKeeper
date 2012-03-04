package is.gussi.bukkit.plugin.gatekeeper;

public abstract class Data {
	protected String comment = "";
	protected String type = "";
	protected String source = "";
	
	abstract public boolean equals(Object obj);

	public String getComment() {
		return this.comment;
	}

	public Data setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getType() {
		return this.type;
	}

	public Data setType(String type) {
		this.type = type;
		return this;
	}
	
	public String getSource() {
		return this.source;
	}
	
	public Data setSource(String source) {
		this.source = source;
		return this;
	}
}