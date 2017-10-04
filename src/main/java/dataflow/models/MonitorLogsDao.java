package dataflow.models;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * INFO about DAO
 * A DAO for the entity User is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author matcon
 */
@Transactional
public interface MonitorLogsDao extends CrudRepository<MonitorLogs, String> {

	  @Query("select u from MonitorLogs u where u.TASPS_ID like ?#{[0]}")
	  public Iterable<MonitorLogs> findByTaspsId(String tasps_id);
	  
	  @Query("select u from MonitorLogs u where u.TASPS_ID like ?#{[0]} and u.DATED > ?#{[1]} and u.DATED < ?#{[2]}")
	  public Iterable<MonitorLogs> findByTaspsIdAndDate(String tasps_id, String date_from, String date_to);
	  
	  @Query("select u from MonitorLogs u where u.DATED > ?#{[0]} and u.DATED < ?#{[1]}")
	  public Iterable<MonitorLogs> findByDate(String date_from, String date_to);
	    
	  @Query("select u from MonitorLogs u where u.EVENT_TYPE like ?#{[0]} and u.DATED > ?#{[1]} and u.DATED < ?#{[2]}")
	  public Iterable<MonitorLogs> findByEventType(String event_type, String date_from, String date_to);
	  
	  @Query("select current_timestamp from StatusTable u")
	  public String currentTime ();
	  
} // class StatusTableDao

