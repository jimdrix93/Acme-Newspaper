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


<display:table pagesize="20" class="displaytag" name="newspapers"
	requestURI="${requestUri}" id="row">

	<spring:message code="newspaper.title" var="newspaperTitle" />
	<acme:column property="title" title="newspaper.title" />

	<spring:message code="newspaper.description" var="newspaperDescription" />
	<acme:column property="description" title="newspaper.description" />

	<spring:message code="newspaper.publicationDate"
		var="newspaperPublicationDate" />
	<acme:column property="publicationDate"
		title="newspaper.publicationDate" />

	<spring:message code="newspaper.picture" var="picture" />
	<display:column title="${picture}" class="${classTd}">
		<IMG src="${row.picture}" class="tableImg" />
	</display:column>

	<spring:message code="newspaper.display" var="display" />
	<display:column title="${display}">
		<jstl:choose>
			<jstl:when test="${row.isPrivate == false || showSubscribe == true}">
				<div>
					<a href="newspaper/display.do?newspaperId=${row.id}"> <spring:message
							code="newspaper.display" />
					</a>
				</div>
			</jstl:when>
			<jstl:otherwise>
				<spring:message code="newspaper.cant" />
			</jstl:otherwise>
		</jstl:choose>
	</display:column>

	<security:authorize access="hasRole('USER')">
		<jstl:if test="${showEdit}">
			<display:column>
				<div>
					<a href="newspaper/user/editForm.do?newspaperId=${row.id}"> <spring:message
							code="newspaper.edit.newspaper" />
					</a>
				</div>
			</display:column>
		</jstl:if>

	</security:authorize>

	<security:authorize access="hasRole('ADMIN')">
		<spring:message code="article.delete" var="articleDelete" />
		<display:column>
			<div>
				<a href="newspaper/administrator/delete.do?id=${row.id}">
					<jstl:out value="${articleDelete}"/> </a>
			</div>
		</display:column>
	</security:authorize>


</display:table>

<jstl:if test="${showEdit}">
	<acme:button url="newspaper/user/create.do" text="newspaper.new"
		css="formButton toLeft" />
</jstl:if>
<jstl:if test="${backSearch}">
	<acme:cancel url="/newspaper/search.do" code="newspaper.back" css="formButton toLeft" />
	<br />
</jstl:if>

