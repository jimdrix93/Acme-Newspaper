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

<!-- fecha actual -->
<jsp:useBean id="date" class="java.util.Date" />

<display:table pagesize="5" class="displaytag" name="articles" requestURI="${requestUri}" id="row">

	<spring:message code="article.title" var="articleTitle" />
	<display:column title="${articleTitle}">
		<div>
			<a href="article/display.do?articleId=${row.id}"> ${row.title} </a>
		</div>
	</display:column>
	
	<acme:column property="newspaper.title" title="article.newspaper.name"/>
	<spring:message code="article.summary" var="articleSummary" />
	<display:column title="${articleSummary}">
		<div class="ellipsis" onclick="ellipsis(this);">
			<jstl:out value="${row.summary}"/>
		</div>
	</display:column>

	<spring:message code="article.publicationMoment" var="articlePublicationMoment" />
	<acme:column property="publicationMoment" title="article.publicationMoment" />

	<spring:message code="article.edit" var="articleEdit"/>
	<display:column title="${articleEdit}">
		<div>
			<jstl:if test="${!row.finalMode and showCreateFollowup}">
				<a href="article/user/edit.do?articleId=${row.id}"> 
					<jstl:out value="${articleEdit}"/>
				</a>
			</jstl:if>
		</div>
	</display:column>

	<spring:message code="article.create.followup" var="articleFollowUp"/>
	<display:column title="${articleFollowUp }">
		<jstl:if test="${row.finalMode and showCreateFollowup and row.publicationMoment != null}">
			<div>
				<a href="followup/user/create.do?articleId=${row.id}"> 
					<jstl:out value="${articleFollowUp }"/>
				</a>
			</div>
		</jstl:if>
	</display:column>
	<spring:message code="article.list.followups" var="articleListFollowUp"/>
	<display:column title="${articleListFollowUp }">
		<jstl:if test="${row.finalMode and showCreateFollowup}">
			<div>
				<a href="followup/user/list.do?articleId=${row.id}"> 
					<jstl:out value="${articleListFollowUp}"></jstl:out>
				</a>
			</div>
		</jstl:if>
	</display:column>

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.delete" var="articleDelete" />
		<display:column>
			<div>
				<a href="article/administrator/delete.do?id=${row.id}">
					<jstl:out value="${articleDelete}"/> 
				</a>
			</div>
		</display:column>
	</security:authorize>

</display:table>

<jstl:if test="${backSearch}">
	<acme:cancel url="/article/search.do" code="article.back" css="formButton toLeft" />
	<br />
</jstl:if>
