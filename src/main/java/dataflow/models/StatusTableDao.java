package dataflow.models;

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
public interface StatusTableDao extends CrudRepository<StatusTable, String> {

	  @Query("select u, sysdate from StatusTable u where u.module like ?#{[0]}")
	  public StatusTable findByModule(String module);
	  
	  @Modifying  
	  @Query("update StatusTable u set u.last_running_time = current_timestamp where u.module like ?#{[0]}")
	  public Integer updateLastRunningDate(String module);
	  
	  @Query("select current_timestamp from StatusTable u")
	  public String currentTime ();
	  
} // class StatusTableDao

