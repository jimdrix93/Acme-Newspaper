<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
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

<form:form action="${requestUri}" modelAttribute="modelMessage">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="moment" />
	<form:hidden path="sender" />


	<spring:message code="ms.priority.low" var="low" />
	<spring:message code="ms.priority.neutral" var="neutral" />
	<spring:message code="ms.priority.high" var="high" />
	


	<form:label path="priority">
		<spring:message code="ms.priority" />
	</form:label>
	<form:select path="priority">
		<form:option label="${low}" value="LOW" />
		<form:option label="${neutral}" value="NEUTRAL" />
		<form:option label="${high}" value="HIGH" />
	</form:select>
	<form:errors cssClass="error" path="priority" />
	<br />
	<br />
	
	<jstl:if test="${requestUri eq 'myMessage/edit.do'}">
	<acme:select items="${actors}" itemLabel="userAccount.username" code="ms.recipient" path="recipient"/>
	<br />
	</jstl:if>
	
	<acme:textbox code="ms.subject" path="subject"/>
	<br />
	
	<acme:textarea code="ms.body" path="body"/>
	<br />


	<acme:submit name="save" code="ms.send"/>&nbsp;
	<acme:cancel url="/" code="ms.cancel"/>


</form:form>