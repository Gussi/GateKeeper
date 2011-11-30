package is.gussi.bukkit.plugin.whitelist;

import java.util.Date;

public abstract class Data {
	protected String comment = "";
	protected String type = "whitelist";
	protected Date expire = new Date();
	
	abstract public boolean equals(Object obj);

	public String getComment() {
		return comment;
	}

	public Data setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public String getType() {
		return type;
	}

	public Data setType(String type) {
		this.type = type;
		return this;
	}

	public Date getExpire() {
		return expire;
	}

	public Data setExpire(Date expire) {
		this.expire = expire;
		return this;
	}

	
}
