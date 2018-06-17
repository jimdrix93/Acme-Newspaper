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
<div id="dropbox" ondragover="return false" ondrop="drop(event, 'article')">

	<form:form action="article/user/edit.do" modelAttribute="article">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<jstl:if test="${article.id != 0 }">
			<form:hidden path="newspaper" />
		</jstl:if>
		<h5>
			<acme:select code="newspaper" items="${newspapers}" path="newspaper"
				itemLabel="title" css="formInput" readonly="${article.id != 0}" />
		</h5>
		<br />
		<h5>
			<acme:textbox code="article.title" path="title" css="formInput" />
		</h5>
		<br />
		<h5>
			<acme:textarea code="article.summary" path="summary"
				css="formTextArea" />
			<br />
		</h5>
		<h5>
			<acme:textarea code="article.body" path="body" css="formTextArea" />
			<br />
		</h5>
		<acme:textarea code="article.pictures.drag" path="pictures"
			css="formTextArea " id="fotosPath" />
		<br />
		<br />
		<div id="fotos" style="margin-bottom: 0.2em;">
			<jstl:forEach items="${article.pictures}" var="picture">
				<img src="${picture}" class="tableImg">
			</jstl:forEach>
		</div>


		<!-- Carrusel se fotos  -->
		<div class="carrusel" style="background-color: black;">
			<div class="slideshow-container" id="carrusel">
				<a class="prev" onclick="plusSlides(-1)">&#10094;</a> <a
					class="next" onclick="plusSlides(1)">&#10095;</a>
				<jstl:forEach items="${article.pictures}" var="picture">
					<jstl:set var="count" scope="application" value="${count + 1}" />
					<div class="mySlides fade">
						<img src="${picture}" style="width: 100%">
					</div>
				</jstl:forEach>
			</div>
		</div>
		<br>
		<jstl:set var="count" scope="application" value="${0}" />
		<div style="text-align: center" id="punto">
			<jstl:forEach items="${article.pictures}" var="picture">
				<jstl:set var="count" scope="application" value="${count + 1}" />
				<span class="dot" onclick="currentSlide(${count})"></span>
			</jstl:forEach>
		</div>


		<br />

		<br>

		<acme:checkBox code="article.finalMode" path="finalMode"
			css="formCheck" />
		<br />


		<jstl:if test="${!article.finalMode}">
			<acme:submit name="save" code="article.save" css="formButton toLeft" />&nbsp;
    </jstl:if>
		<acme:cancel url="/article/user/list.do" code="article.cancel"
			css="formButton toLeft" />
		<br />

	</form:form>
</div>

