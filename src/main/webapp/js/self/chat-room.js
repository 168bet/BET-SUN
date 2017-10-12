// var dragable = false;
// var x;
// var y;
var chatRoom;
var minChatRoom;
var chatRoomBar;
var margin_left;
var margin_top;
var windowWidth;
var windowHeight;
var chatRoomWidth;
var chatRoomHeight;
var originChatRoomBar_margin_top;
var originMinChatRoom_margin_top;

$(document).ready(function() {
	var chatRoom = $("#chatRoom");
	var minChatRoom = $("#minChatRoom");
	var chatRoomBar = $("#chatRoomBar");
	chatRoomWidth = $("#chatRoom").width();
	chatRoomHeight = $("#chatRoom").height();
	windowWidth = $(window).width();
	windowHeight = $(window).height();
	originChatRoomBar_margin_top = $("#chatRoomBar").css("margin-top");
	originMinChatRoom_margin_top = $("#minChatRoom").css("margin-top");

	$(".chat-close").click(close);

	$("#btnClose").click(close);

	$(".chat-minimize").click(function() {
		$("body").css("overflow", "hidden");
		var screenHeight = $(window).height();
		var screenWidth = $(window).width();
		margin_left = chatRoom.css("margin-left");
		margin_top = chatRoom.css("margin-top");
		chatRoom.animate({
			"margin-left": screenWidth / 2 + 200,
			"margin-top": screenHeight / 2 + 200,
			"width:": 0,
			"height": 0,
			"opacity": 0,
		}, "500", callback);
		minChatRoom.css("display", "block");
	});

	$("#chatRoom").draggable({
		handle: ".headBar,.btnDialog",
		containment: "document"
	});

	// $(".headBar").mousedown(function(e) {
	// 	windowWidth = $(window).width();
	// 	windowHeight = $(window).height();

	// 	dragable = true;
	// 	margin_left = $("#chatRoom").css("margin-left");
	// 	margin_top = $("#chatRoom").css("margin-top");
	// 	x = e.clientX;
	// 	y = e.clientY;
	// });

	// $("html").mousemove(function(e) {
	// 	if (dragable == true) {
	// 		var _x = e.clientX - x;
	// 		var _y = e.clientY - y;
	// 		if (parseInt(margin_left) + _x > windowWidth / 2 - windowWidth && parseInt(margin_left) + _x < windowWidth / 2 - chatRoomWidth && parseInt(margin_top) + _y > windowHeight / 2 - windowHeight && parseInt(margin_top) + _y < windowHeight / 2 - chatRoomHeight) {
	// 			$("#chatRoom").css("margin-left", parseInt(margin_left) + _x);
	// 			$("#chatRoom").css("margin-top", parseInt(margin_top) + _y);
	// 		}
	// 	}
	// });

	// $("html").mouseup(stopDrag);

	$("#btnSend").click(sendMsg);

	$("#btnClose").click(close);

	$(".resumeChat").click(function() {
		$("body").css("overflow", "hidden");
		chatRoom.animate({
			"margin-left": margin_left,
			"margin-top": margin_top,
			"width:": chatRoomWidth,
			"height": chatRoomHeight,
			"opacity": 100,
		}, "500", callback);
		minChatRoom.css("display", "none");
	});

	$(".chatRoomBar").click(function() {
		chatRoom.show("500");
		chatRoomBar.css("display", "none");
	});

	$(document).keydown(function(e) {
		if (e.ctrlKey && e.which == 13) {
			sendMsg();
		}
	});

	$(window).scroll(function() {
		var scrollTop = $(window).scrollTop();
		chatRoomBar.css("margin-top", scrollTop + parseInt(originChatRoomBar_margin_top));
		minChatRoom.css("margin-top", scrollTop + parseInt(originMinChatRoom_margin_top));
	});
});

var close = function() {
	// $("#messageContent").val("");
	// $(".chat-thread").html("");
	// $(".chatTitle").html("");
	$("#chatRoom").hide(500);
	$("#chatRoomBar").css("display", "block");
}

var sendMsg = function() {
	var msg = $("#messageContent").val();
	if (msg.trim() != "") {
		var msgLi = $("<li>");
		msgLi.attr("class", "msgRight");
		msgLi.text(msg);
		$(".chat-thread").append(msgLi);
		$("#messageContent").val("");
	}
	$(".chat-thread").scrollTop($(".chat-thread")[0].scrollHeight);
}

var callback = function() {
	$("body").css("overflow", "auto");
}

// function stopDrag(e) {
// 	dragable = false;
// }