<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="../bootstrap/css/uniform.css" />
<link rel="stylesheet" href="../bootstrap/css/select2.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.main.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.grey.css" class="skin-color" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<title>组织编辑</title>
</head>
<body style="background:#ffffff;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">

				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="icon-align-justify"></i>
						</span>
						<h5>组织添加</h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="#"
							name="organizationForm" id="organizationForm"
							novalidate="novalidate" />
							<div class="control-group">
								<div class="controls">
									<input type="hidden" name="organizationid"
										id="organizationid" value ="${organizationid}") />
								</div>
							</div>
							<div class="control-group">
								<label class="control-label">组织名称</label>
								<div class="controls">
									<input type="text" name="organizationname"
										id="organizationname" value ="${organizationname}" placeholder="请输入组织名称" />
								</div>
							</div>
							<div align="center" class="control-group">
								<input type="submit" value="确定" class="btn btn-primary"
									id="btnAdd" /> <input type="reset" value="重置"
									class="btn btn-primary" style="margin: 50px;"
									id="btnReset" />
							</div>
						</form>
					</div>
				</div>


			</div>
		</div>
	</div>

	<script src="../bootstrap/js/jquery.min.js"></script>
	<script src="../bootstrap/js/jquery.ui.custom.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<script src="../bootstrap/js/jquery.uniform.js"></script>
	<script src="../bootstrap/js/select2.min.js"></script>
	<script src="../bootstrap/js/jquery.validate.js"></script>
	<script src="../bootstrap/js/unicorn.js"></script>
	<script src="../bootstrap/js/unicorn.form_validation.js"></script>
	<script type="text/javascript">		
		
		
		$(function() {			
			
			$("#organizationForm").validate({
				rules : {
					organizationname : "required"
				},
				messages : {
					organizationname : "组织名称不能为空!"
				},
				errorClass : "error",
				success : 'valid',
				unhighlight : function(element, errorClass, validClass) { //验证通过
					$(element).tooltip('destroy').removeClass(errorClass);
				},
				errorPlacement : function(label, element) {
					$(element).tooltip('destroy'); /*必需*/
					$(element).attr('title', $(label).text()).tooltip('show');
				},
				submitHandler : function(form) {
					$.ajax({
						type : 'post',
						url : "organizationEdit.htm",
						data : {
							organizationid: $("#organizationid").val(),
							organizationName : $("#organizationname").val(),
							creatorid : $('#unid', window.parent.document).val()
						},
						success : function(result) {
							if (result.code == 1) {
								self.location = "organizationList.jsp";
							}
						}
					});
				}
			});
		});
	</script>
</body>
</html>