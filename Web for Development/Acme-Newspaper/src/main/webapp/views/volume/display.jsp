
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
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

<div>

	<div>
		<div class="toRight">
			<spring:message code="volume.year" />
			<jstl:out value="${volume.year}"></jstl:out>	
			
		</div>
		<h5>
			<jstl:out value="${volume.title}" />
		</h5>
		<h2>
			<acme:textarea code="newspaper.description"
				path="volume.description" readonly="true" css="formTextArea" />
		</h2>
	</div>
<h4>
		<spring:message code="newspaper.newspapers" />
	</h4>
	<br>
	<br>
	<%@ include file="/views/newspaper/list.jsp" %>
	<br />
	 <br />
<security:authorize access="hasRole('CUSTOMER')">
				<acme:button url="/vsubscription/customer/create.do" text="subscription.subscribe" 
	css="formButton toLeft"/>
		</security:authorize>
	
	
</div>
<br />
<br />
