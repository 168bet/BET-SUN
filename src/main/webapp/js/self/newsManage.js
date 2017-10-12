var newsCount = 0;


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

		if (newsCount == numberPerPage) {
			currentPage = 1;
		} else if (newsCount < currentPage * numberPerPage) {
			currentPage = (newsCount - newsCount % numberPerPage) / numberPerPage + 1;
		}

		loadData();
	});

	$(".btnBack").click(function() {
		$("#_newsContent").val("");
		$(".frontDiv").show();
		$(".backDiv").hide();

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});

	$("#btnDelete").click(function() {
		var newsId = $("#txtId").val();
		deleteNews(newsId);
		$(".frontDiv").show();
		$(".backDiv").hide();

		window.parent.frameLoad();
		window.parent.document.body.scrollTop = curScrollTop;
	});

	$("#btnPublishNews").click(function() {
		$(".frontDiv").css("display", "none");
		$("#publishDiv").show();

		curScrollTop = window.parent.document.body.scrollTop;
		window.parent.document.body.scrollTop = 0;
		window.parent.frameLoad();
	});

	$("#publishForm").validate({
		submitHandler: function(form) {
			publishNews();
		},
		rules: {
			_newsContent: {
				required: true
			},
		},
	});
});

function addDataTableHead() {
	$("#newshead").find("tr>th").remove();
	var newth = $('<tr>');
	newth.append("<th><span class='language' data-word='detail'>详细</span></th>")
		.append("<th><span class='language' data-word='publisher'>发布人</span></th>")
		.append("<th><span class='language' data-word='publishTime'>发布时间</span></th>")
		.append("<th><span class='language' data-word='newsContent'>新闻内容</span></th>")
		.append("<th><span class='language' data-word='delete'>删除</span></th>");
	$("#newshead").find("tr").replaceWith(newth);
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
		url: "/game/publicMsg/news/all?num=" + numberPerPage + "&page=" + currentPage,
		success: function(data) {
			pagedata = data.list;
			newsCount = data.count;
			addData(newsCount, data.list);
			window.parent.frameLoad();
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
		}
	});
}

function addData(count, list) {
	$("#newsBody").find("tr>td").remove();

	if (null == list || 0 == list.length || 0 == count)
		return;

	for (var index = 0; index < list.length; index++) {
		var rowID = index + 1;
		var date = new Date();
		date.setTime(list[index].createTime);
		date = date.Format("yyyy-MM-dd hh:mm");

		var btnDetail = $("#txtDetail").html();
		var btnDelete = $("#txtDelete").html();
		var content = list[index].content.length > 40 ? list[index].content.substring(0, 40) + "……" : list[index].content

		$("#row_" + rowID).remove(); //移除表格之前的行

		var newRow = $("<tr id=row_" + rowID + "></tr>");
		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
		newRow.append("<td><button type='button' class='btn btn-primary' onclick='lookUpDetail(" + rowID + ");'>" + btnDetail + "</button></td>")
			.append("<td>" + list[index].publisher + "</td>")
			.append("<td>" + date + "</td>")
			.append("<td>" + content + "</td>")
			.append("<td><button id='btnDelete" + rowID + "' type='button' class='btn btn-danger' onclick='deleteNews(" + "\"" + list[index].id + "\"," + currentPage + "\," + numberPerPage + "\," + rowID + ");'>" + btnDelete + "</button></td>");
		$("#newsBody").find("tr:last").after(newRow);

		allPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
		showPages(list.length, numberPerPage, currentPage, allPages);

	}
}

function lookUpDetail(rowID) {
	currentRow = rowID - 1;
	var id = pagedata[currentRow].id;
	var publisher = pagedata[currentRow].publisher;
	var time = new Date();
	time.setTime(pagedata[currentRow].createTime);
	time = time.Format("yyyy-MM-dd hh:mm");
	var content = pagedata[currentRow].content;

	$("#txtId").val(id);
	$("#publisher").val(publisher);
	$("#publishTime").val(time);
	$("#newsContent").val(content);
	$(".frontDiv").hide();
	$("#detailDiv").show();

	curScrollTop = window.parent.document.body.scrollTop;
	window.parent.document.body.scrollTop = 0;
	window.parent.frameLoad();
}

function deleteNews(newsID, selectedPage, linesPerPage, rowID) {
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		type: "delete",
		dataType: "json",
		async: false,
		url: "/game/publicMsg/news/" + newsID,
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

function publishNews() {
	var content = $("#_newsContent").val();
	var data = {
		content: content
	};
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		async: false,
		type: "post",
		contentType: "application/json",
		data: JSON.stringify(data),
		url: "/game/publicMsg/news",
		success: function(data) {
			alert(window.parent.errorLangConfig["1008"]);
			$("#publishForm").get(0).reset();
			loadData();
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
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