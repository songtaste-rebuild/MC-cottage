/**
 * 
 */
$(document).ready(function() {
	$("#username").val("请输入用户名");
	$("#password").val("请输入密码");
	$("#login-button").click(function() {
		loginJson = '{"userName":"' + $("#username").val() + '","password":"' + $("#password").val() + '"}';
		$.ajax({
			contentType : 'application/json',
			type : 'POST',
			url : 'login.do',
			dataType : 'JSON',
			data : loginJson,
			success : function(data) {
				if (data.isSuccess) {
					alert("登陆成功");
					window.location.href="home.do";
				} else
					alert(data.errorMsg);
			},
			error : function(){
				alert("返回数据异常");
			}
		});
	});
}); 