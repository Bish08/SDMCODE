package dataflow.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import dataflow.utilities.CustomConstants;

/**
 * The Entity annotation indicates that this class is a JPA entity. The Table
 * annotation specifies the name for the table in the db.
 *
 * @author matcon
 */
class CompositeKey implements Serializable {
    private String DATED;
    private String MESSAGE;
    private String EVENT_TYPE;
    private String TASPS_ID;

}

@Entity
@Table(name = CustomConstants.TABLE_DB_MONITORLOGS)
@IdClass(CompositeKey.class)
public class MonitorLogs {
	
	private String USER_D;
	@Id
	private String DATED;
	private String LOGGER;
	private String LEVEL;
	@Id
	private String MESSAGE;
	@Id
	private String EVENT_TYPE;
	private String DATAFLOW_ID;
	@Id
	private String TASPS_ID;

	public MonitorLogs(){
		
	}
	public MonitorLogs(String uSER_D, String dATED, String lOGGER, String lEVEL, String mESSAGE, String eVENT_TYPE,
			String dATAFLOW_ID, String tASPS_ID) {
		super();
		USER_D = uSER_D;
		DATED = dATED;
		LOGGER = lOGGER;
		LEVEL = lEVEL;
		MESSAGE = mESSAGE;
		EVENT_TYPE = eVENT_TYPE;
		DATAFLOW_ID = dATAFLOW_ID;
		TASPS_ID = tASPS_ID;
	}
	public String getUSER_D() {
		return USER_D;
	}
	public void setUSER_D(String uSER_D) {
		USER_D = uSER_D;
	}
	public String getDATED() {
		return DATED;
	}
	public void setDATED(String dATED) {
		DATED = dATED;
	}
	public String getLOGGER() {
		return LOGGER;
	}
	public void setLOGGER(String lOGGER) {
		LOGGER = lOGGER;
	}
	public String getLEVEL() {
		return LEVEL;
	}
	public void setLEVEL(String lEVEL) {
		LEVEL = lEVEL;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String mESSAGE) {
		MESSAGE = mESSAGE;
	}
	public String getEVENT_TYPE() {
		return EVENT_TYPE;
	}
	public void setEVENT_TYPE(String eVENT_TYPE) {
		EVENT_TYPE = eVENT_TYPE;
	}
	public String getDATAFLOW_ID() {
		return DATAFLOW_ID;
	}
	public void setDATAFLOW_ID(String dATAFLOW_ID) {
		DATAFLOW_ID = dATAFLOW_ID;
	}
	public String getTASPS_ID() {
		return TASPS_ID;
	}
	public void setTASPS_ID(String tASPS_ID) {
		TASPS_ID = tASPS_ID;
	}
	@Override
	public String toString() {
		return "MonitorLogs [USER_D=" + USER_D + ", DATED=" + DATED + ", LOGGER=" + LOGGER + ", LEVEL=" + LEVEL
				+ ", MESSAGE=" + MESSAGE + ", EVENT_TYPE=" + EVENT_TYPE + ", DATAFLOW_ID=" + DATAFLOW_ID + ", TASPS_ID="
				+ TASPS_ID + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((DATAFLOW_ID == null) ? 0 : DATAFLOW_ID.hashCode());
		result = prime * result + ((DATED == null) ? 0 : DATED.hashCode());
		result = prime * result + ((EVENT_TYPE == null) ? 0 : EVENT_TYPE.hashCode());
		result = prime * result + ((LEVEL == null) ? 0 : LEVEL.hashCode());
		result = prime * result + ((LOGGER == null) ? 0 : LOGGER.hashCode());
		result = prime * result + ((MESSAGE == null) ? 0 : MESSAGE.hashCode());
		result = prime * result + ((TASPS_ID == null) ? 0 : TASPS_ID.hashCode());
		result = prime * result + ((USER_D == null) ? 0 : USER_D.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MonitorLogs other = (MonitorLogs) obj;
		if (DATAFLOW_ID == null) {
			if (other.DATAFLOW_ID != null)
				return false;
		} else if (!DATAFLOW_ID.equals(other.DATAFLOW_ID))
			return false;
		if (DATED == null) {
			if (other.DATED != null)
				return false;
		} else if (!DATED.equals(other.DATED))
			return false;
		if (EVENT_TYPE == null) {
			if (other.EVENT_TYPE != null)
				return false;
		} else if (!EVENT_TYPE.equals(other.EVENT_TYPE))
			return false;
		if (LEVEL == null) {
			if (other.LEVEL != null)
				return false;
		} else if (!LEVEL.equals(other.LEVEL))
			return false;
		if (LOGGER == null) {
			if (other.LOGGER != null)
				return false;
		} else if (!LOGGER.equals(other.LOGGER))
			return false;
		if (MESSAGE == null) {
			if (other.MESSAGE != null)
				return false;
		} else if (!MESSAGE.equals(other.MESSAGE))
			return false;
		if (TASPS_ID == null) {
			if (other.TASPS_ID != null)
				return false;
		} else if (!TASPS_ID.equals(other.TASPS_ID))
			return false;
		if (USER_D == null) {
			if (other.USER_D != null)
				return false;
		} else if (!USER_D.equals(other.USER_D))
			return false;
		return true;
	}
	
	
	

}
