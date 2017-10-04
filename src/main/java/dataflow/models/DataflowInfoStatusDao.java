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
public interface DataflowInfoStatusDao extends CrudRepository<DataflowInfoStatus, Long> {

	  public DataflowInfoStatus findById(long id);
	  @Query("select u from DataflowInfoStatus u where u.tasps_id = ?#{[0]}")
	  public DataflowInfoStatus findByTasps_id(String tasps_id);
	  
	  @Query("select u from DataflowInfoStatus u where u.tasps_id = ?#{[0]} and u.dataflow_load_date = ?#{[1]}")
	  public DataflowInfoStatus[] findByTaspsAndDate(String tasps_id, String dataflow_load_date);
	  
	  @Modifying
	  @Query("delete from DataflowInfoStatus u where u.value_pack = ?#{[0]} and u.dataflow_load_date = ?#{[1]}")
	  public void deletePreviousRuns(String value_pack, String dataflow_load_date);
} // class DataflowInfoStatusDao
