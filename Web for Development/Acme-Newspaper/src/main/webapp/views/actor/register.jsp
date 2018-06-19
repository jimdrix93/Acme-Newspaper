
<%@page import="org.springframework.test.web.ModelAndViewAssert"%>
<%@page import="org.springframework.web.servlet.ModelAndView"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<script>
	var check = function() {
		document.getElementById('matching').style.display = 'none';
		document.getElementById('notMatching').style.display = 'none';
		
		if (document.getElementById('password').value != "" &&
				document.getElementById('password').value == document.getElementById('confirm_password').value) {
			document.getElementById('matching').style.color = 'green';
		    document.getElementById('matching').style.display = 'inline';
		    document.getElementById("save").disabled = false;
		    document.getElementById("save").className = "formButton toLeft";
		} else {
		    document.getElementById('notMatching').style.color = 'red';
		    document.getElementById('notMatching').style.display = 'inline';
			document.getElementById("save").disabled = true;
			document.getElementById("save").className = "formButton toLeft disabled";
		}
		return result;
	};
</script>

<security:authorize access="isAnonymous()">

	<form:form action="${uri}" modelAttribute="actorRegisterForm">
		
		<div class="seccion">
			<acme:textbox code="user.name" path="name" css="formInput" />
			<br />
			<acme:textbox code="user.surname" path="surname" css="formInput" />
			<br />
			<acme:textbox code="user.email" path="email" css="formInput" />
			<br />
			<acme:textbox code="user.phone" path="phone" css="formInput" placeholder="+XX XXXXXXXXX"/>
			<br />
			<acme:textbox code="user.address" path="address" css="formInput" />
			<br />
		</div>
		
		<div class="seccion">
			<acme:textbox code="user.userAccount.username" path="username" css="formInput" />
			<br />
			<spring:message code="user.userAccount.password"/>
			<form:password path="password" name="password" id="password" onkeyup='check();' class="formInput" /> 
			<form:errors path="password" cssClass="error" />
			<br />
			
			<spring:message code="user.userAccount.password.repeat"/>
			<input class="formInput" type="password" id="confirm_password" name="confirm_password" onkeyup='check();'>
			<div class="error">
				<span id='matching' style="display:none;">
					<spring:message code="user.password.matching" />
				</span>
				<span id='notMatching' style="display:none;" >
					<spring:message code="user.password.not.matching"/>
				</span>
			</div>
		</div>
		<br />
		<br />
		
		<jstl:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], 'register')}">
			<p style="color: rgb(120, 120, 120)">
				<spring:message code="term.registration" />
			</p>
			<br />
		</jstl:if>

		<button type="submit" id="save" name="save" class="formButton toLeft disabled" name="user.save"  disabled>
			<spring:message code="user.save" />
		</button>
		<acme:cancel url="/" code="user.cancel" css="formButton toLeft" />
	</form:form>
</security:authorize>
