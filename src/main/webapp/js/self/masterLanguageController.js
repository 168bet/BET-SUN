var language = "zh-cn";

function loadLanguage(key) {
	$.get("/game/js/language/" + key + ".js", function(data) {
		$("head").append($("<script>").html(data));
		language = key;
		$("span.language").each(function() {
			var key = $(this).attr("data-word");
			var word = LANGUAGE_CONFIG[key];
			$(this).text(word);
		});
		if ($("#ifmContent").attr("src") != "" && $("#ifmContent").attr("src") != undefined)
			window.frames["ifmContent"].initLoadLanguage(language);
	});

	//错误多语言
	$.get("/game/js/language/error-" + key + ".js", function(data) {
		$("head").append($("<script>").html(data));
		errorLangConfig = ERROR_LANGUAGE_CONFIG;
	});
}

$(function() {
	// === 多语言控制 === //

	language = getUrlParam("lang");
	if (language == null)
		language = "zh-cn";
	loadLanguage(language);

	//错误多语言
	// $.get("/game/js/language/error-zh-cn.js", function(data) {
	// 	$("head").append($("<script>").html(data));
	// 	errorLangConfig = ERROR_LANGUAGE_CONFIG;
	// });

	$("a.changelanguage").click(function() {
		var key = $(this).attr("key-lan");
		loadLanguage(key);
	})
});