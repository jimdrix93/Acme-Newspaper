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
<div id="dropbox" ondragover="return false" ondrop="drop(event, 'advertisement')">

	<form:form action="${requestUri}" modelAttribute="advertisement" method="post">

		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="agent" />
		<jstl:if test="${advertisement.id != 0 }">
			<form:hidden path="newspaper" />
		</jstl:if>

		<acme:select code="newspaper" items="${newspapers}" path="newspaper"
			itemLabel="title" css="formInput" readonly="${advertisement.id != 0}" />
		
		<br />
		
			<acme:textbox code="advertisement.title" path="title" css="formInput" />
		
		<br />
		
			<acme:textbox code="advertisement.bannerURL" path="bannerURL"
			css="formInput" />
		<br />
		
		
		<acme:textbox code="advertisement.targetURL" path="targetURL"
			css="formInput" />
		<br />

		
		<b><spring:message code="creditCard.main" /></b>
		<br/>
		<br/>	
		
		<acme:textboxWithValue code="creditCard.holderName" path="creditCard.holderName" value="${holderName}" css="formInput"/>
		<br/>
		
		<acme:textboxWithValue code="creditCard.brandName" path="creditCard.brandName" value="${brandName}" css="formInput"/>
		<br/>
		
		<acme:textboxWithValue code="creditCard.number" path="creditCard.number" value="${number}" css="formInput"/>
		<br/>
		
		<acme:textboxWithValue code="creditCard.expirationMonth" path="creditCard.expirationMonth" value="${expirationMonth}" css="formInput"/>
		<br/>
		
		<acme:textboxWithValue code="creditCard.expirationYear" path="creditCard.expirationYear" value="${expirationYear}" css="formInput"/>
		<br/>
		
		<acme:textboxWithValue code="creditCard.cvv" path="creditCard.cvv" value="${cvv}" css="formInput"/>
		<br/>
		
		<acme:submit name="save" code="advertisement.save" css="formButton toLeft" />&nbsp;
    
		<acme:cancel url="/advertisement/agent/list.do" code="advertisement.cancel"
			css="formButton toLeft" />
		<br />

	</form:form>
</div>

