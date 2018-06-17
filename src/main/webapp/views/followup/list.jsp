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



<display:table pagesize="5" class="displaytag" name="followups"
	requestURI="${requestUri}" id="row">

	<input type="hidden" name="articleId" value="${articleId}">

	<spring:message code="followup.title" var="followupTitle" />
	<display:column title="${followupTitle}">
		<div>
			<a
				href="followup/user/display.do?articleId=${articleId}&&followupId=${row.id}">
				<jstl:out value="${row.title}"/> </a>
		</div>
	</display:column>
	<%-- 	<acme:column property="title" title="followup.title"/> --%>

	<spring:message code="followup.summary" var="followupSummary" />
	<acme:column property="summary" title="followup.summary" />

	<spring:message code="followup.text" var="followupText" />
	<acme:column property="text" title="followup.text" />

	<spring:message code="followup.publicationMoment" var="followupPublicationMoment" />
	<spring:message code="moment.format" var="momentFormat" />
	<acme:column property="publicationMoment" title="followup.publicationMoment" format="${momentFormat}"/>

</display:table>

<acme:cancel url="/article/user/list.do"
		code="followup.cancel" css="formButton toLeft" />

