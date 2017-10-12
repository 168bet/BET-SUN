if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function MsgBoxPanel (argument) {

	var panel = new BasePanel();
	var box = $('<div>').addClass('fb-msg-box').appendTo(panel);

	box.data('fb',{
		reload:function(msg){
			if(msg){
				var content = $('<div>').addClass('fb-msg-box-content');
				for (var i =0; i < msg.length; i++) {
					var tMsg = msg[i];
					var letter = $('<div>').addClass('fb-msg-box-letter').appendTo(content);
					var brief = $('<div>').addClass('fb-msg-box-letter-brief').appendTo(letter);
					$('<h6>').text(tMsg.title).appendTo(brief);
					if(tMsg.state == 0){
						$('<strong>').text("New").appendTo(brief);
					}
					var deleteBtn = $('<strong>').addClass('clickable').text("[删除]").click(function(event){
						var _this = $(this).parents('.fb-msg-box-letter');
						$.ajax({
							type: "delete",
							contentType: "application/json",
							url: "/game/internalMsg/" + _this.data('fb').id,
							success: function(response){
								window.console.log("delete");
								_this.remove();
								var tcontent = $('.fb-msg-box-content');
								if(tcontent.children().size() == 0){
									$('<h3>').addClass('fb-msg-box-empty').text("管理员很懒，都没给您发站内信~").appendTo(tcontent);
								}
							}
						});
						event.stopPropagation();
					});
					$('<span>').text(new Date(tMsg.createTime).toLocaleString()).append(deleteBtn).appendTo(brief);
					$('<div>').addClass('fb-msg-box-letter-content').text(tMsg.content).hide().appendTo(letter);
					letter.data('fb',tMsg);
					brief.click(function(e){
						var tLetter = $(e.target).parents('.fb-msg-box-letter');
						tLetter.children('.fb-msg-box-letter-content').toggle();
						var tLetterData = tLetter.data('fb');
						if(tLetterData.state == 0){
							$.ajax({type:'put',url:"/game/internalMsg/" + tLetterData.id + "/read"});
							tLetterData.state = 1;
							tLetter.children('.fb-msg-box-letter-brief').children('strong').remove();
						}
					});
				};
				content.appendTo($('.fb-msg-box'));
			}else{
				var content = $('<div>').addClass('fb-msg-box-content');
				$('<h3>').addClass('fb-msg-box-empty').text("管理员很懒，都没给您发站内信~").appendTo(content);
				content.appendTo($('.fb-msg-box'));
			}
		}
	});
	$.ajax({
		type: "get",
		contentType: "application/json",
		url: "/game/internalMsg/" + GLOBAL.User.uuid + "/all",
		success: function(response){
			$('.fb-msg-box').data('fb').reload(response.list)
		}
	});
	return panel;
}

GLOBAL.Panels["MsgBoxPanel"] = MsgBoxPanel;