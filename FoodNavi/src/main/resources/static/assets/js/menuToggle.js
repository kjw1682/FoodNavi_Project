function scrollToMembership() {
    var membershipArticle = document.getElementById("membership");
    if (membershipArticle) {
        // 회원가입 섹션으로 부드럽게 스크롤
        membershipArticle.scrollIntoView({ behavior: 'smooth' });
    }
}
// 토글 버튼의 초기 상태를 설정합니다. 'off' 상태로 시작합니다.
let isButtonOn = false;

// 토글 버튼 요소를 찾습니다.
const toggleButton = document.getElementById('toggleButton');


// 테이블 요소를 가져옵니다
var ingredientsTable = document.getElementById('ingredientsTable');

// toggleButton.js
// document.getElementById('toggleButton').addEventListener('click', function(event) {
//     if (event.target.tagName === 'BUTTON') {
//         console.log(event.target.textContent + ' 버튼이 클릭되었습니다.');
//     }
// });

document.getElementById("toggleButtonControl").addEventListener("click", function() {
    var toggleButton = document.getElementById("toggleButton");
    if (toggleButton.style.display === "none") {
        toggleButton.style.display = "block";
    } else {
        toggleButton.style.display = "none";
    }
});

