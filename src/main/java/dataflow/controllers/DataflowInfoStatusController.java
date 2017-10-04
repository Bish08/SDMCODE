package dataflow.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import dataflow.models.DataflowInfoStatus;
import dataflow.models.DataflowInfoStatusDao;
import dataflow.utilities.CustomConstants;
import dataflow.utilities.LoadProperties;

/**
 *
 * @author matcon
 */
@CrossOrigin
@Controller
public class DataflowInfoStatusController {
	static Logger log = Logger.getLogger(DataflowInfoStatusController.class.getName());

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * Create a new dataflow and save it in the database.
	 * 
	 * @return A string describing if the dataflow is successfully created or
	 *         not.
	 */

	@RequestMapping(value = "/info-status/create", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin

	public String create(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /info-status/create received with: " + bodyRequest.toString());

		DataflowInfoStatus dataflow = null;
		HashMap<String, String> response = new HashMap<String, String>();
		try {
			String tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
			String dataflow_load_date = bodyRequest.get("dataflow_load_date") != null
					? bodyRequest.get("dataflow_load_date").toString() : null;
			Integer status = bodyRequest.get("status") != null ? Integer.parseInt(bodyRequest.get("status").toString())
					: null;
			String description_status = bodyRequest.get("description_status") != null
					? bodyRequest.get("description_status").toString() : null;
					String value_pack = bodyRequest.get("value_pack") != null
							? bodyRequest.get("value_pack").toString() : null;

			DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);
			Date today = Calendar.getInstance().getTime();        
			
			dataflow = new DataflowInfoStatus(tasps_id, dataflow_load_date, status, description_status, dateFormat.format(today),value_pack);
			dataflowInfoStatusDao.save(dataflow);
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
	@RequestMapping(value = "/info-status/delete", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin
	public String delete(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /info-status/delete received with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {

			DataflowInfoStatus dataflow = dataflowInfoStatusDao
					.findOne(Long.parseLong(bodyRequest.get("id").toString()));
			dataflowInfoStatusDao.delete(dataflow);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	@RequestMapping("/info-status/get-all")
	@ResponseBody
	@CrossOrigin
	public String retrieveAll() {
		log.info("Request /info-status/get-all received");

		HashMap<String, String> response = new HashMap<String, String>();

		Iterable<DataflowInfoStatus> dataflowList = null;
		List<DataflowInfoStatus> list = new ArrayList<DataflowInfoStatus>();
		String jsonResponse = "";
		try {
			dataflowList = dataflowInfoStatusDao.findAll();
			dataflowList.iterator().forEachRemaining(list::add);
			jsonResponse = new Gson().toJson(list);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		return jsonResponse;
	}

	@RequestMapping("/info-status/get-by-id")
	@ResponseBody
	@CrossOrigin
	public String getById(long id) {
		log.info("Request /info-status/get-by-id");

		String jsonResponse = "";
		HashMap<String, String> response = new HashMap<String, String>();
		DataflowInfoStatus dataflow = null;

		try {
			dataflow = dataflowInfoStatusDao.findById(id);
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

	@RequestMapping("/info-status/get-by-tasps-id")
	@ResponseBody
	@CrossOrigin
	public String getByTaspsId(String tasps_id) {
		log.info("Request /info-status/get-by-tasps-id");

		HashMap<String, String> response = new HashMap<String, String>();

		String jsonResponse = "";
		DataflowInfoStatus dataflow = null;

		try {
			dataflow = dataflowInfoStatusDao.findByTasps_id(tasps_id);

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
	
		@RequestMapping(value = "/info-status/get-by-tasps-and-date", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
		@ResponseBody
		@CrossOrigin

		public String getByTaspsAndDate(@RequestBody Map<String, Object> bodyRequest) {
			log.info("Request /info-status/get-by-tasps-and-date received with: " + bodyRequest.toString());

			DataflowInfoStatus dataflow[] = null;
			HashMap<String, String> response = new HashMap<String, String>();
			String tasps_id = "";
			String jsonResponse = "";
			try {
				tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
				String dataflow_load_date = bodyRequest.get("dataflow_load_date") != null ? bodyRequest.get("dataflow_load_date").toString() : null;

					dataflow = dataflowInfoStatusDao.findByTaspsAndDate(tasps_id,dataflow_load_date);

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

	// ------------------------
	// PRIVATE FIELDS
	// ------------------------

	@Autowired
	private DataflowInfoStatusDao dataflowInfoStatusDao;

}
