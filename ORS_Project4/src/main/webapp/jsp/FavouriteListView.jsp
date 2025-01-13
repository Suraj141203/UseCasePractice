<%@page import="com.rays.pro4.Model.FavouriteModel"%>
<%@page import="java.util.Map"%>
<%@page import="com.rays.pro4.Bean.FavouriteBean"%>
<%@page import="com.rays.pro4.Bean.BaseBean"%>
<%@page import="com.rays.pro4.Util.HTMLUtility"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="com.rays.pro4.Util.DataUtility"%>
<%@page import="com.rays.pro4.Util.ServletUtility"%>
<%@page import="com.rays.pro4.controller.FavouriteListCtl"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16*16" />

<title>Favourite List</title>

<script src="<%=ORSView.APP_CONTEXT%>/js/jquery.min.js"></script>
<script src="<%=ORSView.APP_CONTEXT%>/js/Checkbox11.js"></script>

<link rel="stylesheet" href="/resources/demos/style.css">
<script>
	$(function() {
		$("#udate").datepicker({
			changeMonth : true,
			changeYear : true,
			yearRange : '2020:2025',
		//  mindefaultDate : "01-01-1962"
		});
	});
</script>

</head>
<body>
	<jsp:useBean id="bean" class="com.rays.pro4.Bean.FavouriteBean"
		scope="request"></jsp:useBean>
	<%@include file="Header.jsp"%>


	<form action="<%=ORSView.FAVOURITE_LIST_CTL%>" method="post">

		<center>

			<div align="center">
				<h1>Favourite List</h1>
				<h3>
					<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
					<font color="limegreen"><%=ServletUtility.getSuccessMessage(request)%></font>
				</h3>

			</div>

			<%
				List flist = (List) request.getAttribute("AddedDate");

				int next = DataUtility.getInt(request.getAttribute("nextlist").toString());
			%>

			<%
				Map map = (Map) request.getAttribute("cate");
			%>


			<%
				int pageNo = ServletUtility.getPageNo(request);
				int pageSize = ServletUtility.getPageSize(request);
				int index = ((pageNo - 1) * pageSize) + 1;

				List list = ServletUtility.getList(request);
				Iterator<FavouriteBean> it = list.iterator();

				if (list.size() != 0) {
			%>
			<table width="100%" align="center">
				<input type="hidden" name="id" value="<%=bean.getId()%>">
				<tr>
					<th></th>
					<td align="center">&emsp; <label>Added Date</font> :
					</label> <%=HTMLUtility.getList("ids", DataUtility.getStringData(bean.getAddedDate()), flist)%>


						<label>User Name</font> :
					</label> <input type="text" name="userName" placeholder="Enter User Name"
						value="<%=ServletUtility.getParameter("userName", request)%>">


						&nbsp; <label>Product</font> :
					</label><%=HTMLUtility.getList2("product", DataUtility.getStringData(bean.getProduct()), map)%>




						&nbsp; <input type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_SEARCH%>"> &nbsp; <input
						type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_RESET%>">
					</td>
				</tr>
			</table>

			<table border="1" width="100%" align="center" cellpadding=6px
				cellspacing=".2">
				<tr style="background: orange">
					<th><input type="checkbox" id="select_all" name="select">Select
						All</th>

					<th>S.No.</th>
					<th>Product</th>
					<th>Added Date</th>
					<th>User Name</th>
					<th>Comments</th>
					<th>Edit</th>
				</tr>

				<%
					while (it.hasNext()) {
							bean = it.next();
				%>


				<tr align="center">
					<%--<td><%=map.get(Integer.parseInt(bean.getImportance()))%></td> --%>
					<td><input type="checkbox" class="checkbox" name="ids"
						value="<%=bean.getId()%>"></td>
					<td><%=index++%></td>
					<td><%=map.get(bean.getProduct())%></td>
					<td><%=bean.getAddedDate()%></td>
					<td><%=bean.getUserName()%></td>
					<td><%=bean.getComments()%></td>
					<td><a href="FavouriteCtl?id=<%=bean.getId()%>">Edit</a></td>
				</tr>
				<%
					}
				%>
			</table>

			<table width="100%">
				<tr>
					<th></th>
					<%
						if (pageNo == 1) {
					%>
					<td><input type="submit" name="operation" disabled="disabled"
						value="<%=FavouriteListCtl.OP_PREVIOUS%>"></td>
					<%
						} else {
					%>
					<td><input type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_PREVIOUS%>"></td>
					<%
						}
					%>

					<td><input type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_DELETE%>"></td>
					<td><input type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_NEW%>"></td>
					<td align="right"><input type="submit" name="operation"
						value="<%=FavouriteListCtl.OP_NEXT%>"
						<%=(list.size() < pageSize || next == 0) ? "disabled" : ""%>></td>



				</tr>
			</table>
			<%
				}
				if (list.size() == 0) {
			%>
			<td align="center"><input type="submit" name="operation"
				value="<%=FavouriteListCtl.OP_BACK%>"></td>
			<%
				}
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">
	</form>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>
	</br>

	</center>

	<%@include file="Footer.jsp"%>
</body>
</html>