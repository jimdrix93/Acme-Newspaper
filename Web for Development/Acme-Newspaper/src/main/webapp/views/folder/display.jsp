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

<h2>
	<jstl:out value="${folder.getName()}" />
</h2>

<b><spring:message code="folder.folders" /> :</b>
<br />
<display:table pagesize="10" class="displaytag" name="folders"
	requestURI="folder/display.do" id="row">

	<acme:column property="name" title="folder.name" sortable="true"/>

	<display:column>
		<a href="folder/display.do?folderId=<jstl:out value="${row.id}"/>"><spring:message
				code="folder.show" /></a>
	</display:column>

</display:table>
<br />
<br />
<b><spring:message code="folder.messages" /> :</b>
<br />
<display:table pagesize="10" class="displaytag" name="messages"
	requestURI="folder/display.do" id="row2">


	<acme:column property="subject" title="ms.subject" sortable="true"/>
	
	<display:column>
		<a
			href="myMessage/display.do?messageId=${row2.id}"><spring:message
				code="ms.show" /></a>
	</display:column>


<display:column>
		<a
			href="myMessage/delete.do?messageId=${row2.id}"><spring:message
				code="ms.delete" /></a>
	</display:column>

</display:table>

<br />
<br />

<a href="folder/create.do?folderId=${folder.id }"><spring:message
		code="folder.newfolder" /></a>

<input type="button" name="back"
	value="<spring:message code="folder.back"/>"
	onclick=<jstl:choose>
			<jstl:when test="${empty folder.getParentFolder()}">
			<jstl:out value="javascript:relativeRedir('folder/list.do')"/>
		</jstl:when>
		<jstl:otherwise>
		<jstl:out value="javascript:relativeRedir('folder/display.do?folderId=${folder.parentFolder.id}')"/>
		</jstl:otherwise>
		</jstl:choose> />
<jstl:if test="${folder.getSystemFolder() eq false }">

	<%-- <input type="submit" name="move"
		value="<spring:message code="folder.move" />" /> --%>
	<input type="button" name="move"
		value="<spring:message code="folder.move" />"
		onclick="javascript: relativeRedir('folder/move.do?folderId=<jstl:out value="${folder.getId()}"/>');" />
	<input type="button" name="delete"
		value="<spring:message code="folder.delete" />"
		onclick="javascript: relativeRedir('folder/delete.do?folderId=<jstl:out value="${folder.getId()}"/>');" />

	<jstl:choose>
		<jstl:when test="${empty folder.getParentFolder()}">
			<input type="button"
				value="<spring:message code="folder.changeName" />"
				onclick="javascript: relativeRedir('folder/editFirst.do?folderId=<jstl:out value="${folder.getId()}"/>');" />
		</jstl:when>
		<jstl:otherwise>
			<input type="button"
				value="<spring:message code="folder.changeName" />"
				onclick="javascript: relativeRedir('folder/edit.do?folderId=<jstl:out value="${folder.getId()}"/>');" />
		</jstl:otherwise>
	</jstl:choose>

</jstl:if>


