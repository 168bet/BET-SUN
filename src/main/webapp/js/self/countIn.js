 var numberPerPage = 10;

 var noConfirmCurrentPage = 1;
 var noConfirmAllPages = 1;
 var noConfirmCount = 1;

 var confirmedCurrentPage = 1;
 var confirmedAllPages = 1;
 var confirmCount = 1;

 var refusedCurrentPage = 1;
 var refusedAllPages = 1;
 var refusedCount = 1;

 var state;

 $(document).ready(function() {
 	createCountInModal();
 	addNoConfirmDataTableHead();
 	addConfirmDataTableHead();
 	addRefusedDataTableHead();
 	state = $("#filterstate").prop("selectedIndex");
 	showContentByFilter();

 	$("#querymoneyin").bind("click", function() {
 		state = $("#filterstate").prop("selectedIndex");
 		showContentByFilter();
 	});

 	$("#selectlines").change(function() {
 		state = $("#filterstate").prop("selectedIndex");
 		numberPerPage = $("#selectlines").find("option:selected").text();
 		numberPerPage = parseInt(numberPerPage);

 		if (0 == state) {
 			if (noConfirmCount == numberPerPage) {
 				noConfirmCurrentPage = 1;
 			} else if (noConfirmCount < noConfirmCurrentPage * numberPerPage) {
 				noConfirmCurrentPage = (noConfirmCount - noConfirmCount % numberPerPage) / numberPerPage + 1;
 			}
 			loadNoConfirmDataToTable();
 		} else if (1 == state) {
 			if (confirmCount == numberPerPage) {
 				confirmedCurrentPage = 1;
 			} else if (confirmCount < confirmedCurrentPage * numberPerPage) {
 				confirmedCurrentPage = (confirmCount - confirmCount % numberPerPage) / numberPerPage + 1;
 			}
 			loadConfirmDataToTable();
 		} else if (2 == state) {
 			if (refusedCount == numberPerPage) {
 				refusedCurrentPage = 1;
 			} else if (refusedCount < refusedCurrentPage * numberPerPage) {
 				refusedCurrentPage = (refusedCount - refusedCount % numberPerPage) / numberPerPage + 1;
 			}
 			loadRefusedDataToTable();
 		}
 	});
 });

 function showContentByFilter() {
 	numberPerPage = $("#selectlines").find("option:selected").text();
 	numberPerPage = parseInt(numberPerPage);

 	if (0 == state) {
 		$("#noconfirmhead").removeClass("displayNone");
 		$("#confirmedhead").addClass("displayNone");
 		$("#refusedhead").addClass("displayNone");

 		noConfirmCurrentPage = 1;
 		loadNoConfirmDataToTable();
 	} else if (1 == state) {
 		$("#noconfirmhead").addClass("displayNone");
 		$("#confirmedhead").removeClass("displayNone");
 		$("#refusedhead").addClass("displayNone");

 		confirmedCurrentPage = 1;
 		loadConfirmDataToTable();
 	} else if (2 == state) {
 		$("#noconfirmhead").addClass("displayNone");
 		$("#confirmedhead").addClass("displayNone");
 		$("#refusedhead").removeClass("displayNone");

 		refusedCurrentPage = 1;
 		loadRefusedDataToTable();
 	}
 }


 function addNoConfirmDataTableHead() {
 	$("#noconfirmhead").find("tr>th").remove();
 	var rowHead = $('<tr>');
 	rowHead.append("<th><span class='language' data-word='userName'>用户名</span></th>")
 		.append("<th><span class='language' data-word='realName'>真名</span></th>")
 		.append("<th><span class='language' data-word='countInPoint'>存入点数</span></th>")
 		.append("<th><span class='language' data-word='cardBank'>开户行</span></th>")
 		.append("<th><span class='language' data-word='cardSite'>开户网点</span></th>")
 		.append("<th><span class='language' data-word='methodOfRemittance'>汇款方式</span></th>")
 		.append("<th><span class='language' data-word='commitDate'>提交日期</span></th>")
 		.append("<th><span class='language' data-word='agree'>同意</span></th>")
 		.append("<th><span class='language' data-word='refuse'>拒绝</span></th>");
 	$("#noconfirmhead").find("tr").replaceWith(rowHead);
 }

 function loadNoConfirmDataToTable() {
 	uuid = window.parent.$.cookie("uuid");

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: true,
 		type: "get",
 		dataType: "json",
 		url: "/game/deposite/all?num=" + numberPerPage + "&page=" + noConfirmCurrentPage + "&state=0",
 		success: function(data) {
 			if (null == data) {
 				return;
 			}
 			noConfirmCount = data.count;
 			addNoConfirmDataToDataTableBody(data.count, data.list);
 			window.parent.frameLoad();
 		},
 		error: function(XMLHttpRequest, textStatus, errorThrown) {
 			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
 		}
 	});
 }

 function addNoConfirmDataToDataTableBody(count, list) {
 	if (null == list)
 		return;

 	$("#selectlines").parent().parent().parent().next().find("tbody").attr("id", "noconfirmbody");
 	$("#noconfirmbody").children().remove();
 	$("#noconfirmbody").append("<tr><td></td></tr>");
 	$("#noconfirmbody").find("tr>td").remove();

 	for (var index = 0; index < list.length; index++) {
 		var rowID = index + 1;
 		var date = new Date();
 		date.setTime(list[index].createdTime); //提交时间
 		date = date.Format("yyyy-MM-dd");


 		$("#row_" + rowID).remove(); //移除表格之前的行

 		var btnAgree = $("#btnAgree").html();
 		var btnRefuse = $("#btnRefuse").html();

 		var newRow = $("<tr id=row_" + rowID + "></tr>");
 		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
 		newRow.append("<td>" + list[index].username + "</td>")
 			.append("<td>" + list[index].name + "</td>")
 			.append("<td>" + list[index].point + "</td>")
 			.append("<td>" + list[index].cardType + "</td>")
 			.append("<td>" + list[index].cardSite + "</td>")
 			.append("<td>" + list[index].info + "</td>")
 			.append("<td>" + date + "</td>")
 			.append("<td><button id='btnagreed_" + rowID + "' type='button' class='btn btn-primary' data-toggle='modal' data-target='.bs-example-modal-sm' onclick='agreed(\"" + list[index].uuid + "\",\"" + list[index].userId + "\"," + list[index].point + "," + rowID + ",\"" + list[index].username + "\",\"" + list[index].name + "\");'>" + btnAgree + "</button></td>") //同意存入
 		.append("<td><button id='btnrefused_" + rowID + "' type='button' class='btn btn-warning' onclick='refused(\"" + list[index].uuid + "\",\"" + list[index].userId + "\"," + rowID + ");'>" + btnRefuse + "</button></td>"); //拒绝存入
 		$("#noconfirmbody").find("tr:last").after(newRow);
 	}
 	noConfirmAllPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
 	showPages(list.length, numberPerPage, noConfirmCurrentPage, noConfirmAllPages);
 }


 function addConfirmDataTableHead() {
 	$("#confirmedhead").find("tr>th").remove();
 	var rowHead = $('<tr>');
 	rowHead.append("<th><span class='language' data-word='userName'>用户名</span></th>")
 		.append("<th><span class='language' data-word='realName'>真名</span></th>")
 		.append("<th><span class='language' data-word='countInPoint'>存入点数</span></th>")
 		.append("<th><span class='language' data-word='cardBank'>开户行</span></th>")
 		.append("<th><span class='language' data-word='cardSite'>开户网点</span></th>")
 		.append("<th><span class='language' data-word='methodOfRemittance'>汇款方式</span></th>")
 		.append("<th><span class='language' data-word='commitDate'>提交日期</span></th>")
 	$("#confirmedhead").find("tr").replaceWith(rowHead);

 }

 function loadConfirmDataToTable() {
 	uuid = window.parent.$.cookie("uuid");

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: true,
 		type: "get",
 		dataType: "json",
 		url: "/game/deposite/all?num=" + numberPerPage + "&page=" + confirmedCurrentPage + "&state=1",
 		success: function(data) {
 			if (null == data) {
 				return;
 			}
 			confirmCount = data.count;
 			addConfirmDataToDataTableBody(data.count, data.list);
 			window.parent.frameLoad();
 		},
 		error: function(XMLHttpRequest, textStatus, errorThrown) {
 			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
 		}
 	});
 }

 function addConfirmDataToDataTableBody(count, list) {
 	if (null == list)
 		return;

 	$("#selectlines").parent().parent().parent().next().find("tbody").attr("id", "confirmedbody");
 	$("#confirmedbody").children().remove();
 	$("#confirmedbody").append("<tr><td></td></tr>");
 	$("#confirmedbody").find("tr>td").remove();

 	for (var index = 0; index < list.length; index++) {
 		var rowID = index + 1;
 		var date = new Date();
 		date.setTime(list[index].createdTime); //提交时间
 		date = date.Format("yyyy-MM-dd");


 		$("#row_" + rowID).remove(); //移除表格之前的行

 		var newRow = $("<tr id=row_" + rowID + "></tr>");
 		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
 		newRow.append("<td>" + list[index].username + "</td>")
 			.append("<td>" + list[index].name + "</td>")
 			.append("<td>" + list[index].point + "</td>")
 			.append("<td>" + list[index].cardType + "</td>")
 			.append("<td>" + list[index].cardSite + "</td>")
 			.append("<td>" + list[index].info + "</td>")
 			.append("<td>" + date + "</td>");
 		$("#confirmedbody").find("tr:last").after(newRow);
 	}

 	confirmedAllPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
 	showPages(list.length, numberPerPage, confirmedCurrentPage, confirmedAllPages);
 }

 function addRefusedDataTableHead() {
 	$("#refusedhead").find("tr>th").remove();
 	var rowHead = $('<tr>');
 	rowHead.append("<th><span class='language' data-word='userName'>用户名</span></th>")
 		.append("<th><span class='language' data-word='realName'>真名</span></th>")
 		.append("<th><span class='language' data-word='countInPoint'>存入点数</span></th>")
 		.append("<th><span class='language' data-word='cardBank'>开户行</span></th>")
 		.append("<th><span class='language' data-word='cardSite'>开户网点</span></th>")
 		.append("<th><span class='language' data-word='methodOfRemittance'>汇款方式</span></th>")
 		.append("<th><span class='language' data-word='commitDate'>提交日期</span></th>")
 		.append("<th><span class='language' data-word='agree'>同意</span></th>")
 		.append("<th><span class='language' data-word='setToBeDeposited'>设为待存入</span></th>");
 	$("#refusedhead").find("tr").replaceWith(rowHead);
 }

 function loadRefusedDataToTable() {
 	uuid = window.parent.$.cookie("uuid");

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: true,
 		type: "get",
 		dataType: "json",
 		url: "/game/deposite/all?num=" + numberPerPage + "&page=" + refusedCurrentPage + "&state=2",
 		success: function(data) {
 			if (null == data) {
 				return;
 			}
 			refusedCount = data.count;
 			addRefusedDataToDataTableBody(data.count, data.list);
 			window.parent.frameLoad();
 		},
 		error: function(XMLHttpRequest, textStatus, errorThrown) {
 			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
 		}
 	});
 }

 function addRefusedDataToDataTableBody(count, list) {
 	if (null == list)
 		return;

 	$("#selectlines").parent().parent().parent().next().find("tbody").attr("id", "refusedbody");
 	$("#refusedbody").children().remove();
 	$("#refusedbody").append("<tr><td></td></tr>");
 	$("#refusedbody").find("tr>td").remove();

 	for (var index = 0; index < list.length; index++) {
 		var rowID = index + 1;
 		var date = new Date();
 		date.setTime(list[index].createdTime); //提交时间
 		date = date.Format("yyyy-MM-dd");


 		$("#row_" + rowID).remove(); //移除表格之前的行

 		var btnAgree = $("#btnAgree").html();
 		var btnSetCounting = $("#btnSetCounting").html();

 		var newRow = $("<tr id=row_" + rowID + "></tr>");
 		newRow.addClass((index & 1) ? "success odd" : "warning even"); //通过判断末尾为1还是0来判断奇偶
 		newRow.append("<td>" + list[index].username + "</td>")
 			.append("<td>" + list[index].name + "</td>")
 			.append("<td>" + list[index].point + "</td>")
 			.append("<td>" + list[index].cardType + "</td>")
 			.append("<td>" + list[index].cardSite + "</td>")
 			.append("<td>" + list[index].info + "</td>")
 			.append("<td>" + date + "</td>")
 			.append("<td><button id='btnagreed_" + rowID + "' type='button' class='btn btn-success' onclick='agreed(\"" + list[index].uuid + "\",\"" + list[index].userId + "\"," + list[index].point + "," + rowID + ",\"" + list[index].username + "\",\"" + list[index].name + "\");'>" + btnAgree + "</button></td>") //同意存入
 		.append("<td><button id='btnupdateToNoConfirmed_" + rowID + "' type='button' class='btn btn-success' onclick='updateToNoConfirmed(\"" + list[index].uuid + "\",\"" + list[index].userId + "\"," + rowID + ");'>" + btnSetCounting + "</button></td>"); //设为待存入
 		$("#refusedbody").find("tr:last").after(newRow);

 	}
 	refusedAllPages = (count - count % numberPerPage) / numberPerPage + (count % numberPerPage == 0 ? 0 : 1);
 	showPages(list.length, numberPerPage, refusedCurrentPage, refusedAllPages);
 }


 function agreed(uuid, userId, point, rowID, userName, realName) {
 	bindDataToModal(userName, realName, point);
 	$('#countInDialog').modal('show');
 	$('#confirmCountIn').unbind();
 	$('#confirmCountIn').bind("click", function() {
 		countInCallBack(uuid, rowID, userId, point);
 	});
 }

 function refused(uuid, userId, rowID) {
 	var data = {};
 	data.state = 2;
 	data.userId = userId;

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: false,
 		type: "put",
 		data: JSON.stringify(data),
 		contentType: "application/json",
 		url: "/game/deposite/" + uuid + "/reject",
 		success: function(data) {
 			if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
 				noConfirmCurrentPage--;
 				if (0 >= noConfirmCurrentPage) {
 					$("#row_" + rowID).remove();
 					return;
 				}
 			}
 			loadNoConfirmDataToTable();
 		},
 		error: function(XMLHttpRequest, textStatus, errorThrown) {
 			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
 		}
 	});
 }

 function updateToNoConfirmed(uuid, userId, rowID) {
 	var data = {};
 	data.state = 0;
 	data.userId = userId;

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: false,
 		type: "put",
 		data: JSON.stringify(data),
 		contentType: "application/json",
 		url: "/game/deposite/" + uuid + "/checking",
 		success: function(data) {
 			if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
 				refusedCurrentPage--;
 				if (0 >= refusedCurrentPage) {
 					$("#row_" + rowID).remove();
 					return;
 				}
 			}
 			loadRefusedDataToTable();
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

 	if (0 == state) {
 		noConfirmCurrentPage = selectedPage;
 		loadNoConfirmDataToTable();
 	} else if (1 == state) {
 		confirmedCurrentPage = selectedPage;
 		loadConfirmDataToTable();
 	} else if (2 == state) {
 		refusedCurrentPage = selectedPage;
 		loadRefusedDataToTable();
 	}
 }

 function createCountInModal() {
 	$('body').append('<div id="countInDialog" class="modal fade bs-example-modal-lg" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel" aria-hidden="true">\
        <div class="modal-dialog modal-lg">\
            <div class="modal-content">\
                <div class="modal-header"></div>\
                <div class="modal-body">\
                	<div class="container-fluid">\
						<form id="countInForm" class="form-horizontal" role="form">\
							<div class="control-group">\
								<label class="control-label">\
									<span class="language" data-word="memberAccount">会员帐号</span>\
									：\
								</label>\
								<div class="controls">\
									<input name="acount" type="text" placeholder="会员帐号" class="language" data-word="memberAccount" id="memberAccount" disabled="true"/>\
								</div>\
							</div>\
							<div class="control-group">\
								<label class="control-label">\
									<span class="language" data-word="realName">真实姓名</span>\
									：\
								</label>\
								<div class="controls">\
									<input name="realName" type="text" placeholder="真实姓名" class="language" data-word="realName" id="realName" disabled="true"/>\
								</div>\
							</div>\
							<div class="control-group">\
								<label class="control-label">\
									<span class="language" data-word="chargeAmount">充值金额</span>\
									：\
								</label>\
								<div class="controls">\
									<input id="inNum" name="inNum" type="text" placeholder="充值金额" class="language" data-word="chargeAmount" disabled="true"/>\
								</div>\
							</div>\
						</form>\
					</div>\
                </div>\
                <div class="modal-footer">\
                    <button class="btn btn-primary" data-dismiss="modal" id="confirmCountIn">确定</button>\
                </div>\
            </div>\
        </div>\
    </div>');
 }

 function bindDataToModal(userName, realName, point) {
 	$("#memberAccount").val(userName);
 	$("#realName").val(realName);
 	$("#inNum").val(point);
 }

 function countInCallBack(uuid, rowID, userID, point) {
 	var data = {};
 	data.state = 1;
 	data.userId = userID;

 	$.ajax({
 		headers: {
 			"uuid": window.parent.$.cookie("uuid"),
 			"role": window.parent.$.cookie("role"),
 		},
 		async: false,
 		type: "put",
 		data: JSON.stringify(data),
 		contentType: "application/json",
 		url: "/game/deposite/" + uuid + "/check",
 		success: function(data) {
 			alert(window.parent.errorLangConfig["1007"]);
 			if (0 == state) {
 				if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
 					noConfirmCurrentPage--;
 					if (0 >= noConfirmCurrentPage) {
 						$("#row_" + rowID).remove();
 						return;
 					}
 				}
 				loadNoConfirmDataToTable();
 			} else if (2 == state) {
 				if (1 == rowID && 0 == $("#row_" + rowID).next().length) {
 					refusedCurrentPage--;
 					if (0 >= refusedCurrentPage) {
 						$("#row_" + rowID).remove();
 						return;
 					}
 				}
 				loadRefusedDataToTable();
 			}
 		},
 		error: function(XMLHttpRequest, textStatus, errorThrown) {
 			alert(window.parent.errorLangConfig[XMLHttpRequest.status]);
 		}
 	});
 }