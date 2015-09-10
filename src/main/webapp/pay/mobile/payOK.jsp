<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
<title>中介宝</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="reset.css" rel="stylesheet">
<link href="style.css" rel="stylesheet">
</head>
<body class="pay ok">
<div id="wrap" class="bodyer">
    <div id="header" class="header">
        <span class="left">

        </span>
        <span class="conter">
            支付成功
        </span>
        <span class="right">
            
        </span>
    </div>
    <div id="mainer" class="mainer">
        <div class="conts">
            <div class="tits">
                <span class="icon">
                    <i class="iconfont">&#xe61b;</i>
                </span>
                <span class="cont">
                    <h2>支付成功！</h2>
                    <b id="paytime"></b>
                </span>
            </div>
            <ul class="payOkList">
                <li>付款金额：<b id="payAmount">${fee }</b>元</li>
                <li>到期时间：<b>${mobileDeadtime }</b></li>
                <li>付款方式：支付宝支付</li>
            </ul>
        </div>
        <a onclick="window.close();" class="btn orange block round5" >确定</a>
    </div>
</div>
</body>
</html>
