<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
String path = request.getContextPath();  
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<title>index.jsp</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="../bootstrap/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="../bootstrap/css/fullcalendar.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.main.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.grey.css"
	class="skin-color" />
<link rel="stylesheet" href="../common/default/styles/index.css"
	class="skin-color" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body scroll="no" style="overflow: hidden; font-weight: bold;">
	<div id="header">
		 <p style="color: #ffffff; font-size: 36px; padding: 30px 15px; font-weight: blod;">管理系统</p> 
		<!-- <p style="color: #777777; font-size: 36px; padding: 30px 15px; font-weight: blod;">管理系统</p> -->
		<!-- <p class="topclass">管理系统</p> -->
		<!--  <h1>
		<a href="#">
			系统管理
		</a>
	</h1> -->
	</div>
	<div id="user-nav" class="navbar navbar-inverse">
		<ul class="nav btn-group">
			<li class="btn btn-inverse">
				<div class="controls">
					<input type="hidden" value="" id="unid" /> <input type="hidden"
						value="" id="un" />
				</div>
			</li>
			<li class="btn btn-inverse"><a title="" href="#"> <i
					class="icon icon-user"> </i> <span class="text" id="ln"> </span>
			</a></li>
			<li class="btn btn-inverse"><a title="" href="#myAlert"
				data-toggle="modal"> <i class="icon icon-cog"> </i> <span
					class="text"> 密码修改 </span>
			</a></li>
			<li class="btn btn-inverse"><a title=""
				href="javascript:void(0);" id="logout"> <i
					class="icon icon-share-alt"> </i> <span class="text"> 退出 </span>
			</a></li>
		</ul>
	</div>
	<div id="sidebar">
	</div>
	<!--  <div id="sidebar">
		<a href="#" class="visible-phone"> <i class="icon icon-home">
		</i> 主页
		</a>
		<ul>
			<li class="submenu active"><a href="javascript:void(0);"
				onClick="showMain('../calendar/calendar.html')"> <i
					class="icon icon-home"> </i> <span> 主页 </span>
			</a></li>
			<li class="submenu"><a href="#" level="1"> <i
					class="icon icon-th-list"> </i> <span> 系统管理 </span> <span
					class="label"> 4 </span>
			</a>
				<ul>
					<li><a href="javascript:void(0);" level="2"
						onClick="showMain('../organization/organizationList.jsp')">
							组织管理 </a></li>
					<li><a href="javascript:void(0);" level="2"
						onClick="showMain('../user/userList.jsp')"> 人员管理 </a></li>
					<li><a href="javascript:void(0);" level="2"
						onClick="showMain('../role/roleList.jsp')"> 角色管理 </a></li>
					<li><a href="javascript:void(0);" level="2"
						onClick="showMain('../link/linkList.jsp')"> 链接管理 </a></li>
					<li><a href="javascript:void(0);" level="2"
						onClick="showMain('../permission/permissionList.jsp')"> 权限管理 </a>
					</li>
				</ul></li>
		</ul>
	</div>-->
	<div id="style-switcher">
		<i class="icon-arrow-left icon-white"> </i> <span> 风格: </span> <a
			href="#grey"
			style="background-color: #555555; border-color: #aaaaaa;"> </a> <!-- <a
			href="#blue" style="background-color: #2D2F57;"> </a>  --> <a href="#red"
			style="background-color: #673232;"></a> <a href="#cyan"
			style="background-color: #8080ff;"> </a> <a href="#white"
			style="background-color: #EDF0EA;"> </a>
	</div>
	<div id="content" scroll="no" style="overflow-x: hidden;">
		<iframe id="iframe" frameBorder="yes" scrolling="yes" src=""
			style="width: 100%; height: 100%;"> </iframe>
	</div>
	<div id="myAlert" class="modal hide">
		<div class="modal-header">
			<button data-dismiss="modal" class="close" type="button">×</button>
			<h3>密码修改</h3>
		</div>
		<div class="modal-body">
			<!-- <iframe id="myframe" frameBorder="yes" scrolling="yes" src="" style="width:
		100%; height: 100%;"></iframe> -->
			<form class="form-horizontal" method="post" 
				name="configForm" id="configForm" novalidate="novalidate" />
			<div class="modal-body">
				<div class="control-group">
					<div class="controls">
						<input type="hidden" name="userid" id="userid" value="${userid}" ) />
						<p id="mytip"></p>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"> 旧密码 </label>
					<div class="controls">
						<input type="password" name="oldpassword" id="oldpassword"
							placeholder="请输入旧密码" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"> 新密码 </label>
					<div class="controls">
						<input type="password" name="newpassword" id="newpassword"
							placeholder="请输入新密码" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"> 确认密码 </label>
					<div class="controls">
						<input type="password" name="prepassword" id="prepassword"
							placeholder="请确认密码" />
					</div>
				</div>
			</div>
			</form>
		</div>
		<div class="modal-footer">
			<a class="btn btn-primary" href="#" onclick="doAction()"> 确认 </a> <a
				data-dismiss="modal" class="btn" href="#"> 取消 </a>
		</div>
		<script src="../bootstrap/js/excanvas.min.js"></script>
		<script src="../bootstrap/js/jquery.min.js"></script>
		<script src="../bootstrap/js/jquery.ui.custom.js"></script>
		<script src="../bootstrap/js/bootstrap.min.js"></script>
		<script src="../bootstrap/js/jquery.peity.min.js"></script>
		<script src="../bootstrap/js/fullcalendar.min.js"></script>
		<script src="../bootstrap/js/unicorn.js"></script>
		<script src="../js/jquery.md5.js"></script>
		<script src="../js/jquery.cookie.js"></script>
		<script type="text/javascript">
	var userid = '<%=request.getAttribute("userid")%>';
	var username = '<%=request.getAttribute("username")%>';
	var logonname = '<%=request.getAttribute("logonname")%>';
	if (userid == 'null' || userid == "") {
		//self.location = "../login/login.jsp?dt=" + new Date().getTime();
		window.open("../login/login.jsp?dt=" + new Date().getTime());
		closeWindow();
	}
	
	var links = eval('<%=request.getAttribute("link")%>');
	var htmlText = '<a href="#" class="visible-phone"> <i class="icon icon-home"></i> 主页 </a><ul><li class="submenu active"><a href="javascript:void(0);" onClick=showMain("../calendar/calendar.html")> <i class="icon icon-home"> </i> <span> 主页 </span></a></li>';
	for(var i=0;i<links.length;i++){
		if(links[i]["parentid"]==0){
			var k=0;
			var htmlTextTemp = "<ul>";
			for(var j=0;j<links.length;j++){
				if(links[j]["parentid"]==links[i]["linkid"]){
					k++;
					htmlTextTemp +='<li><a href="javascript:void(0);" level="2" onClick=showMain("'+links[j]["linktext"]+'")>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;'+links[j]["linkname"]+' </a></li>';
				}
				
			}
			htmlTextTemp += "</ul>";
			htmlText+='<li class="submenu"><a href="#" level="1"> <i class="icon icon-th-list"> </i> <span> '+links[i]["linkname"]+' </span> <span class="label"> '+k+' </span></a>';
			htmlText+= htmlTextTemp;			
			htmlText+='</li>';			
		}
	}
	htmlText+='</ul>';
	
	$("#sidebar").append(htmlText);

	function showMain(url) {
		$("#iframe").load(function() {
			//var mainheight = $(this).contents().find("body").height() + 600;
			var mainheight = $(document.body).height() - 70;
			$(this).height(mainheight);
		});
		$("#iframe").attr('src', url);
	}	
	
	function closeWindow()
	{
		var userAgent = navigator.userAgent;
		if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
		   window.location.href="about:blank";
		} else {
		   window.opener = null;
		   window.open("", "_self");
		   window.close();
		};
	}

	$(function() {
		$("#unid").val(userid);
		$("#un").val(username);
		$("#userid").val(userid);
		$("#ln").html("&nbsp;&nbsp;"+logonname);
		$("#content").css("min-height", $(document.body).height() - 70);
		function logout() {
			$.getJSON("../login/logout.htm", function(result) {
				if (result.code == 0) {
					closeWindow();
				} else {
					window.location.href = "../login/login.jsp?dt=" + new Date().getTime();
				}
			});
		}

		$("#logout").click(function() {
			logout();
		})
		
		showMain("../calendar/calendar.html");		
		$(".submenu").click(function(){
			$('.submenu').removeClass('active');
		    $(this).addClass('active');
		})
		
		$('#myAlert').on('show.bs.modal',
		function() {
			$("#oldpassword").val("");
			$("#newpassword").val("");
			$("#prepassword").val("");
			$("#mytip").html("");
		});
	});
	function doAction() {
		var oldpassword = $.trim($("#oldpassword").val());
		var newpassword = $.trim($("#newpassword").val());
		var prepassword = $.trim($("#prepassword").val());
		if (newpassword != prepassword) {
			$("#mytip").css("color", "red");
			$("#mytip").html("新密码与确认密码不一致!");
		} else {
			$.ajax({
				type: 'post',
				url: "../main/configForm.htm",
				data: {
					userid: userid,
					newpassword: $.md5(newpassword),
					oldpassword: $.md5(oldpassword)
				},
				success: function(result) {
					if (result.code == 1) {
						$("#myAlert").modal('hide');
					} else if (result.code == -2) {
						$("#mytip").css("color", "red");
						$("#mytip").html("旧密码错误!");
					}
				}
			});
		}
	}
	</script>
</body>
</html>