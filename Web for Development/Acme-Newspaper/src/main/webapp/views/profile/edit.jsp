
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

<security:authorize access="isAuthenticated()">
	<form:form action="profile/actor/edit.do" modelAttribute="actorModel">
		<div class="seccion">

			<acme:textbox code="profile.name" path="name" css="formInput" />
			<br />

			<acme:textbox code="profile.surname" path="surname" css="formInput" />
			<br />

			<acme:textbox code="profile.email" path="email" css="formInput" />
			<br />

			<acme:textbox code="profile.phone" path="phone" css="formInput" placeholder="+XX XXXXXXXXX"/>
			<br />

			<acme:textbox code="profile.address" path="address" css="formInput" />

			<br />
		</div>
		<div class="seccion">

			<acme:textbox code="profile.userAccount.username" path="username"
				css="formInput" />
			<br />

			<acme:password code="profile.userAccount.oldPassword" path="password"
				css="formInput" />
			<br />
			<acme:password code="profile.userAccount.newPassword" path="password"
				css="formInput" onkeyup="check();" id = "password"/>
			<acme:password code="profile.userAccount.repeatPassword" path="password"
				css="formInput"  onkeyup="check();" id="confirm_password"/>
			<span id='message'></span>
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
		<acme:cancel url="profile/actor/display.do" code="user.cancel"
			css="formButton toLeft" />
		<br>
		<br>
	</form:form>

</security:authorize>
<script>
	var check = function() {
	  if (document.getElementById('password').value ==
	    document.getElementById('confirm_password').value) {
	    document.getElementById('message').style.color = 'green';
	    document.getElementById('message').innerHTML = '<spring:message code="profile.userAccount.repeatPassword.ok" />';
	  } else {
	    document.getElementById('message').style.color = 'red';
	    document.getElementById('message').innerHTML = '<spring:message code="profile.userAccount.repeatPassword.mismatch" />';
	  }
	}
</script>

