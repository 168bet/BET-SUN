/**
 * 描述：管理员配置真人游戏
 * 
 * @author deniulor
 * @date 2014-09-06
 * 
 */

 var agentCount = 0;
 var linesPerPage = 0;
 var currentPage = 1;
 $(function() {
 	if (false || "客服管理员" == "role") {
 		return;
 	}

 	agentCount = getUrlParam("childagent");
 	linesPerPage = getUrlParam("linesperpage");
 	currentPage = getUrlParam("currentpage");

 	if (null == linesPerPage) {
 		linesPerPage = 10;
 	}
 	if (null == currentPage) {
 		currentPage = 1;
 	}

 	$("#s2id_selectlines a span").text(linesPerPage);

 	var showLines = agentCount - linesPerPage * (currentPage - 1);
 	addLotteryGameToTable(agentCount, linesPerPage, currentPage);
 	url = "underling-manage.html";
 	allCountParam = "childagent";
 });

 function addLotteryGameToTable(agentCount, linesPerPage, currentPage) {
 	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		url: config.serverAddr + "/game/game/lotteryGame/all",
 		type:"GET",
 		success:function(response){
 			$("#lotteryGameHead").find("tr").empty();
 			$("#lotteryGameHead").find("tr").append("<th>编号</th>").append("<th>名称</th>").append("<th>连接</th>").append("<th>图片</th>")
 			.append("<th>启用/停用</th>").append("<th>编辑</th>").append("<th>删除</th>");
 			var gameList = response.list;
 			$("#lotteryGameBody").empty();
 			for (var index = 0; gameList && index < gameList.length; index++) {
 				var rgame = gameList[index];
 				var newRow = $('<tr id=row_' + rgame.id +  " styles='text-align:center'></tr>");
		 		newRow.addClass((index & 1) ? "success odd" : "warning even");  //通过判断末尾为1还是0来判断奇偶
		 		newRow.append("<td>" + rgame.id + '</td>');
		 		newRow.append("<td>" + rgame.name + "</td>");
		 		newRow.append("<td><a onclick=\"window.open(\'" + rgame.link + "\')\">" + rgame.link + "</a></td>");
		 		newRow.append("<td><button class='btn' onclick='lgcShowImg(\"" + rgame.image + "\")'>预览</a></td>");
		 		if(rgame.enable){
		 			newRow.append("<td><button class='btn btn-warning enable' onclick='lgcDisable("+ rgame.id +");'>停用</button></td>");
		 		}else{
		 			newRow.append("<td><button class='btn btn-primary enable' onclick='lgcEnable("+ rgame.id +");'>启用</button></td>");
		 		}
		 		newRow.append("<td><button class='btn btn-warning' onclick='lgcEditGame("+ rgame.id +");'>编辑</button></td>");
		 		newRow.append("<td><button class='btn btn-warning delete' onclick='lgcDeleteGame("+ rgame.id +");'>删除</button></td>");
		 		$("#lotteryGameBody").append(newRow);
		 	}
		 },
		 error:function(){
		 	alert("error");
		 	console.log("get game error");
		 }
		});
}

function lgcGroup(ldata, ldefault, inputId, inputPlaceHolder, inputData, inputInline){
	var group = $('<div class="control-group">');
	var label = $('<label class="control-label">').appendTo(group);
	$('<span class="language" data-word="' + ldata + '">').text(ldefault).appendTo(label);
	label.append("：");
	var control = $('<div class="controls">').appendTo(group);
	var input = $('<input type="text" class="language">');
	input.attr({"id":inputId, "placeholder":inputPlaceHolder, "data-word":inputData}).appendTo(control);
	$('<span class="help-inline" id="' + inputInline + '">');
	return group;
}

function lgcBackground(){
	var background = $('<div>').addClass('rgc-model-background');
	background.click(function(e){
		if(e.target.className == "rgc-model-background"){
			background.remove();
		}
	});
	return background;
}

function lgcModelGameInfoPanel(mode){
	var background = new lgcBackground();
	var editPanel = $('<div>').addClass('rgc-edit-panel').appendTo(background);
	editPanel.css('margin-top',$(window).height() / 3 - 100 + 'px');

	var head = $('<div class="rgc-edit-panel-head">').append('<a style="cursor:default" class="current">').appendTo(editPanel);
	head.append($('<span class="language" data-word="newGame">').text("新增游戏"));
	if(mode == "edit"){
		head.attr("data-word","editGame").text("编辑游戏");
	}

	var container = $('<div class="container-fluid">').appendTo(editPanel);
	var form = $('<form class="form-horizontal" role="form">').appendTo(container);

	var idGroup = new lgcGroup('gameId',"游戏编号","newGameId","请输入游戏名称","plsInputGameName","nameHelpInline").appendTo(form);
	if(mode != "edit"){
		idGroup.hide();
	}else{
		idGroup.find("input").attr("readonly","readonly");
	}

	new lgcGroup('gameName',"游戏名称","newGameName","请输入游戏名称","plsInputGameName","nameHelpInline").appendTo(form);
	new lgcGroup('gameLink',"游戏链接","newGameLink","请输入游戏链接","plsInputGameLink","linkHelpInline").appendTo(form);

	var imgGroup = $('<div class="control-group">');
	var label = $('<label class="control-label">').appendTo(imgGroup);
	$('<span class="language" data-word="gameImage">').text("游戏图片").appendTo(label);
	label.append("：");
	var control = $('<div class="controls">').appendTo(imgGroup);
	var preview = $('<img id="newGamePreview" src="../img/game-preview.png">').appendTo(control);
	var input = $('<input type="file" class="language" id="newGameImage">').appendTo(control);
	input.data("image",{ready:true});
	input.change(function(e){
		var target = $(e.target);
		target.data('image').ready = false;
		if(!window.FileReader) {
			target.val("浏览器不支持文件上传，图片将使用默认图片代替。");
			return;
		}else{
			if(e.target.files && e.target.files[0]){
				var reader = new FileReader();
				reader.onload = function(e){
					$('#newGamePreview').attr("src",this.result);
					target.data('image').data = /data:(.+);base64,(.+)/i.exec(this.result)[2];
					target.data('image').ready = true;
				}
				reader.readAsDataURL(e.target.files[0]);
			}else{
				$('#newGamePreview').attr("src", "../img/game-preview.png");
			}
		}
	});
	$('<span class="help-inline" id="gameImageHelpInline">');
	imgGroup.appendTo(form);

	return background;
}

function lgcNewGame () {
	var panel = new lgcModelGameInfoPanel();
	var form = panel.find('form');

	var commitGroup = $('<div class="control-group">').append('<label class="control-lable">').append('<div class="controls">');
	var commitBtn = $('<button type="button" class="btn btn-success btn-medium" id="btnNewGame">');
	commitBtn.append('<span class="language" data-word="commit">').text('提交');
	commitBtn.appendTo(commitGroup.children('.controls'));
	commitGroup.appendTo(form);
	commitBtn.click(function(e){
		if($("#newGameName").val()==""){
			alert("游戏名称不能为空！");
			return;
		}
		if($("#newGameLink").val()==""){
			alert("游戏链接不能为空！");
			return;
		}
		if(!$("#newGameImage").data('image').ready){
			alert("图片还未上传完毕，请稍等");
		}
		var data = {
			name: $("#newGameName").val(),
			link: $("#newGameLink").val(),
			image: $("#newGameImage").data('image').data
		};
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			type: "post",
			contentType: "application/json",
			data: JSON.stringify(data),
			url: config.serverAddr + "/game/game/lotteryGame",
			success: function(data) {
				alert("新增游戏成功。");
				addLotteryGameToTable(agentCount, linesPerPage, currentPage);
				panel.remove();
			},
			error: function(XMLHttpRequest, textStatus, errorThrow) {
				console.log(errorThrow);
			}
		});
	});
	$('body').prepend(panel);
}


function lgcShowImg(url){
	var background = new lgcBackground();

	var panel =  $('<div>').addClass('rgc-album-panel').appendTo(background);
	panel.append("图片加载中…");

	var img = new Image();
	img.onload = function(){
		panel.empty();
		$('<img src="../' + url + '">').appendTo(panel);
		panel.css('margin-top',($(window).height() - img.height)/ 3 + 'px');
		panel.animate({width:img.width,height:img.height},"fast");
	};
	img.src = '../' + url;

	$('body').prepend(background);
}


function lgcEnable(rowID) {
	var btn = $("#row_" + rowID).find("button.enable");

	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},url: config.serverAddr + "/game/game/lotteryGame/" + rowID + "/enable",
		type:"PUT",
		beforeSend:function(){
			btn.attr('disabled',"true");
		},
		success:function(response){
			btn.text("停用");
			btn.attr("onclick","lgcDisable(" + rowID + ")");
			btn.removeClass("btn-primary");
			btn.addClass("btn-warning");
			btn.removeAttr("disabled");
		}
	});
}
function lgcDisable(rowID) {
	var btn = $("#row_" + rowID).find("button.enable");

	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},url: config.serverAddr + "/game/game/lotteryGame/" + rowID + "/disable",
		type:"PUT",
		beforeSend:function(){
			btn.attr('disabled',"true");
		},
		success:function(response){
			btn.text("启用");
			btn.attr("onclick","lgcEnable(" + rowID + ")");
			btn.removeClass("btn-warning");
			btn.addClass("btn-primary");
			btn.removeAttr("disabled");
		}
	});
}

function lgcEditGame (rowID) {
	var panel = new lgcModelGameInfoPanel("edit");
	var form = panel.find('form');

	var commitGroup = $('<div class="control-group">').append('<label class="control-lable">').append('<div class="controls">');
	var commitBtn = $('<button type="button" class="btn btn-success btn-medium" id="btnNewGame">');
	commitBtn.append('<span class="language" data-word="commit">').text('提交');
	commitBtn.appendTo(commitGroup.children('.controls'));
	commitGroup.appendTo(form);
	commitBtn.click(function(e){
		if($("#newGameName").val()==""){
			alert("游戏名称不能为空！");
			return;
		}
		if($("#newGameLink").val()==""){
			alert("游戏链接不能为空！");
			return;
		}
		if(!$("#newGameImage").data('image').ready){
			alert("图片还未上传完毕，请稍等");
		}
		var data = {
			id : $("#newGameId").val(),
			name: $("#newGameName").val(),
			link: $("#newGameLink").val(),
			image: $("#newGameImage").data('image').data
		};
		$.ajax({
			headers: {
				"uuid": window.parent.$.cookie("uuid"),
				"role": window.parent.$.cookie("role"),
			},
			type: "put",
			contentType: "application/json",
			data: JSON.stringify(data),
			url: config.serverAddr + "/game/game/lotteryGame/" + $("#newGameId").val(),
			success: function(data) {
				alert("游戏编辑成功");
				addLotteryGameToTable(agentCount, linesPerPage, currentPage);
				panel.remove();
			},
			error: function(XMLHttpRequest, textStatus, errorThrow) {
				console.log(errorThrow);
			}
		});
	});
	$('body').prepend(panel);
	var content = panel.find('.rgc-edit-panel');
	var mask = $('<div class="rgc-panel-mask">').text("信息加载中…").height(content.height()).css("line-height",content.height()+"px");
	content.prepend(mask);
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},
		type: "Get",
		url: config.serverAddr + "/game/game/lotteryGame/" + rowID,
		success: function(data) {
			//{id: 5, name: " 封神英雄榜", link: "封神英雄榜", image: "uploads/gameimages/dc5b8ceb7a9904ebcad2f4d9c38551b2", enable: true} 
			$("#newGameId").val(data.id);
			$("#newGameName").val(data.name);
			$("#newGameLink").val(data.link);
			$("#newGamePreview").attr("src","../" + data.image);
			mask.remove();
		},
		error: function(XMLHttpRequest, textStatus, errorThrow) {
			console.log(errorThrow);
		}
	});
}



function lgcDeleteGame(rowID) {
	var btn = $("#row_" + rowID).find("button.delete");
	$.ajax({
		headers: {
			"uuid": window.parent.$.cookie("uuid"),
			"role": window.parent.$.cookie("role"),
		},url: config.serverAddr + "/game/game/lotteryGame/" + rowID ,
		type:"DELETE",
		beforeSend:function(){
			btn.attr('disabled',"true");
		},
		success:function(response){
			btn.removeAttr("disabled");
			addLotteryGameToTable();
		}
	});
}
