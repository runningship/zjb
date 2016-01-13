<%@page import="com.youwei.zjb.user.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
User user = (User)request.getSession().getAttribute("user");
request.setAttribute("user", user);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Examples</title>
<link rel="stylesheet" type="text/css" href="../style/css.css" />
<link rel="stylesheet" type="text/css" href="../style/style.css" />
<link rel="stylesheet" type="text/css" href="../style/css_ky.css" />
<link href="../style/animate.min.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script type="text/javascript" src="../js/buildHtml.js"></script>
<script type="text/javascript" src="../js/pagination.js"></script>
<script type="text/javascript" src="../js/DatePicker/WdatePicker.js"></script>
<script src="../js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="../js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="../js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript">
function alertBoxFun(j){
  var T=$('.ss_alertBox'),
  TW=T.innerWidth(),
  TH=T.innerHeight(),
  B=$('body'),
  BW=B.width(),
  TL=BW/2-TW/2;
  T.css({'display':'block','left':TL+'px'});
  T.find('.input').focus().val('');
  if(j){
    var leftContStr='';
    leftContStr+='<h3>'+j.area+'</h3>';
    leftContStr+='<p>'+j.mji+' m²</p>';
    leftContStr+='<p>'+j.djia+' 元</p>';
    leftContStr+='<p>'+j.zjia+' 元</p>';
    T.find('.ss_cont_left').html(leftContStr);
    T.find('.input').attr('data-info',JSON.stringify(j));
  }
}
$(document).on('click','.newHouseList a',function(e){
  var T=$(this),
  TP=T.parent(),
  TD=T.data('info'),
  TPno=TP.siblings(),
  C='active',
  Thid=T.data('hid');
  if(Thid&&TD&&!TP.hasClass('a0')){
    var TDs=eval( TD );//$.parseJSON(TD);//
    TP.addClass(C).siblings().removeClass(C);
    //alert(Thid+'|'+TDs.area);
    alertBoxFun(TDs);
  }else if(TP.hasClass('a0')){
    alert('已售！');
    $('.ss_alertBox').hide();
  }
  event.preventDefault();
})
  $(document).on('click', '.ss_alertBox .ss_alert_setPhone', function(event) {
    var T=$(this),
    TV=T.next().find('input'),
    TJson=TV.attr('data-info');
    var obj = JSON.parse(TJson);
    obj.account = '${user.lname}';
    obj.uname = '${user.uname}';
    var tel = TV.val();
    var myreg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
    if(!myreg.test(tel)){
        alert('请输入有效的手机号码！');
        return false; 
    }
    //ajax....
    YW.ajax({
        type: 'POST',
        url: 'houseTuijian.jsp',
        data:{conts: JSON.stringify(obj), tel : tel},
        mysuccess: function(data){
            infoAlert('您的请求已经提交成功，稍后中介宝客服将与您电话联系.');
            //$('.ss_alertBox').hide();
        }
      });
    // ok  alert
    // 
    event.preventDefault();
    /* Act on the event */
  });
</script>
<style type="text/css">

</style>
<script src="../js/laytpl/laytpl.js" type="text/javascript"></script>
<script src="nhouse_json.js?16113" type="text/javascript" charset="utf-8"></script>
<script id="nhltpl" type="text/html">
{{# for(var i = 0, len = d.newhouse.length; i < len; i++){ }}
<li class=" zshou{{ d.newhouse[i].zshou }}"><a href="" class="" data-hid="{{ d.newhouse[i].hid }}" data-info='{"hid":"{{ d.newhouse[i].hid }}","area":"{{ d.newhouse[i].area }}","flag":"{{ d.newhouse[i].flag }}","mji":"{{ d.newhouse[i].mji }}","djia":"{{ d.newhouse[i].djia }}","zjia":"{{ d.newhouse[i].zjia }}","beizhu":"{{ d.newhouse[i].beizhu }}","zshou":"{{ d.newhouse[i].zshou }}"}'>
  <h2 class="h"><span class="flag">{{ d.newhouse[i].flag }}</span>{{ d.newhouse[i].area }}</h2>
  <div class="hinfo">
    <span class="hibox">
      <div><strong>面积</strong><b>{{ d.newhouse[i].mji }} m²</b></div>
    </span>
    <span class="hibox">
      <div><strong>单价</strong><b>{{ d.newhouse[i].djia }} 元</b></div>
    </span>
    <span class="hibox">
      <div><strong>总价</strong><b>{{ d.newhouse[i].zjia }} 元</b></div>
    </span>
    <span class="hibox">
      <div><strong>备注</strong><b>{{ d.newhouse[i].beizhu }}</b></div>
    </span>
  </div>
</a></li>
{{# } }}
</script>
<script type="text/javascript">

$(document).ready(function() {
  //alert(nhouseJson.newhouse[0].hid)
  // $.each(nhouseJson.newhouse, function(i, val) {
  //    alert(val.hid)
  // });
  var obj=nhouseJson.newhouse;
  obj.sort(getSortFun('desc', 'zshou'));
  function getSortFun(order, sortBy) {
    var ordAlpah = (order == 'asc') ? '>' : '<';
    var sortFun = new Function('a', 'b', 'return a.' + sortBy + ordAlpah + 'b.' + sortBy + '?1:-1');
    return sortFun;
  }   
  var gettpl = document.getElementById('nhltpl').innerHTML;
  laytpl(gettpl).render(nhouseJson, function(html){
      document.getElementById('newHouseList').innerHTML = html;
addcs();
  });
  });
</script>

<script language="javascript">
//从一个给定的数组arr中,随机返回num个不重复项
function getArrayItems(arr, num) {
    //新建一个数组,将传入的数组复制过来,用于运算,而不要直接操作传入的数组;
    var temp_array = new Array();
    for (var index in arr) {
        temp_array.push(arr[index]);
    }
    //取出的数值项,保存在此数组
    var return_array = new Array();
    for (var i = 0; i<num; i++) {
        //判断如果数组还有可以取出的元素,以防下标越界
        if (temp_array.length>0) {
            //在数组中产生一个随机索引
            var arrIndex = Math.floor(Math.random()*temp_array.length);
            //将此随机索引的对应的数组元素值复制出来
            return_array[i] = temp_array[arrIndex];
            //然后删掉此索引的数组元素,这时候temp_array变为新的数组
            temp_array.splice(arrIndex, 1);
        } else {
            //数组中数据项取完后,退出循环,比如数组本来只有10项,但要求取出20项.
            break;
        }
    }
    return return_array;
}
function addcs(){
var ArrList=['a1','a2','a3','a4','a5','a6'];
//blockAlert(getArrayItems(ArrList,1));
  $('#newHouseList li').each(function(index, el) {
    var this_class=getArrayItems(ArrList,1);
    if($(this).hasClass('zshou1')){
      $(this).addClass(this_class[0]);
    }else{
      $(this).addClass('a0');
    }
  });
}
</script>
<style type="text/css">
#menuTop{position:fixed;width:100%;}
</style>
</head>
<body class="newhouse">
<div class="mainbox">
  <div class="bodyer">
  <jsp:include page="menuTop.jsp?type=new" />
    <div class="mainer">
      <div class="bg">
        <div class="maintop">
          <div class="ewmBox"><img src="../style/images/zjb_wx_150.png" alt=""><span>中介宝微信公众号</span></div>
          <h1>另付20万，享受<b>6.5折</b>超级优惠！</h1>
        </div>
        <div class="NH_list">
          <ul class="NHL_ul newHouseList" id="newHouseList"></ul>
        </div>
      </div>
    </div>
  </div>

  <div class="ss_alertBox">
    <div class="ss_alert_tit">
      <a href="#" class="ss_alert_close">X</a>
    </div>
    <div class="ss_alert_cont">
      <div class="textcont">
        <a href="" class="textconta">详情查看</a>
        <div class="textconts hidden"><b>佣金<i>2</i>万元</b><br>在支付首付款后3-7个工作日结佣</div>
      </div>
      <div class="adinputbox">
        <a href="#" class="btns ss_alert_setPhone">提交</a>
        <span class="inputbox"><input type="text" class="input" placeholder="经纪人手机号码"></span>
      </div>
      <div class="ss_cont_left"></div>
    </div>
  </div>
  <script type="text/javascript">
  $(document).ready(function() {
  	ChangeW();
      initTopMenu();
    $('.textconta').hover(function() {
      $(this).next().removeClass('hidden');
    }, function() {
      $(this).next().addClass('hidden');
    }).click(function(event) {
      event.preventDefault();
    });
    $(document).on('click', '.ss_alertBox .ss_alert_close', function(event) {
      $('.ss_alertBox').hide();
      event.preventDefault();
      /* Act on the event */
    });
  });

  function resizebox(){
    win=$(window),
    winH=win.innerHeight(),
    main=$('.mainer'),
    menu=$('#menuTop'),
    menuH=menu.innerHeight();
    menu.css({'margin-top':-menuH})
    menu.find('ul').css({'display':'inline-block','width':'auto'})
    main.height(winH-menuH).css({
      'overflow': 'hidden',
      'overflow-y': 'auto'
    });
  }
  $(document).ready(function(){
    resizebox();
  })
  $(window).resize(function(){
    resizebox();
  })
  </script>
</div>
</body>
</html>