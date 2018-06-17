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

<form:form action="followup/user/create.do" modelAttribute="followup">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="publicationMoment" />
	<input type="hidden" name="articleId" value="${articleId}">

	<acme:textbox code="followup.title" path="title" css="formInput" />
	<br />

	<acme:textarea code="followup.summary" path="summary"
		css="formTextArea" />
	<br />

	<acme:textarea code="followup.text" path="text" css="formTextArea" />
	<br />

	<acme:textarea code="followup.pictures" path="pictures"
		css="formTextArea" />
	<br />



	<acme:submit name="save" code="followup.save" css="formButton toLeft" />&nbsp;
    <acme:cancel url="/followup/user/list.do?articleId=${articleId}"
		code="followup.cancel" css="formButton toLeft" />
	<br />

</form:form>
