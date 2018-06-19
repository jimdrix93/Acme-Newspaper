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

<security:authorize access="hasRole('USER')">
	<form:form action="${requestUri}" modelAttribute="volumeForm">
		<form:hidden path="id"/>
		<h5>
			<acme:textbox code="volume.title" path="title" css="formInput" />
			<br />
		</h5>
		<h5>
			<acme:textarea code="volume.description" path="description"
				css="formTextArea" />
			<br />
		</h5>
		<h5>
			<acme:textbox code="volume.year" path="year" css="formInput" />
			<br />
		</h5>
		<h5>
			<spring:message code="newspaper.newspapers" />
		</h5>
		<br />
		<br />
		<table id="addedNespapers" class="display compact" style="width: 100%">
			<thead>
				<tr>
					<th><spring:message code="newspaper.title" /></th>
					<th><spring:message code="newspaper.description" /></th>
					<th><spring:message code="newspaper.publicationDate" /></th>
				</tr>
			</thead>
			<tbody>
				<jstl:forEach items="${volumeForm.newspapers}" var="mapEntry">
					<jstl:if test="${mapEntry.value}">
						<tr>
							<td><jstl:out value="${mapEntry.key.title}"/></td>
							<td><jstl:out value="${mapEntry.key.description}"/></td>
							<td><fmt:formatDate value="${mapEntry.key.publicationDate}"
		pattern="dd-MM-yyyy" var="publicationDate" /><jstl:out value="${publicationDate}"/></td>
						</tr>
					</jstl:if>
				</jstl:forEach>
			</tbody>
			<tfoot>
				<tr>
					<th><spring:message code="volume.title" /></th>
					<th><spring:message code="volume.description" /></th>
					<th><spring:message code="volume.year" /></th>
				</tr>
			</tfoot>
		</table>
		<br>
		<acme:button text="volume.newspapers.manage" id="myBtn" url=""
			css="formButton toLeft" />
			<br>
			<br>
		<!-- Trigger/Open The Modal -->


		<!-- The Modal -->
		<div id="myModal" class="modal">

			<!-- Modal content -->
			<div class="modal-content">
				<div class="modal-header">
					<span class="close">&times;</span>
				</div>
				<div class="modal-body shadow">
					<table id="modal" class="display compact" style="width: 100%">
						<thead>
							<tr>
								<th><spring:message code="newspaper.title" /></th>
								<th><spring:message code="newspaper.publicationDate" /></th>
								<th><spring:message code="volume.add" /></th>
							</tr>
						</thead>
						<tbody>
							<jstl:forEach items="${volumeForm.newspapers}" var="mapEntry">

								<tr>
									<td><jstl:out value="${mapEntry.key.title}"/></td>
									<td><jstl:out value="${mapEntry.key.publicationDate}"/></td>
									<td><acme:checkBox code="volume.add"
											path="newspapers[${mapEntry.key.id}]" css="formCheck"
											value="${mapEntry.value}" /></td>
								</tr>
							</jstl:forEach>
						</tbody>
						<tfoot>
							<tr>
								<th><spring:message code="volume.title" /></th>
								<th><spring:message code="newspaper.publicationDate" /></th>
								<th><spring:message code="volume.add" /></th>
							</tr>
						</tfoot>
					</table>
					<br />
					<acme:submit name="addNewspaper" code="volume.add"
						css="formButton toLeft" />
					<br /> <br />

				</div>
				<div class="modal-footer">
					
				</div>
			</div>

		</div>
		<br />
		
		<acme:submit name="save" code="newspaper.save" css="formButton toLeft" />
		<acme:cancel url="/volume/user/list.do" code="newspaper.cancel"
			css="formButton toLeft" />
	</form:form>

</security:authorize>

<script>
	$(document).ready(function() {
		$('#addedNespapers').DataTable({
			"language" : {
				"url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/${userSelectedLanguage}.json"
			},
			"lengthMenu" : [ [ -1, 1, 2, 4, 8, 16, 32 ], [ "All", 1, 2, 4, 8, 16, 32 ] ]
		});
		$('#modal').DataTable({
			"language" : {
				"url" : "//cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/${userSelectedLanguage}.json"
			},
			"lengthMenu" : [ [ -1, 1, 2, 4, 8, 16, 32 ], [ "All", 1, 2, 4, 8, 16, 32 ] ]
		});
	});

	// Get the modal
	var modal = document.getElementById('myModal');

	// Get the button that opens the modal
	var btn = document.getElementById("myBtn");

	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("close")[0];

	// When the user clicks the button, open the modal 
	btn.onclick = function() {
		modal.style.display = "block";
	}

	// When the user clicks on <span> (x), close the modal
	span.onclick = function() {
		modal.style.display = "none";
	}

	// When the user clicks anywhere outside of the modal, close it
	window.onclick = function(event) {
		if (event.target == modal) {
			modal.style.display = "none";
		}
	}
</script>