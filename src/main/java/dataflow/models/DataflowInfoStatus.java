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
@Table(name = CustomConstants.TABLE_DB_INFO)

public class DataflowInfoStatus {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String tasps_id;
	private String dataflow_load_date;
	private Integer status;
	private String description_status;
	private String date_action;
	private String value_pack;


	public DataflowInfoStatus() {
	}

	public DataflowInfoStatus(long id) {
		this.id = id;
	}

	public DataflowInfoStatus(String tasps_id, String dataflow_load_date, Integer status,
			String description_status, String date_action,String value_pack) {
		super();
		this.tasps_id = tasps_id;
		this.dataflow_load_date = dataflow_load_date;
		this.status = status;
		this.description_status = description_status;
		this.date_action = date_action;
		this.value_pack= value_pack;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTasps_id() {
		return tasps_id;
	}

	public void setTasps_id(String tasps_id) {
		this.tasps_id = tasps_id;
	}

	public String getDataflow_load_date() {
		return dataflow_load_date;
	}

	public void setDataflow_load_date(String dataflow_load_date) {
		this.dataflow_load_date = dataflow_load_date;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getDescription_status() {
		return description_status;
	}

	public void setDescription_status(String description_status) {
		this.description_status = description_status;
	}
	
	public String getDate_action() {
		return date_action;
	}

	public void setDate_action(String date_action) {
		this.date_action = date_action;
	}

	public String getValue_pack() {
		return value_pack;
	}

	public void setValue_pack(String value_pack) {
		this.value_pack = value_pack;
	}

	@Override
	public String toString() {
		return "DataflowInfoStatus [id=" + id + ", tasps_id=" + tasps_id + ", dataflow_load_date=" + dataflow_load_date + ", status=" + status + ", description_status="
				+ description_status + "]";
	}

	
	

}
