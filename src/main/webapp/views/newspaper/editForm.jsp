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

<div id="dropbox" ondragover="return false"  ondrop="drop(event, 'newspaper')">
	<form:form action="newspaper/user/editForm.do"
		modelAttribute="newspaper">

		<form:hidden path="id" />
		<form:hidden path="version" />

		<div class="toRight">
			<spring:message code="newspaper.publicationDate" />
			:
			<fmt:formatDate value="${newspaper.publicationDate}"
				pattern="yyyy-MM-dd" var="publicationDate" />
			<jstl:out value="${publicationDate}"/>
		</div>
		<h5>
			<acme:textbox code="newspaper.title" path="title" css="formInput" />
		</h5>
		<br />
		<h5>
			<acme:textarea code="newspaper.description" path="description"
				css="formTextArea" />
			<br />
		</h5>
		<h5>
			<acme:textarea code="newspaper.pictures.drag" path="picture"
				css="formTextArea" id="fotosPath" visible="none" />
			<br />
		</h5>

		<!-- Carrusel se fotos  -->
		<div class="carrusel" style="background-color: black;">
			<div class="slideshow-container" id="carrusel">
				<jstl:if test="${newspaper.picture!=null}">
					<div class="mySlides fade">
						<img src="${newspaper.picture}" style="width: 100%">
					</div>
				</jstl:if>
			</div>
		</div>
		<br />
		<acme:checkBox code="newspaper.isPrivate" path="isPrivate"
			css="formCheck" />
		<br />
		<acme:checkBox code="newspaper.isPublicated" path="isPublicated"
			readonly="${newspaper.isPublicated}" css="formCheck" />
		<br />
		<acme:submit name="save" code="newspaper.save" css="formButton toLeft" />&nbsp;
    <acme:cancel url="/newspaper/user/list.do" code="newspaper.cancel" css="formButton toLeft" />
		<br />

	</form:form>
</div>