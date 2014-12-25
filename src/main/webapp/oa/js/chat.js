var coco_ws;
var my_avatar;
var my_name;
var my_uid;
var ue_text_editor;
//http://pub.idqqimg.com/lib/qqface/0.gif
function openChat(contactId,contactName,avatar){
	//打开聊天面板
	showBox();
	$('.chat_title').text('与 '+contactName+'... 聊天中');
	// 判断chat是否已经存在
	if($('#chat_'+contactId).length>0){
		$('.cocoWinLxrList li').removeClass('now');
		$('#chat_'+contactId).toggleClass('now');

		$('.WinInfoListShowMainBox').css('display','none');
		$('#msgContainer_'+contactId).css('display','');
		return;
	}
	// 添加联系人
	var lxrHtml=	'<li avatar="'+avatar+'" cname="'+contactName+'" cid="'+contactId+'" id="chat_'+contactId+'" class="now" onclick="selectChat(this)">'
                    +   '<div  class="cocoWinLxrListTx Fleft"><img src="/oa/images/avatar/'+avatar+'.jpg" /></div>'
                    +   '<div class="cocoWinLxrListPerInfo Fleft">'
                    +   '   <p class="name">'+contactName+'</p>'
                    +   '</div>'
                    +	'<div class="new_msg_count"></div>'
					+	'<div class="cocoWinNewsNum" onclick="closeChat('+contactId+')">×</div>'
                    +'</li>';
	
	// 添加聊天内容窗口
	var msgContainer = '<div currentPageNo="1" class="WinInfoListShowMainBox" id="msgContainer_'+contactId+'">'
						+'<a href="#" class="msg_more" onclick="nextPage();">查看更多消息</a>'
						+'</div>';
	$('.WinInfoListShowMainBox').css('display','none');
	$('.cocoWinInfoListShow').prepend(msgContainer);
	
	// 显示最新聊天
	$('.cocoWinLxrList li').removeClass('now');
	$('.cocoWinLxrList').prepend(lxrHtml);

	loadHistory(contactId,1);
	// $('.msgContainer').css('display','');
}

function loadHistory(contactId , currentPageNo){
  $.ajax({
    type: 'get',
    dataType: 'json',
    url: '/c/im/getHistory?contactId='+contactId+'&currentPageNo='+currentPageNo,
    success:function(data){
    	buildHistory(data.history);
    }
  });
}

function buildHistory(history){
	var chat = getCurrentChat();
	var offsetTop = 0;
	for(var i=0;i<history.length;i++){
		var msg = history[i];
		var container = $('#msgContainer_'+chat.contactId);
		if(msg.senderId==my_uid){
			//我发送的消息
			var html = buildSentMessage(msg.conts,msg.sendtime);
			container.prepend(html);
		}else{
			var html = buildRecvMessage(chat.avatar,msg.conts , msg.sendtime);
			container.prepend(html);
		}
		var top = container.children()[0];
		offsetTop +=$(top).height();
	}
	if(container){
		container.scrollTop(offsetTop);	
	}
	
}
function send(){
	var chat = getCurrentChat();

	var text = ue_text_editor.getContent();
	onSendMsg(text,chat);
	chat.msg = text;
	sendToServer(chat);
	ue_text_editor.setContent('',false);
	$('#msg_textarea').focus();
	
	scrollToLatestNews();
}

function sendToServer(chat){
	chat.type="msg";
	chat.senderAvatar = my_avatar;
	chat.senderName = my_name;
	coco_ws.send(JSON.stringify(chat));
}
function selectChat(li){
	$('.cocoWinLxrList li').removeClass('now');
	$(li).toggleClass('now');
	$('.chat_title').text('与 '+$(li).find('p').text()+'... 聊天中');
	
	$('.WinInfoListShowMainBox').css('display','none');
	var msgContainer = $('#msgContainer_'+$(li).attr('cid'));
	msgContainer.css('display','');

	$(li).find('.new_msg_count').removeClass('cocoWinNewsNum').text('');
	$('#msg_textarea').focus();
	scrollToLatestNews();
}

function buildSentMessage(text,time){
	var sentMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImgRight"><img src="/oa/images/avatar/'+my_avatar+'.jpg" /></div>'
                    +     '<div class="newsAppend">'
                    +          '<div class="newsAppendBox Fright">'
                    +               '<div class="conTxt">'+text+'</div>'
                    +               '<div class="conTime"><i class="clock"></i>'+time+'</div>'
                    +               '<div class="sanjiaoRight"></div>'
                    +          '</div>'
                    +     '</div>'
                    +'</div>';
    var obj =  $(sentMsgHtml);
    obj.find('img').on('dblclick',function(){
    	showBigImg(this);
    });
    return obj;
    
}
//var idNum = 0;
function showBigImg(img){
	
	//var id="imgBigSee"+idNum;
	var htmlText = "<div id='imgBigSee' onclick='$(this).remove()' style='display:block; position:absolute; background-color:#ffffff; z-index:9999999999; box-shadow:#666 0px 0px 10px; border-radius:3px; font-family:'宋体'; background-color:#ffffff;'>"+ img.outerHTML +"</div>";
	$("body").append(htmlText);
	layerShowBox("imgBigSee");
	idNum++;
	
}

function buildRecvMessage(senderAvatar , msg , time){
	var recvMsgHtml='<div class="WinInfoListAppend">'
                    +     '<div class="txImg"><img src="/oa/images/avatar/'+senderAvatar+'.jpg" /></div>'
                    +     '<div class="newsAppend">'
                    +          '<div class="newsAppendBox Fleft">'
                    +               '<div class="conTxt">'+msg+'</div>'
                    +               '<div class="conTime"><i class="clock"></i>'+time+'</div>'
                    +               '<div class="sanjiaoLeft"></div>'
                    +          '</div>'
                    +     '</div>'
                    +'</div>';
    var obj= $(recvMsgHtml);
    obj.find('img').on('dblclick',function(){
    	showBigImg(this);
    });
    return obj;
}
function onSendMsg(text,chat){
	var now = new Date();
	var time = now.getHours()+':'+now.getMinutes();
    $('#msgContainer_'+chat.contactId).append(buildSentMessage(text,time));
}

function notifyNewChat(contactId,msgCount){
	// $('#user_avatar_'+contactId).css();
	// $('#user_avatar_'+contactId).addClass();
	// sort contacts panel
}
function onReceiveMsg(msg){
	var data = JSON.parse(msg);
	if(data.type=='user_status'){
		setUserStatus(data);
		return;
	}

	// 判断是否新会话
	var chat = $('#chat_'+data.senderId);
	if(chat.length==0){
		openChat(data.senderId,data.senderName,data.senderAvatar);
		//给消息提醒
		notifyNewChat();
		return;
	}
    $('#msgContainer_'+data.senderId).append(buildRecvMessage(data.senderAvatar,data.msg , data.sendtime));

    //判断是否当前会话
    var curr = getCurrentChat();
    if(curr.contactId!=data.senderId){
    	//未读消息数量++
    	var jmsgCount = $('#chat_'+data.senderId).find('.new_msg_count');
    	var num_msg_count = 0;
    	if(jmsgCount.text()!=''){
    		num_msg_count = Number.parseInt(jmsgCount.text());
    	}
    	num_msg_count++;
		jmsgCount.text(num_msg_count);
		jmsgCount.addClass('cocoWinNewsNum');
    }else{
    	//滚动到最新消息
    	scrollToLatestNews();
    }
}

function scrollToLatestNews(){
	var chat = getCurrentChat();
	var msgContainer = $('#msgContainer_'+chat.contactId);
	msgContainer.scrollTop(9999999);
}
function getCurrentChat(){
	var li = $('.now');
	var chat = JSON.parse('{}');
	chat.contactId=li.attr('cid');
	chat.contactName=li.attr('cname');
	//好友的头像
	chat.avatar=li.attr('avatar');
	chat.currentPageNo = $('#msgContainer_'+chat.contactId).attr('currentPageNo');
	return chat;
}

function nextPage(){
	var chat = getCurrentChat();
	var currentPageNo = Number.parseInt(chat.currentPageNo);
	currentPageNo++;
	loadHistory(chat.contactId,currentPageNo);
	$('#msgContainer_'+chat.contactId).attr('currentPageNo',currentPageNo);
}

function setUserStatus(json){
	var statusDom = $('.user_status_'+json.contactId);
	if(json.status==0){
		//离线
		statusDom.removeClass('cocoOnline');
		statusDom.addClass('cocoLeave');
	}else if(json.status==1){
		// 在线
		statusDom.removeClass('cocoLeave');
		statusDom.addClass('cocoOnline');
	}
	console.log(json.contactName+'状态: '+json.status);
}

function msgAreaKeyup(event){
	if(event.keyCode==13 && event.ctrlKey){
		send();
	}
}

function getUnReadChats(){
	YW.ajax({
		type: 'get',
		dataType: 'json',
		url: '/c/im/getUnReadChats',
		mysuccess:function(data){

		}
	});
}

function closeChat(contactId){
	if($('.cocoWinLxrList li').length<=1){
		closeBox(closeAllChat);
		return;
	}
	var next = $('#chat_'+contactId).next();
	$('#chat_'+contactId).remove();
	$('#msgContainer_'+contactId).remove();
	//如果删除的是当前聊天，重新选择下一个聊天为当前聊天，如果没有其他聊天，关闭聊天面板
	event.cancelBubble=true;
	if(next.length>0){
		selectChat(next[0]);	
	}
}
function closeAllChat(){
	$('.cocoWinLxrList').empty();
	$('.cocoWinInfoListShow').empty();
}

function recoverChatPanel(){
	if($('.cocoWinLxrList li').length>0){
		showBox();	
	}
	
}

function showCoco(){
    $('.cocoMain').toggleClass('hide');
}

function showFacePanel(){
	$('#facePanel').css('display','');
}

function appendFaceToMsg(face){
	var url = "http://forum.csdn.net/PointForum/ui/scripts/csdn/Plugin/001/face/"+face+".gif";
	$('#msg_textarea').val($('#msg_textarea').val()+url);
	$('#facePanel').css('display','none');
}

function startChangeName(){
	$('#user_name_div').css('display','none');
	$('#user_name_input').css('display','');
	$('#user_name_input').val($('#user_name_div').text());
}

function endChangeName(){
	$('#user_name_div').css('display','');
	$('#user_name_input').css('display','none');
	$('#user_name_div').text($('#user_name_input').val());
	var a = JSON.parse('{}');
	a.name=$('#user_name_input').val();
	YW.ajax({
		type: 'get',
		dataType: 'json',
		data:a,
		url: '/c/im/setUserName',
		mysuccess:function(data){

		}
	});
}

function selectAvatar(i){
	my_avatar = i;
	$('#avatarId').attr('src','/oa/images/avatar/'+i+'.jpg')
}
