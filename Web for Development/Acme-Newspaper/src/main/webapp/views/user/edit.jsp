
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<security:authorize access="hasRole('USER')">

	<form:form action="${requestUri}" modelAttribute="userModel">
		<div class="seccion">

			<acme:textbox code="user.name" path="name" css="formInput" />
			<br />

			<acme:textbox code="user.surname" path="surname" css="formInput" />
			<br />

			<acme:textbox code="user.email" path="email" css="formInput" />
			<br />

			<acme:textbox code="user.phone" path="phone" css="formInput" />
			<br />

			<acme:textbox code="user.address" path="address" css="formInput" />

			<br />
		</div>
		<div class="seccion">

			<acme:textbox code="user.userAccount.username" path="username"
				css="formInput" />
			<br />

			<acme:password code="user.userAccount.password" path="password"
				css="formInput" />
			<br />
		</div>

		<br />
		<br />

		<jstl:if
			test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], 'register')}">
			<p style="color: rgb(120, 120, 120)">
				<spring:message code="term.registration" />
			</p>
			<br />
		</jstl:if>

		<acme:submit name="save" code="user.save" css="formButton toLeft" />
		<acme:cancel url="user/user/display.do" code="user.cancel"
			css="formButton toLeft" />
	</form:form>

</security:authorize>
