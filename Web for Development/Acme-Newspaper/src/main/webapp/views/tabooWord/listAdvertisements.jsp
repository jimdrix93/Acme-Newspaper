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
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<display:table pagesize="5" class="displaytag" name="advertisements"
	requestURI="tabooWord/administrator/listAdvertisements.do" id="row">

	<spring:message code="advertisement.title" var="advertisementTitle" />
	<acme:column property="title" title="advertisement.title" />
	
	<spring:message code="advertisement.bannerURL" var="advertisementBannerURL" />
	<display:column>
 		<a href="${row.bannerURL }"><jstl:out value="${row.bannerURL }"/></a>
	</display:column>
	
	<spring:message code="advertisement.targetURL" var="advertisementTargetURL" />
	<display:column>
 		<a href="${row.targetURL }"><jstl:out value="${row.targetURL }"/></a>
	</display:column>
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="advertisement.delete" var="advertisementDelete" />
		<display:column>
			<div>
				<a href="advertisement/administrator/delete.do?id=${row.id}">
					<jstl:out value="${advertisementDelete}"/> </a>
			</div>
		</display:column>
	</security:authorize>


</display:table>


<acme:cancel url="/tabooWord/administrator/list.do" code="msg.back" css="formButton toLeft" />
			

