<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="../bootstrap/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.login.css" />
<link rel="stylesheet" href="../common/default/styles/login.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录界面</title>
</head>
<body>
	<div id="logo" class="login-top">
	    <span style="color:#ffffff;font-size:36px;font-weight:blod;">管理系统</span>
		<!--  <img src="../common/default/images/logo.png" alt="" /> -->
	</div>
	<div id="loginbox">
		<form id="loginform" class="form-vertical" />
		<p class="login-tip" id="logintip">输入登录名和密码</p>
		<div class="control-group">
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-user"></i></span><input
						type="text" id="logonname" name="logonname" placeholder="登录名" required />
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="controls">
				<div class="input-prepend">
					<span class="add-on"><i class="icon-lock"></i></span><input
						type="password" id="password" name="password" placeholder="密码" required/>
				</div>
			</div>
		</div>
		<div class="form-actions">
			<span class="pull-left"><input type="checkbox"
				class="checkbox" id="remember" checked="false"> 记住</input></span> <span class="pull-right"><input
				type="submit" class="btn btn-inverse" value="登录" id="loginbtn" /></span>
		</div>
		</form>
	</div>

	<script src="../bootstrap/js/jquery.min.js"></script>
	<script src="../js/jquery.md5.js"></script>
	<script src="../js/jquery.cookie.js"></script>
	<!--  <script src="../bootstrap/js/unicorn.login.js"></script>-->
	<script src="login.js"></script>

</body>
</html>
