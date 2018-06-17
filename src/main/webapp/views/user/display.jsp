
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<spring:message code="moment.format" var="momentFormat" />
<div class="seccion">

	<acme:textbox code="user.name" path="user.name" css="formInput" />
	<br />

	<acme:textbox code="user.surname" path="user.surname" css="formInput" />
	<br />

	<acme:textbox code="user.email" path="user.email" css="formInput" />
	<br />

	<acme:textbox code="user.phone" path="user.phone" css="formInput" />
	<br />

	<acme:textbox code="user.address" path="user.address" css="formInput" />
	<br />
	
	<jstl:if test="${owner}">
		<acme:button url="/user/user/edit.do" text="user.showProfile" />
	</jstl:if>
	
</div>
<br />
<br />

<h4>
	<spring:message code="user.chirps" />
</h4>

<display:table class="displaytag" name="chirps" id="row">
	<spring:message code="chirp.title" var="chirpTitle" />
	<acme:column property="title" title="chirp.title" />

	<spring:message code="chirp.description" var="chirpDescription" />
	<acme:column property="description" title="chirp.description" />

	<spring:message code="chirp.moment" var="chirpMoment" />
	<acme:column property="moment" title="chirp.moment" format="${momentFormat}"/>

	<security:authorize access="hasRole('ADMIN')">
		<display:column>
			<div>
				<a href="chirp/administrator/delete.do?Id=${row.id}"> 
					<spring:message code="chirp.delete" />
				</a>
			</div>
		</display:column>
	</security:authorize>
</display:table>

<br />
<br />

<h4>
	<spring:message code="newspaper.Articles" />
</h4>

<display:table class="displaytag" name="articles" requestURI="user/display.do" id="row">

	<acme:column property="title" title="article.title" />

	<acme:column property="summary" title="article.summary" />

	<acme:column property="publicationMoment" title="article.publicationMoment" format="${momentFormat}"/>

	<display:column>
		<div>
			<a href="article/display.do?articleId=${row.id}"> 
				<spring:message code="article.display" />
			</a>
		</div>
	</display:column>
</display:table>

<br />
<br />




