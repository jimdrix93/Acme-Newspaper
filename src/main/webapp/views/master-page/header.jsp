<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>


<!-- Menu and banner usually + "$") -->
<div class="topnav" id="myTopnav">
	<security:authorize access="isAnonymous()">

		<div class="dropdown" style="float: left">
			<button class="dropbtn" onclick="relativeRedir('security/login.do')">
				<spring:message	code="master.page.login" />
			</button>
			<a href="security/login.do">
			</a>
			<div class="dropdown-content">
				<a href="user/register.do"> 
					<spring:message	code="master.page.userRegister" />
				</a> 
				<a href="customer/register.do"> 
					<spring:message	code="master.page.customerRegister" />
				</a> 
				<a href="agent/register.do"> 
					<spring:message code="master.page.agentRegister" />
				</a>
			</div>
		</div>
	</security:authorize>
	<a href="${requestScope['javax.servlet.forward.request_uri']}
		<my:replaceParam name='language' value='en' />">en</a>

	<a href="${requestScope['javax.servlet.forward.request_uri']}
		<my:replaceParam name='language' value='es' />">es</a>

	<security:authorize access="isAuthenticated()">

		<div class="dropdown" style="float: left">
			<button class="dropbtn">
				<security:authentication property="principal.username" />
			</button>
			<div class="dropdown-content">
				<a href="j_spring_security_logout"> 
					<spring:message code="master.page.logout" />
				</a> 
				<a href="myMessage/create.do">
					<spring:message code="master.page.newmessage" /> 
				</a>
				<security:authorize access="hasRole('ADMIN')">
					<a href="myMessage/administrator/create.do">
						<spring:message code="master.page.broadcast" />
					</a>
				</security:authorize>
				<a href="folder/list.do"> 
					<spring:message code="master.page.myfolders" />
				</a> 
				<a href="profile/actor/display.do"> 
					<spring:message code="user.display" />
				</a>
			</div>
		</div>
	</security:authorize>

	<div class="dropdown" title="Volumenes">
		<button class="dropbtn" onclick="relativeRedir('volume/list.do')">
			<spring:message code="volume.volumes" />
			<security:authorize access="hasRole('USER')">
			</security:authorize>
		</button>
		<security:authorize access="hasRole('USER')">
			<div class="dropdown-content">
				<a href="volume/user/create.do">
					<spring:message code="master.page.user.volume.create" />
				</a> 
				<a href="volume/user/list.do">
					<spring:message code="master.page.user.volume.list" />
				</a>
			</div>
		</security:authorize>
		<security:authorize access="hasRole('CUSTOMER')">
			<div class="dropdown-content">
				<a href="vsubscription/customer/list.do">
					<spring:message code="master.page.customer.vsubscription.list" />
				</a>
			</div>
		</security:authorize>
	</div>

	<div class="dropdown" title="Newspapers">
		<button class="dropbtn" onclick="relativeRedir('newspaper/list.do')">
			<spring:message code="newspaper.newspapers" />
		</button>
		<div class="dropdown-content">
			<a href="newspaper/search.do"> 
				<spring:message code="master.page.searchNewspapers" />
			</a>
			<security:authorize access="hasRole('USER')">
				<a href="newspaper/user/create.do">
				<spring:message code="master.page.user.newspaper.create" />
				</a>
				<a href="newspaper/user/list.do">
				<spring:message code="master.page.user.newspaper.list" />
				</a>
			</security:authorize>
			<security:authorize access="hasRole('ADMIN')">
				<a href="newspaper/listAll.do">
					<spring:message code="newspaper.all.newspapers" />
				</a>
			</security:authorize>
			<security:authorize access="hasRole('CUSTOMER')">
				<a href="newspaper/customer/list.do">
					<spring:message code="master.page.customer.newspaper.privates" />
				</a> 
				<a href="subscription/customer/list.do">
					<spring:message code="master.page.customer.subscription.list" />
				</a>
			</security:authorize>
		</div>

	</div>

	<div class="dropdown" title="Articles">
		<button class="dropbtn" onclick="relativeRedir('article/list.do')">
			<spring:message code="master.page.user.article" />
		</button>
		<div class="dropdown-content">
			<a href="article/search.do"> 
				<spring:message code="master.page.searchArticles" />
			</a>
			
			<security:authorize access="hasRole('USER')">
				<a href="article/user/create.do">
					<spring:message code="master.page.user.article.create" />
				</a>
				<a href="article/user/list.do">
					<spring:message code="master.page.user.article.list" />
				</a>
			</security:authorize>
		</div>
	</div>

	<security:authorize access="hasRole('ADMIN')">
		<div class="dropdown">
			<button class="dropbtn" onclick="relativeRedir('tabooWord/administrator/list.do')">
				<spring:message code="master.page.tabooWord" />
			</button>
			<div class="dropdown-content">
				<a href="tabooWord/administrator/create.do"> 
					<spring:message code="master.page.tabooWord.create" />
				</a> 
				<a href="tabooWord/administrator/listNewspapers.do"> 
					<spring:message code="master.page.tabooWord.listNewspapers" />
				</a> 
				<a href="tabooWord/administrator/listArticles.do"> 
					<spring:message code="master.page.tabooWord.listArticles" />
				</a> 
				<a href="tabooWord/administrator/listChirps.do"> 
					<spring:message code="master.page.tabooWord.listChirps" />
				</a> 
				<a href="tabooWord/administrator/listAdvertisement.do"> 
					<spring:message code="master.page.tabooWord.listAdvertisement" />
				</a>
			</div>
		</div>

		<a href="dashboard/administrator/display.do">
			<spring:message code="dashboard" />
		</a>
	</security:authorize>

	<security:authorize access="hasRole('AGENT')">
		<div class="dropdown">
			<button class="dropbtn" onclick="relativeRedir('advertisement/agent/list.do')">
				<spring:message code="master.page.agent.advertisements" />
			</button>
			<div class="dropdown-content">
				<a href="newspaper/agent/listAdvertisement.do">
					<spring:message code="master.page.agent.nespaper.list.advertisements" />
				</a> 
				<a href="newspaper/agent/list.do">
					<spring:message code="master.page.agent.nespaper.list" />
				</a>
			</div>
		</div>
	</security:authorize>

	<security:authorize access="hasRole('ADMIN')">
		<a href="chirp/administrator/list.do"> 
			<spring:message code="master.page.tabooWord.listChirps" />
		</a>
	</security:authorize>

	<security:authorize access="hasRole('USER')">

		<div class="dropdown">
			<button class="dropbtn" onclick="relativeRedir('chirp/user/timeline.do')">
				<spring:message code="chirps" />
			</button>
			<div class="dropdown-content">
				<a href="chirp/user/create.do">
					<spring:message code="master.page.user.create.chirp" />
				</a> 
				<a href="chirp/user/written.do">
					<spring:message code="master.page.user.list.my.chirp" />
				</a>
			</div>
		</div>
	</security:authorize>

	<div class="dropdown" title="Usuarios">
		<button class="dropbtn" onclick="relativeRedir('user/list.do')">
			<spring:message code="master.page.users" />
			<security:authorize access="hasRole('USER')">
			</security:authorize>

		</button>
		<security:authorize access="hasRole('USER')">
			<div class="dropdown-content">
				<a href="follow/user/list.do"><spring:message
						code="master.page.user.follow" /></a> <a
					href="follow/user/followeds.do"><spring:message
						code="master.page.user.list.followeds" /></a> <a
					href="follow/user/followers.do"><spring:message
						code="master.page.user.list.followers" /></a>

			</div>
		</security:authorize>
	</div>
	<a href="#about">About</a>
</div>
