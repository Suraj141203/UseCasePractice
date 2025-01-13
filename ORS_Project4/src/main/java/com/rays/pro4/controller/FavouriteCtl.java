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
import com.rays.pro4.Bean.FavouriteBean;
import com.rays.pro4.Exception.ApplicationException;
import com.rays.pro4.Exception.DuplicateRecordException;
import com.rays.pro4.Model.FavouriteModel;
import com.rays.pro4.Util.DataUtility;
import com.rays.pro4.Util.DataValidator;
import com.rays.pro4.Util.PropertyReader;
import com.rays.pro4.Util.ServletUtility;

//TODO: Auto-generated Javadoc
/**
 * The Class FavouriteCtl.
 * 
 * @author Suraj Yadav
 * 
 */
@WebServlet(name = "FavouriteCtl", urlPatterns = { "/ctl/FavouriteCtl" })
public class FavouriteCtl extends BaseCtl {

	private static final long serialVersionUID = 1L;

	/** The log. */
	private static Logger log = Logger.getLogger(FavouriteCtl.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#validate(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		System.out.println("uctl Validate");
		log.debug("FavouriteCtl Method validate Started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("comments"))) {
			request.setAttribute("comments", PropertyReader.getValue("error.require", "Comments"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("comments"))) {
			request.setAttribute("comments", "Comments contains alphabet only");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("product"))) {
			request.setAttribute("product", PropertyReader.getValue("error.require", "Product"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("addedDate"))) {
			request.setAttribute("addedDate", PropertyReader.getValue("error.require", "Added Date"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("addedDate"))) {
			request.setAttribute("addedDate", PropertyReader.getValue("error.date", "Added Date"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("userName"))) {
			request.setAttribute("userName", PropertyReader.getValue("error.require", "User Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("userName"))) {
			request.setAttribute("userName",
					PropertyReader.getValue("error.require", "User Name must contains alphabet only"));
			pass = false;
		}

		log.debug("FavouriteCtl Method validate Ended");

		return pass;
	}

	@Override
	protected void preload(HttpServletRequest request) {

		FavouriteModel model = new FavouriteModel();
		Map<Integer, String> map = new HashMap();

		map.put(1, "Washing Machine");
		map.put(2, "Laptop");
		map.put(3, "TV");
		map.put(4, "Refrigerator");
		map.put(5, "MicroWave");

		request.setAttribute("cate", map);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#populateBean(javax.servlet.http.
	 * HttpServletRequest)
	 */

	/**
	 * This is Populate Bean
	 */
	protected BaseBean populateBean(HttpServletRequest request) {
		System.out.println(" uctl Base bean P bean");
		log.debug("FavouriteCtl Method populatebean Started");

		FavouriteBean bean = new FavouriteBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setComments(DataUtility.getString(request.getParameter("comments")));

		bean.setProduct(DataUtility.getInt(request.getParameter("product")));

		bean.setAddedDate(DataUtility.getDate(request.getParameter("addedDate")));

		bean.setUserName(DataUtility.getString(request.getParameter("userName")));

		log.debug("FavouriteCtl Method populatebean Ended");

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
		log.debug("FavouriteCtl Method doGet Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		// get model
		FavouriteModel model = new FavouriteModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		System.out.println("Favourite Edit Id >= " + id);
		if (id != 0 && id > 0) {
			System.out.println("in id > 0  condition");
			FavouriteBean bean;
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
		log.debug("FavouriteCtl Method doGet Ended");
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

		log.debug("FavouriteCtl Method doPost Started");

		String op = DataUtility.getString(request.getParameter("operation"));
		long id = DataUtility.getLong(request.getParameter("id"));

		FavouriteModel model = new FavouriteModel();
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			FavouriteBean bean = (FavouriteBean) populateBean(request);
			System.out.println(" U ctl DoPost 11111111");

			try {
				if (id > 0) {

					// System.out.println("hi i am in dopost update");

					model.update(bean);
					ServletUtility.setBean(bean, request);
					System.out.println(" U ctl DoPost 222222");
					ServletUtility.setSuccessMessage("Favourite is successfully Updated", request);

				} else {
					System.out.println(" U ctl DoPost 33333");
					long pk = model.add(bean);

					ServletUtility.setSuccessMessage("Favourite is successfully Added", request);
                     
					bean = model.findByPK(pk);
					
					ServletUtility.setBean(bean, request);
					
					ServletUtility.forward(getView(), request, response);
					return;
				}
				/*
				 * ServletUtility.setBean(bean, request);
				 * ServletUtility.setSuccessMessage("Favourite is successfully saved", request);
				 */
				ServletUtility.setBean(bean, request);

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

			FavouriteBean bean = (FavouriteBean) populateBean(request);
			try {
				model.delete(bean);
				System.out.println(" u ctl D Post  6666666");
				ServletUtility.redirect(ORSView.FAVOURITE_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			System.out.println(" U  ctl Do post 77777");

			ServletUtility.redirect(ORSView.FAVOURITE_LIST_CTL, request, response);
			return;
		}
		
		
		ServletUtility.forward(getView(), request, response);

		log.debug("FavouriteCtl Method doPostEnded");
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see in.co.rays.ors.controller.BaseCtl#getView()
	 */
	@Override
	protected String getView() {
		return ORSView.FAVOURITE_VIEW;
	}

}