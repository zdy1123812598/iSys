$(function() {
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
	
	$("#logonname").blur(function() {
		var logonname = $.trim($("#logonname").val());
		if (logonname != $.cookie("logonname")) {
			$("#password").val("");
		}
	})

	if ($.cookie("remember") == 1) {
		$("#remember").attr("checked", true);
		$("#logonname").val($.cookie("logonname"));
		$("#password").val($.cookie("password"));
	} else {
		$("#remember").attr("checked", false);
	}

	$("#loginform").submit(function(e){
	    e.preventDefault();
	    var logonname = $.trim($("#logonname").val());
		var password = $.trim($("#password").val());
		var remember = $("#remember").prop("checked") ? 1 : 0;
		if (logonname == "" || password == "") {
			$("#logintip").css("color", "red");
			$("#logintip").html("用户名密码不能为空!");
		} else {
			$.post('login.htm', {
                logonname : logonname,
                password : $.md5(password)
            }, function(result) {
            	if (result.code == 1) {
					var userAgent = navigator.userAgent;
					if (userAgent.indexOf("Firefox") != -1 || userAgent.indexOf("Chrome") !=-1) {
						window.location ="../login/main.htm?dt=" + new Date().getTime();
					} else {
						window.open("../login/main.htm?dt=" + new Date().getTime());
						closeWindow();
					};
					
					if(remember == 1){
						$.cookie("logonname", logonname, {
							expires : 30
						});
						$.cookie("remember", remember, {
							expires : 30
						});
						$.cookie("password", password, {
							expires : 30
						});							
					} else {
						$.cookie("logonname", null);
						$.cookie('remember', null);
						$.cookie('password', null);
						$("#remember").attr("checked", false);
					}
					
				} else {						
					$("#logintip").css("color", "red");
					$("#logintip").html(result.message);
				}
            });
		}		
	  });
	
})