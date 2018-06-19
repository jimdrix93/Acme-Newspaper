<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<display:table pagesize="5" class="displaytag" name="users" requestURI="${requestUri}" id="row">

	<spring:message code="user.name" var="userName" />
	<acme:column property="name" title="user.name" />

	<spring:message code="user.surname" var="userSurname" />
	<acme:column property="surname" title="user.surname" />

	<spring:message code="user.address" var="userAddress" />
	<acme:column property="address" title="user.address" />

	<spring:message code="user.email" var="userEmail" />
	<acme:column property="email" title="user.email" />

	<spring:message code="user.phone" var="userPhone" />
	<acme:column property="phone" title="user.phone" />
	
	<display:column>
			<div>
				<a href="user/display.do?userId=${row.id}"> 
					<spring:message code="user.showProfile" />
				</a>
			</div>
	</display:column>
	
	<security:authorize access="hasRole('USER')">
		<display:column>
			<jstl:choose>
				<jstl:when test="${!followeds.contains(row)}">
					<div>
						<a href="follow/user/follow.do?userId=${row.id}"> 
							<spring:message code="user.follow" />
						</a>
					</div>
				</jstl:when>
			</jstl:choose>
			<jstl:choose>
				<jstl:when test="${followeds.contains(row)}">
					<div>
						<a href="follow/user/unfollow.do?userId=${row.id}"> 
							<spring:message code="user.unfollow" />
						</a>
					</div>
				</jstl:when>
			</jstl:choose>
		</display:column>
	</security:authorize>
</display:table>


<!-- <table id="tablaUsuarios" class="display" style="width: 100%"> -->
<!-- 	<thead> -->
<!-- 		<tr> -->
<%-- 			<th><spring:message code="user.name" /></th> --%>
<%-- 			<th><spring:message code="user.surname" /></th> --%>
<%-- 			<th><spring:message code="user.address" /></th> --%>
<%-- 			<th><spring:message code="user.email" /></th> --%>
<%-- 			<th><spring:message code="user.phone" /></th> --%>
<!-- 			<th></th> -->
<%-- 			<security:authorize access="hasRole('USER')"> --%>
<%-- 					<jstl:if test="${!hideFollow}"> --%>
<!-- 					<th></th>					 -->
<%-- 					</jstl:if> --%>
<%-- 			</security:authorize> --%>
<!-- 		</tr> -->
<!-- 	</thead> -->
<!-- 	<tbody> -->
<%-- 		<jstl:forEach items="${users}" var="user"> --%>
<!-- 			<tr> -->
<%-- 				<td><jstl:out value="${user.name}"></jstl:out></td> --%>

<%-- 				<td><jstl:out value="${user.surname}"></jstl:out></td> --%>

<%-- 				<td><jstl:out value="${user.address}"></jstl:out></td> --%>

<%-- 				<td><jstl:out value="${user.email}"></jstl:out></td> --%>
 
<%-- 				<td><jstl:out value="${user.phone}"></jstl:out></td> --%>

<!-- 				<td><div> -->
<%-- 						<a href="user/display.do?userId=${user.id}">  --%>
<%-- 							<spring:message code="user.showProfile" /> --%>
<!-- 						</a> -->
<!-- 					</div></td> -->
<%-- 				<security:authorize access="hasRole('USER')"> --%>
<%-- 					<jstl:if test="${!hideFollow}"> --%>
<%-- 						<td><jstl:choose> --%>
<%-- 								<jstl:when test="${!followeds.contains(user)}"> --%>
<!-- 									<div> -->
<%-- 										<a href="follow/user/follow.do?userId=${user.id}">  --%>
<%-- 											<spring:message code="user.follow" />  --%>
<!-- 										</a> -->
<!-- 									</div> -->
<%-- 								</jstl:when> --%>
<%-- 							</jstl:choose> <jstl:choose> --%>
<%-- 								<jstl:when test="${followeds.contains(user)}"> --%>
<!-- 									<div> -->
<%-- 										<a href="follow/user/unfollow.do?userId=${user.id}">  --%>
<%-- 											<spring:message code="user.unfollow" />  --%>
<!-- 										</a> -->
<!-- 									</div> -->
<%-- 								</jstl:when> --%>
<%-- 							</jstl:choose></td> --%>
<%-- 					</jstl:if> --%>
<%-- 				</security:authorize> --%>
<!-- 			</tr> -->
<%-- 		</jstl:forEach> --%>
<!-- 	</tbody> -->
<!-- 	<tfoot> -->
<!-- 		<tr> -->
<%-- 			<th><spring:message code="user.name" /></th> --%>
<%-- 			<th><spring:message code="user.surname" /></th> --%>
<%-- 			<th><spring:message code="user.address" /></th> --%>
<%-- 			<th><spring:message code="user.email" /></th> --%>
<%-- 			<th><spring:message code="user.phone" /></th> --%>
<!-- 			<th></th> -->
<%-- 			<security:authorize access="hasRole('USER')"> --%>
<%-- 					<jstl:if test="${!hideFollow}"> --%>
<!-- 					<th></th>					 -->
<%-- 					</jstl:if> --%>
<%-- 			</security:authorize> --%>
<!-- 		</tr> -->
<!-- 	</tfoot> -->
<!-- </table> -->

<script>
// 	$(document).ready(function() {	
// 		$('#tablaUsuarios').DataTable({
// 			"language" : {
// 				"url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/${userSelectedLanguage}.json"
// 			},
// 			"lengthMenu" : [ [ -1, 1, 2, 4, 8, 16, 32 ], [ "All", 1, 2, 4, 8, 16, 32 ] ]			
// 		});
// 	});
</script>
