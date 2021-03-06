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
<link rel="stylesheet" href="../bootstrap/css/uniform.css" />
<link rel="stylesheet" href="../bootstrap/css/select2.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.main.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.grey.css"
	class="skin-color" />
<link rel="stylesheet" href="../css/bootstrap-multiselect.css"
	class="skin-color" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<title>人员编辑</title>
</head>
<body style="background:#ffffff;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="icon-align-justify"> </i>
						</span>
						<h5>人员添加</h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="userAdd.htm"
							name="userForm" id="userForm" novalidate="novalidate" />
						<div class="control-group">
							<div class="controls">
								<input type="hidden" id="userid" name="userid" value ="${userid}" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> 姓名 </label>
							<div class="controls">
								<input type="text" id="username" name="username" value ="${username}" />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> 登录名 </label>
							<div class="controls">
								<input type="text" id="logonname" name="logonname" value ="${logonname}" />
							</div>
						</div>						
						<div class="control-group">
							<label class="control-label"> 部门 </label>
							<div class="controls">
								<select id="organizationid" style="width: 50%" autocomplete="off">
								</select>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> 性别 </label>
							<div class="controls">
								<select id="gender" style="width: 50%" autocomplete="off">
								<c:if test="${'1'==gender}">
								<option value="1" selected="selected">男</option>
								<option value="2">女</option>
								</c:if>
								<c:if test="${'2'==gender}">
								<option value="1">男</option>
								<option value="2" selected="selected">女</option>
								</c:if>
								</select>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> 电话 </label>
							<div class="controls">
								<input type="text" id="telephone" name="telephone" value ="${telephone}"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label"> 是否启用 </label>
							<div class="controls">
								<select id="status" style="width: 50%" autocomplete="off">
								<c:if test="${'1'==status}">
								<option value="1" selected = "selected">启用</option>
								<option value="2">禁用</option>
								</c:if>
								<c:if test="${'2'==status}">
								<option value="1">启用</option>
								<option value="2" selected = "selected">禁用</option>
								</c:if>
								</select>
							</div>
						</div>
						<div align="center" class="control-group">
							<input type="submit" value="确定" class="btn btn-primary"
								id="btnAdd" /> <input type="reset" value="重置"
								class="btn btn-primary" style="margin: 50px;" id="btnReset" />
						</div>						
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="../bootstrap/js/jquery.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.ui.custom.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap.min.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap-colorpicker.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap-datepicker.js"></script>
	<script src="../bootstrap/js/jquery.uniform.js">
		
	</script>
	<script src="../bootstrap/js/select2.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.validate.js">
		
	</script>
	<script src="../bootstrap/js/unicorn.js">
		
	</script>
	<script src="../bootstrap/js/unicorn.form_validation.js">
		
	</script>
	
	<script src="../js/jquery.md5.js">
		
	</script>
	<script src="../js/pinyin.js">
		
	</script>
	<script type="text/javascript">
	$(function() {

		function refreshMultiSelect() {
			$.ajax({
				type: "POST",
				url: "../organization/organizationListAll.htm",
				data : {creatorid : $('#unid', window.parent.document).val()},
				dataType : "json",
				success : function(data) {
						$.each(data, function(index, organization) {
							$("#organizationid").append(
									'<option value="' + organization.organizationId + '">'
											+ organization.organizationName
											+ '</option>');

						});
						$("#organizationid").val("${organizationid}");
						$("#organizationid").trigger('change');
				}
				});
			}
			refreshMultiSelect();

			$("#username")
					.blur(
							function() {
								if ($("#username").val() == "") {
									$("#logonname").val("");
									return;
								} else {
									$("#logonname").val(
											pinyin.getCamelChars($("#username")
													.val()));
									$("#logonname").focus();
								}
							});

			$("#userForm").validate({
				rules : {
					username : "required",
					logonname : "required",
					gender : "required",
					organizationid : "required",
					status : "required",
					telephone : "required"
				},
				messages : {
					username : "姓名不能为空!",
					logonname : "登录名不能为空!",
					gender : "请选择性别!",
					telephone : "电话不能为空!",
					organizationid : "请选择部门",
					status : "状态必选"
				},
				errorClass : "error",
				success : 'valid',
				unhighlight : function(element, errorClass, validClass) { //验证通过
					$(element).tooltip('destroy').removeClass(errorClass);
				},
				errorPlacement : function(label, element) {
					$(element).tooltip('destroy');
					/*必需*/
					$(element).attr('title', $(label).text()).tooltip('show');
				},
				submitHandler : function(form) {
					$.ajax({
						type : 'post',
						url : "userEdit.htm",
						data : {
							userid : $("#userid").val(),
							username : $("#username").val(),
							logonname : $("#logonname").val(),
							gender : $("#gender").val(),
							telephone : $("#telephone").val(),
							organizationid : $("#organizationid").val(),
							status : $("#status").val(),
							creatorid : $('#unid', window.parent.document).val()
						},
						success : function(result) {
							if (result.code == 1) {
								self.location = "userList.jsp";
							}
						},
						error : function() {
							alert('保存错误');
						}
					});
				}
			});
		});
	</script>
</body>

</html>