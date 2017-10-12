if(!GLOBAL){
	var GLOBAL = {};
}
if(!GLOBAL.Panels){
	GLOBAL.Panels = {};
}


function PublicMsgPanel(){
	var panel = new BasePanel();
	var table = $('<table>').addClass('fb-pmsg-table').appendTo(panel);
	var head = $('<thead>').addClass('fb-pmsg-head').appendTo(table);
	var trHread = $('<tr>').appendTo(head);
	$('<th colspan="1">').width(100).append($('<div>').width(100).text('时间')).appendTo(trHread);
	$('<th colspan="1">').text('信息内容').appendTo(trHread);

	var body = $('<tbody>').addClass('fb-pmsg-body').appendTo(table);

	panel.data('fb').load = function(param){
		$.ajax({type: "get",
			contentType: "application/json",
			url:"/game/publicMsg/" + param + "/all",
			beforesend:function(){
				panel.find('.fb-panel-mask').show();
			},
			success:function(response){
				var records = response.list;
				for (var i = 0; records && i < records.length; i++) {
					var r = records[i];
					var line = $('<tr>').appendTo(body);
					if(i%2 == 1){
						line.css({"background-color":"rgb(245, 245, 245)"});
					}
					var date = new Date(r.createTime);
					$('<td>').append(date.toLocaleDateString() + "<br>" + date.toLocaleTimeString()).appendTo(line);
					$('<td>').text(r.content).appendTo(line);
				}
				panel.find('.fb-panel-mask').hide();
			}
		});
	};
	return panel;
}

GLOBAL.Panels["PublicMsgPanel"] = PublicMsgPanel;