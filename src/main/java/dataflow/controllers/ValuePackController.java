package dataflow.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import dataflow.models.DataflowInfoStatus;
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
public class ValuePackController {
	static Logger log = Logger.getLogger(ValuePackController.class.getName());

	// ------------------------
	// PUBLIC METHODS
	// ------------------------

	/**
	 * Create a new dataflow and save it in the database.
	 * 
	 * @return A string describing if the dataflow is successfully created or
	 *         not.
	 */
	
	@RequestMapping(value = "value-pack/create", method = RequestMethod.POST, 
            consumes = "application/json", produces = "application/json")	
	@ResponseBody
	@CrossOrigin

	public String create(@RequestBody Map<String, Object> bodyRequest ) {
		log.info("Request value-pack/create with: " + bodyRequest.toString());

		ValuePack valuePack = null;
		HashMap<String, String> response = new HashMap<String, String>();
		DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATEONLY_FORMAT);
		Date today = Calendar.getInstance().getTime();   
		try {
			String dataflow_id=bodyRequest.get("dataflow_id")!= null ? bodyRequest.get("dataflow_id").toString() : null;
				String dataflow_load_date=bodyRequest.get("dataflow_load_date")!= null ? bodyRequest.get("dataflow_load_date").toString() : dateFormat.format(today);
				valuePack = new ValuePack(dataflow_load_date,dataflow_id);
			valuePackDao.save(valuePack);
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
	@RequestMapping(value = "value-pack/delete", method = RequestMethod.POST, 
            consumes = "application/json", produces = "application/json")	
	@ResponseBody
	@CrossOrigin
	public String delete(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request value-pack/delete with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {
			DateFormat dateFormat = new SimpleDateFormat(CustomConstants.DB_DATE_FORMAT);
			Date today = Calendar.getInstance().getTime();        
			ValuePack valuePack = valuePackDao.findByDatataflow_id(bodyRequest.get("dataflow_id").toString());
			valuePackDao.delete(valuePack);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}

	@RequestMapping(value = "value-pack/update", method = RequestMethod.PUT, 
            consumes = "application/json", produces = "application/json")	
	@ResponseBody
	@CrossOrigin
	public String updateDataflow(@RequestBody Map<String, Object> bodyRequest) {
		log.info("Request value-pack/update with: " + bodyRequest.toString());

		HashMap<String, String> response = new HashMap<String, String>();

		try {
			ValuePack valuePack = valuePackDao.findByDatataflow_id(bodyRequest.get("dataflow_id").toString());

			String dataflow_load_date=bodyRequest.get("dataflow_load_date")!= null ? bodyRequest.get("dataflow_load_date").toString() : null;
			
			//dataflow.setId(id);
			
			if (dataflow_load_date != null)
				valuePack.setDataflow_load_date(dataflow_load_date);
			
			valuePackDao.save(valuePack);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		response.put(CustomConstants.RESULT, CustomConstants.OK);
		return new Gson().toJson(response);
	}



	@RequestMapping("value-pack/get-all")
	@ResponseBody
	@CrossOrigin
	public String retrieveAll() {
		log.info("Request value-pack/get-all");

		HashMap<String, String> response = new HashMap<String, String>();

		Iterable<ValuePack> valuePackList = null;
		List<ValuePack> list = new ArrayList<ValuePack>();
		String jsonResponse = "";
		try {
			valuePackList = valuePackDao.findAll();
			valuePackList.iterator().forEachRemaining(list::add);
			jsonResponse = new Gson().toJson(list);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		return jsonResponse;
	}

	@RequestMapping("value-pack/get-by-dataflow-id")
	@ResponseBody
	@CrossOrigin
	public String getById(String dataflow_id) {
		log.info("Request /get-by-id");
		String jsonResponse = "";
		HashMap<String, String> response = new HashMap<String, String>();
		ValuePack valuePack = null;

		try {
			valuePack = valuePackDao.findByDatataflow_id(dataflow_id);
			jsonResponse = new Gson().toJson(valuePack);
		} catch (Exception ex) {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, ex.toString());
			return new Gson().toJson(response);
		}
		if (valuePack != null) {
			jsonResponse = new Gson().toJson(valuePack);
			return jsonResponse;
		} else {
			response.put(CustomConstants.RESULT, CustomConstants.KO);
			response.put(CustomConstants.DESCRIPTION, "No valuepack with id: " + dataflow_id);
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
	private ValuePackDao valuePackDao;

}
