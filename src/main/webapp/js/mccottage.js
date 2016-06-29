/**
 * 
 */
$(document).ready(function() {
	$("#login").click(function() {
		loginJson = '{"username:"' + $("#username").val() + ',"password":' + $("#password").val();
		$.ajax({
			type : 'POST',
			url : 'login.do',
			dataType : 'JSON',
			data : loginJson,
			success : function(data) {
				if (data.isSuccess) {
					alert("登陆成功");
				} else
					alert(data.errorMsg);
			},
			error : function(){
				alert("返回数据异常");
			}
		});
	});
}); 