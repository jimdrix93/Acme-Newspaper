<%--
 * index.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<br />
Acme Newspaper Co., Inc.
<br />
N� Reg.: 123456
<br />
CIF: 12345678X
<br />
Avda Reina Mercedes s/n.
<br />
Tlf: 955010101
<br />
<spring:message code="welcome.contact" />
<br />
<a href="mailto:management@acmenewspaper.com">management@acmenewspaper.com</a>

<p>
	<spring:message code="welcome.greeting.current.time" />
	${moment}
</p>
