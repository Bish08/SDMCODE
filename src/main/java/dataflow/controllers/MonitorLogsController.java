package dataflow.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import dataflow.models.MonitorLogs;
import dataflow.models.MonitorLogsDao;
import dataflow.utilities.CustomConstants;
import dataflow.utilities.LoadProperties;

/**
 *
 * @author matcon
 */
@CrossOrigin
@Controller
public class MonitorLogsController {
	static Logger log = Logger.getLogger(MonitorLogsController.class.getName());

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * 
	 */

	@RequestMapping(value = "/monitorlogs/get", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	@ResponseBody
	@CrossOrigin

	public String get(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request /monitorlogs/get received with: " + bodyRequest.toString());
		Iterable<MonitorLogs> monitorLogs = new ArrayList<MonitorLogs>();
		HashMap<String, String> response = new HashMap<String, String>();
		String tasps_id = bodyRequest.get("tasps_id") != null ? bodyRequest.get("tasps_id").toString() : null;
		String dateFrom = bodyRequest.get("date_from") != null ? bodyRequest.get("date_from").toString() : null;
		String dateTo = bodyRequest.get("date_to") != null ? bodyRequest.get("date_to").toString() : null;
		String event_type = bodyRequest.get("event_type") != null ? bodyRequest.get("event_type").toString() : null;
		String request_log = bodyRequest.get("request_log") != null ? bodyRequest.get("request_log").toString()
				: "false";

		if (dateTo == null) {
			Date dateNow = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);
			dateTo = sdf.format(dateNow);
			//dateTo= monitorLogsDao.currentTime();
			log.info("Request /monitorlogs/get received without dateTo that is automatically setted to: " + bodyRequest.toString());

		}
		String jsonResponse = "";

		List<MonitorLogs> listLogs = new ArrayList<MonitorLogs>();
		try {
			if (event_type != null && dateFrom != null) {
				monitorLogs = monitorLogsDao.findByEventType(event_type, dateFrom, dateTo);
			} else if (tasps_id != null && dateFrom != null) {
				monitorLogs = monitorLogsDao.findByTaspsIdAndDate(tasps_id, dateFrom, dateTo);
			} else if (tasps_id != null && dateFrom == null) {
				monitorLogs = monitorLogsDao.findByTaspsId(tasps_id);
			} else {
				monitorLogs = monitorLogsDao.findByDate(dateFrom, dateTo);
			}

			monitorLogs.iterator().forEachRemaining(listLogs::add);
			log.debug("Before FOR: " + monitorLogs.toString());
			Iterator<MonitorLogs> i = listLogs.iterator();

			while (i.hasNext()) {
				MonitorLogs monitorLogsWhile = i.next();
				if ((monitorLogsWhile.getEVENT_TYPE() == null || monitorLogsWhile.getEVENT_TYPE().equals(""))
						|| (request_log.equals("false") && monitorLogsWhile.getEVENT_TYPE().equals("LOG"))) {
					i.remove();
				}
			}
			log.debug("Result pre-set: " + listLogs.toString());
			Set<MonitorLogs> setLogs = new LinkedHashSet<MonitorLogs>(listLogs);
			log.debug("Result post-set: " + setLogs.toString());

			jsonResponse = new Gson().toJson(setLogs);
			return jsonResponse;
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
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
	private MonitorLogsDao monitorLogsDao;

}
