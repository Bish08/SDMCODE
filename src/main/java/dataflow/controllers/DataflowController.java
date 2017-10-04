package dataflow.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTSigner.Options;
import com.google.gson.Gson;

import dataflow.models.Dataflow;
import dataflow.models.DataflowDao;
import dataflow.models.DataflowInfoStatus;
import dataflow.models.DataflowInfoStatusDao;
import dataflow.models.ValuePack;
import dataflow.models.ValuePackDao;
import dataflow.utilities.CustomConstants;
import dataflow.utilities.DataflowUtilities;
import dataflow.utilities.LoadProperties;

/**
 *
 * @author matcon
 */
@CrossOrigin
@Controller
public class DataflowController {
	static Logger log = Logger.getLogger(DataflowController.class.getName());

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * Create a new dataflow and save it in the database.
	 * 
	 * @return A string describing if the dataflow is successfully created or
	 *         not.
	 */

	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin

	public String create(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /create with: " + bodyRequest.toString());

		Dataflow dataflow = null;
		HashMap<String, String> response = new HashMap<String, String>();
		DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATEONLY_FORMAT);
		Date today = Calendar.getInstance().getTime();
		try {
			String dataflow_id = bodyRequest.get("dataflow_id") != null ? bodyRequest.get("dataflow_id").toString()
					: null;
			String tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
			String description = bodyRequest.get("description") != null ? bodyRequest.get("description").toString()
					: null;
			Integer trigger_type = bodyRequest.get("trigger_type") != null
					? Integer.parseInt(bodyRequest.get("trigger_type").toString()) : null;
			Integer frequency = bodyRequest.get("frequency") != null
					? Integer.parseInt(bodyRequest.get("frequency").toString()) : null;
			String last_run_date = bodyRequest.get("last_run_date") != null
					? bodyRequest.get("last_run_date").toString() : dateFormat.format(today);
			String dataflow_load_date = bodyRequest.get("dataflow_load_date") != null
					? bodyRequest.get("dataflow_load_date").toString() : dateFormat.format(today);
			Integer status = bodyRequest.get("status") != null ? Integer.parseInt(bodyRequest.get("status").toString())
					: null;
			String description_status = bodyRequest.get("description_status") != null && bodyRequest.get("description_status").toString().length() < 900
					? bodyRequest.get("description_status").toString() : null;
			String dataflow_dependencies = bodyRequest.get("dataflow_dependencies") != null
					? bodyRequest.get("dataflow_dependencies").toString() : null;
			String regex = bodyRequest.get("regex") != null ? bodyRequest.get("regex").toString() : null;
			Integer number_files = bodyRequest.get("number_files") != null
					? Integer.parseInt(bodyRequest.get("number_files").toString()) : null;
			String folder = bodyRequest.get("folder") != null ? bodyRequest.get("folder").toString() : null;
			Integer delete_status = bodyRequest.get("delete_status") != null
					? Integer.parseInt(bodyRequest.get("delete_status").toString()) : null;
			String delete_date = bodyRequest.get("delete_date") != null ? bodyRequest.get("delete_date").toString()
					: null;
			Integer auto_increment_date = bodyRequest.get("auto_increment_date") != null
					? Integer.parseInt(bodyRequest.get("auto_increment_date").toString()) : null;
			String last_end_run_date = bodyRequest.get("last_end_run_date") != null
					? bodyRequest.get("last_end_run_date").toString() : null;
			Integer isLast = bodyRequest.get("isLast") != null ? Integer.parseInt(bodyRequest.get("isLast").toString())
					: null;
			String value_pack = bodyRequest.get("value_pack") != null ? bodyRequest.get("value_pack").toString() : null;

			dataflow = new Dataflow(description, trigger_type, frequency, last_run_date, dataflow_load_date, status,
					dataflow_dependencies, regex, number_files, folder, dataflow_id, tasps_id, description_status,
					delete_status, delete_date, auto_increment_date, last_end_run_date, isLast, value_pack);
			dataflowDao.save(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	/**
	 * Delete a dataflow from the database using his id.
	 * 
	 * @return A string describing if the dataflow is successfully created or
	 *         not.
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin
	public String delete(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /delete with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {
			DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);
			Date today = Calendar.getInstance().getTime();
			Dataflow dataflow = dataflowDao.findOne(Long.parseLong(bodyRequest.get("id").toString()));
			dataflow.setDelete_status(CustomConstants.STATUS_DELETE);
			dataflow.setStatus(CustomConstants.STATUS_DELETE);
			dataflow.setDelete_date(dateFormat.format(today));
			dataflowDao.save(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin
	public String updateDataflow(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /update with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {
			Dataflow dataflow = dataflowDao.findOne(Long.parseLong(bodyRequest.get("id").toString()));

			String dataflow_id = bodyRequest.get("dataflow_id") != null ? bodyRequest.get("dataflow_id").toString()
					: null;
			String tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
			String description = bodyRequest.get("description") != null ? bodyRequest.get("description").toString()
					: null;
			Integer trigger_type = bodyRequest.get("trigger_type") != null
					? Integer.parseInt(bodyRequest.get("trigger_type").toString()) : null;
			Integer frequency = bodyRequest.get("frequency") != null
					? Integer.parseInt(bodyRequest.get("frequency").toString()) : null;
			String last_run_date = bodyRequest.get("last_run_date") != null
					? bodyRequest.get("last_run_date").toString() : null;
			String dataflow_load_date = bodyRequest.get("dataflow_load_date") != null
					? bodyRequest.get("dataflow_load_date").toString() : null;
			Integer status = bodyRequest.get("status") != null ? Integer.parseInt(bodyRequest.get("status").toString())
					: null;
			String description_status = bodyRequest.get("description_status") != null
					? bodyRequest.get("description_status").toString() : null;
			String dataflow_dependencies = bodyRequest.get("dataflow_dependencies") != null
					? bodyRequest.get("dataflow_dependencies").toString() : null;
			String regex = bodyRequest.get("regex") != null ? bodyRequest.get("regex").toString() : null;
			Integer number_files = bodyRequest.get("number_files") != null
					? Integer.parseInt(bodyRequest.get("number_files").toString()) : null;
			String folder = bodyRequest.get("folder") != null ? bodyRequest.get("folder").toString() : null;
			Integer delete_status = bodyRequest.get("delete_status") != null
					? Integer.parseInt(bodyRequest.get("delete_status").toString()) : null;
			String delete_date = bodyRequest.get("delete_date") != null ? bodyRequest.get("delete_date").toString()
					: null;
			Integer auto_increment_date = bodyRequest.get("auto_increment_date") != null
					? Integer.parseInt(bodyRequest.get("auto_increment_date").toString()) : null;
			Integer isLast = bodyRequest.get("isLast") != null ? Integer.parseInt(bodyRequest.get("isLast").toString())
					: null;
			String value_pack = bodyRequest.get("value_pack") != null ? bodyRequest.get("value_pack").toString() : null;

			// dataflow.setId(id);
			if (auto_increment_date != null)
				dataflow.setAuto_increment_date(auto_increment_date);
			//LOGIC TO ALIGN ALL DATAFLOW ANALYSIS AND VALUE PACK 
			//TO THE SAME LOAD DATE AND DELETE HISTORY FOR THIS DATE
			if (dataflow_load_date != null){
				String oldDateDataflow= dataflow.getDataflow_load_date();
				dataflow.setDataflow_load_date(dataflow_load_date);
				
				DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATEONLY_FORMAT);
				Date oldDate = dateFormat.parse(oldDateDataflow);
				Date newDate = dateFormat.parse(dataflow_load_date);
				int dayToAdd = DataflowController.betweenDates(oldDate, newDate);

  				ValuePack valuePack = valuePackDao.findByDatataflow_id(dataflow.getValue_pack());
				String newVPLoadDate = valuePack.getDataflow_load_date();
				String VPloadDate = valuePack.getDataflow_load_date();
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(dateFormat.parse(VPloadDate));
				} catch (ParseException e) {
					log.error("ERROR", e);
				}
				c.add(Calendar.DAY_OF_MONTH, dayToAdd); // number of days to add
				newVPLoadDate = dateFormat.format(c.getTime());
				
				valuePack.setDataflow_load_date(newVPLoadDate);
  				valuePackDao.save(valuePack);
  				dataflowInfoStatusDao.deletePreviousRuns(dataflow.getValue_pack(), newVPLoadDate);
				dataflowDao.updateDataflowsDate(dataflow_load_date, dataflow.getValue_pack());
			}
			if (delete_status != null)
				dataflow.setDelete_status(delete_status);
			if (delete_date != null)
				dataflow.setDelete_date(delete_date);
			if (description != null)
				dataflow.setDescription(description);
			if (frequency != null)
				dataflow.setFrequency(frequency);
			if (isLast != null)
				dataflow.setIsLast(isLast);
			if (value_pack != null)
				dataflow.setValue_pack(value_pack);
			if (last_run_date != null)
				dataflow.setLast_run_date(last_run_date);
			if (status != null)
				dataflow.setStatus(status);
			if (trigger_type != null) {
				dataflow.setTrigger_type(trigger_type);

				if (trigger_type == 0) {
					dataflow.setDataflow_dependencies(null);
					dataflow.setRegex(null);
					dataflow.setNumber_files(0);
					dataflow.setFolder(null);
				} else if (trigger_type == 1) {
					dataflow.setDataflow_dependencies(null);
					if (regex != null)
						dataflow.setRegex(regex);
					if (number_files != null)
						dataflow.setNumber_files(number_files);
					if (folder != null)
						dataflow.setFolder(folder);
				} else if (trigger_type == 2) {
					dataflow.setRegex(null);
					dataflow.setNumber_files(0);
					dataflow.setFolder(null);
					if (dataflow_dependencies != null)
						dataflow.setDataflow_dependencies(dataflow_dependencies);
				}
			}
			if (dataflow_id != null)
				dataflow.setDataflow_id(dataflow_id);
			if (tasps_id != null)
				dataflow.setTasps_id(tasps_id);
			if (description_status != null)
				dataflow.setDescription_status(description_status);
			dataflowDao.save(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	@RequestMapping(value = "/update-status", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin
	public String updateStatus(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /update-status with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {
			Dataflow dataflow = dataflowDao.findOne(Long.parseLong(bodyRequest.get("id").toString()));
			Integer status = bodyRequest.get("status") != null ? Integer.parseInt(bodyRequest.get("status").toString())
					: null;
			// dataflow.setId(id);
			dataflow.setStatus(status);
			if (status == CustomConstants.STATUS_COMPLETE
					&& dataflow.getAuto_increment_date() == CustomConstants.AUTO_INCREMENT_ON) {
				String loadDate = dataflow.getDataflow_load_date();
				DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATEONLY_FORMAT);
				Calendar c = Calendar.getInstance();
				try {
					c.setTime(dateFormat.parse(loadDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.add(Calendar.DAY_OF_MONTH, 1); // number of days to add
				String newLoadDate = dateFormat.format(c.getTime());
				// System.out.println(newLoadDate);
				dataflow.setDataflow_load_date(newLoadDate);
			}
			dataflowDao.save(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	@RequestMapping(value = "/update-status-by-tasps", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin
	public String updateStatusByTasps(@RequestBody Map<String, Object> bodyRequest) throws ParseException, IOException {
		log.info("Request /update-status-by-tasps with: " + bodyRequest.toString());

		HashMap<String, Object> response = new HashMap<String, Object>();
		Integer status = bodyRequest.get("status") != null ? Integer.parseInt(bodyRequest.get("status").toString())
				: null;
		String description_status = bodyRequest.get("description_status") != null && bodyRequest.get("description_status").toString().length() < 900
				? bodyRequest.get("description_status").toString() : null;
		String tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
		String last_end_run_date = bodyRequest.get("last_end_run_date") != null
				? bodyRequest.get("last_end_run_date").toString() : null;

		List<String> roles = new ArrayList<String>();
		roles = DataflowUtilities.addRoles(roles);
		// speadmin,speoper,spedesigner,speexpertdesigner,spehpdeveloper
		// if status is in progress, try to start and after update on sdm status in progress
		Dataflow dataflow = null;
		dataflow = dataflowDao.findByTasps_id(tasps_id);
		ValuePack valuePack = valuePackDao.findByDatataflow_id(dataflow.getValue_pack());
		DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATEONLY_FORMAT);
		DateFormat dateTimeFormat = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);

		Date today = Calendar.getInstance().getTime();
		String valuePackDataflowInfoStatusDate = valuePack.getDataflow_load_date();
		if (status == CustomConstants.STATUS_INPROGRESS) {
			String xAccessToken = mkJWT(LoadProperties.getProperty(CustomConstants.USER_API_TASPS), roles);
			String url = LoadProperties.getProperty(CustomConstants.API_PUT_TASPS_STATUS) + tasps_id;
			HashMap<String, Integer> data = new HashMap<String, Integer>();
			data.put("runOnce", 1);
			data.put("factsMaxAge", 0);
			data.put("factsMinAge", 0);
			HttpResponse responseTasps = DataflowUtilities.sendPut(new Gson().toJson(data), url, xAccessToken);
			int responseCode = responseTasps.getStatusLine().getStatusCode();
			if (responseCode != 0 && responseCode != 204) {
				response.put(CustomConstants.RESULT, CustomConstants.KO);
				response.put("tasps_id", tasps_id);
				String error = "";
				if (responseTasps.getEntity().getContent().toString().length()>=800){
					error= responseTasps.getEntity().getContent().toString().substring(0, 700);
				}else{
					error= responseTasps.getEntity().getContent().toString();
				}
				response.put(CustomConstants.DESCRIPTION, "Error during API request to SPS Backend with error: " + error);
				
				// SET ERROR STATUS		
				dataflow.setTasps_id(tasps_id);
				dataflow.setStatus(CustomConstants.STATUS_DELETE);
				dataflow.setLast_run_date(dateFormat.format(today));
				
				dataflowDao.save(dataflow);

			} else {
				try {
		
					dataflow.setTasps_id(tasps_id);
					dataflow.setStatus(status);
					dataflow.setLast_run_date(dateFormat.format(today));
					dataflow.setDescription_status(description_status);
					dataflowDao.save(dataflow);
				} catch (Exception ex) {
					response.put(CustomConstants.RESULT, CustomConstants.KO);
					response.put(CustomConstants.DESCRIPTION,
							"Dataflow successfully started on TASPS, error while updating status on Datamart DB, "
									+ ex.toString());
					return new Gson().toJson(response);
				}
				response.put(CustomConstants.RESULT, CustomConstants.OK);
				response.put("id", dataflow.getDataflow_id());
				response.put("tasps_id", dataflow.getTasps_id());
			}
		} else {
			try {
				dataflow.setTasps_id(tasps_id);
				dataflow.setStatus(status);
				dataflow.setDescription_status(description_status);
				if (status == CustomConstants.STATUS_COMPLETE || status == CustomConstants.STATUS_ERROR) {
					dataflow.setLast_end_run_date(last_end_run_date);
				}
				
				
				/*Changing the Status for Hourly  Load..
				if(dataflow.getFrequency().intValue() == CustomConstants.FREQUENCY_HOURLY  
						&& dataflow.getIsLast() != CustomConstants.IS_LAST && status != CustomConstants.STATUS_COMPLETE) {
					dataflow.setStatus(new Integer(CustomConstants.STATUS_HOURLY_COMPLETE));
					//status= CustomConstants.STATUS_HOURLY_COMPLETE;
					log.info("change the status for hourly data load as STATUS_HOURLY_COMPLETE");
				}*/
				

				// IF STATUS COMPLETE , INCREMENT DATE and IS LAST
				// UPDATE LOAD_DATE TO ALL DATAFLOW WITH THE SAME value_pack
				// UPDATE LOAD_DATE TO VALUE_PACK
				if (status == CustomConstants.STATUS_COMPLETE
						&& dataflow.getAuto_increment_date() == CustomConstants.AUTO_INCREMENT_ON
						&& dataflow.getIsLast() == CustomConstants.IS_LAST) {
					//CHECK DATE DATAFLOW
					String newLoadDate = dataflow.getDataflow_load_date();
					String loadDate = dataflow.getDataflow_load_date();
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(dateFormat.parse(loadDate));
					} catch (ParseException e) {
						log.error("ERROR", e);
					}
					c.add(Calendar.DAY_OF_MONTH, 1); // number of days to add
					newLoadDate = dateFormat.format(c.getTime());
					dataflow.setDataflow_load_date(newLoadDate);
					//CHECK AND INCREMENT LOAD DATE VALUE PACK
					String newVPLoadDate = valuePack.getDataflow_load_date();
					String VPloadDate = valuePack.getDataflow_load_date();
					try {
						c.setTime(dateFormat.parse(VPloadDate));
					} catch (ParseException e) {
						log.error("ERROR", e);
						response.put(CustomConstants.RESULT, CustomConstants.KO);
						response.put(CustomConstants.DESCRIPTION,
								"Dataflow status error updating on Datamart DB, " + e.toString());
						return new Gson().toJson(response);

					}
					c.add(Calendar.DAY_OF_MONTH, 1); // number of days to add
					newVPLoadDate = dateFormat.format(c.getTime());
					valuePack.setDataflow_load_date(newVPLoadDate);
					valuePackDao.save(valuePack);
					dataflowDao.updateDataflowsDate(newLoadDate, dataflow.getValue_pack());
				}
				dataflowDao.save(dataflow);
				response.put(CustomConstants.RESULT, CustomConstants.OK);
				response.put("id", dataflow.getDataflow_id());
				response.put("tasps_id", dataflow.getTasps_id());
				
			} catch (Exception ex) {
				response.put(CustomConstants.RESULT, CustomConstants.KO);
				response.put(CustomConstants.DESCRIPTION,
						"Dataflow status error updating on Datamart DB, " + ex.toString());
				return new Gson().toJson(response);

			}

			
		}
		description_status = "Dataflow " + tasps_id + " updated with status " + status;

		DataflowInfoStatus dataflowInfoStatus = new DataflowInfoStatus(tasps_id, valuePackDataflowInfoStatusDate, status,
				description_status, dateTimeFormat.format(today),dataflow.getValue_pack());
		dataflowInfoStatusDao.save(dataflowInfoStatus);
		
	
		return new Gson().toJson(response);
	}

	@RequestMapping("/get-all")
	@ResponseBody
	@CrossOrigin
	public String retrieveAll() {
		log.info("Request /get-all");

		HashMap<String, String> response = new HashMap<String, String>();

		Iterable<Dataflow> dataflowList = null;
		List<Dataflow> list = new ArrayList<Dataflow>();
		String jsonResponse = "";
		try {
			dataflowList = dataflowDao.findAll();
			dataflowList.iterator().forEachRemaining(list::add);
			jsonResponse = new Gson().toJson(list);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		return jsonResponse;
	}

	@RequestMapping("/get-by-id")
	@ResponseBody
	@CrossOrigin
	public String getById(long id) {
		log.info("Request /get-by-id");
		String jsonResponse = "";
		HashMap<String, String> response = new HashMap<String, String>();
		Dataflow dataflow = null;

		try {
			dataflow = dataflowDao.findById(id);
			jsonResponse = new Gson().toJson(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		if (dataflow != null) {
			jsonResponse = new Gson().toJson(dataflow);
			return jsonResponse;
		} else {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, "No dataflow with id: " + id);
			return new Gson().toJson(response);
		}
	}

	@RequestMapping("/get-by-tasps-id")

	@ResponseBody
	@CrossOrigin
	public String getByTaspsId(String tasps_id) {
		HashMap<String, String> response = new HashMap<String, String>();
		log.info("Request /get-by-tasps-id");

		String jsonResponse = "";
		Dataflow dataflow = null;

		try {
			dataflow = dataflowDao.findByTasps_id(tasps_id);

		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		if (dataflow != null) {
			jsonResponse = new Gson().toJson(dataflow);
			return jsonResponse;
		} else {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, "No dataflow with TASPS id: " + tasps_id);
			return new Gson().toJson(response);
		}

	}

	public static String mkJWT(String username, List<String> roles) {
		String secret = LoadProperties.getProperty(CustomConstants.SECRET);
		JWTSigner signer = new JWTSigner(secret);
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", username);
		claims.put("roles", roles);
		Options options = new Options();
		// can play with a few of those options
		return signer.sign(claims, options);
	}
	
	public static int betweenDates(Date firstDate, Date secondDate) throws IOException
	{
	    return (int)(ChronoUnit.DAYS.between(firstDate.toInstant(), secondDate.toInstant()));
	}
	
	
	
	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Autowired
	private DataflowDao dataflowDao;
	@Autowired
	private ValuePackDao valuePackDao;
	@Autowired
	private DataflowInfoStatusDao dataflowInfoStatusDao;
}
