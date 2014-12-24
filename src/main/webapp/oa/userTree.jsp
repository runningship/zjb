<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
<link href="/style/css.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<link href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
        
<script type="text/javascript">
var setting = {
  view: {
    showIcon: false,
    addDiyDom: addDiyDom,
    showLine:false,
    dblClickExpand: false,
  },
  check:{
    enable:true
  },
  data: {
    simpleData: {
      enable: true
    }
  },
  callback: {
    // onRightClick: OnRightClick
    // onClick: onClick
    // onCheck: onCheck
  }
};

function onCheck(event, treeId, treeNode){
  console.log(treeNode.id);
}

function initUserTree(treeId){
  $.ajax({
    type: 'POST',
    url: '/c/dept/getUserTree',
    data:'',
    success: function(data){
        var result=JSON.parse(data);
        $.fn.zTree.init($("#"+treeId), setting, result.result);
    }
  });
}


function addDiyDom(treeId, treeNode) {
  console.log(treeId);
  var aObj = $("#" + treeNode.tId + "_a");
  aObj.css('display','inline');

  aObj.parent().addClass('a_'+treeNode.type);
  if(treeNode.cnum!=null){
    var cnumStr = '<span class="">'+ treeNode.cnum +' </span>';
    aObj.prepend(cnumStr);  
  }
  // var color="icon_sh";
  // if(treeNode.sh==0 || treeNode.sh==undefined){
  //   color="icon_wsh";
  // }
  // if(treeNode.type!="group"){
  //   var lockStr = '<span id="'+treeNode.tId+'_sh_a" onClick="shenhe(\''+treeNode.tId+'\')" class="icon iconfont '+color+'">&#xe64e;</span>';
  //   aObj.after(lockStr);
  // }
  // aObj.append('<span class="icon iconfont btns runMenu" data-type="runMenu" data-tree="'+treeNode.tId+'">&#xe641;</span>');
}

</script>
<style type="text/css">
  div#rMenu {position:absolute; top:0;width:150px; text-align: left;padding: 2px;}
  div#rMenu ul li{
    margin: 5px 0;
    padding: 0 15px;
    cursor: pointer;
    list-style: none outside none;
  }
  ul.jtree li a{
    position: initial;
  }
</style>
</head>