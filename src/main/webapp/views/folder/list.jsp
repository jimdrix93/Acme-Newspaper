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

<display:table pagesize="10" class="displaytag" name="folders"
	requestURI="folder/list.do" id="row">


	<acme:column property="name" title="folder.name" sortable="true"/>
	
	<display:column>
		<a href="folder/display.do?folderId=<jstl:out value="${row.id}"/>"><spring:message
				code="folder.show" /></a>

	</display:column>
</display:table>



<a href="folder/createFirst.do"><spring:message
		code="folder.newfolder" /></a>&nbsp;
