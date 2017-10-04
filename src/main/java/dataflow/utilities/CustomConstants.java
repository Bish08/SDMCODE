package dataflow.utilities;

public class CustomConstants {

	//public static final String SOURCE_DIR_LOCAL = "./src/main/resources/";
	public static final String SOURCE_DIR_LOCAL = "/home/sps/monitoring/";
	//public static final String SOURCE_DIR_LOCAL = "/opt/monitoring/";

	public static final String PROPERTIES_FLE ="app.properties";
	public static final String TABLE_VALUEPACK ="dataflow";
	public static final String TABLE_DB ="sdm_dataflow";
	public static final String TABLE_DB_INFO ="dataflow_info_status";
	public static final String TABLE_DB_STATUS ="status_table";
	public static final String TABLE_DB_MONITORLOGS ="monitorlogs";


	public static final int STATUS_WAITING =0;
	public static final int STATUS_SCHEDULED =1;
	public static final int STATUS_INPROGRESS =2;
	public static final int STATUS_COMPLETE =3;
	public static final int STATUS_ERROR =4;
	public static final int STATUS_DELETE =5;
	public static final int STATUS_HOURLY_COMPLETE =7;
	
	public static final int AUTO_INCREMENT_ON =1;
	public static final int AUTO_INCREMENT_OFF =0;

	public static final int IS_LAST =1;
	public static final int IS_NOT_LAST =0;
	
	public static final int FREQUENCY_MONTHLY = 2;
	public static final int FREQUENCY_HOURLY = 3;
	public static final int trigger_type=1;
	
	public static final String OK ="200";
	public static final String KO ="401";
	public static final String SECRET ="SECRET_TASPS";
	public static final String RESULT = "result";
	public static final String DESCRIPTION = "description";
	public static final String API_PUT_TASPS_STATUS = "api_put_tasps_status";
	public static final String DB_DATE_FORMAT =	"yyyy-MM-dd HH:mm:ss";
	public static final String DB_DATEONLY_FORMAT =	"yyyy-MM-dd";
	
	public static final String USER_API_TASPS="USER_API_TASPS";

}
