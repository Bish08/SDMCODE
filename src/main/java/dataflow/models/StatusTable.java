package dataflow.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import dataflow.utilities.CustomConstants;

/**
 * The Entity annotation indicates that this class is a JPA entity. The Table
 * annotation specifies the name for the table in the db.
 *
 * @author matcon
 */

@Entity
@Table(name = CustomConstants.TABLE_DB_STATUS)

public class StatusTable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String module;
	private String last_running_time;
	@Transient
	private boolean isAlive;
	@Transient
	private String sysdate;

	public StatusTable() {
	}

	public StatusTable(String module, String last_running_time, String sysdate) {
		super();
		this.module = module;
		this.last_running_time = last_running_time;
		this.sysdate = sysdate;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getLast_running_time() {
		return last_running_time;
	}

	public void setLast_running_time(String last_running_time) {
		this.last_running_time = last_running_time;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getSysdate() {
		return sysdate;
	}

	public void setSysdate(String sysdate) {
		this.sysdate = sysdate;
	}

	@Override
	public String toString() {
		return "StatusTable [module=" + module + ", last_running_time=" + last_running_time + ", isAlive=" + isAlive
				+ ", sysdate=" + sysdate + "]";
	}



	

}
