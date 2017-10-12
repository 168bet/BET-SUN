function initLoadLanguage(key) {
	if (key != "undefined") {
		var arr = window.location.pathname.split("/");
		var pagename = arr[arr.length - 1].replace(".html", "");
		if (key != null) {
			$.get("/game/js/language/" + key + ".js", function(data) {
				$("head").append($("<script>").html(data));
				loadLanguage(LANGUAGE_CONFIG);
			});
			$.get("/game/js/language/validator-message-" + key + ".js", function(data) {
				$("head").append($("<script>").html(data));
			});
		}
	}
}

function loadLanguage(l_config) {
	$(".language").each(function() {
		var key = $(this).attr("data-word");
		var word = l_config[key];
		var x = $(this);
		if ($(this).get(0).tagName == "SPAN" || $(this).get(0).tagName == "OPTION")
			$(this).text(word);
		else if ($(this).get(0).tagName == "INPUT") {
			if ($(this).get(0).value != "" && $(this).get(0).value != "undefined")
				$(this).attr("value", word);
			else if ($(this).get(0).placeholder != "" && $(this).get(0).placeholder != "undefined")
				$(this).attr("placeholder", word);
			// if ($(this).get(0).placeholder != "" && $(this).get(0).placeholder != "undefined")
			// 	$(this).attr("placeholder", word);
		}
	});
}

$(document).ready(function() {
	// === load language === //
	var key = getUrlParam("lang");
	initLoadLanguage(key);
})