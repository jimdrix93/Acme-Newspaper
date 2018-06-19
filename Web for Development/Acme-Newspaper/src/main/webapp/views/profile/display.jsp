
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

<div class="seccion">

	<acme:textbox code="profile.name" path="profile.name" css="formInput" readonly="true"/>
	<br />

	<acme:textbox code="profile.surname" path="profile.surname" css="formInput" readonly="true"/>

	<br />

	<acme:textbox code="profile.email" path="profile.email" css="formInput" readonly="true"/>
	<br />

	<acme:textbox code="profile.phone" path="profile.phone" css="formInput" readonly="true"/>
	<br />


	<acme:textbox code="profile.address" path="profile.address" css="formInput" readonly="true" />
	<br />
	<br>
	<jstl:if test="${owner}">
		<acme:button url="/profile/actor/edit.do" text="profile.editProfile" css="formButton toLeft"/>
	</jstl:if>
</div>
<br />
<br />

<acme:backButton text="msg.back" css="formButton toLeft" />



