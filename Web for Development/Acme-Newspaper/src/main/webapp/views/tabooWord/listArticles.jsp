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

<display:table pagesize="5" class="displaytag" name="articles"
	requestURI="tabooWord/administrator/listArticles.do" id="row">

	<acme:column property="title" title="article.title" />

	<acme:column property="summary" title="article.summary" />

	<acme:column property="body" title="article.body" />
	
	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.delete" var="articleDelete" />
		<display:column>
			<div>
				<a href="article/administrator/delete.do?id=${row.id}">
					<jstl:out value="${articleDelete}"/> </a>
			</div>
		</display:column>
	</security:authorize>

</display:table>

<acme:button text="tabooWord.back"
	url="/tabooWord/administrator/list.do" css="formButton toLeft" />

