package dataflow.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import dataflow.utilities.CustomConstants;

/**
 * The Entity annotation indicates that this class is a JPA entity. The Table
 * annotation specifies the name for the table in the db.
 *
 * @author matcon
 */

@Entity
@Table(name = CustomConstants.TABLE_VALUEPACK)

public class ValuePack {
	@Id
	private String dataflow_id;
	private String dataflow_load_date;
	
	public ValuePack() {
	}

	
	public ValuePack(
			String dataflow_load_date, String dataflow_id) {
		// super();
		// this.id = id;
		this.dataflow_load_date = dataflow_load_date;
		this.dataflow_id = dataflow_id;
	}

	
	public String getDataflow_load_date() {
		return dataflow_load_date;
	}

	public void setDataflow_load_date(String dataflow_load_date) {
		this.dataflow_load_date = dataflow_load_date;
	}

	public String getDataflow_id() {
		return dataflow_id;
	}

	public void setDataflow_id(String dataflow_id) {
		this.dataflow_id = dataflow_id;
	}




}
