package is.gussi.bukkit.plugin.whitelist;

import java.util.Date;

public abstract class Data {
	protected String comment = "";
	protected Type type = Type.WHITELIST;
	protected Date expire = new Date();

	public enum Type {
		WHITELIST("whitelist"),
		BLACKLIST("blacklist");
		
		private final String type;
		
		Type(String type) {
			this.type = type;
		}
		
		public String toString() {
			return type;
		}
	}
	
	abstract public boolean equals(Object obj);

	public String getComment() {
		return comment;
	}

	public Data setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public Type getType() {
		return type;
	}

	public Data setType(Type type) {
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
