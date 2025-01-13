package com.rays.pro4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.rays.pro4.Bean.BaseBean;
import com.rays.pro4.Bean.FollowupBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.FollowupModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class FollowupCtl.
 * 
 * @author Suraj Yadav
 * 
 */
@WebServlet(name = "FollowupCtl", urlPatterns = { "/ctl/FollowupCtl" })
public class FollowupCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(FollowupCtl.class);


	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("FollowupCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("patient"))) {
			request.setAttribute("patient", PropertyReader.getValue("error.require", "Patient Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("patient"))) {
			request.setAttribute("patient", "Patient Name contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("doctor"))) {
			request.setAttribute("doctor", PropertyReader.getValue("error.require", "Doctor"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("doctor"))) {
			request.setAttribute("doctor", " Doctor contains alphabet only");
			pass = false;
		}
		
		if (DataValidator.isNull(request.getParameter("visitDate"))) {
			request.setAttribute("visitDate", PropertyReader.getValue("error.require", "Visiting Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("visitDate"))) {
			request.setAttribute("visitDate", PropertyReader.getValue("error.date", "Visiting Date"));
			pass = false;
		}
		
	

		if (DataValidator.isNull(request.getParameter("fees"))) {
			request.setAttribute("fees", PropertyReader.getValue("error.require", "Fees"));
			pass = false;
		} else if (!DataValidator.isInteger(request.getParameter("fees"))) {
			request.setAttribute("fees", PropertyReader.getValue("error.integer", "Fees"));
			pass = false;
		}


		log.debug("FollowupCtl Method validate Ended");

		return pass;
	}
	
	@Override
	protected void preload(HttpServletRequest request) {
		
		
			FollowupModel model = new FollowupModel();
			Map<String, String> map = new HashMap();

			map.put("Dermatologist", "Dermatologist");
			map.put("Orthopaedist", "Orthopaedist");
			map.put("Neurologists", "Neurologists");
			
			request.setAttribute("cate", map);
		
		
		
		super.preload(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */
	
	

	
	
	/**
	 *  This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("FollowupCtl Method populatebean Started");

		FollowupBean bean = new FollowupBean();

		bean.setId(DataUtility.getLong(request.getParameter("id"))); 
		
		bean.setPatient(DataUtility.getString(request.getParameter("patient")));

		bean.setDoctor(DataUtility.getString(request.getParameter("doctor"))); 
		
		bean.setVisitDate(DataUtility.getDate(request.getParameter("visitDate")));

		bean.setFees(DataUtility.getLong(request.getParameter("fees")));

	

		log.debug("FollowupCtl Method populatebean Ended");

		return bean;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("FollowupCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		FollowupModel model = new FollowupModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Followup Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			FollowupBean bean;
			try {
				bean = model.findByPK(id);
				System.out.println(bean);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("FollowupCtl Method doGet Ended");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("uctl Do Post");

		log.debug("FollowupCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));


		FollowupModel model = new FollowupModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			FollowupBean bean = (FollowupBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");
					
					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Followup is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);
					

					ServletUtility.setSuccessMessage("Followup is successfully Added", request);
					/*
					 * ServletUtility.forward(getView(), request, response);
					 */				}
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Followup is successfully saved", request);
				 */

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				System.out.println(" U ctl D post 4444444");
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {
			System.out.println(" U ctl D p 5555555");

			FollowupBean bean = (FollowupBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.FOLLOWUP_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.FOLLOWUP_LIST_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

		log.debug("FollowupCtl Method doPostEnded");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.FOLLOWUP_VIEW;
	}

}