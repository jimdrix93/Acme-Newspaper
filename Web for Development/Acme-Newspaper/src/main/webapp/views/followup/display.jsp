
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

<spring:message code="moment.pattern" var="momentFormat" />
<div class="toRight">
	<fmt:formatDate value="${followup.publicationMoment}"
		pattern="${momentFormat}" var="publicationDate" />
		
	<jstl:out value="${publicationDate}"/>
</div>
<div>
	<h5>
		<jstl:out value="${followup.title}" />
	</h5>
</div>


<p>
	<acme:textarea code="followup.summary" path="followup.summary"
		readonly="true" css="formInput" />

</p>

<p>
	<acme:textarea code="followup.text" path="followup.text"
		readonly="true" css="formInput" />
</p>

<!-- Carrusel se fotos  -->
<spring:message code="followup.picture" var="picture" />
<div class="slideshow-container">
	<jstl:set var="count" scope="application" value="${0}" />
	<jstl:forEach items="${followup.pictures}" var="picture">
		<jstl:set var="count" scope="application" value="${count + 1}" />
		<div class="mySlides fade">
			<div class="numbertext">${count}/ ${followup.pictures.size()}</div>
			<img src="${picture}" style="width: 100%">
			<div class="text">${followup.title}</div>
		</div>
	</jstl:forEach>

	<a class="prev" onclick="plusSlides(-1)">&#10094;</a> <a class="next"
		onclick="plusSlides(1)">&#10095;</a>
</div>
<jstl:set var="count" scope="application" value="${0}" />
<div style="text-align: center">
	<jstl:forEach items="${followup.pictures}" var="picture">
		<jstl:set var="count" scope="application" value="${count + 1}" />
		<span class="dot" onclick="currentSlide(${count})"></span>
	</jstl:forEach>
</div>
<br />
<br />

<acme:cancel url="/followup/user/list.do?articleId=${articleId}"
		code="followup.cancel" css="formButton toLeft" />

<script>
	var slideIndex = 1;
	showSlides(slideIndex);
</script>



