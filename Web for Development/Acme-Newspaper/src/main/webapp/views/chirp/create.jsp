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

<form:form action="chirp/user/create.do" modelAttribute="chirp">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="moment" />

	<acme:textbox code="newspaper.title" path="title" />
	<br />

	<acme:textbox code="newspaper.description" path="description" />
	<br />


	<acme:submit name="save" code="chirp.save" css="formButton toLeft" />
	<acme:cancel url="/chirp/user/written.do" code="chirp.cancel" css="formButton toLeft" />
	<br />

</form:form>