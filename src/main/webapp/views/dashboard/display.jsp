
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<div class="dashboard">

	<h5><spring:message code="dashboard.level"></spring:message> C</h5>
	<br>

	<!-- Dashboard D10 C1 Periodicos por usuario. -->
	<h3>
		<spring:message code="dashboard.user.newspapers" />
	</h3>
	<display:table class="displaytag" name="c1Datos" id="row">
		<spring:message code="dashboard.average" var="titleRequests" />
		<display:column title="${titleRequests}">
			<jstl:out value="${row[1]}" />
		</display:column>
		<spring:message code="dashboard.deviation" var="titleName" />
		<display:column title="${titleName}">
			<jstl:out value="${c1Stdev}" />
		</display:column>
	</display:table>

	<!-- Dashboard D10 C2 Articulos por usuario. -->
	<h3>
		<spring:message code="dashboard.user.articles" />
	</h3>
	<display:table class="displaytag" name="c2Datos" id="row">
		<spring:message code="dashboard.average" var="titleRequests" />
		<display:column title="${titleRequests}">
			<jstl:out value="${row[1]}" />
		</display:column>
		<spring:message code="dashboard.deviation" var="titleName" />
		<display:column title="${titleName}">
			<jstl:out value="${c2Stdev}" />
		</display:column>
	</display:table>

	<!-- Dashboard D10 C3 Articulos por periodico. -->
	<h3>
		<spring:message code="dashboard.newspaper.articles" />
	</h3>
	<display:table class="displaytag" name="c3Datos" id="row">
		<spring:message code="dashboard.average" var="titleRequests" />
		<display:column title="${titleRequests}">
			<jstl:out value="${row[1]}" />
		</display:column>
		<spring:message code="dashboard.deviation" var="titleName" />
		<display:column title="${titleName}">
			<jstl:out value="${c3Stdev}" />
		</display:column>
	</display:table>

	<!-- Dashboard D10 C4 Periodicos con mas articulos que la media. -->
	<h3>
		<spring:message code="dashboard.more.articles.newspapers" />(<jstl:out value="${rasero1}" />)
	</h3>
	<display:table class="displaytag" name="newspapersC4" id="row">
		<spring:message code="newspaper" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.title}" />
		</display:column>
		<spring:message code="newspaper.Articles" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.articles.size()}" />
		</display:column>
	</display:table>
	<br>
	
	<!-- Dashboard D10 C5 Periódicos con al menos un 10% menos artículos que la media -->
	<h3>
		<spring:message code="dashboard.less.articles.newspapers" />(<jstl:out value="${rasero2}" />)
	</h3>
	<display:table class="displaytag" name="newspapersC5" id="row">
		<spring:message code="newspaper" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.title}" />
		</display:column>
		<spring:message code="newspaper.Articles" var="title" />
		<display:column title="${title}">
			<jstl:out value="${row.articles.size()}" />
		</display:column>
	</display:table>	
	<br>

	<!-- Dashboard D10 C6 La proporción de usuarios que alguna vez crearon un periódico -->
	<h3>
		<spring:message code="dashboard.ratio.user.newspaper" />
		:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${ratioEditors}" />
	</h4>
	<br />

	<!-- Dashboard D10 C7 La proporción de usuarios que alguna vez escribieron un artículo -->
	<h3>
		<spring:message code="dashboard.ratio.user.articles" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${ratioWriters}" />
	</h4>
	<br />
	
	<!-- N2.0 C3 The ratio of newspapers that have at least one advertisement versus the newspapers that haven't any. -->
	<h3>
		<spring:message code="dashboard.ratio.newspaper" />: 
	</h3>
	<h4 class="formInput">
		<jstl:out value="${ratioNewspaper}" />
	</h4>
	<br />
	
	<!-- N2.0 C3 The ratio of advertisements that have taboo words. -->
	<h3>
		<spring:message code="dashboard.ratio.tabooAdvertisement" />: 
	</h3>
	<h4 class="formInput">
		<jstl:out value="${ratioTabooAdvertisements}" />
	</h4>
	<br />
</div>

<div class="dashboard">

	<h5><spring:message code="dashboard.level"/>  B</h5>
	<br>
	
	<!-- Dashboard D10 B1 Número medio de followups por artículo -->
	<h3>
		<spring:message code="dashboard.avg.followups.articles" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${b1}" />
	</h4>
	<br />

	<!-- Dashboard D10 B2 Número medio de followups por artículo 1 semana despues -->
	<h3>
		<spring:message code="dashboard.avg.followups.articles.1week" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${b2}" />
	</h4>
	<br />

	<!-- Dashboard D10 B3 Número medio de followups por artículo 2 semanas despues -->
	<h3>
		<spring:message code="dashboard.avg.followups.articles.2week" />
	</h3>
	<h4 class="formInput">
		<jstl:out value="${b3}" />
	</h4>
	<br />

	<!-- Dashboard D10 B4 Media y desviación típica de los chirps por usuario -->
	<h3>
		<spring:message code="dashboard.avg.stdev.chirps.user" />
	</h3>

	<display:table class="displaytag" name="b4" id="row">
		<spring:message code="dashboard.average" var="averageB4Title" />
		<display:column title="${averageB4Title }">
			<jstl:out value="${row[0] }" />
		</display:column>

		<spring:message code="dashboard.deviation" var="deviationB4Title" />
		<display:column title="${deviationB4Title }">
			<jstl:out value="${row[1] }" />
		</display:column>
	</display:table>

	<!-- Dashboard D10 B5 Ratio de usuarios que hayan posteado por encima del 75 por ciento del número medio de chirps por usuario -->
	<h3>
		<spring:message code="dashboard.ratio.users.75.chirps.user" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${b5}" />
	</h4>
	<br/>
	
	<!-- N2.0 B The average number of newspapers per volume. -->
	<h3>
		<spring:message code="dashboard.avg.newspaper.volume" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${avgNewspaperPerVolume}" />
	</h4>
	<br/>
	
	<!-- N2.0 B The ratio of subscriptions to volumes versus subscriptions to newspapers -->
	<h3>
		<spring:message code="dashboard.ratio.subscription.vsubscriptions" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${ratioSubscriptionsVSubscriptions}" />
	</h4>
	<br/>
</div>

<div class="dashboard">

	<h5><spring:message code="dashboard.level"/> A</h5>
	<br>
	
	<!-- A1	The ratio of public versus private newspapers. -->
	<h3>
		<spring:message code="dashboard.user.newspapers" />
	</h3>
	<display:table class="displaytag" name="consultaA1" id="row">
		<spring:message code="dashboard.ratio" var="titleName" />
		<display:column title="${titleName}">
			<jstl:out value="${ratioPublic}" />
		</display:column>
		<spring:message code="newspaper.isPrivate" var="privadoTitle" />
		<display:column title="${privadoTitle}">
			<jstl:out value="${privado}" />
		</display:column>
		<spring:message code="newspaper.public" var="publicTitle" />
		<display:column title="${publicTitle}">
			<jstl:out value="${publico}" />
		</display:column>
	</display:table>

	<!-- A2	The average number of articles per private newspapers. -->
	<h3>
		<spring:message code="dashboard.average.article.per.private.newspaper" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${avgA2}" />
	</h4>
	<br />
	
	<!-- A3	The average number of articles per public newspapers. -->
	<h3>
		<spring:message code="dashboard.average.article.per.public.newspaper" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${avgA3}" />
	</h4>
	<br />

	<!-- A4	The ratio of subscribers per private newspaper versus the total number of customers. -->
	<h3>
		<spring:message code="newspaper.private.subscriptions" />
	</h3>
	<display:table class="displaytag" name="consultaA4" id="row">
		<spring:message code="dashboard.ratio" var="titleName" />
		<display:column title="${titleName}">
			<jstl:out value="${ratioSubscribers}" />
		</display:column>
		<spring:message code="newspaper.private.subscribers"
			var="privadoTitle" />
		<display:column title="${privadoTitle}">
			<jstl:out value="${countSubscribers}" />
		</display:column>
		<spring:message code="newspaper.subscribers" var="allTitle" />
		<display:column title="${allTitle}">
			<jstl:out value="${allSubscribers}" />
		</display:column>
	</display:table>

	<!-- A5	The average ratio of private versus public newspapers per publisher -->
	<h3>
		<spring:message	code="dashboard.average.ratio.private.newspaper.per.publisher" />:
	</h3>
	<h4 class="formInput">
		<jstl:out value="${avgA5}" />
	</h4>
	<br />
</div>

