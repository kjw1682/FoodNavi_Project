/**
 *
 */

/* id 중복확인 화면 출력 요청*/
function idcheck() {
	if ($("#userId").val() == "") {
		alert("아이디를 입력해 주세요!");
		$("#userId").focus();
		return false;;
	}

	/*id 중복확인 창 오픈*/
	var url = "id_check_form?userid="+$("#userId").val();
	window.open(url, "_blank_", "toolbar=no, menubar=no, scrollbars=no, " +
		"resizable=yes, width=550, height=300");
}

function pwconfirm() {
    // 비밀번호와 비밀번호 확인 입력란에서 값을 가져옵니다.
    var userpw = $("#userPw").val();
    var userpwCheck = $("#userpwCheck").val();

    // 정규표현식을 사용하여 비밀번호가 영문 소문자, 대문자, 숫자, 특수문자를 최소한 하나 이상 포함하고, 총 8자 이상인지 검사합니다.
    var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&#^])[A-Za-z\d@$!%*?&#^]{8,}$/;

    // 비밀번호가 패턴과 일치하고, 비밀번호와 비밀번호 확인이 일치하는지 확인합니다.
    if (passwordPattern.test(userpw) && userpw === userpwCheck) {
        // 유효한 경우
        $("#pwResult").text("비밀번호가 확인되었습니다.").css("color", "green");
        return true;
    } else if (!passwordPattern.test(userpw)) {
        // 유효하지 않은 경우
        $("#pwResult").text("비밀번호는 영문 소문자, 대문자, 숫자, 특수문자를 최소한 하나 이상 포함하고, 8자 이상이어야 합니다.").css("color", "red");
        return false;
    } else {
		$("#pwResult").text("비밀번호가 일치하지 않습니다.").css("color", "red");
        return false;
	}
}

// 폼이 제출될 때 비밀번호 유효성을 검사합니다.
$("#join").submit(function(event) {
    if (!pwconfirm()) {
        alert("비밀번호는 영문 소문자, 대문자, 숫자, 특수문자를 최소한 하나 이상 포함하고, 8자 이상이어야 합니다.");
        event.preventDefault();
    }
});