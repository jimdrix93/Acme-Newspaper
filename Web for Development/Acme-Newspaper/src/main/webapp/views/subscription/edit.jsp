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
<form:form action="${requestUri}" modelAttribute="subscription"
	method="post">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="creditCard.id" />
	<form:hidden path="creditCard.version" />
	<form:hidden path="customer" />

	<acme:select items="${newspapers}" itemLabel="title"
		code="subscription.newspaper" path="newspaper" />
	<br />
	<br />
	<h5>
	<spring:message code="creditCard.main" />	
	</h5>
	<br />

	<acme:textbox code="creditCard.holderName" path="creditCard.holderName"
		css="formInput" />
	<br />

	<acme:textbox code="creditCard.brandName" path="creditCard.brandName"
		css="formInput" />
	<br />

	<acme:textbox code="creditCard.number" path="creditCard.number"
		css="formInput" />
	<br />

	<acme:textbox code="creditCard.expirationMonth"
		path="creditCard.expirationMonth" css="formInput toLeft" />
	<br />

	<acme:textbox code="creditCard.expirationYear"
		path="creditCard.expirationYear" css="formInput toLeft" />
	<br />

	<acme:textbox code="creditCard.cvv" path="creditCard.cvv"
		css="formInput toLeft" />
	<br>
	<br>
	<br>
	<br>	
	<acme:cancel url="/subscription/customer/list.do" code="subscription.cancel" css="formButton toLeft" />
	<acme:submit name="save" code="subscription.save"
		css="formButton toLeft" />
<br />

</form:form>
