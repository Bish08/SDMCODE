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
public interface DataflowDao extends CrudRepository<Dataflow, Long> {

	  public Dataflow findById(long id);
	  @Query("select u from Dataflow u where u.tasps_id = ?#{[0]}")
	  public Dataflow findByTasps_id(String tasps_id);
	  
	  @Modifying  
	  @Query("update Dataflow u set u.dataflow_load_date = ?#{[0]} where u.value_pack like ?#{[1]}")
	  public Integer updateDataflowsDate(String dataflow_load_date, String value_pack);
	  
} // class DataflowDao
