<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jsp:useBean id="today" class="java.util.Date" />

<display:table pagesize="5" class="displaytag" name="volumes"
	requestURI="${requestUri}" id="row">

	<spring:message code="newspaper.title" var="title" />
	<display:column title="${title}">
		<div>
			<a href="volume/display.do?volumeId=${row.id}"> <jstl:out
					value="${row.title}" />
			</a>
		</div>
	</display:column>

	<acme:column property="description" title="volume.description" />

	<acme:column property="year" title="volume.year" />

	<jstl:if test="${showEdit}">
		<display:column>
			<div>
				<a href="volume/user/edit.do?volumeId=${row.id}"> <spring:message
						code="volume.edit.volume" />
				</a>
			</div>
		</display:column>
	</jstl:if>
</display:table>

<acme:backButton text="msg.back" css="formButton toLeft" />



