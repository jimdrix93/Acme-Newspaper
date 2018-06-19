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

<display:table pagesize="5" class="displaytag" name="subscriptions"
	requestURI="${requestURI}" id="row">

	<spring:message code="subscription.creditCard" var="creditCardNumber" />
	<acme:column property="creditCard.number"
		title="subscription.creditCard.number" />

	<spring:message code="subscription.newspaper"
		var="subscriptionNewspaper" />
	<display:column title="${subscriptionNewspaper}">
		<div>
			<a
				href="newspaper/customer/display.do?newspaperId=${row.newspaper.id}">
				<jstl:out value="${row.newspaper.title}"/> </a>
		</div>


	</display:column>
</display:table>


<acme:button url="subscription/customer/create.do"
	text="subscription.new" css="formButton toLeft" />



