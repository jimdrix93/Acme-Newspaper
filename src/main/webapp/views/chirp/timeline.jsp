<%--
 * list.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>
<%@page import="org.apache.commons.lang.time.DateUtils"%>
<%@page import="org.hibernate.engine.spi.RowSelection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.Date"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib prefix="acme" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

</head>
<body>
	<div class="timeline" id="myTimeline">
		<span class="first"> <img src="images/chirp.png" /> <a
			href="chirp/user/create.do"
			style="float: right; margin-right: 5px; margin-top: -3px;"><img
				src="images/writeWhite.png"></a>
		</span>
		<ul>
			<c:forEach items="${followeds}" var="followed">
				<li>

					<div class="bubble-container">
						<div class="nameChirp">
							<a href="user/display.do?userId=${followed.id}"
								style="color: #000000;">${followed.userAccount.username}</a>
						</div>
						<c:forEach items="${followed.chirps}" var="chirp">
							<div class="bubble">

								<br />
								<div class="nameChirp"><jstl:out value="${followed.name}"/>
									<jstl:out value="${followed.surname}"/>&nbsp;</div>
								<div class="titleChirp">${chirp.title}</div>
								<br /> <jstl:out value="${chirp.description}"/>

							</div>
						</c:forEach>
					</div>



				</li>
			</c:forEach>

		</ul>
	</div>
</body>

