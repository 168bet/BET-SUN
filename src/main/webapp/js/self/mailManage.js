var mailCount = 0;


var numberPerPage = 10;
var currentPage = 1;
var allPages = 1;
var currentRow;
var pagedata;

$(function() {
	addDataTableHead();
	loadData();

	$("#selectlines").change(function() {
		numberPerPage = $("#selectlines").find("option:selected").text();
		numberPerPage = parseInt(numberPerPage);

		if (mailCount == numberPerPage) {
			currentPage = 1;
		} else if (mailCount < currentPage * numberPerPage) {
			currentPage = (mailCount - mailCount % numberPerPage) / numberPerPage + 1;
		}

		loadData();
	});

	$(".btnBack").click(function() {
		$(".frontDiv").show();
		$(".backDiv").hide();

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});

	$("#btnDelete").click(function() {
		var mailId = $("#txtId").val();
		deleteMail(mailId);
		$(".frontDiv").show();
		$(".backDiv").hide();
	});

	$("#btnSendMail").click(function() {
		$(".frontDiv").css("display", "none");
		$("#publishDiv").show();

		curScrollTop = window.parent.document.body.scrollTop;
		window.parent.document.body.scrollTop = 0;
		window.parent.frameLoad();
	});
});

function addDataTableHead() {
	$("#mailhead").find("tr>th").remove();
	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='detail'>详细</span></th>")
		.append("<th><span class='language' data-word='receiver'>收信人</span></th>")
		.append("<th><span class='language' data-word='title'>标题</span></th>")
		.append("<th><span class='language' data-word='time'>时间</span></th>")
		.append("<th><span class='language' data-word='content'>内容</span></th>")
		.append("<th><span class='language' data-word='state'>状态</span></th>")
		.append("<th><span class='language' data-word='delete'>删除</span></th>");
	$("#mailhead").find("tr").replaceWith(newth);
}

function loadData() {
	var uuid = getUrlParam("uuid");
	if (null == uuid) {
		uuid = window.parent.$.cookie("uuid");
	}

	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		type: "get",
		dataType: "json",
		async: true,
		url: "/game/internalMsg/all?num=" + numberPerPage + "&page=" + currentPage,
		success: function(data) {
			pagedata = data.list;
			mailCount = data.count;
			addData(mailCount, data.list);
			window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function addData(count, list) {
	$("#mailBody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		var date = new Date();
		date.setTime(list[index].createTime);
		date = date.Format("yyyy-MM-dd hh:mm");

		var btnDetail = $("#txtDetail").html();
		var btnDelete = $("#txtDelete").html();
		var state = parseInt(list[index].state) == 1 ? $("#txtReaded").html() : $("#txtUnread").html();
		var stateColor = (list[index].state == 1) ? "blue" : "red";
		var content = list[index].content.length > 40 ? list[index].content.substring(0, 40) + "……" : list[index].content

		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
		newRow.append("<td><button type='button' class='btn btn-primary' onclick='lookUpDetail(" + rowID + ");'>" + btnDetail + "</button></td>")
			.append("<td>" + list[index].username + "</td>")
			.append("<td>" + list[index].title + "</td>")
			.append("<td>" + date + "</td>")
			.append("<td>" + content + "</td>")
			.append("<td id='isread_" + rowID + "' style='color:" + stateColor + "'>" + state + "</td>")
			.append("<td><button id='btnDelete" + rowID + "' type='button' class='btn btn-danger' onclick='deleteMail(" + "\"" + list[index].id + "\"," + currentPage + "\," + numberPerPage + "\," + rowID + ");'>" + btnDelete + "</button></td>");
		$("#mailBody").find("tr:last").after(newRow);

		allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
		showPages(list.length, numberPerPage, currentPage, allPages);
	}
}

function lookUpDetail(rowID) {
	currentRow = rowID - 1;
	var id = pagedata[currentRow].id;
	var title = pagedata[currentRow].title;
	var time = new Date();
	time.setTime(pagedata[currentRow].createTime);
	time = time.Format("yyyy-MM-dd hh:mm");
	var content = pagedata[currentRow].content;

	$("#txtId").val(id);
	$("#title").val(title);
	$("#time").val(time);
	$("#content").val(content);
	$(".frontDiv").hide();
	$("#detailDiv").show();

	curScrollTop = window.parent.document.body.scrollTop;
	window.parent.document.body.scrollTop = 0;
	window.parent.frameLoad();
}

function deleteMail(mailID, selectedPage, linesPerPage, rowID) {
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		type: "delete",
		dataType: "json",
		async: false,
		url: "/game/internalMsg/" + mailID,
		success: function(data) {
			alert(window.parent.errorLangConfig["1005"]);
			currentPage = selectedPage;
			numberPerPage = linesPerPage;

			if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
				currentPage--;
				if (0 == currentPage) {
					$("#row_" + rowID).remove();
					return;
				}
			}
			loadData();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function jumpSelectedPage(linesPerPage, selectedPage, allPages) {
	if (selectedPage <= 0 || selectedPage > allPages) {
		return;
	}

	numberPerPage = linesPerPage;
	currentPage = selectedPage;

	loadData();
}