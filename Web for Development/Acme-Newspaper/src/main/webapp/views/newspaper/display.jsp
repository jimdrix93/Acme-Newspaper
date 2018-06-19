
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
<div>
	<div>
		
		<br/> <br/>
		<div class="toRight">
			<spring:message code="newspaper.publicationDate" />
			:
			<fmt:formatDate value="${newspaper.publicationDate}"
				pattern="${momentFormat}" var="publicationDate" />
			<jstl:out value="${publicationDate}"/>
		</div>
		<h5>
			<jstl:out value="${newspaper.title}" />
		</h5>
		<div>
			<IMG src="${newspaper.picture}" class="displayImg" />
		</div>
		<br />
		<h2>
			<acme:textarea code="newspaper.description"
				path="newspaper.description" readonly="true" css="formTextArea" />
		</h2>
	</div>

	<h4>
		<spring:message code="newspaper.Articles" />
	</h4>

	<display:table name="newspaper.articles" class="displaytag" id="row">

		<spring:message code="article.title" var="title" />
		<display:column title="${title}">
			<jstl:choose>
				<jstl:when
					test="${newspaper.isPrivate == false || subscribed == true}">
					<div>
						<a href="article/display.do?articleId=${row.id}"> <jstl:out value="${row.title}"/>
						</a>
					</div>
				</jstl:when>
				<jstl:otherwise>
				 <jstl:out value="${row.title}"/>
			</jstl:otherwise>
			</jstl:choose>
		</display:column>
		<br />
		<spring:message code="article.summary" var="title" />

		<display:column title="${title}">
			<div class="ellipsis" onclick="ellipsis(this);"><jstl:out value="${row.summary}"/></div>
		</display:column>
		<display:column>
			<div>
				<a href="user/displayByArticle.do?articleId=${row.id}"> <spring:message
						code="article.writer" />
				</a>
			</div>
		</display:column>
	</display:table>
	<br />
	
	
	<br /> <br />
	
	<acme:historyBackButton text="newspaper.back" css="formButton toLeft"/>
	<security:authorize access="hasRole('ADMIN')">
		<acme:button text="article.delete"
			url="newspaper/administrator/delete.do?id=${newspaper.id}"
			css="formButton toLeft" />
	</security:authorize>
	<jstl:if test="${newspaper.isPrivate && !subscribed}">
		<security:authorize access="hasRole('CUSTOMER')">
			<acme:button
				url="subscription/customer/create.do?newspaperId=${newspaper.id}"
				text="subscription.subscribe" css="formButton toLeft" />
		</security:authorize>
	</jstl:if>
</div>
<br />
<br />
