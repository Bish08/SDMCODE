package dataflow.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

import dataflow.models.StatusTable;
import dataflow.models.StatusTableDao;
import dataflow.utilities.CustomConstants;
import dataflow.utilities.DataflowUtilities;
import dataflow.utilities.LoadProperties;

/**
 *
 * @author matcon
 */
@CrossOrigin
@Controller
public class StatusTableController {
	static Logger log = Logger.getLogger(StatusTableController.class.getName());

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * Create a new dataflow and save it in the database.
	 * 
	 * @return A string describing if the dataflow is successfully created or
	 *         not.
	 */

	@RequestMapping(value = "/module-status/update", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin

	public String update(@RequestBody Map<String, Object> bodyRequest) {
		StatusTable statusTable = null;
		log.info("Request /module-status/update received with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();
		try {
			String module = bodyRequest.get("module") != null ? bodyRequest.get("module").toString() : null;
			statusTableDao.updateLastRunningDate(module);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}


	@RequestMapping("/module-status/get-all")
	@ResponseBody
	@CrossOrigin
	public String retrieveAll() {
		log.info("Request /module-status/get-all received ");

		HashMap<String, String> response = new HashMap<String, String>();

		Iterable<StatusTable> statusTableList = null;
		List<StatusTable> list = new ArrayList<StatusTable>();
		String jsonResponse = "";
		try {
			statusTableList = statusTableDao.findAll();
			statusTableList.iterator().forEachRemaining(list::add);
			jsonResponse = new Gson().toJson(list);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		return jsonResponse;
	}

	

	@RequestMapping(value = "/module-status/get-by-module", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin

	public String getById(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /module-status/get-by-module received with: " + bodyRequest.toString());

		StatusTable statusTable = null;
		HashMap<String, String> response = new HashMap<String, String>();
		
		Date dateNow = new Date();
		Date last_running_date = new Date();
		String module = bodyRequest.get("module") != null ? bodyRequest.get("module").toString() : null;
		String jsonResponse = "";
		try {
			statusTable = statusTableDao.findByModule(module);
			String currentTime = statusTableDao.currentTime();
			jsonResponse = new Gson().toJson(statusTable);
			SimpleDateFormat sdf = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);
			try {
				dateNow = sdf.parse(currentTime);
				last_running_date = sdf.parse(statusTable.getLast_running_time());

			} catch (ParseException e1) {
			}
			if (DataflowUtilities.getDateDiff(dateNow, last_running_date, TimeUnit.SECONDS)>Long.parseLong(LoadProperties.getProperty("TIME_WINDOW_MONITOR_RUNNING"))){
				statusTable.setAlive(false);
			} else {
				statusTable.setAlive(true);
			}
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		if (statusTable.getModule() != null) {
			jsonResponse = new Gson().toJson(statusTable);
			
			return jsonResponse;
		} else {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, "No module with id: " + module);
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
	private StatusTableDao statusTableDao;

}
