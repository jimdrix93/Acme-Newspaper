
$(document).ready(function() {
	showSlides(1);
});

function askSubmission(msg, form) {
	if (confirm(msg))
		form.submit();
}

function relativeRedir(loc) {
	var b = document.getElementsByTagName('base');
	if (b && b[0] && b[0].href) {
		if (b[0].href.substr(b[0].href.length - 1) == '/'
			&& loc.charAt(0) == '/')
			loc = loc.substr(1);
		loc = b[0].href + loc;
	}
	window.location.replace(loc);
}

function ellipsis(elemento) {
	elemento.classList.toggle("ellipsis");
}

/* When the user clicks on the button, 
toggle between hiding and showing the dropdown content */
function myFunction() {
	document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
	if (!event.target.matches('.dropbtn')) {

		var dropdowns = document.getElementsByClassName("dropdown-content");
		var i;
		for (i = 0; i < dropdowns.length; i++) {
			var openDropdown = dropdowns[i];
			if (openDropdown.classList.contains('show')) {
				openDropdown.classList.remove('show');
			}
		}
	}
}


function plusSlides(n) {
	showSlides(slideIndex += n);
}

function currentSlide(n) {
	showSlides(slideIndex = n);
}

function showSlides(n) {
	var i;
	slideIndex = n;
	var slides = document.getElementsByClassName("mySlides");
	var dots = document.getElementsByClassName("dot");
	if (n > slides.length) {
		slideIndex = 1
	}
	if (n < 1) {
		slideIndex = slides.length
	}
	for (i = 0; i < slides.length; i++) {
		slides[i].style.display = "none";
	}
	for (i = 0; i < dots.length; i++) {
		dots[i].className = dots[i].className.replace(" active", "");
	}
	slides[slideIndex - 1].style.display = "block";
}

/* Drag and Drop */

function drop(evt, target) {
	evt.stopPropagation();
	evt.preventDefault();
	var imageUrl = evt.dataTransfer.getData('text/html');
	var url = $(imageUrl).attr("src");
	var elemento = ('<div class="mySlides fade"><img src="' + url + '" style="width: 100%"></div>');
	var imagen = '<img src="' + url + '" class="tableImg">';
	if (target == 'newspaper') {
		document.getElementById("fotosPath").innerHTML = url;
		$(document.getElementById("carrusel")).append(elemento);
		$(document.getElementById("fotos")).append(imagen);
	} else {
		if (document.getElementById("fotosPath").innerHTML.length > 0) {
			if (!document.getElementById("fotosPath").innerHTML.includes(url)) {
				document.getElementById("fotosPath").innerHTML = document.getElementById("fotosPath").innerHTML.concat(", " + url);
				$(document.getElementById("carrusel")).append(elemento);
				$(document.getElementById("fotos")).append(imagen);
				var slides = document.getElementsByClassName("mySlides");
				var punto = ('<span class="dot" onclick="currentSlide(' + slides.length + ')"></span>');
				$(document.getElementById("punto")).append(punto);
				
			}
		} else {
			document.getElementById("fotosPath").innerHTML = document.getElementById("fotosPath").innerHTML.concat(url);
			$(document.getElementById("carrusel")).append(elemento);
			$(document.getElementById("fotos")).append(imagen);
			var slides = document.getElementsByClassName("mySlides");
			var punto = ('<span class="dot" onclick="currentSlide(' + slides.length + ')"></span>');
			$(document.getElementById("punto")).append(punto);
			
		}
	}
	showSlides(-1);
}
/*  */