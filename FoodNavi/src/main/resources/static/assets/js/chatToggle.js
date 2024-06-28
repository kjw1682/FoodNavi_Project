// 토글 버튼의 초기 상태를 설정합니다. 'off' 상태로 시작합니다.
let isButtonOn = false;

// 토글 버튼 요소를 찾습니다.
const toggleButton = document.getElementById('chatToggleButtonControl');

// 테이블 요소를 가져옵니다
var ingredientsTable = document.getElementById('app');

document.getElementById("chatToggleButtonControl").addEventListener("click", function() {
    var toggleButton = document.getElementById("toggleButton");
    if (toggleButton.style.display === "none") {
        toggleButton.style.display = "block";
    } else {
        toggleButton.style.display = "none";
    }
});