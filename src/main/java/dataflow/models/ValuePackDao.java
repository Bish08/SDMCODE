package dataflow.models;

import javax.transaction.Transactional;

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
public interface ValuePackDao extends CrudRepository<ValuePack, String> {

	  @Query("select u from ValuePack u where u.dataflow_id = ?#{[0]}")
	  public ValuePack findByDatataflow_id(String dataflow_id);
} // class ValuePackDao
